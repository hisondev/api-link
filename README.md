# api-link
API-Link is a novel solution for Spring projects, aimed at streamlining development by eliminating the need for individual controllers. It allows developers to use a single 'cmd' value to invoke service layer methods, simplifying workflow and boosting productivity.

## Introduction
This project, titled "ApiLink", is a novel solution designed to streamline the development process in Spring projects. It primarily aims to eliminate the need for developers to create individual controllers. Instead, ApiLink enables a more efficient approach by allowing developers to invoke service layer methods through a single 'cmd' value. This approach significantly simplifies the development workflow and enhances productivity.

Additionally, ApiLink incorporates support for caching in WebSocket, relying on the Spring Boot WebSocket dependency. This feature is particularly beneficial for real-time applications where performance and efficiency are crucial. Moreover, the project offers a customizable handler, providing users with the flexibility to tailor the WebSocket behavior according to their specific requirements.

The overarching goal of ApiLink is to provide a convenient, efficient, and flexible tool for developers working on Spring projects, making the development process more streamlined and effective.

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
    <version>1.0.0</version>
</dependency>
```

## Usage
### Basic Setup
The api-link library allows you to simplify your Spring application's controller layer. Here's how to set it up:

1. **Define your service methods:**
Create service methods that you want to expose through the `api-link`
1. **Create a custom data converter:**
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

2. **Configure the API Link controller:**
Set up the `ApiLink` controller to handle HTTP requests and invoke your service methods.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiLinkApplication.class, args);
    }
}
```
3. **Making API Calls**
To call a service method via the `api-link`, send an HTTP request with the 'cmd' parameter specifying the service and method names.

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

@SpringBootApplication
public class ApiLinkApplication {
    public static void main(String[] args) {
        CustomApiHandler.register();
        SpringApplication.run(ApiLinkApplication.class, args);
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

1. **Configure WebSocket settings:**
Set up WebSocket configuration using `CachingWebSocketConfig`.

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class CachingWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
```

2. **Implement WebSocket handler:**
Handle WebSocket messages and manage caching logic.

```java
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming WebSocket message and manage caching
        String payload = message.getPayload();
        // Your caching logic here
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
