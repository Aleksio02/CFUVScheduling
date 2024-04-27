package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET

@Serializable
data class GroupModel(
    val name: String
)

interface AdminApiService {
    @GET("/admin/group/findAll")
    suspend fun getGroups(): Response<List<GroupModel>>
}
