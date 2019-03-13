package ai.bale.mapbaz.http

import org.apache.http.client.fluent.Request

class HttpUtil {

    public constructor() {
    }

    fun get(url: String, parameters: List<String>, headers: Map<String, String>) : String {
        var request = Request.Get(url + "?" + parametersToString(parameters))
        for ((k, v) in headers) {
            request = request.addHeader(k, v)
        }

        return request.execute().returnContent().asString()
    }

    private fun parametersToString(params: List<String>) : String {
        var res = ""
        for (i in 0 until params.size) {
            res += params[i] + "&"
        }
        res += params.last()
        return res
    }
}
