# <img src="imgs/LaborLink.png" alt="Icon" width="30" style="vertical-align: middle;"> Laborlink 

## Overview

### Project Description
LaborLink is a fully integratable enterprise system designed for renting labor tools to researchers, students, and hobbyists. It includes a powerful pipeline that effectively tracks customer activities, detects fraudulent behavior, and provides invoices to end customers. The system leverages modern technologies for communication and data processing and is built with future horizontal scaling in mind. Additionally, it supports integration with external third parties (e.g., delivery of labor cards or collaboration with other LaborLink provider companies).

### Architecture: Event-Driven Design with Kafka as the Communication Backbone

<div style="display: flex; justify-content: center; width: 100%;">
    <img src="imgs/Architecture.png" alt="Description" style="width: 100%; max-width: 100%;">
</div>

## Enviroment & Services

Backend is generally based on Java and Python.

//------- *Developments Tools* ----------------

📦 Apache Maven 3.9.9

📦 Python => 3.12 & (3.13.1) Recommended

📦 JDK 17.0.13 2024-10-15 LTS

//------- *Databases* ----------------

📈 MariaDB 11.6.2

📈 MongoDB mongodb-community@6.0

//------- *External Servers* ----------------

👔 Apache Kafka 3.6.0 (Communication Backbone)

👔 Apache Flink 1.20.0 (ETL Stream Data Processing)

👔 Keycloak 26.1.0 (Open Identity Provider)

//------- *Ports* ----------------

✨ Keycloak --

✨ Gateway --

✨ Kafka --

✨ Flink --

✨ Tool Service --

✨ Renting Machine Service --

✨ Activity Service --

✨ Invoice Service --


## Lunching Laborlink 

### Clone Resources & SubModules 

### Install External Requirements

### Confirue KeyCloack IdentityProvider 

### Start Gateway

### Configure & Start Kafka 

### Configure & Start Flink

### Starting Backend

### Starting Frontend






