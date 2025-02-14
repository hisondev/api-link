package io.github.hison.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hison.api.util.CorsValidator;

import javax.annotation.PostConstruct;
import java.util.List;

/** 
 * @author Hani son
 * @version 1.0.6
 */
@RestController
@RequestMapping("${hison.link.api.path:/hison-api-link}")
@CrossOrigin(
    origins = "${hison.link.api.cors.origins:*}",
    allowCredentials = "${hison.link.api.cors.allow-credentials:false}"
)
@ConditionalOnMissingBean(ApiLink.class)
public class ApiLinkController extends ApiLink {

    @Value("${hison.link.api.cors.origins:*}")
    private String corsOrigins;

    @Value("${hison.link.api.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${hison.link.api.status.message:Hison API is running.}")
    private String statusMessage;

    @PostConstruct
    public void validateCorsConfig() {
        List<String> origins = CorsValidator.parseOrigins(corsOrigins);
        CorsValidator.validateCorsSettings(origins, allowCredentials);
    }

    @RequestMapping("/status")
    public String status() {
        return statusMessage;
    }
}
