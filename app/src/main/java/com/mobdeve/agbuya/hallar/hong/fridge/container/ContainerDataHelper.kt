package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.content.Context
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Image


class ContainerDataHelper {
    companion object {
        fun initializeContainers(context: Context): ArrayList<Container> {
            val containers = ArrayList<Container>()
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )
            containers.add(
                Container(
                    name = "Vegetable Bin",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 5,
                    maxCap = 10
                )
            )

            containers.add(
                Container(
                    name = "Fruit Shelf",
                    image = Image(context, R.drawable.container_type_2_cabinet),
                    currCap = 3,
                    maxCap = 5
                )
            )

            containers.add(
                Container(
                    name = "Meat Compartment",
                    image = Image(context, R.drawable.container_type_1_fridge),
                    currCap = 2,
                    maxCap = 4
                )
            )

            containers.add(
                Container(
                    name = "Condiments Door",
                    image = Image(context, R.drawable.container_type_2_cabinet),
                    currCap = 6,
                    maxCap = 8
                )
            )

            containers.add(
                Container(
                    name = "Freezer Top",
                    image = Image(context, R.drawable.container_type_3_freezer),
                    currCap = 1,
                    maxCap = 3
                )
            )

            return containers
        }
    }
}