package com.example.pawelczyszczon.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private QuestionBank mQuestionBank;

    private ArrayList<String> stringDataset;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, QuestionListActivity.class);
        return i;
    }

    private void parser(){
        for(int i = 0; i < mQuestionBank.getQuestions().size(); i++)
            stringDataset.add(getString(mQuestionBank.getQuestions().get(i).getTextResId()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        mQuestionBank = QuestionBank.getInstance();

        stringDataset = new ArrayList<>();
        parser();

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(stringDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
}