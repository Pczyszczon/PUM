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
    public Button mListButton;
    public TextView question_text;
    public TextView tokens_text;
    public boolean mIsCheater;
    private int mCurrentIndex=0;
    private int mtokens=3;
    private static final int REQUEST_CODE_CHEAT = 0;

    private QuestionBank mQuestionBank;

    private boolean isAnyQuestionLeft(){
        boolean result = false;
        for(int i = 0; i < mQuestionBank.size(); i++){
            if(!mQuestionBank.getQuestion(i).wasAnswered()){
                result = true;
            }
        }
        return result;
    }

    private void updateQuestion(){
        mCurrentIndex++;
        if(mCurrentIndex == mQuestionBank.size()){
            mCurrentIndex = 0;
        }
        mIsCheater = mQuestionBank.getQuestion(mCurrentIndex).wasCheated();
        setQuestionText();
    }

    private void setQuestionText(){
        int question = mQuestionBank.getQuestion(mCurrentIndex).getTextResId();
        question_text.setText(question);
    }

    private void setTokensLeft(){
        tokens_text.setText(String.valueOf(mtokens));
    }

    private void previousQuestion(){
        mCurrentIndex--;
        if(mCurrentIndex < 0){
            mCurrentIndex = mQuestionBank.size() - 1;
        }
        mIsCheater = mQuestionBank.getQuestion(mCurrentIndex).wasCheated();
        setQuestionText();
    }

    private void checkAnswer(boolean userPressedTrue) {
        int messageResId;
        if(!mQuestionBank.getQuestion(mCurrentIndex).wasAnswered()) {
            boolean answerIsTrue = mQuestionBank.getQuestion(mCurrentIndex).isAnswerTrue();
            if (mIsCheater) {
                messageResId = R.string.judgment_toast;
            } else {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct;
                } else {
                    messageResId = R.string.incorrect;
                }
            }
            mQuestionBank.getQuestion(mCurrentIndex).answer(userPressedTrue);
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
        for(int i = 0; i < mQuestionBank.size(); i++){
            if(mQuestionBank.getQuestion(i).getPoint()){
                score++;
            }
        }
        Toast toast = Toast.makeText(
                this,
                String.format(getString(R.string.scoreMessage), score),
                Toast.LENGTH_SHORT);
        toast.show();

        for(int i = 0; i < mQuestionBank.size(); i++)
            mQuestionBank.getQuestion(i).reset();
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
        mListButton = findViewById(R.id.list_button);
        question_text = findViewById(R.id.question_text_id);
        tokens_text = findViewById(R.id.tokens);
        mQuestionBank = QuestionBank.getInstance();

        setQuestionText();
        setTokensLeft();

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
                boolean answerIsTrue = mQuestionBank.getQuestion(mCurrentIndex).isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);            }
        });

        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = QuestionListActivity.newIntent(QuizActivity.this);
                startActivity(i);
            }
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
                mQuestionBank.getQuestion(mCurrentIndex).cheat();
                mtokens--;
                if(mtokens==0){
                    mCheatButton.setVisibility(View.INVISIBLE);
                }
                setTokensLeft();
            }
        }
    }
}
