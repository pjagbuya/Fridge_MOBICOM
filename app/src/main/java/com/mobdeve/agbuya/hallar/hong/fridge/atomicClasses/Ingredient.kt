package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class Ingredient(
    val ingredientID : Int,
    val icon : ImageIngredient,
    val name : String,
    val amount : Double,
    val dateAdded : String,
    val expirationDate : String,
    val unit : UnitOfMeasurement = UnitOfMeasurement.PIECE,
    val conditionType: ConditionType = ConditionType.VERY_OK,
    val itemType: ItemType = ItemType.OTHER,
    val imageContainerLists : ArrayList<ImageIngredient> = ArrayList<ImageIngredient>()
//    val attachedContainerName: String <- Singleton needed to access list of containermodel and list of ingredient model
) {
    companion object{
        var TOTAL_NUM = 0
        enum class ItemType(val displayName: String) {
            // Produce
            VEGETABLE("Vegetable"),
            FRUIT("Fruit"),

            // Proteins
            MEAT("Meat"),
            EGG("Egg"),

            // Dairy
            MILK("Milk"),

            // Grains & Breads
            RICE("Rice"),
            PASTA("Pasta"),
            BREAD("Bread"),
            GRAIN("Grain"),
            CEREAL("Cereal"),
            FLOUR("Flour"),

            // Baking & Sweets
            SUGAR("Sugar"),
            SWEETENER("Sweetener"),

            // Condiments & Seasoning
            SPICE("Spice"),
            SEASONING("Seasoning"),
            SAUCE("Sauce"),
            OIL("Oil"),
            VINEGAR("Vinegar"),
            SALT("Salt"),

            // Canned & Jarred
            CANNED_GOOD("Canned Good"),
            JARRED_ITEM("Jarred Item"),

            // Beverages
            BEVERAGE("Beverage"),

            // Misc
            OTHER("Other");



            override fun toString(): String = displayName
        }
        enum class UnitOfMeasurement(val displayName: String) {
            // Volume
            TEASPOON("teaspoon"),
            TABLESPOON("tablespoon"),
            CUP("cup"),
            MILLILITER("milliliter"),
            LITER("liter"),
            FLUID_OUNCE("fluid ounce"),

            // Weight
            GRAM("gram"),
            KILOGRAM("kilogram"),
            OUNCE("ounce"),
            POUND("pound"),

            // Pieces & common grocery units
            PIECE("piece"),
            SLICE("slice"),
            PACK("pack"),
            BOTTLE("bottle"),
            CAN("can"),
            JAR("jar"),
            BOX("box"),
            BAG("bag"),
            TUBE("tube"),
            DOZEN("dozen");


            override fun toString(): String = displayName
        }
        enum class ConditionType(val displayName: String) {
            VERY_OK("Very ok"),
            STILL_OK("Still ok"),
            SLIGHTLY_NOT_OK("Slightly not ok"),
            NOT_OK("Not ok");

            override fun toString(): String = displayName
        }


    }




}