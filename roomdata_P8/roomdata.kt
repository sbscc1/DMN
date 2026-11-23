package com.example.calculadorabasica

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 2. ENTIDAD (@Entity)
@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val completada: Boolean = false
)

// 3. DATA ACCESS OBJECT (@Dao) - CRUD con Corrutinas y Flow
@Dao
interface TareaDao {
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<TareaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(tarea: TareaEntity)

    @Update
    suspend fun actualizar(tarea: TareaEntity)

    @Delete
    suspend fun eliminar(tarea: TareaEntity)
}

// 4. BASE DE DATOS (@Database) con patrón Singleton
@Database(entities = [TareaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tareas_room_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}