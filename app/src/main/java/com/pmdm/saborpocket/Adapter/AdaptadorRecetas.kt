package com.pmdm.saborpocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pmdm.saborpocket.Entities.RecetaEntity
import com.pmdm.saborpocket.R
import com.pmdm.saborpocket.databinding.RecipeItemBinding

class AdaptadorRecetas(
    private var listaRecetas: MutableList<RecetaEntity>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<AdaptadorRecetas.ViewHolder>() {

    private lateinit var contexto: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = RecipeItemBinding.bind(view)

        fun establecerListener(receta: RecetaEntity) {
            with(binding) {
                root.setOnClickListener { listener.alHacerClic(receta) }

                btnLike.setOnClickListener { listener.alDarleAFavorito(receta) }

                root.setOnLongClickListener {
                    listener.alEliminar(receta)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = listaRecetas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val receta = listaRecetas[position]

        with(holder) {
            establecerListener(receta)

            with(binding) {
                tvTitle.text = receta.label
                tvTime.text = receta.totalTime.toString() + " min"

                val icono = if (receta.esFavorita) R.drawable.ic_like else R.drawable.ic_unlike
                btnLike.setImageResource(icono)

                Glide.with(binding.tvImage.context)
                    .load(receta.image)
                    .error(R.drawable.image_recipe_not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding.tvImage)
            }
        }
    }

    fun agregar(receta: RecetaEntity) {
        listaRecetas.add(receta)
        notifyDataSetChanged()
    }

    fun establecerRecetas(recetas: MutableList<RecetaEntity>) {
        this.listaRecetas = recetas
        notifyDataSetChanged()
    }

    fun actualizar(receta: RecetaEntity) {
        val indice = listaRecetas.indexOfFirst { it.id == receta.id }

        if (indice != -1) {
            listaRecetas[indice] = receta
            notifyItemChanged(indice)
        }
    }

    fun eliminar(receta: RecetaEntity) {
        val indice = listaRecetas.indexOf(receta)

        if (indice != -1) {
            listaRecetas.removeAt(indice)
            notifyItemRemoved(indice)
        }
    }
}