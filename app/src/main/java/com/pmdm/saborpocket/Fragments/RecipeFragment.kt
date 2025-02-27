package com.pmdm.saborpocket.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Data.Ingredient
import com.pmdm.saborpocket.Entities.UsuarioEntity
import com.pmdm.saborpocket.Entities.separarCantidadIngrediente
import com.pmdm.saborpocket.R
import com.pmdm.saborpocket.databinding.FragmentLoginBinding
import com.pmdm.saborpocket.databinding.FragmentRecipeBinding
import com.pmdm.saborpocket.databinding.IngredientItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentRecipeBinding

    private  var usuario: UsuarioEntity? = null
    val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        val IvImagen = binding.recipeImage
        val tvNombre = binding.recipeTitle
        val tvDescripcion = "Descripción jeje."
        val btnUrl = binding.recipeUrlButton
        val tvCabezeraIngredientes = binding.ingredientsHeader
        val lyListaIngredientes = binding.ingredientsList
        val tvCalorias = binding.recipeCalories
        val tvTiempoPreparacion = binding.recipeTotalTime
        val tvCanidad = binding.recipeYield

        val receta = args.Receta

        Glide.with(IvImagen.context)
            .load(receta.image)
            .error(R.drawable.image_recipe_not_found)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(IvImagen)

        tvNombre.text = receta.label

        btnUrl.setOnClickListener {
            /*val action = RecipeFragmentDirections.actionRecipeFragmentToWebFragment(receta.url)
            findNavController().navigate(action)*/
        }

        tvCalorias.text = "Calories: " + receta.calories.toString()
        tvTiempoPreparacion.text = "Time: " + receta.totalTime.toString() + " min"
        tvCanidad.text = "Yields: " + receta.yield.toString()

        tvCabezeraIngredientes.text = "Ingredientes (" + receta.ingredients.size.toString() + ")"
        for (ingredient in receta.ingredients) {
            Log.i("manolo", ingredient)
            val itemBinding = IngredientItemBinding.inflate(layoutInflater, lyListaIngredientes, false)
            val aux = separarCantidadIngrediente(ingredient)
            itemBinding.ingredientQuantity.text = aux.first
            itemBinding.ingredientName.text = aux.second

            lyListaIngredientes.addView(itemBinding.root)
        }

        return binding.root
    }
}
