package com.JavaJunkie.ImageProcessor.DTO;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@Data
public class ResizeRequest {

    @NotNull(message = "Width cannot be null")
    @Min(value = 1, message = "Width must be greater than 0")
    private Integer width;


    @NotNull(message = "Height cannot be null")
    @Min(value = 1, message = "Height must be greater than 0")
    private Integer height;
}
