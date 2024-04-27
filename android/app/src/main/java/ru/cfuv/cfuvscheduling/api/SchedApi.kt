package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

enum class NetErrors {
    NO_INTERNET, TIMEOUT, SERVERSIDE, UNKNOWN
}

data class NetStatus(
    val ok: Boolean,
    val error: NetErrors? = null
)

val JSON_MEDIA_TYPE = "application/json".toMediaType()
const val ADMIN_URL = "http://192.168.1.3:8082/"  // Temporary address
const val TT_URL = "http://192.168.1.3:8080/"  // Temporary address

object SchedApi {
    private val json = Json {
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            contextual(LocalTimeSerializer)
        }
    }

    private val adminRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE))
        .baseUrl(ADMIN_URL)
        .build()

    private val ttRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE))
        .baseUrl(TT_URL)
        .build()

    val admin: AdminApiService by lazy { adminRetrofit.create(AdminApiService::class.java) }
    val timetable: TTManagerApiService by lazy { ttRetrofit.create(TTManagerApiService::class.java) }
}
