package com.cs407.offthephone

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> =
        value.split(",").map { it.trim() }

    @TypeConverter
    fun fromList(list: List<String>): String =
        list.joinToString(",")
}