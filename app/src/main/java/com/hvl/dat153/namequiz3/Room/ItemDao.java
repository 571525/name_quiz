package com.hvl.dat153.namequiz3.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM Item")
    List<Item> getAll();

    @Query("SELECT image_url FROM Item WHERE uglified_name = (:name)")
    String getImageUrl(String name);

    @Insert
    void insert(Item item);

    @Delete
    void delete(Item item);


}
