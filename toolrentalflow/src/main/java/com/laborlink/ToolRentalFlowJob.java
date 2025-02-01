package com.laborlink;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Data;

public class ToolRentalFlowJob {

    public static void main(String[] args) throws Exception {

        // Get Flink Enviroment Variable from Apache Flink
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // [Consuming Kafka Events] :: Building Consumer Client
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092") // CLI --bootstrap-server
                .setTopics("tool.rental.events")
                .setGroupId("processing")
                .setStartingOffsets(OffsetsInitializer.latest()) // Commit offests, without that it will consume from beginning, NOTE NOTE the behaviour of Spring are different thant flink spring commit offests automaticlly 
                .setValueOnlyDeserializer(new SimpleStringSchema()) // We use Strings for Messaging
                .build();

        env.enableCheckpointing(5000);


        // [Producing Kafka Events] :: Building a Producers to (activity.complete.events)

        // activity.complete.events
        KafkaSink<String> kafkaActivityProducer = KafkaSink.<String>builder()
                .setBootstrapServers("localhost:9092") // CLI --bootstrap-server
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setTopic("activity.complete.events")
                                .setValueSerializationSchema(new SimpleStringSchema()) // We use Strings for messaging
                                .build())
                .build();


        /* ......... Subtask 1 (Consuming events from tool.rental.events) ......... */

        // Source 1 : String Data comming from tool.rental.events Topic
        DataStream<String> activityStream = env.fromSource(
                kafkaSource, // Data Source
                WatermarkStrategy.noWatermarks(), // First Arrived firset processed
                "KAFKA_SOURCE_CONSUMING_COMPACTED_EVENTS: tool.rental.events"); // for Flink UI

        // Source 2 : Mapping Source 1 to DTO
        DataStream<ActivityDTO> activityDTO = activityStream
                .map(new StringToDTOMapper())
                .returns(ActivityDTO.class);

        /* ......... Subtask 2 (DATA Processing) ......... */
    
        // Key DTO by activityId

        KeyedStream<ActivityDTO, String> keyedStream = activityDTO.keyBy(ActivityDTO::getActivityId);

        // Processing Data in a Stateful State
        keyedStream.process(new KeyedProcessFunction<String, ActivityDTO, String>() {
            private transient ValueState<ActivityDTO> state;

            @Override
            public void open(Configuration parameters) throws Exception {
                // Describe the name & the type of state for apache Flink (Apache Flink use it
                // for init, access and managing in general)
                ValueStateDescriptor<ActivityDTO> descriptor = new ValueStateDescriptor<>("activityState",
                        ActivityDTO.class);

                // Get the metadata of state at runtime to manage it in processing
                state = getRuntimeContext().getState(descriptor);
            }

            @Override
            public void processElement(ActivityDTO newActivityDTO,
                    KeyedProcessFunction<String, ActivityDTO, String>.Context arg1,
                    Collector<String> ret) throws Exception {

                ActivityDTO currentActivityDTO = state.value(); // Load the current State then get the Stored DTO

                if (currentActivityDTO == null) // if no DTO exited in State this means its A Renting Event
                    state.update(newActivityDTO); // Update the State (Adding Renting DTO keyed by activityID)

                else { // Means Renting State existed already to the unique activityId, so this means
                       // also the event is Returning

                    // Processing Fraud Detection Logic (In other words check if the user return all
                    // tools) and update status
                    newActivityDTO.setStatus(
                            currentActivityDTO.getItems().equals(newActivityDTO.getItems())
                                    ? ActivityStatus.COMPLETED // User Reutrn all items
                                    : ActivityStatus.TO_BE_REVIEWED); // Missing Tool, Recored this to inform CRM later

                
                    Long rentingTime = Duration.between(currentActivityDTO.getCreatedAt(),
                                                        LocalDateTime.now()).getSeconds();
                    
                    // Processing Calculating Total Price
                    newActivityDTO.setTotalPrice(
                            newActivityDTO.getItems()
                                    .stream()
                                    .mapToDouble(tool ->
                                                 tool.getPricePerSecond() 
                                                 * tool.getQuantity() 
                                                 * rentingTime)
                                    .sum());
                    
                    newActivityDTO.setTotalRentingTimeInSeconds(rentingTime);

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());

                    // Emiting the result of processing
                    ret.collect(objectMapper.writeValueAsString(newActivityDTO));
  

                    // Clear State. State is Completed
                    state.clear(); 

                }

            }
        }).sinkTo(kafkaActivityProducer);



        env.execute("ToolRentalFlowTrackingJOP"); // Job Execution
    }

    public static class StringToDTOMapper extends RichMapFunction<String, ActivityDTO> {

        private transient ObjectMapper objectMapper;

        @Override
        public void open(Configuration parameters) throws Exception {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.registerModule(new JavaTimeModule()); // To include Java Time Dependency in the final Jar
                                                                    // file, by default will be not included in order to
                                                                    // handlle create_at Time
        }

        @Override
        public ActivityDTO map(String arg0) throws Exception {
            return this.objectMapper.readValue(arg0, ActivityDTO.class);
        }

    }

}

// DTOS

@Data
class ActivityDTO {

    private String activityId;

    private String ownerId;

    private List<SingleRentedItemDTO> items;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private Long totalRentingTimeInSeconds;

    private Double totalPrice;

    private ActivityStatus status;

}

@Data
class SingleRentedItemDTO {

    private Long toolId;

    private Integer quantity;

    private Double pricePerSecond;

}

// Enums

enum ActivityStatus {
    PENDING,
    COMPLETED,
    TO_BE_REVIEWED
}