//package cms.co.in.kat.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.util.List;
//
//import cms.co.in.kat.R;
//import cms.co.in.kat.objectholders.Hearing;
//
///**
// //import cms.co.in.kat.R;
// * Created by pawank on 4/10/2017.
// */
//
//public class CauseListAdapter extends RecyclerView.Adapter<CauseListAdapter.MyViewHolder> {
//
//    private List<Hearing> hearingList;
//
//
//    public void updateHearing(List<Hearing> preHearingList) {
//        this.hearingList = preHearingList;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView caseNo, applName, respName;
//        public LinearLayout linrEmpty, linrCardview;
//
//        public MyViewHolder(View view) {
//            super(view);
//            caseNo = (TextView) view.findViewById(R.id.case_no);
//            applName = (TextView) view.findViewById(R.id.appl_name);
//            respName = (TextView) view.findViewById(R.id.resp_name);
//            linrEmpty = (LinearLayout) view.findViewById(R.id.linr_empty);
//            linrCardview= (LinearLayout) view.findViewById(R.id.linr_cardview);
//        }
//    }
//    public CauseListAdapter(List<Hearing> hearingList) {
//        this.hearingList = hearingList;
//    }
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.cause_list_item, parent, false);
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//
//        if(hearingList==null ||hearingList.size()==0){
//            holder.linrEmpty.setVisibility(View.VISIBLE);
//            holder.linrCardview.setVisibility(View.GONE);
//        } else {
//            Hearing hearing = hearingList.get(position);
//            holder.caseNo.setText(hearing.getCaseNo());
//            holder.applName.setText(hearing.getAppellantNames());
//            holder.respName.setText(hearing.getRespondentNames());
//            holder.linrEmpty.setVisibility(View.GONE);
//            holder.linrCardview.setVisibility(View.VISIBLE);
//        }
//    }
//    @Override
//    public int getItemCount() {
//
//        if(hearingList==null ||hearingList.size()==0)
//            return 1;
//        return  hearingList.size();
//    }
//}