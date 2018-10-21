package com.example.pawelczyszczon.geoquiz;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

public class QuizActivity extends AppCompatActivity {

    public Button true_button;
    public Button false_button;
    public ImageButton previous_button;
    public ImageButton next_button;
    public TextView question_text;
    private int mCurrentIndex=0;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),
            new Question(R.string.question4, true),
    };

    private void updateQuestion(){
        mCurrentIndex++;
        if(mCurrentIndex == mQuestionBank.length){
            mCurrentIndex = 0;
        }
        setQuestionText();
    }

    private void setQuestionText(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        question_text.setText(question);
    }

    private void previousQuestion(){
        mCurrentIndex--;
        if(mCurrentIndex < 0){
            mCurrentIndex = mQuestionBank.length - 1;
        }
        setQuestionText();
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct;
        } else {
            messageResId = R.string.incorrect;
        }
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0, 40);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        false_button = (Button) findViewById(R.id.false_button_id);
        true_button = (Button) findViewById(R.id.true_button_id);
        previous_button = (ImageButton) findViewById(R.id.previous_button_id);
        next_button = (ImageButton) findViewById(R.id.next_button_id);
        question_text = (TextView) findViewById(R.id.question_text_id);

        setQuestionText();

        false_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                updateQuestion();
            }
        });

        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                updateQuestion();
            }
        });

        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousQuestion();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
            }
        });
    }
}
