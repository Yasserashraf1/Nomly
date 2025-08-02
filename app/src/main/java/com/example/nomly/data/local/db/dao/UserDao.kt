package com.example.nomly.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nomly.data.local.db.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun register(user: UserEntity)

    @Query("SELECT * FROM Users WHERE email= :email LIMIT 1")
    suspend fun getUser(email: String): UserEntity?

}