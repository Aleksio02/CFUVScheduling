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
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalTime

// Kotlinx serialization doesn't support LocalTime
object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}

@Serializable
data class TTClassDuration(
    @Contextual
    val startTime: LocalTime,
    @Contextual
    val endTime: LocalTime
)

@Serializable
data class TTClassModel(
    val subjectName: String,
    val classroom: String,
    val duration: TTClassDuration,
    val comment: String,
    val classType: N,  // Aleksioi was too lazy to omit Int ids from response and return a String instead of object
    val teacher: UserModel
) {
    @Serializable
    data class N(
        val name: String
    )
}

interface TTManagerApiService {
    @GET("/tt-manager/class/findClassesForGroup")
    suspend fun getClasses(
        @Query("groupName") groupName: String,
        @Query("startDate") startDate: LocalDate,
        @Query("endDate") endDate: LocalDate
    ): Response<List<TTClassModel>>
}
