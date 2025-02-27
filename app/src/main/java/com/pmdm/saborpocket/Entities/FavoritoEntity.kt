package com.pmdm.saborpocket.Entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "FavoritoEntity",
    primaryKeys = ["usuarioId", "recetaId"],
    foreignKeys = [
        ForeignKey(entity = UsuarioEntity::class, parentColumns = ["id"], childColumns = ["usuarioId"],
            onDelete = CASCADE), // Esto borra los favoritos si se borra la noticia
        ForeignKey(entity = RecetaEntity::class, parentColumns = ["id"], childColumns = ["recetaId"],
            onDelete = CASCADE) // Esto borra los favoritos si se borra la noticia
    ]
)
data class FavoritoEntity(
    val usuarioId: Int,
    val recetaId: Int
)