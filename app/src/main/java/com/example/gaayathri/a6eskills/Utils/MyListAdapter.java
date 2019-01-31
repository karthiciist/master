package com.example.gaayathri.a6eskills.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaayathri.a6eskills.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    Dialog scheduleDialod;

    private final Activity context;
    private final ArrayList<String> agid;
    private final ArrayList<String> interviewid;
    private final ArrayList<String> title;
    private final ArrayList<String> companyid;
    private final ArrayList<String> companyname;
    private final ArrayList<String> desc;
    private final ArrayList<String> status;
    private final ArrayList<String> positionfrom;
    private final ArrayList<String> positionto;
    private final ArrayList<String> packegefrom;
    private final ArrayList<String> packegeto;
    private final ArrayList<String> currencycode;
    private final ArrayList<String> montlyoryearly;
    private final ArrayList<String> fromdate;
    private final ArrayList<String> todate;
    private final ArrayList<String> skillid;
    private final ArrayList<String> skillname;
    private final JSONArray jsonArray;

    public MyListAdapter(Activity context, ArrayList<String> agid, ArrayList<String> interviewid, ArrayList<String> title, ArrayList<String> companyid, ArrayList<String> companyname, ArrayList<String> desc, ArrayList<String> status, ArrayList<String> positionfrom, ArrayList<String> positionto, ArrayList<String> packegefrom, ArrayList<String> packegeto, ArrayList<String> currencycode, ArrayList<String> montlyoryearly, ArrayList<String> fromdate, ArrayList<String> todate, ArrayList<String> skillid, ArrayList<String> skillname, ArrayList<String> skillnameList, JSONArray jsonArray) {
        super(context, R.layout.listrowitem, title);
        this.context = context;
        this.agid = agid;
        this.interviewid = interviewid;
        this.title = title;
        this.companyid = companyid;
        this.companyname = companyname;
        this.desc = desc;
        this.status = status;
        this.positionfrom = positionfrom;
        this.positionto = positionto;
        this.packegefrom = packegefrom;
        this.packegeto = packegeto;
        this.currencycode = currencycode;
        this.montlyoryearly = montlyoryearly;
        this.fromdate = fromdate;
        this.todate = todate;
        this.skillid = skillid;
        this.skillname = skillname;
        this.jsonArray = jsonArray;
    }

    /*public MyListAdapter(Activity context, String[] maintitle, String[] subtitle, Integer[] imgid, String[] skillname) {
        super(context, R.layout.listrowitem, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.agid=maintitle;
        this.interviewid=subtitle;
        this.title=imgid;

        this.skillname = skillname;
    }*/

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listrowitem, null,true);

        TextView skillnametv = rowView.findViewById(R.id.skillname);
        TextView companynametv = rowView.findViewById(R.id.companyname);

        /*TextView agidtv = rowView.findViewById(R.id.agid);
        TextView interviewidtv = rowView.findViewById(R.id.interviewid);
        TextView titletv = rowView.findViewById(R.id.title);
        TextView companyidtv = rowView.findViewById(R.id.companyid);
        TextView desctv = rowView.findViewById(R.id.desc);
        TextView statustv = rowView.findViewById(R.id.status);
        TextView positionfromtv = rowView.findViewById(R.id.positionfrom);
        TextView positiontotv = rowView.findViewById(R.id.positionto);
        TextView packegefromtv = rowView.findViewById(R.id.packegefrom);
        TextView packegetotv = rowView.findViewById(R.id.packegeto);
        TextView currencycodetv = rowView.findViewById(R.id.currencycode);
        TextView montlyoryearlytv = rowView.findViewById(R.id.montlyoryearly);
        TextView fromdatetv = rowView.findViewById(R.id.fromdate);
        TextView todatetv = rowView.findViewById(R.id.todate);
        TextView skillidtv = rowView.findViewById(R.id.skillid);*/


        skillnametv.setText(agid.get(position));
        companynametv.setText(desc.get(position));

        //Toast.makeText(context, desc.toString(), Toast.LENGTH_SHORT).show();

        /*agidtv.setText(agid.get(position));
        interviewidtv.setText(interviewid.get(position));
        titletv.setText(title.get(position));
        companyidtv.setText(companyid.get(position));
        desctv.setText(desc.get(position));
        statustv.setText(status.get(position));
        positionfromtv.setText(positionfrom.get(position));
        positiontotv.setText(positionto.get(position));
        packegefromtv.setText(packegefrom.get(position));
        packegetotv.setText(packegeto.get(position));
        currencycodetv.setText(currencycode.get(position));
        montlyoryearlytv.setText(montlyoryearly.get(position));
        fromdatetv.setText(fromdate.get(position));
        todatetv.setText(todate.get(position));
        skillidtv.setText(skillid.get(position));*/

        Button btnView = rowView.findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String interviewidString = title.get(position);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj= null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        if(obj.getString("interviewid").equals(interviewidString))
                        {
                            String name = obj.getString("skillname");
                            String agid = obj.getString("agid");
                            String interviewid = obj.getString("interviewid");
                            String title = obj.getString("title");
                            String companyid = obj.getString("companyid");
                            String companyname = obj.getString("companyname");
                            String desc = obj.getString("desc");
                            String status = obj.getString("status");
                            String positionfrom = obj.getString("positionfrom");
                            String positionto = obj.getString("positionto");
                            String packegefrom = obj.getString("packegefrom");
                            String packegeto = obj.getString("packegeto");
                            String currencycode = obj.getString("currencycode");
                            String montlyoryearly = obj.getString("montlyoryearly");
                            String fromdate = obj.getString("fromdate");
                            String todate = obj.getString("todate");
                            String skillid = obj.getString("skillid");
                            String skillname = obj.getString("skillname");

                            //Toast.makeText(context, title, Toast.LENGTH_SHORT).show();

                            scheduleDialod = new Dialog(getContext());
                            scheduleDialod.setContentView(R.layout.dialog_schedule);

                            TextView TvSkillname = scheduleDialod.findViewById(R.id.TvSkillname);
                            TextView TvTitle = scheduleDialod.findViewById(R.id.TvTitle);
                            TextView TvCompanyname = scheduleDialod.findViewById(R.id.TvCompanyname);
                            TextView TvFromdate = scheduleDialod.findViewById(R.id.TvFromdate);
                            TextView TvTodate = scheduleDialod.findViewById(R.id.TvTodate);

                            TvSkillname.setText(skillname);
                            TvTitle.setText(title);
                            TvCompanyname.setText(companyname);
                            TvFromdate.setText(fromdate);
                            TvTodate.setText(todate);

                            scheduleDialod.show();

                            Button accept = scheduleDialod.findViewById(R.id.btnAccept);
                            Button reject = scheduleDialod.findViewById(R.id.btnDecline);
                            Button postpone = scheduleDialod.findViewById(R.id.btnPostpone);

                            accept.setOnClickListener(v1 -> scheduleDialod.dismiss());
                            reject.setOnClickListener(v1 -> scheduleDialod.dismiss());
                            postpone.setOnClickListener(v1 -> scheduleDialod.dismiss());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return rowView;

    }
}
