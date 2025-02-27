package com.pmdm.saborpocket.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pmdm.saborpocket.Entities.RecetaEntity

@Dao
interface RecetaDao {
    @Query("SELECT * FROM RecetaEntity")
    suspend fun obtenerTodasLasRecetas(): MutableList<RecetaEntity>
    @Insert
    suspend fun agregarReceta(noticiaEntity: RecetaEntity)
    @Update
    suspend fun actualizarReceta(noticiaEntity: RecetaEntity)
    @Delete
    suspend fun borrarReceta(noticiaEntity: RecetaEntity)
}