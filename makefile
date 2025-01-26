.PHONY: install-servers start-kc prepare-kafka start-kafka create-kafka-topics package-tool-rental-job start-flink stop-flink up-gateway package-gateway run-gateway up-ts package-ts run-ts up-rms package-rms run-rms up-as package-as run-as install-is-debs run-is

# PATHS
OUTPUT=out/
LOGS=logs
GATEWAY=backend/gateway
TS=backend/tool-service
AS=backend/activity-service
RMS=backend/renting-machine-service
IS=backend/invoice-service
RFJ=backend/jobs/rental-flow-jobs/toolrentalflow/
KEYCLOAK=backend/keycloak/bin
KAFKA=backend/kafka/bin
FLINK=backend/flink/bin


# Define Colors 
GREEN=\033[0;32m
BLUE=\033[0;34m
RED=\033[0;31m
YELLOW=\033[0;33m
NC=\033[0m

install-servers:

# Install Apache Kafka
	curl -o kafka.tgz https://archive.apache.org/dist/kafka/3.6.0/kafka_2.13-3.6.0.tgz
	tar -xvzf kafka.tgz -C backend/
	mv backend/kafka_2.13-3.6.0 backend/kafka
	rm -rf kafka.tgz

# Install Apache Flink
	curl -o flink.tgz https://dlcdn.apache.org/flink/flink-1.20.0/flink-1.20.0-bin-scala_2.12.tgz
	tar -xvzf flink.tgz -C backend/
	mv backend/flink-1.20.0 backend/flink
	rm -rf flink.tgz

# Install Apache KeyCloak
	curl -L -o keycloak.zip https://github.com/keycloak/keycloak/releases/download/26.1.0/keycloak-26.1.0.zip
	unzip -q keycloak.zip -d backend/
	mv backend/keycloak-26.1.0 backend/keycloak
	rm -f keycloak.zip

start-kc:
	@cd $(KEYCLOAK) && ./kc.sh start-dev 

prepare-kafka:
	$(eval UUID=$(shell ./backend/kafka/bin/kafka-storage.sh random-uuid))
	./backend/kafka/bin/kafka-storage.sh format -t $(UUID) -c backend/kafka/config/kraft/server.properties

start-kafka:
	./backend/kafka/bin/kafka-server-start.sh backend/kafka/config/kraft/server.properties

create-kafka-topics:
	./$(KAFKA)/kafka-topics.sh --create \
		--bootstrap-server localhost:9092 \
		--topic tool.rental.events \
		--partitions 1 \
		--replication-factor 1 \
		--config cleanup.policy=compact \
		--config segment.ms=604800000 \
		--config retention.ms=-1
	./$(KAFKA)/kafka-topics.sh --create \
		--bootstrap-server localhost:9092 \
		--topic activity.record.events \
		--partitions 1 \
		--replication-factor 1
	./$(KAFKA)/kafka-topics.sh --create \
		--bootstrap-server localhost:9092 \
		--topic activity.complete.events \
		--partitions 1 \
		--replication-factor 1

package-tool-rental-job:
	mkdir -p $(OUTPUT)
	@cd $(RFJ) && mvn clean package
	cp ${RFJ}/target/toolrentalflow-1.0.jar ${OUTPUT}

start-flink:
	./$(FLINK)/start-cluster.sh

stop-flink:
	./$(FLINK)/stop-cluster.sh

up-gateway:
	@cd $(GATEWAY) && mvn clean spring-boot:run


package-gateway:
	mkdir -p $(OUTPUT)
	@cd $(GATEWAY) && mvn clean package
	cp ${GATEWAY}/target/*.jar ${OUTPUT}


run-gateway:
	mkdir -p $(OUTPUT)
	java -jar $(OUTPUT)/gateway-1.0.0.jar > $(LOGS)/gateway.log 2>&1



up-ts:
	@cd $(TS) && mvn clean spring-boot:run


package-ts:
	mkdir -p $(OUTPUT)
	@cd $(TS) && mvn clean package -DskipTests
	cp ${TS}/target/*.jar ${OUTPUT} 


run-ts:
	mkdir -p $(LOGS)
	java -jar $(OUTPUT)/tool-1.0.0.jar > $(LOGS)/tool-service.log 2>&1


up-rms:
	@cd $(RMS) && mvn clean spring-boot:run


package-rms:
	mkdir -p $(OUTPUT)
	@cd $(RMS) && mvn clean package -DskipTests
	cp ${RMS}/target/*.jar ${OUTPUT} 


run-rms:
	mkdir -p $(LOGS)
	java -jar $(OUTPUT)/renting-1.0.0.jar > $(LOGS)/renting-machine-service.log 2>&1
	


up-as:
	@cd $(AS) && mvn clean spring-boot:run


package-as:
	mkdir -p $(OUTPUT)
	@cd $(AS) && mvn clean package -DskipTests
	cp ${AS}/target/*.jar ${OUTPUT} 


run-as:
	mkdir -p $(LOGS)
	java -jar $(OUTPUT)/activity-1.0.0.jar > $(LOGS)/activity-service.log 2>&1
	


install-is-deps:
	tar -xzvf backend/kafka-python2.0.2-modified.tar
	$(IS)/env/bin/pip install -r $(IS)/requirements.txt
	$(IS)/env/bin/pip install  kafka-python-2.0.2/ 
	rm -rf kafka-python-2.0.2/
	
run-is:
	mkdir -p $(LOGS)
	@cd $(IS) && env/bin/python manage.py run > ../../$(LOGS)/invoice-service.log 2>&1
