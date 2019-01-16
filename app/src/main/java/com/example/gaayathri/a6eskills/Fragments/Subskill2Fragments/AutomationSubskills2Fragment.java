package com.example.gaayathri.a6eskills.Fragments.Subskill2Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.Activites.LevelActivity;
import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.adapter.GridListAdapter;

import java.util.ArrayList;

/**
 * Created by sonu on 08/02/17.
 */
public class AutomationSubskills2Fragment extends Fragment {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<String> arrayList;

    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";

    public AutomationSubskills2Fragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        onClickEvent(view);

        //view.findViewById(R.id.back_button).setVisibility(View.GONE);
    }

    private void loadListView(View view) {
        ListView listView = view.findViewById(R.id.list_view);
        arrayList = new ArrayList<>();

        arrayList.add("IoT");
        arrayList.add("BlockChain");
        arrayList.add("RPA using UIPath");
        arrayList.add("RPA using BluePrism");
        arrayList.add("RPA using AutomationAnywhere");
        arrayList.add("Artificial Intelligence");
        arrayList.add("Machine Learning");
        arrayList.add("Connected Car Technologies");

        adapter = new GridListAdapter(context, arrayList, true);
        listView.setAdapter(adapter);
    }

    private void onClickEvent(View view) {
        view.findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray selectedRows = adapter.getSelectedIds();//Get the selected ids from adapter
                //Check if item is selected or not via size
                if ((selectedRows.size() > 0) & (selectedRows.size() < 3)) {

                    ArrayList<String> skillsList = new ArrayList<String>();

                    for (int i = 0; i < selectedRows.size(); i++) {

                        if (selectedRows.valueAt(i)) {

                            String selectedRowLabel = arrayList.get(selectedRows.keyAt(i));
                            skillsList.add(selectedRowLabel);

                        }
                    }

                    String sub2Skill1 = skillsList.get(0);
                    String sub2Skill2 = null;
                    try {
                        sub2Skill2 = skillsList.get(1);
                    } catch (Exception e) {

                    }

                    sharedpreferences = getActivity().getSharedPreferences("mypref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("sub2Skill1", sub2Skill1);
                    editor.putString("sub2Skill2", sub2Skill2);
                    editor.apply();

                    Intent homeintent = new Intent(getActivity(), LevelActivity.class);
                    startActivity(homeintent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

                } else if (selectedRows.size() > 2) {

                    Toast.makeText(getActivity(), "Select only 2 skills", Toast.LENGTH_SHORT).show();

                } else if (selectedRows.size() == 0) {

                    Toast.makeText(getActivity(), "Select atleast 1 skill", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
