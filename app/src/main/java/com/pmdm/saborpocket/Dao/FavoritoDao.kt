package com.pmdm.saborpocket.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pmdm.saborpocket.Entities.FavoritoEntity

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM FavoritoEntity WHERE usuarioId = :userId")
    suspend fun obtenerTodosLosFavoritos(userId: Int): MutableList<FavoritoEntity>
    @Insert
    suspend fun agregarFavorito(favoritoEntity: FavoritoEntity)
    @Delete
    suspend fun borrarFavorito(favoritoEntity: FavoritoEntity)
}