package com.hvl.dat153.namequiz3.Utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class Utils {

    private Random random = new Random();

    private static final int size = 50;

    public static String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static void saveFile(Context context, Bitmap imageToSave, String fileName) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(Constants.PATH_TO_IMAGE_FOLDER, Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, fileName);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            imageToSave.compress(Bitmap.CompressFormat.JPEG,100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage());
        }
    }

    public static Bitmap getFile(String filename, Context context) {

        String path = context.getDir(Constants.PATH_TO_IMAGE_FOLDER, Context.MODE_PRIVATE).getAbsolutePath();
        Bitmap recovered = BitmapFactory.decodeFile(path +"/"+ filename);
        return recovered;
    }

    public static void deleteFile(String filename, Context context) {
        File file = context.getDir(Constants.PATH_TO_IMAGE_FOLDER, Context.MODE_PRIVATE);
        File[] subfiles = file.listFiles();

        for (File f : subfiles) {
            if (f.getName().equals(filename)) f.delete();
        }
    }
}
