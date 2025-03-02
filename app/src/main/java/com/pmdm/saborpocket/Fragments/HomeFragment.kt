package com.pmdm.saborpocket.Fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.pmdm.saborpocket.Entities.UsuarioEntity
import com.pmdm.saborpocket.Adapter.AdaptadorRecetas
import com.pmdm.saborpocket.Adapter.OnClickListener
import com.pmdm.saborpocket.Data.Aplicacion
import com.pmdm.saborpocket.Data.RecetaViewModel
import com.pmdm.saborpocket.Entities.FavoritoEntity
import com.pmdm.saborpocket.Entities.RecetaEntity
import com.pmdm.saborpocket.R
import com.pmdm.saborpocket.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var gridLayout: GridLayoutManager
    private lateinit var adaptadorRecetas: AdaptadorRecetas
    private lateinit var recetaViewModel: RecetaViewModel
    private lateinit var mediaPlayer: MediaPlayer
    private var musicaPosicion: Int = 0

    private var usuario: UsuarioEntity? = null
    val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.soyunagargola)
        mediaPlayer.isLooping = true
        mediaPlayer.seekTo(musicaPosicion)
        mediaPlayer.start()

        usuario = args.Usuario

        logicaBotonFlotante()
        configurarRecyclerView()

        recetaViewModel = ViewModelProvider(requireActivity()).get(RecetaViewModel::class.java)

        recetaViewModel.recetasFavoritas.observe(viewLifecycleOwner) { recetas ->
            adaptadorRecetas.establecerRecetas(recetas.toMutableList())
        }

        usuario?.id?.let { recetaViewModel.actualizarFavoritos(it) }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        musicaPosicion = mediaPlayer.currentPosition
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(musicaPosicion)
            mediaPlayer.start()
        }
    }

    private fun logicaBotonFlotante() {
        val botonFlotante = binding.addNew
        botonFlotante.setOnClickListener {
            Toast.makeText(requireContext(), "Creando receta", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddRecipeFragment(
                    Usuario = usuario
                )
            )
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
                Receta = recetaEntity,
                Usuario = usuario
            )
        )
    }

    override fun alDarleAFavorito(recetaEntity: RecetaEntity) {
        recetaEntity.esFavorita = !recetaEntity.esFavorita
        adaptadorRecetas.actualizar(recetaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
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

            usuario?.id?.let { recetaViewModel.actualizarFavoritos(it) }
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
