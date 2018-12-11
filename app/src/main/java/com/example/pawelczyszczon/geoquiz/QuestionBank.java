package com.example.pawelczyszczon.geoquiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private static final QuestionBank ourInstance = new QuestionBank();

    private Question[] mQuestions = new Question[] {
            new Question(R.string.question1, true),
            new Question(R.string.question2, false),
            new Question(R.string.question3, true),
            new Question(R.string.question4, true),
    };

    private ArrayList<Question> mQuestionList;

    private void fillQuestionsList(){
        for(int i = 0; i < mQuestions.length; i++)
            mQuestionList.add(mQuestions[i]);
    }

    public Question getQuestion(int index){
        return mQuestions[index];
    }

    public List<Question> getQuestions(){
        return mQuestionList;
    }

    public int size(){
        return mQuestionList.size();
    }

    public static QuestionBank getInstance() {
        return ourInstance;
    }

    private QuestionBank() {
        mQuestionList = new ArrayList<>();
        fillQuestionsList();
    }
}
