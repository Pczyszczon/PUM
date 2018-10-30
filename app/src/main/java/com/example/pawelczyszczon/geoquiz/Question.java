package com.example.pawelczyszczon.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean wasAnswered = false;
    private boolean point;

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void answer(boolean ans){
        wasAnswered = true;
        point = (ans == mAnswerTrue);
    }

    public boolean wasAnswered(){
        return(wasAnswered);
    }

    public boolean getPoint(){
        return point;
    }

    public void reset(){
        wasAnswered = false;
    }

    public Question(int textId, boolean trueQuestion) {
        mTextResId = textId;
        mAnswerTrue = trueQuestion;
    }
}
