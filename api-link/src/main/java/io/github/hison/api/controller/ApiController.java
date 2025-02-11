package io.github.hison.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hison.api.handler.CorsValidator;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("${hison.link.api.path:/hison-api-link}")
@CrossOrigin(
    origins = "${hison.link.api.cors.origins:*}",
    allowCredentials = "${hison.link.api.cors.allow-credentials:false}"
)
@ConditionalOnMissingBean(ApiLink.class)
public class ApiController extends ApiLink {

    @Value("${hison.link.api.cors.origins:*}")
    private String corsOrigins;

    @Value("${hison.link.api.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${hison.link.api.status.message:Hison API is running.}")
    private String statusMessage;

    private final CorsValidator corsValidator;

    public ApiController(CorsValidator corsValidator) {
        this.corsValidator = corsValidator;
    }

    @PostConstruct
    public void validateCorsConfig() {
        List<String> origins = corsValidator.parseOrigins(corsOrigins);
        corsValidator.validateCorsSettings(origins, allowCredentials);
    }

    @RequestMapping("/status")
    public String status() {
        return statusMessage;
    }
}
