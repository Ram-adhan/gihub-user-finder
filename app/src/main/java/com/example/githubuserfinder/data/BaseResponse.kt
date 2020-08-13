package com.example.githubuserfinder.data

import com.google.gson.annotations.SerializedName

class BaseResponse<Model>(
    @SerializedName("total_count")
    val count: Int,

    @SerializedName("incomplete_result")
    val incompleteResult: Boolean,

    @SerializedName("items")
    val items: Model
)