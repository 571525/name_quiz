package com.hvl.dat153.namequiz3.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="uglified_name")
    public String uglifiedName;

    @ColumnInfo(name="image_url")
    public String image_url;

    public Item(String uglifiedName, String image_url) {
        this.uglifiedName = uglifiedName;
        this.image_url = image_url;
    }

}
