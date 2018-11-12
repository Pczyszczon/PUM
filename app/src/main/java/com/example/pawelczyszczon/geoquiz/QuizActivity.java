package com.example.pawelczyszczon.geoquiz;

import android.app.Activity;
import android.content.Intent;
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
    public Button mCheatButton;
    public TextView question_text;
    public boolean mIsCheater;
    private int mCurrentIndex=0;
    private static final int REQUEST_CODE_CHEAT = 0;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),
            new Question(R.string.question4, true),
    };

    private boolean isAnyQuestionLeft(){
        boolean result = false;
        for(int i = 0; i < mQuestionBank.length; i++){
            if(!mQuestionBank[i].wasAnswered()){
                result = true;
            }
        }
        return result;
    }

    private void updateQuestion(){
        mCurrentIndex++;
        if(mCurrentIndex == mQuestionBank.length){
            mCurrentIndex = 0;
        }
        mIsCheater = mQuestionBank[mCurrentIndex].wasCheated();
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
        mIsCheater = mQuestionBank[mCurrentIndex].wasCheated();
        setQuestionText();
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResId;
        if(!mQuestionBank[mCurrentIndex].wasAnswered()) {
            boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            if (mIsCheater) {
                messageResId = R.string.judgment_toast;
            } else {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct;
                } else {
                    messageResId = R.string.incorrect;
                }
            }
            mQuestionBank[mCurrentIndex].answer(userPressedTrue);
        }
        else{
            messageResId = R.string.duplicate;
        }
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0, 40);
        toast.show();
    }

    private void displayScoreAndReset(){
        int score = 0;
        for(int i = 0; i < mQuestionBank.length; i++){
            if(mQuestionBank[i].getPoint()){
                score++;
            }
        }
        Toast toast = Toast.makeText(
                this,
                String.format(getString(R.string.scoreMessage), score),
                Toast.LENGTH_SHORT);
        toast.show();

        for(int i = 0; i < mQuestionBank.length; i++)
            mQuestionBank[i].reset();
        mCurrentIndex = 0;
        setQuestionText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        false_button = findViewById(R.id.false_button_id);
        true_button = findViewById(R.id.true_button_id);
        previous_button = findViewById(R.id.previous_button_id);
        next_button = findViewById(R.id.next_button_id);
        mCheatButton = findViewById(R.id.cheat_button);
        question_text = findViewById(R.id.question_text_id);

        setQuestionText();

        question_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
            }
        });

        false_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
                updateQuestion();
                if(!isAnyQuestionLeft()){
                    displayScoreAndReset();
                }
            }
        });

        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
                updateQuestion();
                if(!isAnyQuestionLeft()){
                    displayScoreAndReset();
                }
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

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater){
                mQuestionBank[mCurrentIndex].cheat();
            }
        }
    }
}
