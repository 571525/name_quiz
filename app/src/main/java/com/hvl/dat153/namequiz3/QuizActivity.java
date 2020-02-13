package com.hvl.dat153.namequiz3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hvl.dat153.namequiz3.Room.DbHandler;
import com.hvl.dat153.namequiz3.Room.Item;
import com.hvl.dat153.namequiz3.Utils.Constants;
import com.hvl.dat153.namequiz3.Utils.Utils;

import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private int score = 0;
    private int total = 0;
    private ImageView image;
    private TextView score_view, total_view;
    private EditText input;
    private Button submit;
    private Random random;
    private int previousNumber = 0;
    private int next = 0;

    private SharedPreferences prefs;

    private DbHandler database;
    private List<Item> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences(Constants.PREFS_LAST_SCORE_FILE, MODE_PRIVATE);

        random = new Random();

        try {
            DbHandler.initialize(getApplicationContext());
            database = DbHandler.getInstance();
        } catch (Exception e) {
            database = DbHandler.getInstance();
        }

        dataset = database.getAll();

        image = findViewById(R.id.image_to_guess);
        input = findViewById(R.id.quiz_input);
        submit = findViewById(R.id.quiz_submit);
        score_view = findViewById(R.id.score_value);
        total_view = findViewById(R.id.total_value);

        newRound();
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public void setDataset(List<Item> items) {
        this.dataset = items;
    }

    private void newRound() {

        input.setText("");

        while (next == previousNumber) {
            next = random.nextInt(dataset.size());
        }
        previousNumber = next;

        final Item roundItem = dataset.get(next);
        image.setImageBitmap(Utils.getFile(roundItem.image_url, getApplicationContext()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entered = input.getText().toString();

                if (entered.equalsIgnoreCase(roundItem.uglifiedName)) {
                    input.setBackgroundColor(Color.GREEN);
                    input.setHint(getString(R.string.correct));
                    updateScore(true);
                    newRound();
                } else {
                    input.setBackgroundColor(Color.RED);
                    input.setHint(getString(R.string.wrong) + " " + roundItem.uglifiedName);
                    updateScore(false);
                    newRound();
                }

            }
        });

    }

    public void updateScore(boolean correct) {
        score = Integer.parseInt(score_view.getText().toString());
        total = Integer.parseInt(total_view.getText().toString());

        if(correct) {
            score++;
        }
        total++;

        score_view.setText(""+score);
        total_view.setText(""+total);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.PREFS_SCORE, score);
        editor.putInt(Constants.PREFS_TOTAL, total);
        editor.commit();
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
