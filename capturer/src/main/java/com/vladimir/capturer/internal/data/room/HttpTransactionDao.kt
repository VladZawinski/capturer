package com.vladimir.capturer.internal.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vladimir.capturer.internal.data.entity.HttpTransaction

@Dao
internal interface HttpTransactionDao {
    @Insert
    suspend fun insert(transaction: HttpTransaction): Long?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: HttpTransaction): Int

    @Query("DELETE FROM transactions")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: Long): LiveData<HttpTransaction?>

    @Query("DELETE FROM transactions WHERE requestDate <= :threshold")
    suspend fun deleteBefore(threshold: Long): Int

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<HttpTransaction>

    @Query("SELECT * FROM transactions WHERE requestDate >= :timestamp")
    fun getTransactionsInTimeRange(timestamp: Long): List<HttpTransaction>
}
