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

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
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
    public void closeDb() {
        db.close();
    }

    @Test
    public void add5ItemsToRoomAndGetAllAndCheckNameAndImageURL() {

        Item i1 = new Item("YOLO", "SOMEWHERE");
        Item i2 = new Item("YOLO2", "SOEWHER");
        Item i3 = new Item("YOLO3", "MEWHEE");
        Item i4 = new Item("YOLO4", "OMWHRE");
        Item i5 = new Item("YOLO5", "EERE");

        itemDao.insert(i1);
        itemDao.insert(i2);
        itemDao.insert(i3);
        itemDao.insert(i4);
        itemDao.insert(i5);

        List<Item> items = itemDao.getAll();

        assertEquals(i1.uglifiedName, items.get(0).uglifiedName);
        assertEquals(i2.uglifiedName, items.get(1).uglifiedName);
        assertEquals(i3.uglifiedName, items.get(2).uglifiedName);
        assertEquals(i4.uglifiedName, items.get(3).uglifiedName);
        assertEquals(i5.uglifiedName, items.get(4).uglifiedName);

        assertEquals(i1.image_url, items.get(0).image_url);
        assertEquals(i2.image_url, items.get(1).image_url);
        assertEquals(i3.image_url, items.get(2).image_url);
        assertEquals(i4.image_url, items.get(3).image_url);
        assertEquals(i5.image_url, items.get(4).image_url);

    }

    @Test
    public void addImageToDb() {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car);
        String url = Utils.generateRandomString();
        Utils.saveFile(context, bitmap, url);

        boolean success = false;

        File[] files = context.getDir(Constants.PATH_TO_IMAGE_FOLDER, Context.MODE_PRIVATE).listFiles();

        for (File f : files) {
            if (f.getName().equals(url)) {
                success = true;
                Utils.deleteFile(f.getName(), context);
            }
        }

        assertTrue(success);


    }


    @Test(expected = AssertionFailedError.class)
    public void saveAndRecoverBitmapFromFile() {

        boolean completed = true;

        Bitmap correct = BitmapFactory.decodeResource(context.getResources(), R.drawable.car);
        Utils.saveFile(context, correct, "car");
        Bitmap recovered = Utils.getFile("car", context);

        int recHeight = recovered.getHeight();
        int recWidth = recovered.getWidth();
        int corHeight = correct.getHeight();
        int corWidth = correct.getWidth();

        if(recHeight==corHeight && recWidth==corWidth) {

            int corPix;
            int recPix;

            for (int h = 0; h < recHeight; h++){
                for (int w=0; w < recWidth; w++) {
                    corPix = correct.getPixel(w,h);
                    recPix = recovered.getPixel(w,h);
                    if (corPix != recPix) {
                        completed = false;
                    }
                }
            }

        }

        Utils.deleteFile("car",context);

        assertTrue(completed);
    }

    @Test
    public void add5EntriesAndCheckAmountUpdated() {
        Item i1 = new Item("YOLO", "SOMEWHERE");
        Item i2 = new Item("YOLO2", "SOEWHER");
        Item i3 = new Item("YOLO3", "MEWHEE");
        Item i4 = new Item("YOLO4", "OMWHRE");
        Item i5 = new Item("YOLO5", "EERE");

        itemDao.insert(i1);
        itemDao.insert(i2);
        itemDao.insert(i3);
        itemDao.insert(i4);
        itemDao.insert(i5);

        List<Item> items = itemDao.getAll();

        assertTrue(items.size() == 5);

    }

    @Test
    public void add5EntriesDelete2Add1AndCheckAmountUpdatedCorrect() {
        Item i1 = new Item("YOLO", "SOMEWHERE");
        Item i2 = new Item("YOLO2", "SOEWHER");
        Item i3 = new Item("YOLO3", "MEWHEE");
        Item i4 = new Item("YOLO4", "OMWHRE");
        Item i5 = new Item("YOLO5", "EERE");

        itemDao.insert(i1);
        itemDao.insert(i2);
        itemDao.insert(i3);
        itemDao.insert(i4);
        itemDao.insert(i5);

        List<Item> items = itemDao.getAll();

        assertTrue(items.size() == 5);

        itemDao.delete(i2);
        itemDao.delete(i4);

        items = itemDao.getAll();

        assertTrue(items.size() == 3);

        itemDao.insert(i4);

        items = itemDao.getAll();

        assertTrue(items.size() == 4);


    }


}
