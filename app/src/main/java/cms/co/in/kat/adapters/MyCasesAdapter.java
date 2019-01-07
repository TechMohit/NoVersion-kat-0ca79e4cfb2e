package cms.co.in.kat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;
import java.util.List;

import cms.co.in.kat.activity.Webview;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.objectholders.MyCasesItem;

public class MyCasesAdapter extends RecyclerView.Adapter<MyCasesAdapter.MyViewHolder> {

    private List<MyCasesItem> listAll;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_cases_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.txt.setText(listAll.get(position).getCaseNumber());
        holder.txt_num.setText(""+(position+1));
        holder.view_arrow.setBackground(ContextCompat.getDrawable(context,R.drawable.down_arrow));
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.exp.isExpanded()){
                    holder.exp.collapse();
                    holder.view_arrow.setBackground(ContextCompat.getDrawable(context,R.drawable.down_arrow));

                }else{
                    holder.exp.expand();
                    holder.view_arrow.setBackground(ContextCompat.getDrawable(context,R.drawable.up_arrow));

                }
            }
        });
        holder.branch.setText(listAll.get(position).getBranch());
        holder.division.setText(listAll.get(position).getDivision());
        holder.dateOfFiling.setText(listAll.get(position).getEfillingDate());
        holder.provisionOfLaw.setText(listAll.get(position).getProvisionOfLaw());
        holder.otherProvisionLaw.setText(listAll.get(position).getOtherProvisionOfLaw());
        holder.lcoNo.setText(listAll.get(position).getLcoNo());
        holder.lcoDate.setText(listAll.get(position).getLocDate());
        holder.lcoProvision.setText(listAll.get(position).getLcoProvisionOfLaw());
        holder.surveyNo.setText(listAll.get(position).getServeyno());
        holder.district.setText(listAll.get(position).getDistrict());

    }

    @Override
    public int getItemCount() {
        if(listAll==null)
        {
            return 0;
        }
        return listAll.size();
    }

    public void updateEntry(List<MyCasesItem> listAll) {
        this.listAll=listAll;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt,txt_num,branch,division,dateOfFiling,provisionOfLaw,otherProvisionLaw,lcoNo,lcoDate,lcoProvision,surveyNo,district;
        public ExpandableLayout exp;
        public View view_arrow;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt = (TextView) view.findViewById(R.id.txt);
            exp=(ExpandableLayout) view.findViewById(R.id.exp);
            txt_num=(TextView) view.findViewById(R.id.txt_num);
            view_arrow=(View)view.findViewById(R.id.view_arrow);
            branch=(TextView)view.findViewById(R.id.branch);
            division=(TextView)view.findViewById(R.id.division);
            dateOfFiling=(TextView)view.findViewById(R.id.date_of_filing);
            provisionOfLaw=(TextView)view.findViewById(R.id.provision_of_law);
            otherProvisionLaw=(TextView)view.findViewById(R.id.other_provision_of_law);
            lcoNo=(TextView)view.findViewById(R.id.lco_no);
            lcoDate=(TextView)view.findViewById(R.id.lco_date);
            lcoProvision=(TextView)view.findViewById(R.id.lco_provosion_law);
            surveyNo=(TextView)view.findViewById(R.id.survey_no);
            district=(TextView)view.findViewById(R.id.distric);
        }
    }

    public MyCasesAdapter(Context context) {
        this.context = context;
    }


}
