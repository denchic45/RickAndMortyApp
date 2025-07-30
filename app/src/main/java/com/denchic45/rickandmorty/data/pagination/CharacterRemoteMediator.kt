package com.denchic45.rickandmorty.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.denchic45.rickandmorty.data.api.CharacterApi
import com.denchic45.rickandmorty.data.api.model.CharacterGender
import com.denchic45.rickandmorty.data.api.model.CharacterStatus
import com.denchic45.rickandmorty.data.api.util.ApiException
import com.denchic45.rickandmorty.data.database.AppDatabase
import com.denchic45.rickandmorty.data.database.entity.CharacterEntity
import com.denchic45.rickandmorty.data.mapper.toCharacterEntities
import kotlinx.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val characterApi: CharacterApi,
    private val database: AppDatabase,
    private val name: String? = null,
    private val status: CharacterStatus? = null,
    private val species: String? = null,
    private val type: String? = null,
    private val gender: CharacterGender? = null,
) : RemoteMediator<Int, CharacterEntity>() {

    private var page = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) 1
                    else page + 1
                }
            }
            val characters = characterApi.query(
                page,
                name,
                status,
                species,
                type,
                gender
            )

            val dao = database.characterDao
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.deleteUnfavorited()
                }
                dao.upsert(characters.results.toCharacterEntities())
            }
            MediatorResult.Success(characters.info.next == null)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: ApiException) {
            MediatorResult.Error(exception)
        }
    }
}