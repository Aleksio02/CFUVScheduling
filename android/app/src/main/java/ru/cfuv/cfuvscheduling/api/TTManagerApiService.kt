package ru.cfuv.cfuvscheduling.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalTime

// Kotlinx serialization doesn't support LocalTime and LocalDate
object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

@Serializable
data class TTClassDuration(
    val number: Int,
    @Contextual
    val startTime: LocalTime,
    @Contextual
    val endTime: LocalTime
)

@Serializable
data class TTClassModel(
    val id: Int,
    val subjectName: String,
    val classroom: String,
    val duration: TTClassDuration,
    var comment: String,
    val classType: N,  // Aleksioi was too lazy to omit Int ids from response and return a String instead of object
    val teacher: UserModel
) {
    @Serializable
    data class N(
        val name: String
    )
}

@Serializable
data class ClassCreationBody(
    val subjectName: String,
    val classroom: String,
    val duration: N,
    val comment: String,
    val group: G,
    @Contextual
    val classDate: LocalDate
) {
    // Aleksioi was too lazy to accept Int instead of object
    @Serializable
    data class N(
        val number: Int
    )
    @Serializable
    data class G(
        val id: Int
    )
}

interface TTManagerApiService {
    @GET("/tt-manager/class/findClassesForGroup")
    suspend fun getClasses(
        @Query("groupName") groupName: String,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): Response<List<TTClassModel>>

    @POST("/tt-manager/class/{id}/addComment")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("comment") comment: String
    ): Response<Void>

    @POST("/tt-manager/class/createConsultation")
    suspend fun createConsultation(
        @Header("Authorization") token: String,
        @Body body: ClassCreationBody
    ): Response<Void>  // Returned object is not suitable for inserting in the timetable (Aleksioi hello...)

    @DELETE("/tt-manager/class/deleteClassByAdmin/{id}")
    suspend fun deleteClass(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<Void>
}
