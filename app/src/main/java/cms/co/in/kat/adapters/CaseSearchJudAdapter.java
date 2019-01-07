package cms.co.in.kat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.objectholders.CaseInfo;

import static cms.co.in.kat.activity.CaseTrackHeader.allresultListDetail;


public class CaseSearchJudAdapter extends RecyclerView.Adapter {

    private ArrayList<HashMap> kat_caseList;

    public void updateEntry(ArrayList<HashMap> cList) {
        this.kat_caseList = cList;
    }

    public static class MyViewHolderIteam extends RecyclerView.ViewHolder {
        public TextView title, judgmentDate, courtHall, status, result;

        public MyViewHolderIteam(@NonNull View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.item_title);
            this.judgmentDate = (TextView) view.findViewById(R.id.judgment_date);
            this.courtHall = (TextView) view.findViewById(R.id.court_hall);
            this.status = (TextView) view.findViewById(R.id.status);
            this.result = (TextView) view.findViewById(R.id.result);
        }
    }

    public CaseSearchJudAdapter(ArrayList<HashMap> kat_caseList) {
        this.kat_caseList = kat_caseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kat_case_list_jud_item, parent, false);
        return new MyViewHolderIteam(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
                ((MyViewHolderIteam) holder).title.setText("" +(position+1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ((MyViewHolderIteam) holder).judgmentDate.setText("" + kat_caseList.get(position).get("judmentDate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ((MyViewHolderIteam) holder).courtHall.setText("" + kat_caseList.get(position).get("courtHall"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (kat_caseList.get(position).get("status") != null || !kat_caseList.get(position).get("status").equals("")) {

                ((MyViewHolderIteam) holder).status.setText(kat_caseList.get(position).get("status").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ((MyViewHolderIteam) holder).result.setText("" + kat_caseList.get(position).get("result"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (kat_caseList == null || kat_caseList.size() == 0)
            return 1;
        else if (kat_caseList.size() > 0)
            return 2;
        return -1;
    }

    @Override
    public int getItemCount() {

        if (kat_caseList == null || kat_caseList.size() == 0)
            return 1;
        else
            return kat_caseList.size();
    }
}