package project.backend.exception;

import project.backend.dto.ValidationErrorRestDto;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final ValidationErrorRestDto validationErrorRestDto;

    public ConflictException(ValidationErrorRestDto validationErrorRestDto) {
        super(validationErrorRestDto.getMessage());
        this.validationErrorRestDto = validationErrorRestDto;
    }

}
