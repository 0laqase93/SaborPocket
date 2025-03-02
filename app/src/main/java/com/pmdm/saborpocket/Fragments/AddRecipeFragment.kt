package com.pmdm.saborpocket.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Entities.RecetaEntity
import com.pmdm.saborpocket.Entities.UsuarioEntity
import com.pmdm.saborpocket.databinding.FragmentAddRecipeBinding
import com.pmdm.saborpocket.databinding.IngredientItemAddBinding
import com.pmdm.saborpocket.databinding.InstructionItemAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddRecipeFragment : Fragment() {

    private lateinit var binding: FragmentAddRecipeBinding

    private  var usuario: UsuarioEntity? = null
    val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = FragmentAddRecipeBinding.inflate(layoutInflater)

        usuario = args.Usuario

        val tvName = binding.recipeTitleInput
        val tvImageUrl = binding.imageUrlInput
        val tvSource = binding.recipeSourceInput
        val tvCalories = binding.recipeCaloriesInput
        val tvTime = binding.recipeTotalTimeInput
        val tvYield = binding.recipeYieldInput

        val lyIngredients = binding.ingredientsContainer
        val btnAddIngredient = binding.addIngredientButton
        val btnRemoveIngredient = binding.removeIngredientButton

        val lyInstructions = binding.instructionsContainer
        val btnAddInstruction = binding.addInstructionButton
        val btnRemoveInstruction = binding.removeInstructionButton

        val tvYoutubeUrl = binding.recipeYoutubeInput

        val btnAddRecipe = binding.saveRecipeButton

        btnAddRecipe.setOnClickListener {
            if (tvName.text.toString().isNotEmpty()
                && tvImageUrl.text.toString().isNotEmpty()
                && tvSource.text.toString().isNotEmpty()
                && tvCalories.text.toString().isNotEmpty()
                && tvTime.text.toString().isNotEmpty()
                && tvYield.text.toString().isNotEmpty()
                && lyIngredients.childCount > 0
                && lyInstructions.childCount > 0) {

                val ingredients = mutableMapOf<String, String>()
                val instructions = mutableListOf<String>()

                for (i in 0 until lyIngredients.childCount) {
                    val ingredientView = lyIngredients.getChildAt(i)

                    if (ingredientView is ViewGroup) {
                        val ingredientBinding = IngredientItemAddBinding.bind(ingredientView)

                        val ingredientName = ingredientBinding.ingredientName.text.toString()
                        val ingredientQuantity = ingredientBinding.ingredientQuantity.text.toString()

                        if (ingredientName.isNotEmpty() && ingredientQuantity.isNotEmpty()) {
                            ingredients[ingredientName] = ingredientQuantity
                        }
                    }
                }

                for (i in 0 until lyInstructions.childCount) {
                    val instructionView = lyInstructions.getChildAt(i)

                    if (instructionView is ViewGroup) {
                        val instructionBinding = InstructionItemAddBinding.bind(instructionView)

                        val instructionText = instructionBinding.instruction.text.toString()

                        if (instructionText.isNotEmpty()) {
                            instructions.add(instructionText)
                        }
                    }
                }

                val recipe = RecetaEntity(
                    label = tvName.text.toString(),
                    image = tvImageUrl.text.toString(),
                    source = tvSource.text.toString(),
                    calories = tvCalories.text.toString().toDouble(),
                    totalTime = tvTime.text.toString().toInt(),
                    yield = tvYield.text.toString().toInt(),
                    ingredients = ingredients,
                    instructions = instructions,
                    strYoutube = tvYoutubeUrl.text.toString()
                )

                guardarReceta(recipe)

                Toast.makeText(requireContext(), "Receta guardada", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    AddRecipeFragmentDirections.actionAddRecipeFragmentToHomeFragment(
                        Usuario = usuario
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddIngredient.setOnClickListener {
            val newIngredientBinding = IngredientItemAddBinding.inflate(inflater, lyIngredients, false)
            lyIngredients.addView(newIngredientBinding.root)
        }

        btnRemoveIngredient.setOnClickListener {
            if (lyIngredients.childCount > 0) {
                lyIngredients.removeViewAt(lyIngredients.childCount - 1)
            }
        }

        btnAddInstruction.setOnClickListener {
            val newInstructionBinding = InstructionItemAddBinding.inflate(inflater, lyInstructions, false)
            lyInstructions.addView(newInstructionBinding.root)
        }

        btnRemoveInstruction.setOnClickListener {
            if (lyInstructions.childCount > 0) {
                lyInstructions.removeViewAt(lyInstructions.childCount - 1)
            }
        }

        return binding.root
    }

    private fun guardarReceta(recipe: RecetaEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .recetaDao()
                .agregarReceta(recipe)
        }
    }
}