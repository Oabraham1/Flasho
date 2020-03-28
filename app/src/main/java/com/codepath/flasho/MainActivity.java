package com.codepath.flasho;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView flashcard_quest;
    TextView flashcard_ans;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcard_quest = findViewById(R.id.flashcard_question);
        flashcard_ans = findViewById(R.id.answer_key);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.answer_key)).setText(allFlashcards.get(0).getAnswer());
        }


        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View answerSideView = findViewById(R.id.answer_key);
                View questionSideView = findViewById(R.id.flashcard_question);


                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;


                float finalRadius = (float) Math.hypot(cx, cy);


                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);


                questionSideView.setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(1000);
                anim.start();


            }
        });

        findViewById(R.id.answer_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.answer_key).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.myBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Questions.class);
                MainActivity.this.startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.left_out_animation, R.anim.right_animation_in);
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCardDisplayedIndex++;

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out_animation);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_animation_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        overridePendingTransition(R.anim.left_out_animation, R.anim.right_animation_in);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        overridePendingTransition(R.anim.left_in_animation, R.anim.right_out_animation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });



                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;
                }

                ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                ((TextView) findViewById(R.id.answer_key)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());


                findViewById(R.id.answer_key).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);

                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);


            }


        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());

                allFlashcards = flashcardDatabase.getAllCards();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            flashcardDatabase.insertCard(new Flashcard(question, answer));

            flashcard_quest = findViewById(R.id.flashcard_question);
            flashcard_ans = findViewById(R.id.answer_key);

            flashcard_quest.setText(question);
            flashcard_ans.setText(answer);

            allFlashcards = flashcardDatabase.getAllCards();


            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Question and Answer created successfully",
                    Snackbar.LENGTH_SHORT)
                    .show();


        }
        else    {
            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Unable to create",
                    Snackbar.LENGTH_SHORT)
                    .show();

        }
    }



}
