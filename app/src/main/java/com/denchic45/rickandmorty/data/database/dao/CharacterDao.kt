package com.denchic45.rickandmorty.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.data.database.entity.AggregatedCharacterEntity
import com.denchic45.rickandmorty.data.database.entity.CharacterEntity
import com.denchic45.rickandmorty.data.database.entity.PartialCharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Upsert(entity = CharacterEntity::class)
    suspend fun upsert(entities: List<PartialCharacterEntity>)

    @Query("SELECT * FROM character WHERE character_id=:id")
    fun observeById(id: Int): Flow<AggregatedCharacterEntity?>

    fun getBy(
        name: String?,
        status: CharacterStatus?,
        species: String?,
        type: String?,
        gender: CharacterGender?
    ): PagingSource<Int, CharacterEntity> {
        val query = getByQuery("*", name, status, species, type, gender)
        return getBy(query)
    }

    @RawQuery([CharacterEntity::class])
    fun getBy(query: SupportSQLiteQuery): PagingSource<Int, CharacterEntity>

    fun getByQuery(
        columns: String,
        name: String?,
        status: CharacterStatus?,
        species: String?,
        type: String?,
        gender: CharacterGender?
    ): SimpleSQLiteQuery {
        val args = mutableListOf<Any>()
        val condition = buildList {
            name?.let {
                add("name like ?")
                args.add("%$it%")
            }
            status?.let {
                add("status = ?")
                args.add(it.toString())
            }
            species?.let {
                add("species like ?")
                args.add("%$it%")
            }
            type?.let {
                add("type like ?")
                args.add("%$it%")
            }
            gender?.let {
                add("gender = ?")
                args.add(it.toString())
            }
        }

        val builder = buildString {
            append("SELECT $columns FROM character")
            if (condition.isNotEmpty()) append(" WHERE ${condition.joinToString(" AND ")}")
            append(" ORDER BY character_id")
        }
        val query = SimpleSQLiteQuery(builder, args.toTypedArray())
        return query
    }

    @Query("DELETE FROM character WHERE favorite=0")
    suspend fun deleteUnfavorited()

    @Query("SELECT * FROM character c WHERE favorite=1")
     fun getFavorites(): Flow<List<CharacterEntity>>

    @Query("UPDATE character SET favorite=:favorite WHERE character_id=:characterId")
    suspend fun updateFavorite(characterId: Int, favorite: Boolean)

    @Query("UPDATE character SET location_id=:locationId, origin_id=:originId WHERE character_id=:characterId")
    suspend fun updateForeignKeys(characterId: Int, originId: Int?, locationId: Int?)
} 