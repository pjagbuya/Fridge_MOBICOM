package com.mobdeve.agbuya.hallar.hong.fridge.model

data class GroceryModel (
    var itemName: String = "",
    var numberOfItems: Int = 0,
    var itemType: String = "",
    var unit: String = "",
    var price: Double = 0.0,
    var dateBought: String = "",
    var expirationDate: String = "",
    var container: String = "",
    var itemCondition: String = "",
    var photo: String = ""
)