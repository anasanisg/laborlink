package com.laborlink.activity.configs;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.laborlink.activity.dtos.ActivityDTO;
import com.laborlink.activity.dtos.SingleRentedItemDTO;
import com.laborlink.activity.entities.Activity;
import com.laborlink.activity.entities.RentedTool;
import com.laborlink.activity.entities.ReturnedTool;

@Configuration
public class ModelMapperConfig {

        @Bean
    public ModelMapper modelMapper(){
        ModelMapper ret = new ModelMapper();


        // Mapping ToolDTO into Tool Entity
        ret.addMappings(new PropertyMap<ActivityDTO,Activity>() {
            @Override
            protected void configure() {

                // This will be handeld Manually 
                skip(destination.getRentedTools()); 
                skip(destination.getReturnedTools());

                map(source.getActivityId(),destination.getId());
                map(source.getOwnerId(), destination.getOwnerId());
                map(source.getCreatedAt(),destination.getRentedAt());
            }
        });




        return ret;
    }


    // SOURCE SingleItemDTO DESTINATION RentetToolEntity
    public static Converter<SingleRentedItemDTO,RentedTool> sDTOdRentedToolEntity(Activity activity){
        return new Converter<SingleRentedItemDTO,RentedTool>() {
            @Override
            public RentedTool convert(MappingContext<SingleRentedItemDTO, RentedTool> context) {

                RentedTool destination = new RentedTool();
                
                destination.setToolId(context.getSource().getToolId());
                destination.setPricePerSecond(context.getSource().getPricePerSecond());
                destination.setQuantity(context.getSource().getQuantity());

                // Here why we have create the Convert 
                // We have ManyToOne Relation between RentedTools and Activity 
                // When we mapping we should provide the same Activity in order the JPA recongnize the Relation
                destination.setActivity(activity);
                return destination;
            }
            
        };
    }

    // SOURCE SingleItemDTO DESTINATION ReturnedToolEntity
    public static Converter<SingleRentedItemDTO,ReturnedTool> sDTOdReturnedToolEntity(Activity activity){
        return new Converter<SingleRentedItemDTO,ReturnedTool>() {

            @Override
            public ReturnedTool convert(MappingContext<SingleRentedItemDTO, ReturnedTool> context) {
                ReturnedTool destination = new ReturnedTool();

                destination.setToolId(context.getSource().getToolId());
                destination.setPricePerSecond(context.getSource().getPricePerSecond());
                destination.setQuantity(context.getSource().getQuantity());

                // Here why we have create the Convert 
                // We have ManyToOne Relation between ReutnredToolEntity and Activity 
                // When we mapping we should provide the same Activity in order the JPA recongnize the Relation
                destination.setActivity(activity);
                return destination;

            }
            
        };
    }

    
}
