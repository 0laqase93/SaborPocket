package com.pmdm.saborpocket.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pmdm.saborpocket.Entities.*
import com.pmdm.saborpocket.Dao.*

@Database(entities = [RecetaEntity::class, UsuarioEntity::class, FavoritoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BaseDeDatos: RoomDatabase() {
    abstract fun recetaDao(): RecetaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun favoritoDao(): FavoritoDao
}