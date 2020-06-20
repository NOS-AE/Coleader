package com.nosae.coleader.data

import com.squareup.moshi.JsonClass
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * Create by NOSAE on 2020/6/19
 */
interface UtilService {
    @Headers("Authorization: ")
    @Multipart
    @POST("api/utils/upload")
    suspend fun uploadFile(@Query("type") type: String, @Part file: MultipartBody.Part): UploadFileResDto

    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}

@JsonClass(generateAdapter = true)
data class UploadFileResDto(
    var errno: String, // 0
    var `data`: Data
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        var path: String // /teamwork/src/public/uploads/20200514/image/1589459016253_0.09626192025152736.docx
    )
}