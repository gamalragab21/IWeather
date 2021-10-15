package dev.alimansour.iweather.data.local.dao

import androidx.room.*
import dev.alimansour.iweather.data.local.entity.Historical

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@Dao
interface HistoricalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(elections: List<Historical>)

    @Query("DELETE FROM historical_data WHERE cityId =:cityId")
    fun clearCityHistorical(cityId: Int)

    @Query("SELECT * FROM historical_data WHERE cityId = :cityId")
    fun getHistoricalData(cityId: Int): List<Historical>
}