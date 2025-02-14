# api-link [![Maven Central](https://img.shields.io/maven-central/v/io.github.hisondev/api-link.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/io.github.hisondev/api-link)
API-Link is a novel solution for Spring projects, aimed at streamlining development by eliminating the need for individual controllers. It allows developers to use a single 'cmd' value to invoke service layer methods, simplifying workflow and boosting productivity.

## Introduction
This project, titled "ApiLink", is a novel solution designed to streamline the development process in Spring projects. It primarily aims to eliminate the need for developers to create individual controllers. Instead, ApiLink enables a more efficient approach by allowing developers to invoke service layer methods through a single 'cmd' value. This approach significantly simplifies the development workflow and enhances productivity.

Additionally, ApiLink incorporates support for caching in WebSocket, relying on the Spring Boot WebSocket dependency. This feature is particularly beneficial for real-time applications where performance and efficiency are crucial. Moreover, the project offers a customizable handler, providing users with the flexibility to tailor the WebSocket behavior according to their specific requirements.

The overarching goal of ApiLink is to provide a convenient, efficient, and flexible tool for developers working on Spring projects, making the development process more streamlined and effective.

For enhanced and convenient front-end and server communication, this library can be used in conjunction with `apiLink.min.js` from [hison-js](https://github.com/hisondev/hison-js).

## Getting Started
To start using the `api-link` library in your project, follow the installation and usage instructions below.

### Prerequisites
Before you can use the `api-link` library, you need to have the following software installed on your system:
- Java Development Kit (JDK) 8 or higher
- Apache Maven (for building the project)

### Installation
You can add the `api-link` library to your project by including the following dependency in your Maven `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.hisondev</groupId>
    <artifactId>api-link</artifactId>
    <version>1.0.6</version>
</dependency>
```

## Usage
### Basic Setup
The api-link library allows you to simplify your Spring application's controller layer. Here's how to set it up:

1. **Configuration**
Automatic Bean Registration
ApiController and WebSocketConfig are automatically registered as Beans through spring.factories. This means you do not need to manually create these components.

```properties
# spring.factories configuration (handled internally)
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  io.github.hison.api.caching.WebSocketConfig,\
  io.github.hison.api.controller.ApiController
```

### Conflict Prevention
ApiController is registered only if ApiLink is not already defined in the project.
But you can use your custom Controller with extending ApiLink.
```java
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.github.hison.api.controller.ApiLink;

@RestController
@RequestMapping("hison-api-link")
@CrossOrigin("http://localhost:3000/")
public class ApiLinkController extends ApiLink {}
```

WebSocketConfig is registered only if WebSocketConfigurer is not defined.
But you can use your custom WebSocket with extending CachingWebSocket.
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import io.github.hison.api.caching.CachingWebSocket;

@Configuration
@EnableWebSocket
public class ApiLinkWebSocket implements CachingWebSocket {}
```

2. **Making API Calls**
To call a service method via the `api-link`, send an HTTP request with the 'cmd' parameter specifying the service and method names.

```java
package com.example.demo.biz.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.common.data.wrapper.DataWrapper;

@Service
public class MemberService {
    public DataWrapper getMember(@RequestBody DataWrapper dw) {
        // Your business logic here
        return dw;
    }
}
```

```bash
curl -X POST http://localhost:8080/api -d '{"cmd": "myService.myMethod", "data": {...}}' -H "Content-Type: application/json"
```
## Customizing API Handler
You can customize how the API requests are handled by implementing your own ApiHandler.

1. **Create a custom API handler:**
Extend the ApiHandlerDefault class and override its methods.

```java
import io.github.hison.api.handler.ApiHandlerDefault;
import io.github.hison.api.handler.ApiHandlerFactory;

public class CustomApiHandler extends ApiHandlerDefault {
    public static void register() {
        ApiHandlerFactory.setCustomHandler(new CustomApiHandler());
    }

    @Override
    public DataWrapper handle(DataModel dataModel, HttpServletRequest request) {
        // Custom handling logic
        return super.handle(dataModel, request);
    }
}
```

2. **Register the custom handler in your application:**
Ensure that your custom handler is registered when the application starts.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.config.CustomApiHandler;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        CustomApiHandler.register();
		SpringApplication.run(DemoApplication.class, args);
    }
}
```

## Exception Handling with ServiceRuntimeException
The `api-link` library provides a centralized exception handling mechanism using `ServiceRuntimeException`. This allows for consistent and clean error handling across your application.

1. **Throwing ServiceRuntimeException:**
Throw `ServiceRuntimeException` from your service methods when an error occurs.

```java
import io.github.hison.api.exception.ServiceRuntimeException;

public void myMethod(DataWrapper data) {
    if (data == null) {
        throw new ServiceRuntimeException("Data cannot be null");
    }
    // Your business logic here
}
```

2. **Handling exceptions globally:**
`ServiceRuntimeException` will be automatically handled by the `ApiLink` controller, returning an appropriate HTTP response.

## Caching with WebSocket
The api-link library supports caching through WebSockets, allowing real-time data updates and efficient data management.

1. **Implement WebSocket handler:**
Handle WebSocket messages and manage caching logic.

```java
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    @Override	
        public void setRegistry(WebSocketHandlerRegistration registry) {
        // You can setAllowedOrigins via setRegistry
        registry.setAllowedOrigins("http://localhost:3000/");	
    };
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming WebSocket message and manage caching
        String payload = message.getPayload();
        // Your caching logic here
    }
}
```

2. **Register the custom handler in your application:**
Ensure that your custom handler is registered when the application starts.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.config.CustomCachingHandler;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
		CustomCachingHandler.register();
		SpringApplication.run(DemoApplication.class, args);
    }
}
```

## application.properties Configuration
The api-link library provides several configuration options via application.properties, making it easy to customize behavior.

```properties
# API Path Configuration
hison.link.api.path=/hison-api-link  # Default API path
# CORS Configuration
hison.link.api.cors.origins=*                # Default: Allow all origins
hison.link.api.cors.allow-credentials=false  # Default: Do not allow credentials
# API Status Message
hison.link.api.status.message=Hison API is ready and running.
# WebSocket Endpoint Configuration
hison.link.websocket.endpoint=/hison-caching-websocket-endpoint  # Default WebSocket endpoint
```
### Explanation of Properties:
hison.link.api.path: Sets the base path for the API controller.
hison.link.api.cors.origins: Defines allowed CORS origins. Use a comma-separated list for multiple origins.
hison.link.api.cors.allow-credentials: Specifies whether credentials (cookies, authorization headers) are allowed in CORS requests.
hison.link.api.status.message: Custom status message returned by the /status endpoint.
hison.link.websocket.endpoint: Sets the WebSocket endpoint for real-time data updates.

## License
MIT License

## Authors
Hani Son
hison0319@gmail.com
