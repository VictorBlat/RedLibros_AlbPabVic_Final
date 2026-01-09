package com.example.redlibros_albpabvic.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        Alumno::class,
        Curso::class,
        Libro::class,
        AlumnoCurso::class,
        CursoLibro::class,
        Transaccion::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RedLibrosDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun libroDao(): LibroDao

    companion object {
        @Volatile
        private var Instance: RedLibrosDatabase? = null

        fun getDatabase(context: Context): RedLibrosDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RedLibrosDatabase::class.java,
                    "redlibros_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
