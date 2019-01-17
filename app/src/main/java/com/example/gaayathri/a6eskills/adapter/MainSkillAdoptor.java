package com.example.gaayathri.a6eskills.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gaayathri.a6eskills.R;
import com.example.gaayathri.a6eskills.SkillClickListoner;
import com.example.gaayathri.a6eskills.Skills;

public class MainSkillAdoptor extends RecyclerView.Adapter<MainSkillAdoptor.ViewHolder> {
    private List<Skills> values;

    private SkillClickListoner skillClickListoner;
    public MainSkillAdoptor(List<Skills> input, SkillClickListoner skillClickListoner) {
        this.skillClickListoner= skillClickListoner;
        this.values = input;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView arrow;
        public View layout;
        public  TextView selected_count;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.title);
            arrow=(TextView) v.findViewById(R.id.arrow);
            selected_count =(TextView)v.findViewById(R.id.selected_count);

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainSkillAdoptor.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
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


        String count ="";
if(values.get(position).getChild().size() != 0) {
    count = String.valueOf(values.get(position).getChild().size());
}
        holder.selected_count.setText(String.valueOf(count));
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Interface to next
                skillClickListoner.selectedSkill( values.get(position));
            }
        });

        holder.arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Interface to next
                skillClickListoner.selectedSkill( values.get(position));
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}