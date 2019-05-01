package com.example.askme.model

import com.google.gson.annotations.SerializedName

data class TagsResponse(

    @field:SerializedName("Tags")
    val tags: List<String>? = null
)