package com.abhi.inc.tasknote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ApiTask(
    val id: Int,
    val title: String,
    val completed: Boolean
)

interface ApiService {
    @GET("todos")
    suspend fun getTasks(): List<ApiTask>
}

object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}