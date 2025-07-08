package com.loki.utils.network.bean.mine

data class UserDetailResponse(
    val level: Int?,
    val listenSongs: Int?,
    val userPoint: UserPoint?,
    val mobileSign: Boolean?,
    val pcSign: Boolean?,
    val profile: Profile?,
    val peopleCanSeeMyPlayRecord: Boolean?,
    val bindings: List<Binding>?,
    val adValid: Boolean?,
    val code: Int?,
    val newUser: Boolean?,
    val recallUser: Boolean?,
    val createTime: Long?,
    val createDays: Int?,
    val profileVillageInfo: ProfileVillageInfo?
)

data class UserPoint(
    val userId: Long?,
    val balance: Int?,
    val updateTime: Long?,
    val version: Int?,
    val status: Int?,
    val blockBalance: Int?
)

data class Binding(
    val expiresIn: Long?,
    val refreshTime: Long?,
    val bindingTime: Long?,
    val tokenJsonStr: String?,
    val url: String?,
    val expired: Boolean?,
    val userId: Long?,
    val id: Long?,
    val type: Int?
)

data class ProfileVillageInfo(
    val title: String?,
    val imageUrl: String?,
    val targetUrl: String?
) 