package com.example.nomly.model


import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(user: UserEntity)

    @Query("SELECT * FROM Users WHERE email= :email LIMIT 1")
    suspend fun getUser(email: String):UserEntity?

}