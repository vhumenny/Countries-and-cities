package com.example.countriesandcities.service;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {

    List<CityResponseDto> getCitiesByCountryName(String name);

    List<CityResponseDto> getCitiesByCityName(String cityName);

    List<String> getUniqueCitiesNames();

    void updateCity(Long cityId, CityUpdateRequestDto cityUpdateRequestDto);

    Page<CityResponseDto> getPaginatedListOfCities(int pageNo, int size);
}
