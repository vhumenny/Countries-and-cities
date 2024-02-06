package com.example.countriesandcities.controller;

import com.example.countriesandcities.dto.request.CityUpdateRequestDto;
import com.example.countriesandcities.dto.response.CityResponseDto;
import com.example.countriesandcities.entity.CityEntity;
import com.example.countriesandcities.entity.CountryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "Alex", roles = {"EDITOR"})
class CityControllerTest extends AbstractIntegrationTest {

    private final String countryName = "Ukraine";
    private final Long id = 1L;
    private final String logo = "odessa_ukr.png";
    private final String cityName = "Odessa";
    private CountryEntity countryEntity;
    private CityEntity cityOdessa;

    @BeforeEach
    public void setup() {
        countryEntity = new CountryEntity(id, countryName);
        cityOdessa = new CityEntity(id, cityName, logo, countryEntity);
    }

    @Test
    void whenGetCities_shouldReturnPaginatedListOfCityResponseDtos() throws Exception {
        // given
        CityResponseDto expected = CityResponseDto.builder()
                .countryName(countryName)
                .name(cityOdessa.getName())
                .logo(cityOdessa.getLogo())
                .build();

        // when/then
        mockMvc
                .perform(
                        get(UriComponentsBuilder.fromPath("/api/v1/cities")
                                .toUriString())
                                .param("pageNo", "0")
                                .param("size", "3"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[1].name", is(expected.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].logo", is(expected.logo())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.content[1].countryName", is(expected.countryName())))
        ;
    }

    @Test
    void whenGetUniqueCitiesNames_shouldReturnListOfUniqueCityNames() throws Exception {
        // given
        List<String> expected = List.of("New-York", "Istanbul", "Paris");
        // when/then
        mockMvc
                .perform(
                        get(UriComponentsBuilder.fromPath("/api/v1/cities/unique")
                                .toUriString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]", in(expected)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]", in(expected)));
    }

    @Test
    void whenGetCitiesByCountryName_shouldReturnListOfCityResponseDtos() throws Exception {
        // given
        CityResponseDto expected = CityResponseDto.builder()
                .countryName(countryName)
                .name(cityOdessa.getName())
                .logo(cityOdessa.getLogo())
                .build();

        // when/then
        mockMvc
                .perform(
                        get(UriComponentsBuilder.fromPath("/api/v1/cities/by-country/Ukraine")
                                .toUriString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[1].name", is(expected.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].logo", is(expected.logo())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$[1].countryName", is(expected.countryName())));
    }

    @Test
    void whenUpdateCity_shouldUpdateCityInfo() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.png",
                "text/plain", "some xml".getBytes());
        CityUpdateRequestDto dto = CityUpdateRequestDto.builder()
                .name("new-york").logo(file)
                .build();

        // when/then
        mockMvc
                .perform(
                        multipart(HttpMethod.PATCH,"/api/v1/cities/city/7")
                                .file(file)
                                .param("name", dto.name())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is2xxSuccessful());
    }
}
