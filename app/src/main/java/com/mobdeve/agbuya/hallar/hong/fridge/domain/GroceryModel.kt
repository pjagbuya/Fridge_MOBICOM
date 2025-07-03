package com.mobdeve.agbuya.hallar.hong.fridge.domain

data class GroceryModel (
    var itemName: String? = null,
    var numberOfItems: Int? = null,
    var itemType: String? = null,
    var unit: String? = null,
    var price: Double? = null,
    var dateBought: String? = null,
    var expirationDate: String? = null,
    var container: String? = null,
    var itemCondition: String? = null,
    var photo: String? = null
)