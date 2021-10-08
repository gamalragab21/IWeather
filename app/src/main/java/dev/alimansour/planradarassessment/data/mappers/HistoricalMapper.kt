package dev.alimansour.planradarassessment.data.mappers

import dev.alimansour.planradarassessment.data.local.entity.City
import dev.alimansour.planradarassessment.data.local.entity.Historical
import dev.alimansour.planradarassessment.domain.model.CityData
import dev.alimansour.planradarassessment.domain.model.HistoricalData

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
object HistoricalMapper : Mapper<List<Historical>, List<HistoricalData>> {

    override fun mapFromEntity(type: List<Historical>): List<HistoricalData> {
        val data = mutableListOf<HistoricalData>()
        type.forEach {
            data.add(
                HistoricalData(
                    it.id,
                    CityData(it.city.cityId, it.city.name, it.city.country),
                    it.icon,
                    it.date,
                    it.description,
                    it.temperature,
                    it.humidity,
                    it.windSpeed
                )
            )
        }
        return data
    }

    override fun mapToEntity(type: List<HistoricalData>): List<Historical> {
        val data = mutableListOf<Historical>()
        type.forEach {
            data.add(
                Historical(
                    it.id,
                    City(it.city.id, it.city.name, it.city.country),
                    it.icon,
                    it.date,
                    it.description,
                    it.temperature,
                    it.humidity,
                    it.windSpeed
                )
            )
        }
        return data
    }
}