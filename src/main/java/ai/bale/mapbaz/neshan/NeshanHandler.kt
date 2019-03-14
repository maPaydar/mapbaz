package ai.bale.mapbaz.neshan

import ai.bale.mapbaz.Constants
import ai.bale.mapbaz.http.HttpUtil
import ai.bale.mapbaz.neshan.entity.Location
import ai.bale.mapbaz.neshan.entity.Routes
import com.fasterxml.jackson.databind.ObjectMapper

class NeshanHandler {

    private val objectMapper = ObjectMapper()
    private var httpUtil = HttpUtil()

    fun getRoutes(origin: String, destination: String, avoidTrafficZone: Boolean, avoidOddEvenZone: Boolean) : Routes {
        val response = httpUtil.get(Constants.NESHAN_URL,
                listOf("origin=$origin", "destination=$destination", "avoidTrafficZone=$avoidTrafficZone", "avoidOddEvenZone=$avoidOddEvenZone"),
                mapOf(Pair("Api-Key", Constants.NESHAN_API_KEY)))
        return objectMapper.readValue(response, Routes::class.java)
    }
}
