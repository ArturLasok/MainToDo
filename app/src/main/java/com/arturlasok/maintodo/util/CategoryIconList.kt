package com.arturlasok.maintodo.util

import com.arturlasok.maintodo.R

object CategoryIconList {

    fun getIconsLight() : List<Int> {
        return listOf<Int>(
            R.drawable.home_light,
            R.drawable.shopping_light,
            R.drawable.rach_light,
            R.drawable.repair_light,
            R.drawable.home_light,
            R.drawable.shopping_light,
            R.drawable.rach_light,
            R.drawable.repair_light)
    }
    fun getIconsDark() : List<Int> {
        return listOf<Int>(
            R.drawable.home_dark,
            R.drawable.shopping_dark,
            R.drawable.rach_dark,
            R.drawable.repair_dark,
            R.drawable.home_dark,
            R.drawable.shopping_dark,
            R.drawable.rach_dark,
            R.drawable.repair_dark)
    }
}