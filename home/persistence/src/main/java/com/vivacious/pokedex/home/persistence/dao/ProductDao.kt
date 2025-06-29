package com.vivacious.pokedex.home.persistence.dao

import androidx.room.*
import com.vivacious.pokedex.home.persistence.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM favorite_products")
    fun getFavoriteProducts(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM favorite_products WHERE id = :productId")
    suspend fun getFavoriteProductById(productId: Int): ProductEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)
    
    @Delete
    suspend fun deleteProduct(product: ProductEntity)
    
    @Query("DELETE FROM favorite_products WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE id = :productId)")
    suspend fun isProductFavorite(productId: Int): Boolean
} 