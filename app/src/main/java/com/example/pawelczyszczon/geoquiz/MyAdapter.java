package com.example.pawelczyszczon.geoquiz;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ListRow> {
    private ArrayList<String> mDataset;

    public static class ListRow extends RecyclerView.ViewHolder {
        public LinearLayout row;
        public TextView textView;

        public ListRow(View itemView) {
            super(itemView);

            row = itemView.findViewById(R.id.a_row);
            textView = itemView.findViewById(R.id.text);
        }
    }

    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.ListRow onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout)
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ListRow vh = new ListRow(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListRow holder, int position) {
        holder.textView.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
