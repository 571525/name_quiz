package com.hvl.dat153.namequiz3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hvl.dat153.namequiz3.Room.DbHandler;
import com.hvl.dat153.namequiz3.Utils.Constants;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        buildMenu();

        getPrefs();

    }

    private void getPrefs() {
        SharedPreferences name = getSharedPreferences(Constants.PREFS_FILE,MODE_PRIVATE);
        if (!name.contains(Constants.PREFS_NAME)) {
            createPrefsDialog();
        }
    }

    private void createPrefsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Inflate using dialog themed context.
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.add_prefs_dialog, null, false);

        // Find widgets inside "view".
        final EditText name = view.findViewById(R.id.dialog_prefs_name_entered);
        final TextView error = view.findViewById(R.id.warning_add_prefs_dialog);
        error.setVisibility(View.INVISIBLE);

        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.cancel();
                    return;
                }
            }
        };

        builder
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                boolean inputIsEmpty = (nameStr.trim().isEmpty());
                // if EditText is empty disable closing on positive button
                if (!inputIsEmpty) {
                    setPrefs(nameStr);
                    dialog.dismiss();
                } else {
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setPrefs(String name) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.PREFS_FILE,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Constants.PREFS_NAME, name);
        editor.commit();

    }

    private void buildMenu() {
        Button db = findViewById(R.id.menu_db);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
            }
        });

        Button quiz = findViewById(R.id.start_quiz);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuizActivity.class));
            }
        });
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
            case R.id.options_menu_database:
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
                return true;
            case R.id.options_menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
