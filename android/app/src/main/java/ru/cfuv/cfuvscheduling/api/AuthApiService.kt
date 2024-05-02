package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

@Serializable
data class UserModel(
    val username: String,
    val firstName: String? = null,
    val secondName: String? = null,
    val lastName: String? = null,
    val role: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserModel
)

@Serializable
data class LoginBody(
    val username: String,
    val password: String
)

interface AuthApiService {
    @GET("/auth/getCurrentUser")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserModel>

    @POST("/auth/registerUser")
    suspend fun registerUser(@Body body: LoginBody): Response<AuthResponse>

    @POST("/auth/authenticateUser")
    suspend fun authenticateUser(@Body body: LoginBody): Response<AuthResponse>
}
