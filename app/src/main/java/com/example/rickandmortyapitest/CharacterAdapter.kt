package com.example.rickandmortyapitest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list.view.*


interface CharacterActionListener {
    fun onCharacterDetails(id: String)
}

class CharacterAdapter(
    private val actionListener: CharacterActionListener,
    private val context: Context,
    private val characterList: Character
) :
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    class ViewHolder(

        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.imageCharacter
        val name: TextView = itemView.textNameCharacter
        val gender: TextView = itemView.textGenderCharacter
        val species: TextView = itemView.textSpeciesCharacter
        var id: Int? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = characterList.results[position].id.toString()
        holder.itemView.setOnClickListener {
            actionListener.onCharacterDetails(id)
        }

        Glide.with(context).load(characterList.results[position].image).into(holder.image)
        holder.name.text = characterList.results[position].name
        holder.gender.text = characterList.results[position].gender
        holder.species.text = characterList.results[position].species
        holder.id = characterList.results[position].id
    }

    override fun getItemCount(): Int = characterList.results.size
}