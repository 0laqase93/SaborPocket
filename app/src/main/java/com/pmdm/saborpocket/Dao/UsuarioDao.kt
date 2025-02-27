package com.pmdm.saborpocket.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pmdm.saborpocket.Entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM UsuarioEntity WHERE email = :email")
    suspend fun obtenerUsuario(email: String): UsuarioEntity?

    @Insert
    suspend fun agregarUsuario(usuarioEntity: UsuarioEntity)

    @Update
    suspend fun actualizarUsuario(usuarioEntity: UsuarioEntity)

    @Delete
    suspend fun borrarUsuario(usuarioEntity: UsuarioEntity)
}