package com.example.countriesandcities.repository;

import com.example.countriesandcities.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    List<CityEntity> getCitiesByCountry_Id(Long countryId);

    @Query("select c.name from CityEntity c group by c.name having count(c.name)=1")
    List<String> getUniqueCityNames();

    List<CityEntity> findByName(String name);
}