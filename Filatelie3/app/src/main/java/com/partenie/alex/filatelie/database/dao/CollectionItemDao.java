package com.partenie.alex.filatelie.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.partenie.alex.filatelie.database.model.CollectionItem;

import java.util.List;

@Dao
public interface CollectionItemDao {

    @Query("select * from collectionItems")
    List<CollectionItem> getAll();

    @Query("select * from collectionItems where type LIKE :tip ")
    List<CollectionItem> getType(String tip);

    @Query("select * from collectionItems where price > :value ")
    List<CollectionItem> getOver(Float value);


    @Insert
    long insert(CollectionItem collectionItem);

    @Update
    int update(CollectionItem collectionItem);

    @Delete
    int delete(CollectionItem collectionItem);
}
