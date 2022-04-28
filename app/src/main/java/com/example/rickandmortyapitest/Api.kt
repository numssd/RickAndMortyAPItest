package com.example.rickandmortyapitest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("character")
    fun getCharacter(): Call<Character>

    @GET("character/?")
    fun getPage(@Query("page") pageId: String): Call<Character>

    @GET("character/{id}")
    fun getDetail(@Path("id") id: String): Call<Result>

}