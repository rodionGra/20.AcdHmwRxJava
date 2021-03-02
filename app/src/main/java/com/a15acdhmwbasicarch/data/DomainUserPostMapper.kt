package com.a15acdhmwbasicarch.data

import com.a15acdhmwbasicarch.datasource.UserStatusLocalDataSource
import com.a15acdhmwbasicarch.datasource.model.UserPostData
import com.a15acdhmwbasicarch.domain.model.UserPostDomainModel
import com.a15acdhmwbasicarch.domain.Status
import javax.inject.Inject

class DomainUserPostMapper @Inject constructor(private val statusList: UserStatusLocalDataSource) {

    fun map(
        postData: List<UserPostData>,
    ): List<UserPostDomainModel> {
        return postData.let {
            it.map { userPostResponse ->
                val userStatusById = getStatusFromStatusSet(userPostResponse.userId)
                getUserPostDomainModel(userPostResponse, userStatusById)
            }
        }
    }

    private fun getStatusFromStatusSet(userId: Int): Status {
        return statusList.getSetOfStatusUser().find { it.idUser == userId }?.status
            ?: Status.STANDARD
    }

    private fun getUserPostDomainModel(
        userPostData: UserPostData,
        status: Status
    ): UserPostDomainModel {
        return UserPostDomainModel(
            userId = userPostData.userId,
            id = userPostData.id,
            title = userPostData.title,
            body = userPostData.body,
            status = status,
            addedFrom = userPostData.addedFrom
        )
    }
}