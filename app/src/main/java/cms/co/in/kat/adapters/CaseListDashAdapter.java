package cms.co.in.kat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;

import cms.co.in.kat.activity.CaseTrackHeader;
import cms.co.in.kat.activity.DashBoardCaseList;
import cms.co.in.kat.objectholders.DashboardCaseListHolder;

public class CaseListDashAdapter extends RecyclerView.Adapter<CaseListDashAdapter.MyViewHolder> implements Filterable{

    private ArrayList<DashboardCaseListHolder> caseList = new ArrayList<>();
    private ArrayList<DashboardCaseListHolder> caseListOriginal = new ArrayList<>();

    private CustomFilter filter;
    private Context context;


    public CaseListDashAdapter(Context context, ArrayList<DashboardCaseListHolder> caseList) {

        this.context=context;
        this.caseList=caseList;
        this.caseListOriginal=caseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_list_dash_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final String caseNo = caseListOriginal.get(position).getCaseNo();
        holder.sltitle.setText(caseNo);
        int res = position + 1;
        holder.slno.setText("" + res);
        holder.type.setText(caseListOriginal.get(position).getTypeList());
        holder.division.setText(caseListOriginal.get(position).getDivisionList());
        holder.sltitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GeneralUtilities gu =new GeneralUtilities(context);

                Log.e("SITTRING_LIST", "*** " + caseList.get(position));

                if (caseListOriginal.get(position).getCaseNo() != null && caseListOriginal.get(position).getCaseNo().length() > 0) {
                    Intent i = new Intent(context, CaseTrackHeader.class);
                    i.putExtra("caseNo", caseNo);

                    context.startActivity(i);
                }
//                gu.openFilePDFFromInstalledApp(
//                        //sittingListUrls.get(position)
//                        //"http://meity.gov.in/sites/upload_files/dit/files/Digital%20India.pdf"
//                       sittingListUrls.get(position)
//                );
//                gu.showToastMessage(sittingListUrls.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (caseListOriginal == null) {
            return 0;
        }
        return caseListOriginal.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView sltitle, type, division, slno;

        public MyViewHolder(@NonNull View view) {
            super(view);
            sltitle = (TextView) view.findViewById(R.id.et_case_list);
            type = (TextView) view.findViewById(R.id.et_case_type_list);
            division = (TextView) view.findViewById(R.id.et_case_division_list);
            slno = (TextView) view.findViewById(R.id.slno);

        }
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint!=null && constraint.length()>0) {

                constraint = constraint.toString().toUpperCase();
                ArrayList<DashboardCaseListHolder> filters = new ArrayList<>();

                for (int i = 0; i < caseList.size(); i++) {
                    if (caseList.get(i).getCaseNo().toLowerCase().contains(constraint.toString().toLowerCase())||
                            caseList.get(i).getTypeList().toLowerCase().contains(constraint.toString().toLowerCase())||
                            caseList.get(i).getDivisionList().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        SchoolListViewItems viewItems = new SchoolListViewItems(schoolFilterList.get(i).getSchoolName()
//                                , schoolFilterList.get(i).getSchool_id(),
//                                schoolFilterList.get(i).getSchool_present(), schoolFilterList.get(i).getSchool_absent(),
//                                schoolFilterList.get(i).getSchool_not_taken(), schoolFilterList.get(i).getSchool_total(),
//                                schoolFilterList.get(i).getSchool_y_Value(),schoolFilterList.get(i).getIsClickable(),
//                                schoolFilterList.get(i).getBoysPresent(), schoolFilterList.get(i).getBoysAbsent(), schoolFilterList.get(i).getBoysNotTaken(),
//                                schoolFilterList.get(i).getGirlsPresent(), schoolFilterList.get(i).getGrilsNotTaken(), schoolFilterList.get(i).getGirlsAbsent());

                        filters.add(caseList.get(i));
                    }
                }

                results.count = filters.size();
                results.values = filters;
            }
            else
            {
                results.count = caseList.size();
                results.values = caseList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            caseListOriginal = (ArrayList<DashboardCaseListHolder>)results.values;
            notifyDataSetChanged();

        }
    }
}
