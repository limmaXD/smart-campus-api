# Smart Campus Management API

**University of Westminster | Client-Server Architectures (5COSC022W)**

## 1. Project Overview

The **Smart Campus Management API** is a RESTful web service designed to facilitate the monitoring and management of university infrastructure. It focuses on the relationship between physical locations (Rooms) and environmental monitoring hardware (Sensors).

As the Lead Backend Architect, I have developed this system using the **JAX-RS (Jersey)** framework on a **Grizzly HTTP server**. The architecture prioritizes semantic accuracy, resource nesting via sub-resource locators, and robust error handling to ensure a "leak-proof" API that is suitable for a professional campus environment.

### Key Features:

- **HATEOAS-compliant** discovery endpoint.
- **Thread-safe** in-memory data management (No external database required).
- **Deeply nested resources** for sensor history tracking.
- **Global Exception Mapping** to prevent sensitive information leakage.

---

## 2. Build & Launch Instructions

### Prerequisites

- **Java Development Kit (JDK) 17** or higher.
- **Apache Maven** installed.
- **Postman** (for testing and demonstration).

### Steps to Run

1. **Clone the Repository**:Bash
    
    `git clone [YOUR_GITHUB_REPO_LINK]
    cd smart-campus-api`
    
2. **Compile the Project**:Bash
    
    `mvn clean compile`
    
3. **Launch the Server**:Bash
    
    `mvn exec:java -Dexec.mainClass="com.smartcampus.Main"`
    
    *The server will start at: `http://localhost:8080/api/v1/`*
    

---

## 3. Sample Interaction Commands (CURL)

Below are five sample commands to interact with the API. Ensure the server is running before executing these in your terminal.

**1. Service Discovery**

`curl -X GET http://localhost:8080/api/v1/`

**2. Create a New Room**

`curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id": "LIB-01", "name": "Main Library", "capacity": 100}'`

**3. Register a Sensor to a Room**

`curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id": "TEMP-01", "type": "Temperature", "status": "ACTIVE", "roomId": "LIB-01"}'`

**4. Post a New Sensor Reading (Sub-Resource)**

`curl -X POST http://localhost:8080/api/v1/sensors/TEMP-01/read \
-H "Content-Type: application/json" \
-d '{"id": "R101", "timestamp": 1713872400, "value": 22.5}'`

**5. Filter Sensors by Type**

`curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"`
