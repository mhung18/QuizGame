package com.example.quizzone.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quizzone.DbQuery;
import com.example.quizzone.Model.CategoryModel;
import com.example.quizzone.R;
import com.example.quizzone.TestActivity;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> catList;

    public CategoryAdapter(List<CategoryModel> catList) {
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView;

        if(view == null){
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup,false);
        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DbQuery.g_selected_cat_index = i;
                Intent intent = new Intent(view.getContext(), TestActivity.class);
                intent.putExtra("CAT_INDEX",i);
                view.getContext().startActivity(intent);
            }
        });

        TextView catName = myView.findViewById(R.id.catName);
        TextView noOfTests = myView.findViewById(R.id.no_of_tests);

        catName.setText(catList.get(i).getName());
        noOfTests.setText(String.valueOf(catList.get(i).getNoOfTests() + " Tests"));

        return myView;
    }
}
