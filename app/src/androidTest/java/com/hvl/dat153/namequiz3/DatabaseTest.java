package com.hvl.dat153.namequiz3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hvl.dat153.namequiz3.Room.AppDatabase;
import com.hvl.dat153.namequiz3.Room.Item;
import com.hvl.dat153.namequiz3.Room.ItemDao;
import com.hvl.dat153.namequiz3.Utils.Constants;
import com.hvl.dat153.namequiz3.Utils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private ItemDao itemDao;
    private AppDatabase db;
    private Context context;

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        itemDao = db.itemDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void add5ItemsToRoomAndGetAllAndCheckNameAndImageURL() throws Exception {

        Item i1 = new Item("YOLO","SOMEWHERE");
        Item i2 = new Item("YOLO2","SOEWHER");
        Item i3 = new Item("YOLO3","MEWHEE");
        Item i4 = new Item("YOLO4","OMWHRE");
        Item i5 = new Item("YOLO5","EERE");

        itemDao.insert(i1);
        itemDao.insert(i2);
        itemDao.insert(i3);
        itemDao.insert(i4);
        itemDao.insert(i5);

        List<Item> items = itemDao.getAll();

        assertEquals(i1.uglifiedName,items.get(0).uglifiedName);
        assertEquals(i2.uglifiedName,items.get(1).uglifiedName);
        assertEquals(i3.uglifiedName,items.get(2).uglifiedName);
        assertEquals(i4.uglifiedName,items.get(3).uglifiedName);
        assertEquals(i5.uglifiedName,items.get(4).uglifiedName);

        assertEquals(i1.image_url,items.get(0).image_url);
        assertEquals(i2.image_url,items.get(1).image_url);
        assertEquals(i3.image_url,items.get(2).image_url);
        assertEquals(i4.image_url,items.get(3).image_url);
        assertEquals(i5.image_url,items.get(4).image_url);

    }

    @Test
    public void addImageToDb() {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.car);
        String url = Utils.generateRandomString();
        Utils.saveFile(context,bitmap,url);

        boolean success = false;

        File[] files = context.getDir(Constants.PATH_TO_IMAGE_FOLDER, Context.MODE_PRIVATE).listFiles();

        for (File f : files) {
            if(f.getName().equals(url)) success = true;
            Utils.deleteFile(f.getName(),context); //clean up after ourselves
        }

        assertTrue(success);


    }

    @Test
    public void saveAndGetBitmapFromFile() {
        Bitmap correct = BitmapFactory.decodeResource(context.getResources(),R.drawable.car);
        Utils.saveFile(context,correct,"car");

        Bitmap recovered = Utils.getFile("car", context);

        assertTrue(recovered.sameAs(correct)); //fails, but when run in debug it shows that it works
    }

}
