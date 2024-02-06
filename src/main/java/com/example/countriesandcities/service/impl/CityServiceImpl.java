package com.example.countriesandcities.service.impl;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.entity.CityEntity;
import com.example.countriesandcities.entity.CountryEntity;
import com.example.countriesandcities.exception.ClientException;
import com.example.countriesandcities.exception.ImageUploadingException;
import com.example.countriesandcities.mapper.CityMapper;
import com.example.countriesandcities.repository.CityRepository;
import com.example.countriesandcities.repository.CountryRepository;
import com.example.countriesandcities.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CityMapper cityMapper;

    public static final String LOCATION_IMAGES =
            "C:/Users/v.gumennyi/IdeaProjects/Countries-and-cities/src/main/resources/images/logos";

    @Override
    public List<CityResponseDto> getCitiesByCountryName(String name) {
        log.debug("{getCitiesByCountryName}.start -> Getting cities by countryName: {}", name);

        CountryEntity countryEntity = countryRepository.findByName(name).orElseThrow(() ->
                new ClientException(HttpStatus.BAD_REQUEST, "Country with name %s can not be found".formatted(name)));

        List<CityEntity> cities = cityRepository.getCitiesByCountry_Id(countryEntity.getId());
        List<CityResponseDto> citiesDtos = cityMapper.toDtoList(cities);

        log.debug("{getCitiesByCountryName}.end -> Getting cities: {}", citiesDtos);
        return citiesDtos;
    }

    @Override
    public List<CityResponseDto> getCitiesByCityName(String cityName) {
        log.debug("{getCitiesByCityName}.start -> Getting cities with name: {}", cityName);

        List<CityEntity> cities = cityRepository.findByName(cityName);
        List<CityResponseDto> dtoList = cityMapper.toDtoList(cities);

        log.debug("{getCitiesByCityName}.end -> Getting cities with name: {}", dtoList);
        return dtoList;
    }

    @Override
    public List<String> getUniqueCitiesNames() {
        log.debug("{getUniqueCitiesNames}.start -> getting unique cities names");

        List<String> uniqueCityNames = cityRepository.getUniqueCityNames();

        log.debug("{getUniqueCitiesNames}.end -> getting unique cities names: {}", uniqueCityNames);
        return uniqueCityNames;
    }

    @Transactional
    @Override
    public void updateCity(Long cityId, CityUpdateRequestDto cityUpdateRequestDto) {
        log.info("{updateCity}.start -> updating city with id {}", cityId);
        CityEntity cityEntity = cityRepository.findById(cityId).orElseThrow(() ->
                new ClientException(HttpStatus.BAD_REQUEST, "City with id %s can not be found".formatted(cityId)));

        cityEntity.setName(cityUpdateRequestDto.name());
        if (Objects.nonNull(cityUpdateRequestDto.logo())) {
            cityEntity.setLogo(getLogoPart(cityUpdateRequestDto.logo()));
        }

        log.info("{updateCity}.end -> successfully updated city with id {}", cityId);
    }

    @Override
    public Page<CityResponseDto> getPaginatedListOfCities(int pageNo, int size) {
        log.debug("{getPaginatedListOfCities}.start -> getting paginated list of cities");

        Pageable pageable = PageRequest.of(pageNo, size);
        Page<CityResponseDto> cityResponseDtos = cityRepository.findAll(pageable).map(cityMapper::toDto);

        log.debug("{getPaginatedListOfCities}.end -> getting paginated list of cities: {}", cityResponseDtos);
        return cityResponseDtos;
    }

    private String getLogoPart(MultipartFile logoFile) {
        String logoName = null;
        try {
            if (logoFile.getSize() > 0) {
                logoName = UUID.randomUUID() + "_" + logoFile.getOriginalFilename();
                String location = "logos/";
                File pathFile = new File(LOCATION_IMAGES);
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
                pathFile = new File(location + logoName);
                logoFile.transferTo(pathFile);
            }
        } catch (IOException e) {
            log.error("Error while uploading image: {}", e.getMessage(), e);
            throw new ImageUploadingException(HttpStatus.BAD_REQUEST, ImageUploadingException.IMAGE_ERROR);
        }
        return logoName;
    }
}
