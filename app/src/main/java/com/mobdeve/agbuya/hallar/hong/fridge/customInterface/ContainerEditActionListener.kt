package com.mobdeve.agbuya.hallar.hong.fridge.customInterface

import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity

interface ContainerEditActionListener {
    fun onOkClick(position: Int, model: ContainerEntity)
    fun onCancelClick(position: Int)
}