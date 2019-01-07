package cms.co.in.kat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;
import java.util.List;

import cms.co.in.kat.activity.CaseJudgmentInfo;
import cms.co.in.kat.activity.CaseList;
import cms.co.in.kat.activity.CaseTrackHeader;
import cms.co.in.kat.activity.Judgement;
import cms.co.in.kat.activity.Webview;
import cms.co.in.kat.utils.Constant;

public class JudgeCaseListAdapter extends RecyclerView.Adapter<JudgeCaseListAdapter.MyViewHolder> {

    private ArrayList<String> caseList=new ArrayList<>();
    private Context context;

    public JudgeCaseListAdapter(Context context, ArrayList<String> caseTempList) {
        this.context=context;
        this.caseList=caseTempList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final String caseNo = caseList.get(position);
        holder.sltitle.setText(caseNo);
        holder.sltitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GeneralUtilities gu =new GeneralUtilities(context);

                Log.e("SITTRING_LIST","*** "+caseList.get(position));

                if(caseList.get(position)!=null && caseList.get(position).length()>0) {
                    Log.e("SITTRING_LIST","*** "+caseNo);
                    /*Log.e("SITTRING_LIST","*** "+caseNo);
                    Intent i = new Intent(context, CaseTrackHeader.class);
                    i.putExtra("caseNo",caseNo);*/


                        Log.e("SITTRING_LIST","*** "+caseNo);
                        Intent i = new Intent(context, CaseJudgmentInfo.class);
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
        if(caseList==null)
        {
            return 0;
        }
        return caseList.size();
    }

    public void updateCaselist(ArrayList<String> multiListCase) {


        this.caseList=multiListCase;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView sltitle;

        public MyViewHolder(@NonNull View view) {
            super(view);
            sltitle = (TextView) view.findViewById(R.id.et_case_list);
        }
    }


}
