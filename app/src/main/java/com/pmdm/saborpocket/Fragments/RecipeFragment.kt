package com.pmdm.saborpocket.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Entities.FavoritoEntity
import com.pmdm.saborpocket.Entities.UsuarioEntity
import com.pmdm.saborpocket.R
import com.pmdm.saborpocket.databinding.FragmentRecipeBinding
import com.pmdm.saborpocket.databinding.IngredientItemBinding
import com.pmdm.saborpocket.databinding.InstructionItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val btnLike = binding.favoriteIcon
        val btnYoutube = binding.recipeVideoButton
        val tvCabezeraIngredientes = binding.ingredientsHeader
        val lyListaIngredientes = binding.ingredientsList
        val lyInstrucciones = binding.recipeInstructions
        val tvCalorias = binding.recipeCalories
        val tvTiempoPreparacion = binding.recipeTotalTime
        val tvPorciones = binding.recipeYield

        val receta = args.Receta
        val usuario = args.Usuario

        Glide.with(IvImagen.context)
            .load(receta.image)
            .error(R.drawable.image_recipe_not_found)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(IvImagen)

        tvNombre.text = receta.label

        btnLike.setImageResource(if (receta.esFavorita) R.drawable.ic_like else R.drawable.ic_unlike)
        btnLike.setOnClickListener {
            receta.esFavorita = !receta.esFavorita
            btnLike.setImageResource(if (receta.esFavorita) R.drawable.ic_like else R.drawable.ic_unlike)
            lifecycleScope.launch(Dispatchers.IO) {
                // Esto asignará -1 en caso de que usuario sea null. (No debería)
                val favoritoEntity = FavoritoEntity(usuario?.id ?: -1, receta.id)
                if (receta.esFavorita) {
                    Aplicacion
                        .baseDeDatos
                        .favoritoDao()
                        .agregarFavorito(favoritoEntity)
                } else {
                    Aplicacion
                        .baseDeDatos
                        .favoritoDao()
                        .borrarFavorito(favoritoEntity)
                }
                Aplicacion
                    .baseDeDatos
                    .recetaDao()
                    .actualizarReceta(receta)
            }
        }

        btnYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(receta.strYoutube)))
        }

        tvCabezeraIngredientes.text = "Ingredientes (" + receta.ingredients.size.toString() + ")"
        for (entry in receta.ingredients) {
            val ingredientBinding = IngredientItemBinding.inflate(inflater, lyListaIngredientes, false)

            ingredientBinding.ingredientName.text = entry.key
            ingredientBinding.ingredientQuantity.text = entry.value

            lyListaIngredientes.addView(ingredientBinding.root)
        }

        var contador = 0
        for (instruction in receta.instructions) {
            contador++;
            val instructionBinding = InstructionItemBinding.inflate(inflater, lyInstrucciones, false)

            instructionBinding.instructionNumber.text = contador.toString() + " -"
            instructionBinding.instruction.text = instruction

            lyInstrucciones.addView(instructionBinding.root)
        }

        tvCalorias.text = "Kilocalorias: " + receta.calories.toString()
        tvTiempoPreparacion.text = "Tiempo: " + receta.totalTime.toString() + " min."
        tvPorciones.text = "Porciones: " + receta.yield.toString()

        return binding.root
    }
}
