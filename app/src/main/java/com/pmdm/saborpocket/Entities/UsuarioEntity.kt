package com.pmdm.saborpocket.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "UsuarioEntity")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String? = "",
    val nombre: String? = "",
    val contrasena: String? = ""
) : Serializable // El Serializable es para poder pasarlo entre activitys