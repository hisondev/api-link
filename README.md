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
    <version>1.0.2</version>
</dependency>
```

## Usage
### Basic Setup
The api-link library allows you to simplify your Spring application's controller layer. Here's how to set it up:

1. **Define your service methods:**
Create api controller that you want to expose through the `api-link`
```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.api.controller.ApiLink;

@RestController
@RequestMapping("/hison-api-link")
public class ApiController extends ApiLink{
}
```

2. **Create a custom data converter:**
Define a class that extends `DataConverterDefault` and override necessary methods for customization.

```java
import org.springframework.stereotype.Service;

@Service
public class MyService {
    public void myMethod(DataWrapper data) {
        // Your business logic here
    }
}
```

3. **Configure the API Link controller:**
Set up the `ApiLink` controller to handle HTTP requests and invoke your service methods.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
4. **Making API Calls**
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

## Contributing
Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request on GitHub. Make sure to follow the project's code style and add tests for any new features or changes.

## License
MIT License

## Authors
Hani Son
hison0319@gmail.com
