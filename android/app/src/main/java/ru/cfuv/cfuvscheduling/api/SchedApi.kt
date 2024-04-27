package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "http://192.168.1.3:8082/"  // Temporary address

enum class NetErrors {
    NO_INTERNET, TIMEOUT, SERVERSIDE, UNKNOWN
}

data class NetStatus(
    val ok: Boolean,
    val error: NetErrors? = null
)

object SchedApi {
    private val json = Json { ignoreUnknownKeys = true }

    private val adminRetrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    val admin: AdminApiService by lazy { adminRetrofit.create(AdminApiService::class.java) }
}
