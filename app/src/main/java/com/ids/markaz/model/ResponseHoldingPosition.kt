package com.ids.markaz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseHoldingPosition {

    @SerializedName("nameEn")
    @Expose
    var nameEn: String? = null
    @SerializedName("nameAr")
    @Expose
    var nameAr: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("tickerAr")
    @Expose
    var tickerAr: String? = null
    @SerializedName("tickerEn")
    @Expose
    var tickerEn: String? = null
    @SerializedName("codeTickerAr")
    @Expose
    var codeTickerAr: String? = null
    @SerializedName("codeTickerEn")
    @Expose
    var codeTickerEn: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Double? = null
    @SerializedName("stockDivDue")
    @Expose
    var stockDivDue: Double? = null
    @SerializedName("lastPrice")
    @Expose
    var lastPrice: Double? = null
    @SerializedName("totalMarketValue")
    @Expose
    var totalMarketValue: Double? = null
    @SerializedName("avgCost")
    @Expose
    var avgCost: Double? = null
    @SerializedName("totalCost")
    @Expose
    var totalCost: Double? = null
    @SerializedName("currentPrice")
    @Expose
    var currentPrice: Double? = null
    @SerializedName("totalCurrentValue")
    @Expose
    var totalCurrentValue: Double? = null
    @SerializedName("unrealizedValue")
    @Expose
    var unrealizedValue: Double? = null
    @SerializedName("unrealizedPercent")
    @Expose
    var unrealizedPercent: Double? = null
    @SerializedName("realizedPL")
    @Expose
    var realizedPL: Double? = null
    @SerializedName("divRecieved")
    @Expose
    var divRecieved: Double? = null
    @SerializedName("wtdReturn")
    @Expose
    var wtdReturn: Double? = null
    @SerializedName("mtdReturn")
    @Expose
    var mtdReturn: Double? = null
    @SerializedName("ytdReturn")
    @Expose
    var ytdReturn: Double? = null
    @SerializedName("changeValue")
    @Expose
    var changeValue: Double? = null
    @SerializedName("changePercent")
    @Expose
    var changePercent: Double? = null
    @SerializedName("costPercent")
    @Expose
    var costPercent: Double? = null
    @SerializedName("marketValuePercent")
    @Expose
    var marketValuePercent: Double? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("typeName")
    @Expose
    var typeName: String? = null

}