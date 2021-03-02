package com.a15acdhmwbasicarch.data

import com.a15acdhmwbasicarch.datasource.api.PostsReposApi
import com.a15acdhmwbasicarch.datasource.db.PostsDao
import com.a15acdhmwbasicarch.domain.model.NewPostModel
import com.a15acdhmwbasicarch.domain.model.UserPostDomainModel
import com.a15acdhmwbasicarch.tools.UpdatingState
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsInfoRepository @Inject constructor(
    private val infoApiService: PostsReposApi,
    private val postsCacheDataSource: PostsDao,
    private val toDbMapper: PostResponseToPostDbEntityMapper,
    private val domainUserPostMapper: DomainUserPostMapper,
    private val mapNewPostToDataPostModel: NewPostToDataPostMapper
) {

    fun getPostsFromLocalStorage(): Flowable<List<UserPostDomainModel>> {
        return postsCacheDataSource.getAllUsersFromDB().map(domainUserPostMapper::map)
    }

    fun updateLocalStorage(): Completable {
        return infoApiService.getPostsList()
            .observeOn(Schedulers.io())
            .flatMapCompletable { listUserPostResponse ->
                postsCacheDataSource.insertListPosts(toDbMapper.map(listUserPostResponse))
            }
    }


    private fun getNewPostId(): Single<Int> {
        return postsCacheDataSource.getMaxPostId()
            .subscribeOn(Schedulers.io())
            .map { it + 1 }
    }

    fun saveNewPostFromUser(postForSaving: NewPostModel): Completable {
        return getNewPostId()
            .flatMapCompletable {
                postsCacheDataSource.insertPost(
                    mapNewPostToDataPostModel.map(
                        postForSaving,
                        it
                    )
                )
            }
    }
}

