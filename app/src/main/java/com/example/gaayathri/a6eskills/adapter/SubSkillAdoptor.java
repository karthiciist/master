package com.example.gaayathri.a6eskills.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.SkillClickListoner;
import com.example.gaayathri.a6eskills.Skills;

import java.util.List;

public class SubSkillAdoptor extends RecyclerView.Adapter<SubSkillAdoptor.ViewHolder> {
    private List<Skills> values;

    private SkillClickListoner skillClickListoner;

    public SubSkillAdoptor(List<Skills> input, SkillClickListoner skillClickListoner) {
        this.skillClickListoner = skillClickListoner;
        this.values = input;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public View layout;

        public TextView txtHeaderc;
        public CheckBox subskillcheckbox;
        public LinearLayout main;
        public LinearLayout sub;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.title);
            subskillcheckbox = (CheckBox) v.findViewById(R.id.subskillcheckbox);
            txtHeaderc = (TextView) v.findViewById(R.id.titlec);
            main = (LinearLayout) v.findViewById(R.id.main);
            sub = (LinearLayout) v.findViewById(R.id.child);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubSkillAdoptor.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.subskillview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position).getName();

        holder.txtHeader.setText(name);
        holder.txtHeaderc.setText(name);


        if (values.get(position).getSelectable()) {
            holder.main.setVisibility(View.VISIBLE);
            holder.sub.setVisibility(View.GONE);
        } else {
            holder.main.setVisibility(View.GONE);
            holder.sub.setVisibility(View.VISIBLE);
        }

        holder.subskillcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b && skillClickListoner.selectedCount() == 2){
                    holder.subskillcheckbox.setChecked(false);
                return;
                }
                if (b) {
                    skillClickListoner.selectedSkill(values.get(position));
                } else {
                    skillClickListoner.deselectSkill(values.get(position));
                }


            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}