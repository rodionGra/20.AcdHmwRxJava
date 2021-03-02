package com.a15acdhmwbasicarch.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.a15acdhmwbasicarch.datasource.model.UserPostData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface PostsDao {

    @Query("SELECT * FROM UserPostData")
    fun getAllUsersFromDB(): Flowable<List<UserPostData>>

    @Query("SELECT MAX(id) FROM UserPostData")
    fun getMaxPostId(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(userPostData: UserPostData) : Completable

    @Insert
    fun insertListPosts(userPostData: List<UserPostData>) : Completable
}