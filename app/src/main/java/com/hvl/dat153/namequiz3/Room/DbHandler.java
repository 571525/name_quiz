package com.hvl.dat153.namequiz3.Room;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.room.Room;

import com.hvl.dat153.namequiz3.Utils.Utils;

import java.util.List;

public class DbHandler {

    private static DbHandler instance;

    private Context context;
    private AppDatabase database;

    private DbHandler(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context, AppDatabase.class, "database.db").allowMainThreadQueries().build();
    }

    public static DbHandler getInstance(Context context) {
        //checks if instance doesnt exist, and if it doesnt just make a new one
        if (instance == null) instance = new DbHandler(context);
        return instance;
    }

    public List<Item> getAll() {
        return database.itemDao().getAll();
    }

    public void deleteItem(Item item) {
        Utils.deleteFile(item.image_url, context);
        database.itemDao().delete(item);
    }

    public void addItem(Item item, Bitmap image) {
        database.itemDao().insert(item);
        Utils.saveFile(context, image, item.image_url);
    }

    public void close() {
        database.close();
    }

}
