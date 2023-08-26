package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSingleStepDto {

    private Long id;

    private int position;

    private String name;

    private String description;

    private String imageSource;

}
