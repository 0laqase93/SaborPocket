package com.pmdm.saborpocket.Adapter

import com.pmdm.saborpocket.Entities.RecetaEntity


interface OnClickListener {
    fun alHacerClic(recetaEntity: RecetaEntity)

    fun alDarleAFavorito(recetaEntity: RecetaEntity)

    fun alEliminar(recetaEntity: RecetaEntity)
}