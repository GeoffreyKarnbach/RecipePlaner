package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.service.ExampleService;

@RestController
@RequestMapping(value = "/api/v1/example")
@Slf4j
@RequiredArgsConstructor
public class ExampleEndpoint {

    private final ExampleService exampleService;

    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Example endpoint", security = @SecurityRequirement(name = "apiKey"))
    @ResponseStatus(HttpStatus.OK)
    public void example() {
        log.info("GET /api/v1/example");
    }
}
