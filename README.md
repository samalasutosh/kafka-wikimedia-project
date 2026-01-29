Real-Time Wikipedia Event Streaming Pipeline

A production-grade real-time data pipeline that streams live Wikipedia edit events through Apache Kafka and persists them to PostgreSQL using Spring Boot microservices.


Overview:
This project demonstrates a real-time event-driven architecture by consuming live Wikipedia edit events from Wikimedia's public API, processing them through Apache Kafka, and storing them in PostgreSQL. It showcases industry-standard patterns used by companies like Netflix, Uber, and LinkedIn for building scalable data pipelines.

Real-World Data Source: Connects to Wikimedia's EventStreams API to receive 5-10 events per second of actual Wikipedia edits from around the world.


Architecture:

Wikimedia API │ EventStreams  -->  Kafka Producer │ Service   -->    Message Broker   -->   Kafka Consumer │ Service    -->   PostgreSQL DB │ pgAdmin 

Built with passion for learning and sharing knowledge in distributed systems!

