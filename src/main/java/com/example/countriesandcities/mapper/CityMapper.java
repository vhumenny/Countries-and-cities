package com.example.countriesandcities.mapper;

import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.entity.CityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(source = "country.name", target = "countryName")
    CityResponseDto toDto(CityEntity cityEntity);

    List<CityResponseDto> toDtoList(List<CityEntity> cityEntityList);
}
