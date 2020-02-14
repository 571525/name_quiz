package com.hvl.dat153.namequiz3.Room;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.room.Room;

import com.hvl.dat153.namequiz3.Utils.Utils;

import java.util.List;

public class DbHandler {

    public static DbHandler instance;

    private Context context;
    private AppDatabase database;

    private DbHandler(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context,AppDatabase.class, "database.db").allowMainThreadQueries().build();
    }

    public static DbHandler getInstance() {
        if (instance==null) throw new NullPointerException("Call initialize first");
        else return instance;
    }

    public static void initialize(Context context) throws Exception {
        if (instance != null) throw new Exception("Instance is initialized, use getInstance()");
        else {
            instance = new DbHandler(context);
        }
    }

    public List<Item> getAll() {
        return database.itemDao().getAll();
    }

    public void deleteItem(Item item) {
        Utils.deleteFile(item.image_url,context);
        database.itemDao().delete(item);
    }

    public void addItem(Item item, Bitmap image) {
        database.itemDao().insert(item);
        Utils.saveFile(context,image,item.image_url);
    }

    public void close() {
        database.close();
    }

}
