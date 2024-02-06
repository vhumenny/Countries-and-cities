package com.example.countriesandcities.service;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.entity.CityEntity;
import com.example.countriesandcities.entity.CountryEntity;
import com.example.countriesandcities.exception.ClientException;
import com.example.countriesandcities.mapper.CityMapper;
import com.example.countriesandcities.repository.CityRepository;
import com.example.countriesandcities.repository.CountryRepository;
import com.example.countriesandcities.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    private CityService cityService;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CityMapper cityMapper;
    @Mock
    private MultipartFile multipartFile;
    private final String countryName = "Ukraine";
    private final Long id = 1L;
    private final String logo = "logo";
    private final String cityName = "Odessa";
    private CountryEntity countryEntity;
    private CityEntity cityOdessa;


    @BeforeEach
    public void setup() {
        cityService = new CityServiceImpl(cityRepository, countryRepository, cityMapper);
        countryEntity = new CountryEntity(id, countryName);
        cityOdessa = new CityEntity(id, cityName, logo, countryEntity);
    }

    @Test
    void whenGetCitiesByCountryName_thenSuccess() {
        // given
        List<CityEntity> citiesList = List.of(cityOdessa);
        List<CityResponseDto> dtoList = List.of(CityResponseDto.builder()
                .countryName(countryName)
                .name(cityOdessa.getName())
                .logo(cityOdessa.getLogo())
                .build());

        when(countryRepository.findByName(countryName)).thenReturn(Optional.of(countryEntity));
        when(cityRepository.getCitiesByCountry_Id((countryEntity.getId()))).thenReturn(citiesList);
        when(cityMapper.toDtoList(citiesList)).thenReturn(dtoList);

        // when
        final List<CityResponseDto> result = cityService.getCitiesByCountryName(countryName);

        // then
        assertEquals(dtoList.get(0).name(), result.get(0).name());
        assertEquals(dtoList.get(0).logo(), result.get(0).logo());
        assertEquals(dtoList.get(0).countryName(), result.get(0).countryName());
        verify(countryRepository, times(1)).findByName(countryName);
        verify(cityRepository, times(1)).getCitiesByCountry_Id(countryEntity.getId());
        verify(cityMapper, times(1)).toDtoList(citiesList);
    }

    @Test
    void whenGetCitiesByCountryName_thenFail() {
        // given
        when(countryRepository.findByName(countryName)).thenReturn(Optional.empty());

        // when then
        assertThrows(ClientException.class, () -> cityService.getCitiesByCountryName(countryName));
    }

    @Test
    void whenGetCitiesByCityName_thenSuccess() {
        // given
        List<CityEntity> citiesList = List.of(cityOdessa);
        List<CityResponseDto> dtoList = List.of(CityResponseDto.builder()
                .countryName(countryName)
                .name(cityOdessa.getName())
                .logo(cityOdessa.getLogo())
                .build());

        when(cityRepository.findByName(cityName)).thenReturn(citiesList);
        when(cityMapper.toDtoList(citiesList)).thenReturn(dtoList);

        // when
        final List<CityResponseDto> result = cityService.getCitiesByCityName(cityName);

        // then
        assertEquals(dtoList.get(0).name(), result.get(0).name());
        assertEquals(dtoList.get(0).logo(), result.get(0).logo());
        assertEquals(dtoList.get(0).countryName(), result.get(0).countryName());
        verify(cityRepository, times(1)).findByName(cityName);
        verify(cityMapper, times(1)).toDtoList(citiesList);
    }

    @Test
    void whenGetUniqueCitiesNames_thenSuccess() {
        // given
        List<String> citiesNamesList = List.of("Odessa", "Kyiv", "Paris");
        when(cityRepository.getUniqueCityNames()).thenReturn(citiesNamesList);

        // when
        final List<String> result = cityService.getUniqueCitiesNames();

        // then
        assertEquals(citiesNamesList.get(0), result.get(0));
        assertEquals(citiesNamesList.get(1), result.get(1));
        assertEquals(citiesNamesList.get(2), result.get(2));
        verify(cityRepository, times(1)).getUniqueCityNames();
    }

    @Test
    void whenUpdateCity_WithExistingCityIdAndNoLogo_thenSuccess() {
        //given
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(id);
        cityEntity.setName("Old City Name");
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto("New name", null);
        when(cityRepository.findById(id)).thenReturn(Optional.of(cityEntity));

        //when
        cityService.updateCity(id, cityUpdateRequestDto);

        //then
        assertEquals("New name", cityEntity.getName());
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).save(cityEntity);
    }

    @Test
    void whenUpdateCity_WithExistingCityIdAndLogo_thenSuccess() throws IOException {
        //given
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(id);
        cityEntity.setName("Old City Name");
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto("New name", multipartFile);
        when(cityRepository.findById(id)).thenReturn(Optional.of(cityEntity));
        when(cityRepository.save(cityEntity)).thenReturn(cityEntity);
        when(multipartFile.getOriginalFilename()).thenReturn("logo.png");
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(mock(InputStream.class));

        //when
        cityService.updateCity(id, cityUpdateRequestDto);

        //then
        assertEquals("New name", cityEntity.getName());
        assertNotNull(cityEntity.getLogo());
        verify(cityRepository, times(1)).findById(id);
        verify(cityRepository, times(1)).save(cityEntity);
    }

    @Test
    void whenUpdateCity_WithNonExistingCityId_thenFail() {
        //given
        CityUpdateRequestDto cityUpdateRequestDto = new CityUpdateRequestDto("New name", null);
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        //when/then
        assertThrows(ClientException.class, () -> cityService.updateCity(2L, cityUpdateRequestDto));
    }

    @Test
    void whenGetPaginatedListOfCities_thenSuccess() {
        // given
        int pageNo = 0;
        int size = 3;
        CityResponseDto dto = CityResponseDto.builder()
                .countryName(countryName)
                .name(cityOdessa.getName())
                .logo(cityOdessa.getLogo())
                .build();
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<CityEntity> cityEntityPage = new PageImpl<>(List.of(cityOdessa));

        when(cityRepository.findAll(pageable)).thenReturn(cityEntityPage);
        when(cityMapper.toDto(cityOdessa)).thenReturn(dto);

        // when
        final Page<CityResponseDto> result = cityService.getPaginatedListOfCities(pageNo, size);

        // then
        assertEquals(dto.name(), result.get().toList().get(0).name());
        assertEquals(dto.logo(), result.get().toList().get(0).logo());
        assertEquals(dto.countryName(), result.get().toList().get(0).countryName());
    }
}

