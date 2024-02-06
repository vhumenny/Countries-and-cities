package com.example.countriesandcities.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
public record CityUpdateRequestDto(
        @Schema(example = "Odessa")
        @NotBlank(message = "city name can not be blank")
        @Pattern(regexp = "^[a-zA-Z-]{2,}$",
                message = "Invalid characters or number of characters. Only letters are allowed.")
        String name,
        MultipartFile logo) {
}
