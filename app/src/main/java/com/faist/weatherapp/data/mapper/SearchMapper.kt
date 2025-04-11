package com.faist.weatherapp.data.mapper

import com.faist.weatherapp.data.network.dto.CityDto
import com.faist.weatherapp.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)

fun City.toCityDto(): CityDto = CityDto(id, name, country)

fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }