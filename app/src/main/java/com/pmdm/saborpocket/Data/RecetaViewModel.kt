package com.pmdm.saborpocket.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.saborpocket.Entities.RecetaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecetaViewModel : ViewModel() {
    private val _recetasFavoritas = MutableLiveData<List<RecetaEntity>>()
    val recetasFavoritas: LiveData<List<RecetaEntity>> = _recetasFavoritas

    fun actualizarFavoritos(usuarioId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val favoritos = Aplicacion.baseDeDatos.favoritoDao().obtenerTodosLosFavoritos(usuarioId)
            val recetas = Aplicacion.baseDeDatos.recetaDao().obtenerTodasLasRecetas()

            recetas.forEach { receta ->
                receta.esFavorita = favoritos.any { it.recetaId == receta.id }
            }

            withContext(Dispatchers.Main) {
                _recetasFavoritas.value = recetas
            }
        }
    }
}
