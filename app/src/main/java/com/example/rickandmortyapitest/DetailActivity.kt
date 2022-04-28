package com.example.rickandmortyapitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    private val api: Api by lazy {
        getRequest().create(Api::class.java)
    }

    lateinit var result: Result

    private var retrofit: Retrofit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //api = getRequest(BASE_URL).create(Api::class.java)

        val id = intent.getStringExtra(ID)!!

        getCharacter(id)
    }

    private fun getCharacter(id: String) = api.getDetail(id).enqueue(object : Callback<Result> {
        override fun onResponse(call: Call<Result>, response: Response<Result>) {
            result = response.body() as Result

            Glide.with(this@DetailActivity).load(result.image).into(imageViewCharacterDetail)
            textNameDetail.text = result.name
            textGenderDetail.text = result.gender
            textSpeciesDetail.text = result.species
            textStatusDetail.text = result.status
            textViewLastPlace.text = result.location.name
            textCountEpisodes.text = result.episode.size.toString()
        }

        override fun onFailure(call: Call<Result>, t: Throwable) {

        }
    })

    private fun getRequest(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val ID = "ID"
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}