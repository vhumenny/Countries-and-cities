package com.example.countriesandcities.controller;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.dto.response.MessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public interface CityController {

    @Operation(summary = "Get paginated list of cities")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Information provided",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                    @ArraySchema(
                                            schema = @Schema(implementation = CityResponseDto.class))))})
    Page<CityResponseDto> getCities(@RequestParam(defaultValue = "0") int pageNo,
                                    @RequestParam(defaultValue = "3") int pageSize);

    @Operation(summary = "Get paginated list of cities")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Information provided",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                    @ArraySchema(
                                            schema = @Schema(implementation = String.class))))})
    List<String> getUniqueCitiesNames();

    @Operation(summary = "Get cities by country name")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Information provided",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                    @ArraySchema(
                                            schema = @Schema(implementation = CityResponseDto.class)))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cannot find country entity",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MessageResponseDto.class)))
            })
    List<CityResponseDto> getCitiesByCountryName(@PathVariable
                                                 @NotBlank(message = "city name can not be blank")
                                                 @Pattern(regexp = "^[a-zA-Z]{2,}$",
                                                         message = "Invalid characters or number of characters. " +
                                                                 "Only letters are allowed.")
                                                 String countryName);


    @Operation(summary = "Get cities by city name")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Information provided",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                    @ArraySchema(
                                            schema = @Schema(implementation = CityResponseDto.class))))
            })
    List<CityResponseDto> getCitiesByCityName(@PathVariable
                                              @NotBlank(message = "city name can not be blank")
                                              @Pattern(regexp = "^[a-zA-Z]{2,}$",
                                                      message = "Invalid characters or number of characters. " +
                                                              "Only letters are allowed.")
                                              String cityName);

    @Operation(summary = "Update city info")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "City info is changed",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array =
                                    @ArraySchema(
                                            schema = @Schema(implementation = MessageResponseDto.class)))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cannot find city entity",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MessageResponseDto.class)))
            })
    void updateCity(@PathVariable Long id,
                    @RequestBody @Valid CityUpdateRequestDto request);
}
