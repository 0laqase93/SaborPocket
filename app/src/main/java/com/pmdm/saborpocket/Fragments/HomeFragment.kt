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
import androidx.recyclerview.widget.GridLayoutManager
import com.pmdm.saborpocket.Entities.UsuarioEntity
import com.pmdm.saborpocket.Adapter.AdaptadorRecetas
import com.pmdm.saborpocket.Adapter.OnClickListener
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Entities.FavoritoEntity
import com.pmdm.saborpocket.Entities.RecetaEntity
import com.pmdm.saborpocket.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(), OnClickListener     {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var gridLayout: GridLayoutManager
    private lateinit var adaptadorRecetas: AdaptadorRecetas

    private  var usuario: UsuarioEntity? = null
    val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        usuario = args.Usuario

        logicaBotonFlotante()
        configurarRecyclerView()

        return binding.root
    }

    private fun logicaBotonFlotante() {
        val botonFlotante = binding.addNew
        botonFlotante.setOnClickListener {
            Toast.makeText(requireContext(), "Creando noticia", Toast.LENGTH_SHORT).show()
            /*val intent = Intent(requireContext(), AgregarNoticiaActivity::class.java)
            intent.putExtra("Usuario", usuario)
            startActivity(intent)*/
        }
    }

    private fun configurarRecyclerView() {
        adaptadorRecetas = AdaptadorRecetas(mutableListOf(), this)
        gridLayout = GridLayoutManager(requireContext(), 2)

        obtenerRecetas()

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayout
            adapter = adaptadorRecetas
        }
    }

    private fun obtenerRecetas() {
        lifecycleScope.launch(Dispatchers.IO) {
            val recetas = Aplicacion
                .baseDeDatos
                .recetaDao()
                .obtenerTodasLasRecetas()

            val favoritos = usuario?.let {
                Aplicacion
                    .baseDeDatos
                    .favoritoDao()
                    .obtenerTodosLosFavoritos(it.id)
            }

            recetas.forEach { receta ->
                if (favoritos != null) {
                    receta.esFavorita = favoritos.any { it.recetaId == receta.id }
                }
            }

            withContext(Dispatchers.Main) {
                adaptadorRecetas.establecerRecetas(recetas)
            }
        }
    }

    override fun alHacerClic(recetaEntity: RecetaEntity) {
        Toast.makeText(requireContext(), "Receta seleccionada", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToRecipeFragment(
                Receta = recetaEntity
            )
        )
    }

    override fun alDarleAFavorito(recetaEntity: RecetaEntity) {
        recetaEntity.esFavorita = !recetaEntity.esFavorita
        adaptadorRecetas.actualizar(recetaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            // Esto asignará -1 en caso de que usuario sea null. (No debería)
            val favoritoEntity = FavoritoEntity(usuario?.id ?: -1, recetaEntity.id)
            if (recetaEntity.esFavorita) {
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
                .actualizarReceta(recetaEntity)
        }
    }

    override fun alEliminar(recetaEntity: RecetaEntity) {
        adaptadorRecetas.eliminar(recetaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .recetaDao()
                .borrarReceta(recetaEntity)
        }
    }
}