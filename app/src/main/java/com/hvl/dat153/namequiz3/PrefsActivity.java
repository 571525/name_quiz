package com.hvl.dat153.namequiz3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hvl.dat153.namequiz3.Utils.Constants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrefsActivity extends AppCompatActivity {

    private String name;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(Constants.PREFS_FILE, MODE_PRIVATE);
        name = prefs.getString(Constants.PREFS_NAME, null);

        buildView();
    }

    private void buildView() {
        final EditText editField = findViewById(R.id.edit_text_prefs);
        final TextView error = findViewById(R.id.prefs_error);

        if (name != null) {
            editField.setText(name);
        }

        Button save = findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = editField.getText().toString();
                boolean inputIsEmpty = (nameStr.trim().isEmpty());

                if (!inputIsEmpty) {
                    prefs.edit()
                            .putString(Constants.PREFS_NAME, nameStr)
                            .apply();
                    Toast.makeText(getApplicationContext(), getText(R.string.name_changed) + " " + nameStr, Toast.LENGTH_SHORT).show();
                } else {
                    error.setVisibility(View.VISIBLE);
                }
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
