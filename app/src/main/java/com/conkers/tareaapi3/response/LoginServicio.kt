package com.conkers.tareaapi3.response

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginServicio {
    @Headers(
        "Content-Type: text/xml",
        "Accept-Charset: utf-8",
        "SOAPAction: http://tempuri.org/accesoLogin"
    )
    @POST("https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx")
    fun login(@Body requestBody: RequestBody): Call<ResponseBody>
}