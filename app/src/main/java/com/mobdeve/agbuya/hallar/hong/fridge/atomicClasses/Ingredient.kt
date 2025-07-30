package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Parcelize
class Ingredient(
    val ingredientID : Int = TOTAL_NUM++,
    var icon : Int,
    var name : String,
    var quantity: Double,
    var price : Double,
    var dateAdded : String,
    var expirationDate : String,
    var unit : String = UnitOfMeasurement.PIECE.displayName,
    var conditionType: String = ConditionType.VERY_OK.displayName,
    var itemType: String = ItemType.OTHER.displayName,
    val imageContainerLists : ArrayList<ImageRaw> = ArrayList<ImageRaw>(),
    val attachedContainerID: Int
): Parcelable {

    companion object{
        var TOTAL_NUM = 0
        fun getTimeStamp() : String {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH)
                val now = LocalDateTime.now()
                val timeStamp = "Last updated: ${now.format(formatter)}"

                return timeStamp
            }else{

                val formatter = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH)
                val now = Date()
                val timeStamp = "Last updated: ${formatter.format(now)}"

                return timeStamp

            }

        }

        enum class ItemType(val displayName: String) {
            // Vegetable
            VEGETABLE("Vegetable"),

            // Fruit
            FRUIT("Fruit"),

            // Meat
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

            // Sweets
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