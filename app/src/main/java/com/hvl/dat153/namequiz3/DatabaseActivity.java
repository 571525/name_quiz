package com.hvl.dat153.namequiz3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hvl.dat153.namequiz3.Adapters.RecyclerAdapter;
import com.hvl.dat153.namequiz3.Room.DbHandler;
import com.hvl.dat153.namequiz3.Room.Item;
import com.hvl.dat153.namequiz3.Utils.Constants;
import com.hvl.dat153.namequiz3.Utils.Utils;

import java.io.IOException;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    private static final String TAG = "DB_ACTIVITY";

    TextView textview_gallery, textview_camera;
    boolean isOpen = false;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private DbHandler database;
    private List<Item> dataset;
    private FloatingActionButton fab_main, fab_camera, fab_gallery;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = DbHandler.getInstance(getApplicationContext());

        buildFabs();
        buildRecyclerView();
    }

    private void buildFabs() {

        fab_main = findViewById(R.id.fab);
        fab_camera = findViewById(R.id.fab_camera);
        fab_gallery = findViewById(R.id.fab_gallery);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_camera = findViewById(R.id.textview_camera);
        textview_gallery = findViewById(R.id.textview_gallery);

        //handles the animation for fab button
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpen = isOpen ? closeAnimation() : openAnimation();
            }
        });

        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromExternalStorage();
            }
        });

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });

    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dataset = database.getAll();

        adapter = new RecyclerAdapter(dataset, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private void getImageFromExternalStorage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Constants.LOAD_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_REQ_EXTERNAL_STORAGE);
        }
    }

    private void getImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(this, MediaStore.ACTION_IMAGE_CAPTURE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Constants.CAMERA_PICTURE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_REQ_CAMERA);
        }
    }

    //the animation for closing the fab button
    private boolean closeAnimation() {
        textview_camera.setVisibility(View.INVISIBLE);
        textview_gallery.setVisibility(View.INVISIBLE);
        fab_gallery.startAnimation(fab_close);
        fab_camera.startAnimation(fab_close);
        fab_main.startAnimation(fab_anticlock);
        fab_gallery.setClickable(false);
        fab_camera.setClickable(false);
        return false;
    }

    //the animation for clicking the fab button
    private boolean openAnimation() {
        textview_camera.setVisibility(View.VISIBLE);
        textview_gallery.setVisibility(View.VISIBLE);
        fab_gallery.startAnimation(fab_open);
        fab_camera.startAnimation(fab_open);
        fab_main.startAnimation(fab_clock);
        fab_gallery.setClickable(true);
        fab_camera.setClickable(true);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_REQ_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.CAMERA_PICTURE);
                    return;
                }
            }
        }
        if (requestCode == Constants.PERMISSION_REQ_EXTERNAL_STORAGE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, Constants.LOAD_IMAGE);
                    return;
                }
            }
        }
        Log.e(TAG, "onRequestPermissionsResult: Error getting permissions");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.LOAD_IMAGE && data.getData() != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    createAddPersonDialog(bitmap);
                } catch (IOException e) {
                    Log.e(TAG, "onClick: " + e.getMessage());
                }
            } else if (requestCode == Constants.CAMERA_PICTURE && data.getExtras().get("data") != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                createAddPersonDialog(bitmap);
            }
        }

    }

    private void createAddPersonDialog(final Bitmap bitmap) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DatabaseActivity.this);

        // Inflate using dialog themed context.
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.add_dialog, null, false);

        // Find widgets inside "view".
        final EditText name = view.findViewById(R.id.dialog_prefs_name_entered);
        final ImageView image = view.findViewById(R.id.dialog_add_image);
        image.setImageBitmap(bitmap);
        final TextView error = view.findViewById(R.id.warning_add_prefs_dialog);
        error.setVisibility(View.INVISIBLE);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) dialog.cancel();
            }
        };

        builder
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                boolean inputIsEmpty = (nameStr.trim().isEmpty());
                // if EditText is empty disable closing on positive button
                if (!inputIsEmpty) {
                    addNewEntry(nameStr, bitmap);
                    dialog.dismiss();
                } else {
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void addNewEntry(String name, Bitmap image) {
        String location = Utils.generateRandomString();
        Item item = new Item(name, location);

        database.addItem(item, image);

        dataset.add(item);
        adapter.notifyDataSetChanged(); //update view
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.options_menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.options_menu_database:
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
                return true;
            case R.id.options_menu_start_quiz:
                startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                return true;
            case R.id.options_menu_preferences:
                startActivity(new Intent(getApplicationContext(), PrefsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

