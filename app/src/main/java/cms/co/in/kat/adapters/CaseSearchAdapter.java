package cms.co.in.kat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cms.co.in.kat.activity.CaseTrackHeader;
import cms.co.in.kat.objectholders.CaseInfo;
import cms.co.in.kat.objectholders.KatCase;

import static cms.co.in.kat.activity.CaseTrackHeader.allresultListDetail;


public class CaseSearchAdapter extends RecyclerView.Adapter {

    private ArrayList<HashMap> kat_caseList;

    public void updateEntry(ArrayList<HashMap> cList) {
        this.kat_caseList = cList;
    }

    public static class MyViewHolderIteam extends RecyclerView.ViewHolder {
        public TextView title, hearingDate, courtHall, status, proceeding, next_hearing_date;

        public MyViewHolderIteam(@NonNull View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.item_title);
            this.hearingDate = (TextView) view.findViewById(R.id.hearing_date);
            this.courtHall = (TextView) view.findViewById(R.id.court_hall);
            this.status = (TextView) view.findViewById(R.id.status);
            this.proceeding = (TextView) view.findViewById(R.id.proceeding);
            this.next_hearing_date = (TextView) view.findViewById(R.id.next_hearing_date);
        }
    }

    public CaseSearchAdapter(ArrayList<HashMap> kat_caseList) {
        this.kat_caseList = kat_caseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kat_case_list_item, parent, false);
        return new MyViewHolderIteam(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            if (allresultListDetail != null && allresultListDetail.size() > 0)
                ((MyViewHolderIteam) holder).title.setText("" + kat_caseList.get(position).get("slno"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (kat_caseList != null && kat_caseList.size() > 0) {
//            CaseInfo aCase = kat_caseList.get(position);

//            if (aCase != null) {

            try {
                ((MyViewHolderIteam) holder).hearingDate.setText("" + kat_caseList.get(position).get("hearingDate"));
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
                if (kat_caseList.get(position).get("procedingNote") != null || !kat_caseList.get(position).get("procedingNote").equals("")) {

                    ((MyViewHolderIteam) holder).proceeding.setText(kat_caseList.get(position).get("procedingNote").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (kat_caseList.get(position).get("nextHearingDate") != null || !kat_caseList.get(position).get("nextHearingDate").equals("")) {
                    ((MyViewHolderIteam) holder).next_hearing_date.setText(kat_caseList.get(position).get("nextHearingDate").toString());
                }
//                else {
//                    ((MyViewHolderIteam) holder).next_hearing_date.setText("Not Available");
////                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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