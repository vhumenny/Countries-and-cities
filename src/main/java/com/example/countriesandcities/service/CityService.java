package com.example.countriesandcities.service;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.dto.response.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CityService {

    List<CityResponseDto> getCitiesByCountryName(String name);

    List<CityResponseDto> getCitiesByCityName(String cityName);

    List<String> getUniqueCitiesNames();

    void updateCity(Long cityId, CityUpdateRequestDto cityUpdateRequestDto);

    Page<CityResponseDto> getPaginatedListOfCities(int pageNo, int size);
}
