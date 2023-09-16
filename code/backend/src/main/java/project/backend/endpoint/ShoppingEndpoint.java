package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.dto.PlannedShoppingCreationDto;
import project.backend.dto.PlannedShoppingDto;
import project.backend.service.ShoppingService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/shopping")
@Slf4j
@RequiredArgsConstructor
public class ShoppingEndpoint {

    private final ShoppingService shoppingService;

    @PermitAll
    @PostMapping
    @Operation(summary = "Creates a new planned shopping session")
    @ResponseStatus(HttpStatus.CREATED)
    public PlannedShoppingDto createPlannedShopping(@RequestBody PlannedShoppingCreationDto plannedShoppingCreationDto) {
        log.info("POST /api/v1/shopping");
        log.info("Request body: {}", plannedShoppingCreationDto);

        return shoppingService.createPlannedShopping(plannedShoppingCreationDto);
    }

    @PermitAll
    @GetMapping("/planned")
    @Operation(summary = "Returns all planned shopping sessions for a month")
    @ResponseStatus(HttpStatus.OK)
    public Map<Integer, List<PlannedShoppingDto>> getPlannedShopping(@RequestParam Integer year, @RequestParam Integer month) {
        log.info("POST /api/v1/shopping/planned");

        return shoppingService.getPlannedShopping(year, month);
    }

    @PermitAll
    @GetMapping("/{id}")
    @Operation(summary = "Returns a planned shopping session by id")
    @ResponseStatus(HttpStatus.OK)
    public PlannedShoppingDto getPlannedShoppingById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/shopping/{}", id);

        return shoppingService.getPlannedShoppingById(id);
    }

}
