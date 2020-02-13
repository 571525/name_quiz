package com.hvl.dat153.namequiz3;

import android.os.Build;

import com.hvl.dat153.namequiz3.Room.Item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class QuizTest {

    private QuizActivity quizActivity;

    @Before
    public void setup() {
        ActivityController<QuizActivity> controller = Robolectric.buildActivity(QuizActivity.class);

        //imitate database
        List<Item> dataset = new ArrayList<>();
        dataset.add(new Item("test","test"));
        dataset.add(new Item("test","test"));
        dataset.add(new Item("test","test"));
        dataset.add(new Item("test","test"));
        dataset.add(new Item("test","test"));

        controller.
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(quizActivity);
    }

}
