package project.backend.service;

import project.backend.dto.PlannedShoppingCreationDto;
import project.backend.dto.PlannedShoppingDto;
import project.backend.exception.NotFoundException;

import java.util.List;
import java.util.Map;

public interface ShoppingService {

    /**
     * Creates a new planned shopping session
     *
     * @param plannedShoppingDto the planned shopping session to create
     * @return the created planned shopping session, including its id
     */
    PlannedShoppingDto createPlannedShopping(PlannedShoppingCreationDto plannedShoppingDto);


    /**
     * Returns all planned shopping sessions for a specific month.
     *
     * @param year the year of the month
     * @param month the month to get the planned shopping sessions for
     *
     * @return the planned shopping sessions for the given month
     */
    Map<Integer, List<PlannedShoppingDto>> getPlannedShopping(int year, int month);


    /**
     * Returns a planned shopping session by id
     *
     * @param id the id of the planned shopping session
     * @return the planned shopping session
     * @throws NotFoundException if the planned shopping session does not exist
     */
    PlannedShoppingDto getPlannedShoppingById(Long id);
}
