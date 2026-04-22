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


---

## 4. Academic Report & Conceptual Answers

# 1. Service Architecture and Setup

**In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.**

In a standard JAX-RS environment, resource classes follow a per request lifecycle. This means the runtime instantiates a fresh object for every single incoming HTTP request, which is then garbage collected once the response is dispatched. From a developer's perspective, this is a critical realization, instance level variables are transient and will not persist data between calls. To solve this in a system without a traditional database, I implemented thread safe, static data structures like Concurrent HashMap. This ensures that even though the objects handling the requests are short-lived, the Smart Campus data remains persistent and consistent across multiple concurrent threads.

**Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

Hypermedia as the Engine of Application State (HATEOAS) represents the highest level of REST maturity. By embedding discovery links such as the contact email and resource entry points directly into the root JSON response, we create a self-documenting API. For a client developer, this is far superior to relying on static PDF documentation. It allows the client to navigate the API dynamically, effectively decoupling the client’s logic from the server's specific URI paths. If I decide to move the room management endpoint in the future, a HATEOAS compliant client would simply follow the new link provided in the discovery response without needing a code update.

# 2. Room Management and Logic

- **When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.**

Choosing between returning a list of IDs versus full objects is a classic trade off in distributed systems. While returning only IDs minimizes the initial bandwidth usage, it often leads to the N+1 select problem, where a client must make dozens of additional network calls to fetch the details of each room. In the context of a campus monitoring system, network latency is often a bigger bottleneck than payload size. Therefore, I chose to return full room objects. This allows a dashboard to populate all necessary room data in a single round trip, significantly improving the perceived performance for the end user.

- **Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.**

A core tenet of REST is idempotency, particularly for the DELETE verb. An operation is idempotent if its side effect on the server is the same whether it is called once or ten times. In my implementation, the first DELETE request successfully removes the room. If the client perhaps due to a network glitch sends the same request again, the room is already gone. While the status code might change from 204 No Content to 404 Not Found, the state of the system remains identical. This provides a safety net for clients, ensuring that accidental duplicate requests don't cause unintended side effects.

# 3. Sensor Operation and Linking

- **We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?**

Content negotiation is the handshake that ensures the client and server are synchronized on data formats. By explicitly marking our methods with @Consumes(MediaType.APPLICATION_JSON), we set a strict contract. If a client attempts to push data as text/plain or application/xml, the JAX-RS runtime will automatically intervene and return an HTTP 415 Unsupported Media Type error. This fail-fast approach is vital for system stability, it prevents malformed or unexpected data types from ever reaching our business logic, where they could cause unhandled exceptions or data corruption.

- **You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?**

When it comes to filtering collections, finding sensors by type, query parameters are architecturally superior to path variables. In REST, paths should represent the nouns or resources (the sensors themselves), while query parameters represent the adjectives or filters (the type of sensor). Using query parameters like ?type=CO2 provides much better flexibility. It allows users to combine multiple filters such as filtering by both type and status without creating a rigid and confusing URL hierarchy that would be impossible to maintain as the system grows.

# 4. Deep Nesting with Sub Resources

- **Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?**

The Sub-Resource Locator pattern is a sophisticated design choice that promotes the Separation of Concerns. By delegating the logic for sensor readings to a dedicated SensorReadingResource class rather than keeping it in the main SensorResource, we maintain high cohesion. In large scale APIs, defining every nested path within a single God Class leads to a codebase that is fragile and difficult to navigate. By isolating nested logic, we ensure that the parent resource only handles the dispatching of the request, while the sub resource class manages the specific business logic for that context. This modularity makes the API significantly easier to unit test, debug, and scale as the Smart Campus infrastructure expands.

# 5. Advance Error Handling, Exception Mapping and Logging

- **Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**

Choosing the correct HTTP status code is vital for clear communication between the client and server. While a 404 Not Found typically indicates that the URI itself does not exist, an HTTP 422 Unprocessable Entity is semantically more precise when the request is syntactically correct but logically flawed. In our scenario, the client has reached a valid endpoint with a valid JSON structure, but the data inside (the roomId) refers to a nonexistent entity. Using 422 informs the client that their request was understood, but the server cannot process the instructions because of a failed dependency, allowing the client developer to distinguish between a broken link and a data error.

- **From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?**

From a security perspective, raw Java stack traces are a form of Information Leakage. Exposing a stack trace to an external consumer provides a blueprint of the server's internal architecture to potential attackers. A trace can reveal specific library versions (such as Grizzly or Jersey), internal class structures, and even snippets of source code logic. An attacker can use this information to identify known vulnerabilities in specific software versions or map out the server's file system and logic flow to plan a targeted exploit. Implementing a Global Safety Net with an ExceptionMapper ensures that we provide a sanitized, generic error message to the user while keeping the sensitive technical details hidden behind the server's firewall.

- **Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?**

Logging and observability are classic examples of cross cutting concerns functionalities that affect the entire system rather than a single business method. Implementing logging via JAX-RS ContainerFilters is far superior to manually inserting log statements into every resource. Using a filter follows the DRY principle, allowing us to implement global logging in a single, centralized location. This ensures that every request and response is captured in a consistent format automatically, without cluttering the business logic. If our logging requirements change in the future, we only need to modify one class rather than updating dozens of individual methods, significantly reducing the risk of human error and code duplication.
