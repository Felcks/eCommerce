package com.vivacious.ecommerce.home.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vivacious.ecommerce.home.persistence.dao.ProductDao
import com.vivacious.ecommerce.home.persistence.models.ProductEntity

@Database(entities = [ProductEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}