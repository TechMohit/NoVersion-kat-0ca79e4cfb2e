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
import java.util.List;

import cms.co.in.kat.objectholders.CaseInfo;


public class CaseSearchDetailAdapter extends RecyclerView.Adapter {

    private List<CaseInfo> kat_caseList;

    public void updateEntry(List<CaseInfo> cList) {
        this.kat_caseList = cList;
    }

    public static class MyViewHolderIteam extends RecyclerView.ViewHolder {
        public TextView txtl,txtl1,txtr,txtr1;
        public LinearLayout linrr,linrl;

        public MyViewHolderIteam(@NonNull View view) {
            super(view);

            this.txtr = (TextView) view.findViewById(R.id.txtr);
            this.txtr1 = (TextView) view.findViewById(R.id.txtrr);
            this.txtl = (TextView) view.findViewById(R.id.txtl);
            this.txtl1 = (TextView) view.findViewById(R.id.txtll);
            this.linrl=(LinearLayout)view.findViewById(R.id.linerl);
            this.linrr=(LinearLayout)view.findViewById(R.id.linerr);
        }
    }

    public CaseSearchDetailAdapter(List<CaseInfo> kat_caseList) {
        this.kat_caseList = kat_caseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kat_case_list_detail_item, parent, false);
        return new MyViewHolderIteam(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if((position+1)%2==0) {
            ((MyViewHolderIteam) holder).linrr.setVisibility(View.VISIBLE);
            ((MyViewHolderIteam) holder).linrl.setVisibility(View.INVISIBLE);
            try {
                ((MyViewHolderIteam) holder).txtr.setText("" + kat_caseList.get(0).getProdedingTreeHashmap().get(kat_caseList.get(0).getProcedingArryTree().get(position)));
//                ((MyViewHolderIteam) holder).txtr1.setText("" + kat_caseList.get(0).getProcedingArry().get(position).get("courtHall"));
                ((MyViewHolderIteam) holder).txtr1.setText("" + kat_caseList.get(0).getProcedingArryTree().get(position));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{

            ((MyViewHolderIteam) holder).linrr.setVisibility(View.INVISIBLE);
            ((MyViewHolderIteam) holder).linrl.setVisibility(View.VISIBLE);
            try {
                ((MyViewHolderIteam) holder).txtl.setText("" + kat_caseList.get(0).getProdedingTreeHashmap().get(kat_caseList.get(0).getProcedingArryTree().get(position)));
//                ((MyViewHolderIteam) holder).txtl1.setText("" + kat_caseList.get(0).getProcedingArry().get(position).get("courtHall"));
                ((MyViewHolderIteam) holder).txtl1.setText("" + kat_caseList.get(0).getProcedingArryTree().get(position));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//
//    @Override
//    public int getItemViewType(int position) {
//
//        if (kat_caseList == null || kat_caseList.size() == 0)
//            return 1;
//        else if (kat_caseList.size() > 0)
//            return 2;
//        return -1;
//    }

    @Override
    public int getItemCount() {

        if (kat_caseList == null ||kat_caseList.get(0).getProcedingArryTree().size() == 0)
            return 1;
        else
            return kat_caseList.get(0).getProcedingArryTree().size();
    }
}