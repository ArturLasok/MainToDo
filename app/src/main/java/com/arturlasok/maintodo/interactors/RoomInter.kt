package com.arturlasok.maintodo.interactors

import com.arturlasok.maintodo.cache.CategoryDao
import com.arturlasok.maintodo.cache.model.CategoryToDoEntity
import com.arturlasok.maintodo.domain.model.CategoryToDo
import com.arturlasok.maintodo.interactors.util.RoomDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomInter(
    private val categoryDao: CategoryDao
) {
    //category domain to entity
    fun categoryFromDomainToEntity(categoryToDo: CategoryToDo) : CategoryToDoEntity {
        return CategoryToDoEntity(
            category_id_room = categoryToDo.dCatId,
            category_name_room = categoryToDo.dCatName,
            category_desc_room = categoryToDo.dCatDescription,
            category_token_room = categoryToDo.dCatToken,
            category_icon_room = categoryToDo.dCatIcon,
            category_sort_room = categoryToDo.dCatSort,
            category_fav_room = categoryToDo.dCatFav
        )
    }
    //category entity to domain
    fun categoryFromEntityToDomain(categoryToDoEntity: CategoryToDoEntity) : CategoryToDo {
        return CategoryToDo(
             dCatId = categoryToDoEntity.category_id_room,
             dCatName = categoryToDoEntity.category_name_room,
             dCatDescription = categoryToDoEntity.category_desc_room,
             dCatToken = categoryToDoEntity.category_token_room,
             dCatIcon = categoryToDoEntity.category_icon_room,
             dCatSort = categoryToDoEntity.category_sort_room,
             dCatFav = categoryToDoEntity.category_fav_room
        )
    }

    //category entity list to domain list
    fun categoryFromListEntityToListDomain(categoryListEntity: List<CategoryToDoEntity>) : List<CategoryToDo> {
        return categoryListEntity.map {
            categoryFromEntityToDomain(it)
        }
    }
    //category domain list to entity list
    fun categoryFromListDomainToListEntity(categoryListDomain: List<CategoryToDo>) : List<CategoryToDoEntity> {
        return categoryListDomain.map {
            categoryFromDomainToEntity(it)
        }
    }

    //insert category to Database
    fun insertCategoryToRoom(categoryToDo: CategoryToDo) : Flow<RoomDataState<Boolean>> = flow {

        try {

            categoryDao.insertCategoryToRoom(categoryFromDomainToEntity(categoryToDo)
                .copy(category_token_room = "${System.currentTimeMillis()}"+"added")
            )

            emit(RoomDataState.data_stored(true))

        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }

    }

    //get all category from database
    fun getCategoryFromRoom() : Flow<RoomDataState<Boolean>> = flow {

        try {

            val categoryList = categoryDao.selectAllFromCategoryRoom()

            if(categoryList.isNotEmpty()) {
                emit(RoomDataState.data_recived(categoryFromListEntityToListDomain(categoryList)))
            } else emit(RoomDataState.data_recived(emptyList<CategoryToDo>()))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }

    //get one category from database
    fun getOneCategoryFromRoom(categoryId: Long) : Flow<RoomDataState<Boolean>> = flow {

        try {

            val oneCategory = categoryDao.selectOneCategory(categoryId)

            if(oneCategory.category_id_room!=null) {
                emit(RoomDataState.data_recived(categoryFromEntityToDomain(oneCategory)))
            } else emit(RoomDataState.data_error("room_error"))
        }
        catch(e:Exception) {

            emit(RoomDataState.data_error("room_error"))
        }


    }


}