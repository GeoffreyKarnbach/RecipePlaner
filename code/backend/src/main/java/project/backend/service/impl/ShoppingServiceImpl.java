package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.dto.PlannedShoppingCreationDto;
import project.backend.dto.PlannedShoppingDto;
import project.backend.entity.PlannedShopping;
import project.backend.exception.NotFoundException;
import project.backend.repository.PlannedShoppingRepository;
import project.backend.service.ShoppingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final PlannedShoppingRepository plannedShoppingRepository;

    @Override
    public PlannedShoppingDto createPlannedShopping(PlannedShoppingCreationDto plannedShoppingCreationDto) {

        PlannedShopping plannedShopping = new PlannedShopping();
        plannedShopping.setComment(plannedShoppingCreationDto.getComment());
        plannedShopping.setDate(plannedShoppingCreationDto.getDate());
        plannedShopping.setIsMorning(plannedShoppingCreationDto.getIsMorning());

        plannedShopping = plannedShoppingRepository.save(plannedShopping);

        PlannedShoppingDto plannedShoppingDto = PlannedShoppingDto.builder()
            .id(plannedShopping.getId())
            .comment(plannedShoppingCreationDto.getComment())
            .date(plannedShoppingCreationDto.getDate())
            .isMorning(plannedShoppingCreationDto.getIsMorning()).build();

        return plannedShoppingDto;
    }

    @Override
    public Map<Integer, List<PlannedShoppingDto>> getPlannedShopping(int year, int month) {

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, month, firstDayOfMonth.lengthOfMonth());

        List<PlannedShopping> plannedShoppings = plannedShoppingRepository.findPlannedShoppingBetweenDates(firstDayOfMonth, lastDayOfMonth);

        Map<Integer, List<PlannedShoppingDto>> plannedShoppingDtoMap = new HashMap<>();

        for (PlannedShopping plannedShopping : plannedShoppings) {
            PlannedShoppingDto plannedShoppingDto = new PlannedShoppingDto();
            plannedShoppingDto.setId(plannedShopping.getId());
            plannedShoppingDto.setComment(plannedShopping.getComment());
            plannedShoppingDto.setDate(plannedShopping.getDate());
            plannedShoppingDto.setIsMorning(plannedShopping.getIsMorning());

            plannedShoppingDtoMap.computeIfAbsent(plannedShopping.getDate().getDayOfMonth(), k -> new ArrayList<>());

            List<PlannedShoppingDto> currentContent = plannedShoppingDtoMap.get(plannedShopping.getDate().getDayOfMonth());
            currentContent.add(plannedShoppingDto);

            plannedShoppingDtoMap.put(plannedShopping.getDate().getDayOfMonth(), currentContent);
        }

        return plannedShoppingDtoMap;
    }

    @Override
    public PlannedShoppingDto getPlannedShoppingById(Long id) {

        Optional<PlannedShopping> plannedShoppingOptional = plannedShoppingRepository.findById(id);
        if (plannedShoppingOptional.isEmpty()) {
            throw new NotFoundException("Planned shopping session with id " + id + " does not exist");
        }

        PlannedShopping plannedShopping = plannedShoppingOptional.get();

        PlannedShoppingDto plannedShoppingDto = PlannedShoppingDto.builder()
            .id(plannedShopping.getId())
            .comment(plannedShopping.getComment())
            .date(plannedShopping.getDate())
            .isMorning(plannedShopping.getIsMorning()).build();

        return plannedShoppingDto;
    }
}
