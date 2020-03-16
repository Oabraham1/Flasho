package com.codepath.flasho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        final EditText flashcard_question = findViewById(R.id.flashcard_question);
        final EditText flashcard_answer = findViewById(R.id.flashcard_answer);




        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data = new Intent();
                data.putExtra("question", flashcard_question.getText().toString());
                data.putExtra("answer", flashcard_answer.getText().toString());
                setResult(RESULT_OK, data);
                finish();


            }
        });

    }
}
