package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET

@Serializable
data class GroupModel(
    val id: Int,
    val name: String
)

@Serializable
data class ClassTypeModel(
    val id: Int,
    val name: String
)

interface AdminApiService {
    @GET("/admin/group/findAll")
    suspend fun getGroups(): Response<List<GroupModel>>

    @GET("/admin/classType/findAll")
    suspend fun getClassTypes(): Response<List<ClassTypeModel>>
}
