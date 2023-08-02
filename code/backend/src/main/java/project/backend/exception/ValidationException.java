package project.backend.exception;

import project.backend.dto.ValidationErrorRestDto;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final ValidationErrorRestDto validationErrorRestDto;

    public ValidationException(ValidationErrorRestDto validationErrorRestDto) {
        super(validationErrorRestDto.getMessage());
        this.validationErrorRestDto = validationErrorRestDto;
    }

}
