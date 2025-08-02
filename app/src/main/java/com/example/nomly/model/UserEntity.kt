package com.example.nomly.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey val email: String,
    val password:String
)