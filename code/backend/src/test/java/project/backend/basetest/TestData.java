package project.backend.basetest;

import java.util.ArrayList;
import java.util.List;

public interface TestData {

    String BASE_URI = "/api/v1";

    String LOGIN_BASE_URI = BASE_URI + "/authentication";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };

    String DEFAULT_USER = "user@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

}
