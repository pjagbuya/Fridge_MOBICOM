package com.mobdeve.agbuya.hallar.hong.fridge.Room
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "inventories",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["firebaseUid"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("ownerId")]
)
data class InventoryEntity(
    @PrimaryKey val inventoryId: String,
    val inventoryName: String,
    val ownerId: String // FK to UserEntity.firebaseUid
)
//@TypeConverters(Converters::class)
//data class InventoryEntity(
//    @PrimaryKey(autoGenerate = true) val RecipeId: Int = 0,
//    val name: String,
//    val containers: List<ContainerEntity>,
//    val ingredients: List<IngredientEntity>,
//    val recipes: List<RecipeEntity>,
//    val members: List<Member>,
//    val type: InventoryType
//)
//
//enum class InventoryType {
//    OWNED,
//    JOINED
//}
