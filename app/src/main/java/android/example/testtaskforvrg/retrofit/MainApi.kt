package android.example.testtaskforvrg.retrofit

import retrofit2.http.GET

interface MainApi {
    @GET("top.json")
    suspend fun getTopPosts(): Listing

}