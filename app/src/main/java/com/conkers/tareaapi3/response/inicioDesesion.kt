package com.conkers.tareaapi3.response

import android.content.Context
import androidx.navigation.NavController
import com.conkers.tareaapi3.RecursosAPI.AddCookiesInterceptor
import com.conkers.tareaapi3.RecursosAPI.ReceivedCookiesInterceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

fun incioDesesion(
    context: Context,
    username: String,
    password: String,
    navController: NavController,
    onResult: (Boolean, String?, String?) -> Unit
) {
    try {
        val requestBody = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
                <soapenv:Header/>
                <soapenv:Body>
                    <tem:accesoLogin>
                        <tem:username>$username</tem:username>
                        <tem:password>$password</tem:password>
                        <tem:tipoUsuario>ALUMNO</tem:tipoUsuario>
                    </tem:accesoLogin>
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent().toRequestBody("text/xml".toMediaTypeOrNull())

        val client = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor(context))
            .addInterceptor(ReceivedCookiesInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx")
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        val service = retrofit.create(LoginServicio::class.java)

        val call = service.login(requestBody)
        val response = call.execute()
        if (response.isSuccessful) {
            val responseBody = response.body()?.string()
            val receivedCookie = response.headers()["Set-Cookie"]
            onResult(true, responseBody, receivedCookie)
        } else {
            onResult(false, "Error de conexión: ${response.code()}", null)
        }
    } catch (e: IOException) {
        onResult(false, "Error de conexión: ${e.message}", null)
    } catch (e: SocketTimeoutException) {
        onResult(false, "Timeout de conexión: ${e.message}", null)
    }
}