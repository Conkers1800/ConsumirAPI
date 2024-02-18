package com.conkers.tareaapi3.RecursosAPI

import android.content.Context
import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet(
                PreferenceManager.getDefaultSharedPreferences(
                    context
                ).getStringSet(AddCookiesInterceptor.PREF_COOKIES, HashSet())
            )
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            val editor = PreferenceManager.getDefaultSharedPreferences(
                context
            ).edit()
            editor.putStringSet(AddCookiesInterceptor.PREF_COOKIES, cookies).apply()
        }
        return originalResponse
    }
}

