package project.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class ExampleGenerator {

    @PostConstruct
    private void generateData() {
        System.out.println("DATA");
    }
}
