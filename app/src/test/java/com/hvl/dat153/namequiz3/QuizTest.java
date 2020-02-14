package com.hvl.dat153.namequiz3;

import android.os.Build;
import android.widget.EditText;

import com.hvl.dat153.namequiz3.Room.Item;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class QuizTest {

    private QuizActivity quizActivity;
    private ActivityController<QuizActivity> controller;
    private final static String WRONG_GUESS = "WRONG";
    private final static String RIGHT_GUESS = "RIGHT";


    @Before
    public void setup() {
        controller = Robolectric.buildActivity(QuizActivity.class);
        quizActivity = controller.create().get();

        //imitate database
        List<Item> dataset = new ArrayList<>();
        dataset.add(new Item(RIGHT_GUESS,"test"));
        dataset.add(new Item(RIGHT_GUESS,"test"));
        dataset.add(new Item(RIGHT_GUESS,"test"));
        dataset.add(new Item(RIGHT_GUESS,"test"));
        dataset.add(new Item(RIGHT_GUESS,"test"));

        //Setup mock dataset for testing the quiz activity
        quizActivity.setDataset(dataset);
        quizActivity.newRound();
    }

    @After
    public void close() {
        quizActivity.getDatabase().close();
        controller.destroy();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(quizActivity);
    }

    @Test
    public void startWithEmptyScoreGuessWrongUpdateTotalButNotScore() {
        int score = quizActivity.getScore();
        int total = quizActivity.getTotal();

        //start with empty score
        assertEquals(score,0);
        assertEquals(total,0);

        EditText input = quizActivity.findViewById(R.id.quiz_input);
        input.setText(WRONG_GUESS);
        quizActivity.findViewById(R.id.quiz_submit).performClick();

        score = quizActivity.getScore();
        total = quizActivity.getTotal();


        //update total with +1 but not score
        assertEquals(score,0);
        assertEquals(total,1);

    }

    @Test
    public void startWithEmptyScoreGuessRightUpdateScoreAndTotal() {
        int score = quizActivity.getScore();
        int total = quizActivity.getTotal();

        //start with empty score
        assertEquals(score,0);
        assertEquals(total,0);

        EditText input = quizActivity.findViewById(R.id.quiz_input);
        input.setText(RIGHT_GUESS);
        quizActivity.findViewById(R.id.quiz_submit).performClick();

        score = quizActivity.getScore();
        total = quizActivity.getTotal();


        //update total and score with +1
        assertEquals(score,1);
        assertEquals(total,1);

    }
}
