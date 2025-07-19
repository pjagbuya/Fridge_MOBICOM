package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.content.Context
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.colorGeneratorComponents.ColorGenerator
val FIXED_EXCLUDED_COLOR_HEX_LIST = listOf(
    "#FF000000", // black
    "#FFFFFFFF", // white
    "#FF768A4E",   // light_green
    "#80768A4E", // light_green_50 (Note: this has an alpha of 80. Generated colors are opaque FF.)
    "#FF41644A",   // forest_green
    "#FFFFFCF2",   // dirty_white
    "#FF1C274D",   // dark_blue
    "#FF3B3B3B",   // light_black
    "#FFD64521",   // red
    "#FFCC8020",   // orange
    "#FF926E50",   // brown
    "#FFF0EFE8",   // gray_white
    "#FFC8D0B8"    // form_green
)

class ContainerDataHelper {

    companion object {

        val colorGen : ColorGenerator = ColorGenerator(FIXED_EXCLUDED_COLOR_HEX_LIST)

        fun getRandomColorGenerator() : Int{

            val color = colorGen.getRandomColor()
            if(color == null){
                return 0
            }

            return color
        }

        val containerModelsSelection = arrayListOf(
            ContainerModel(
                name = "My Fridge1",
                imageContainer = ImageContainer(R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                currCap = 5,
                maxCap = 10
            ),
            ContainerModel(
                name = "My Fridge2",
                imageContainer = ImageContainer(R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                currCap = 5,
                maxCap = 10
            ),
            ContainerModel(
                name = "My Fridge3",
                imageContainer = ImageContainer(R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                currCap = 5,
                maxCap = 10
            )
        )
        fun initializeContainers(context: Context): ArrayList<ContainerModel> {
            val containerModels = ArrayList<ContainerModel>()
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer(R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containerModels.add(
                ContainerModel(
                    name = "Vegetable Bin",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 5,
                    maxCap = 10
                )
            )

            containerModels.add(
                ContainerModel(
                    name = "Fruit Shelf",
                    imageContainer = ImageContainer( R.drawable.container_type_2_cabinet, getRandomColorGenerator()),
                    currCap = 3,
                    maxCap = 5
                )
            )

            containerModels.add(
                ContainerModel(
                    name = "Meat Compartment",
                    imageContainer = ImageContainer( R.drawable.container_type_1_fridge, getRandomColorGenerator()),
                    currCap = 2,
                    maxCap = 4
                )
            )

            containerModels.add(
                ContainerModel(
                    name = "Condiments Door",
                    imageContainer = ImageContainer( R.drawable.container_type_2_cabinet, getRandomColorGenerator()),
                    currCap = 6,
                    maxCap = 8
                )
            )

            containerModels.add(
                ContainerModel(
                    name = "Freezer Top",
                    imageContainer = ImageContainer( R.drawable.container_type_3_freezer, getRandomColorGenerator()),
                    currCap = 1,
                    maxCap = 3
                )
            )

            return containerModels
        }
    }
}