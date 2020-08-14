package com.ids.markaz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseAccountPerformance {

    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("mtdReturn")
    @Expose
    var mtdReturn: Double? = null
    @SerializedName("ytdReturn")
    @Expose
    var ytdReturn: Double? = null
    @SerializedName("ytdPerformance")
    @Expose
    var ytdPerformance: Double? = null
    @SerializedName("mtdPerformance")
    @Expose
    var mtdPerformance: Double? = null
    @SerializedName("siReturn")
    @Expose
    var siReturn: Double? = null
    @SerializedName("siPerformance")
    @Expose
    var siPerformance: Double? = null

}