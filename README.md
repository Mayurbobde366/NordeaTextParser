# NordeaTextParser

This project is a **Spring Boot-based batch processing application** that parses large text files, breaks them into sentences and words, and outputs the structured data in **XML** and **CSV** formats. It leverages **Spring Batch** with **multi-threading** for high performance and memory-efficient execution.

## Features

- Reads large text input from files
- Parses content into sentences and words
- Outputs both:
  - Well-structured **XML** format with `<sentence>` and `<word>` elements
  - Clean **CSV** format listing words per sentence
- Implements **Spring Batch** for chunk-based processing
- Uses **multi-threading** for performance
- Thread-safe writing for both output formats

## Technologies Used

- Java 8+
- Spring Boot
- Spring Batch
- Maven 3
- SLF4J + Logback (Logging)
- IDE

## Project Structure

## NordeaTextParser
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.nordea.textparser/
│   │   │       ├── config/         # Batch configuration, file reading, file creation configuration
│   │   │       ├── model/          # Sentence POJOs
│   │   │       ├── processor/      # SentenceProcessor
│   │   │       ├── writer/         # XML and CSV output writers
│   │   │       └── TextParserApplication.java
│   │   └── resources/
│   │       └── application.properties     # Spring Batch config
│           └── input.txt                   # Sample input file
│           └── output.xml                  # Parsed XML output
            └── output.csv                  # Parsed CSV output          

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Build the Project

Open a terminal in the project root and run:

```bash
mvn clean install
```

### Run the Application
- Import the application in any IDE(STS4/intellij idea latest version)
- Ensure your input/output file path need to be mentioned in application.properties file, 
  we can implement other approches as well like reading from sharedrive location etc. but due to a time contraint haven't got chance for it, we will implement in future.

### Additional Details
-  We have use spring batch+multithreading approch without database to implement this project due to a time contraint.
   For now normal exception handling is done, but we can improve it with the help of database configuration
   so that all the spring batch metadata details like job, job excution time etc will store it in DB and we can use it to create a fault-tolerant/fine-grain implementation.
