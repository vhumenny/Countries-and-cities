package com.example.countriesandcities.controller.impl;

import com.example.countriesandcities.controller.CityController;
import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.service.CityService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("api/v1/cities")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CityControllerImpl implements CityController {
    private final CityService cityService;

    @GetMapping()
    public Page<CityResponseDto> getCities(@RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "3") int pageSize) {
        return cityService.getPaginatedListOfCities(pageNo, pageSize);
    }

    @GetMapping("/unique")
    public List<String> getUniqueCitiesNames() {
        return cityService.getUniqueCitiesNames();
    }

    @GetMapping("/by-country/{countryName}")
    public List<CityResponseDto> getCitiesByCountryName(@PathVariable
                                                        @NotBlank(message = "city name can not be blank")
                                                        @Pattern(regexp = "^[a-zA-Z]{2,}$",
                                                                message = "Invalid characters or number of characters. " +
                                                                        "Only letters are allowed.")
                                                        String countryName) {

        return cityService.getCitiesByCountryName(countryName);
    }

    @GetMapping("/{cityName}")
    public List<CityResponseDto> getCitiesByCityName(@PathVariable
                                                     @NotBlank(message = "city name can not be blank")
                                                     @Pattern(regexp = "^[a-zA-Z]{2,}$",
                                                             message = "Invalid characters or number of characters. " +
                                                                     "Only letters are allowed.")
                                                     String cityName) {
        return cityService.getCitiesByCityName(cityName);
    }

    @PatchMapping("/city/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('EDITOR')")
    public void updateCity(@PathVariable Long id,
                           @RequestBody @Valid CityUpdateRequestDto request) {
        cityService.updateCity(id, request);
    }
}
