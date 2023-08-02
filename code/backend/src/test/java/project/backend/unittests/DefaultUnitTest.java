package project.backend.unittests;

import project.backend.basetest.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class DefaultUnitTest implements TestData {

    /*
    Use Mockito when to mock the return value of the methods.
     */
}
