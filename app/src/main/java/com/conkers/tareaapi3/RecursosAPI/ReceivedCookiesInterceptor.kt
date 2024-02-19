package com.conkers.tareaapi3.RecursosAPI

//import android.content.Context
//import okhttp3.Interceptor
//import okhttp3.Response

/**class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
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
}**/

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet(getStoredCookies())

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }

            saveCookies(cookies)
        }

        return originalResponse
    }

    private fun saveCookies(cookies: Set<String>) {
        context.getSharedPreferences(PREF_COOKIES, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(PREF_COOKIES, cookies)
            .apply()
    }

    private fun getStoredCookies(): Set<String> {
        return context.getSharedPreferences(PREF_COOKIES, Context.MODE_PRIVATE)
            .getStringSet(PREF_COOKIES, setOf()) ?: setOf()
    }

    companion object {
        const val PREF_COOKIES = "PREF_COOKIES"
    }
}