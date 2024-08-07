package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRatingDto {

    private Long recipeId;

    private Long id;

    private int rating;

    private String comment;

    private String title;

    private LocalDate date;
}
