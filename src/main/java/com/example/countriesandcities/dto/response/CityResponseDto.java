package com.example.countriesandcities.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CityResponseDto(
        @Schema(description = "name of city", example = "Odessa") String name,
        @Schema(description = "image with logo of the city") String logo,
        @Schema(description = "name of country", example = "Ukraine") String countryName) {
}
