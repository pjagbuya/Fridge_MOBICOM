package com.mobdeve.agbuya.hallar.hong.fridge.colorGeneratorComponents

data class ComponentColorRange(val min: Int, val max: Int) {
    init {
        require(min in 0..255) { "Min component must be between 0 and 255 (inclusive)" }
        require(max in 0..255) { "Max component must be between 0 and 255 (inclusive)" }
        require(min <= max) { "Min component must be less than or equal to max component" }
    }
}