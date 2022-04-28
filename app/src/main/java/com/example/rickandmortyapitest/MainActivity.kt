package com.example.rickandmortyapitest

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val api: Api by lazy {
        getRequest().create(Api::class.java)
    }

    private lateinit var character: Character

    lateinit var adapter: CharacterAdapter

    private var retrofit: Retrofit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewCharacter.layoutManager = LinearLayoutManager(this)

        buttonBack.setOnClickListener {
            var pageId: Any? = character.info.prev
            if (pageId == null) {
                Toast.makeText(it.context, "Такой страницы не существует", Toast.LENGTH_SHORT)
                    .show()
            } else {
                pageId = pageId.toString().substring(48)
                getCharacterPage(pageId)
            }
        }

        buttonNext.setOnClickListener {
            var pageId: Any? = character.info.next
            if (pageId == null) {
                Toast.makeText(it.context, "Такой страницы не существует", Toast.LENGTH_SHORT)
                    .show()
            } else {
                pageId = pageId.toString().substring(48)
                getCharacterPage(pageId as String)
            }
        }
        getCharacters()
    }

    private fun getCharacters() {
        api.getCharacter().enqueue(object : Callback<Character> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                character = response.body() as Character
                adapter = CharacterAdapter(object : CharacterActionListener {
                    override fun onCharacterDetails(id: String) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID, id)
                        ContextCompat.startActivity(this@MainActivity, intent, null)
                    }
                }, baseContext, character)
                adapter.notifyDataSetChanged()
                recyclerViewCharacter.adapter = adapter
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
            }
        })
    }

    private fun getCharacterPage(pageId: String) {
        api.getPage(pageId).enqueue(object : Callback<Character> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                character = response.body() as Character
                adapter = CharacterAdapter(object : CharacterActionListener {
                    override fun onCharacterDetails(id: String) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID, id)
                        ContextCompat.startActivity(this@MainActivity, intent, null)
                    }
                }, baseContext, character)
                adapter.notifyDataSetChanged()
                recyclerViewCharacter.adapter = adapter

            }

            override fun onFailure(call: Call<Character>, t: Throwable) {

            }
        })
    }

    private fun getRequest(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }


    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
}