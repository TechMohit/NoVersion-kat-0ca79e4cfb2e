package cms.co.in.kat.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import cms.co.in.kat.activity.Webview;

public class SittingListAdapter extends RecyclerView.Adapter<SittingListAdapter.MyViewHolder> {

    private List<String> sittingList;
    private List<String> sittingListUrls;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sitting_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final String title = sittingList.get(position);
        holder.sltitle.setText(title);
        holder.sltitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GeneralUtilities gu =new GeneralUtilities(context);

                Log.e("SITTRING_LIST","*** "+sittingListUrls.get(position));

                if(sittingListUrls.get(position)!=null && sittingListUrls.get(position).length()>0) {
                    Intent intent = new Intent(context, Webview.class);
                    intent.putExtra("URL", sittingListUrls.get(position));
                    context.startActivity(intent);
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(sittingListUrls.get(position)),"application/pdf");
                    context.startActivity(intent);*/
                    /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sittingListUrls.get(position)));
                    context.startActivity(browserIntent);*/




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
        if(sittingList==null)
        {
            return 0;
        }
        return sittingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView sltitle;

        public MyViewHolder(@NonNull View view) {
            super(view);
            sltitle = (TextView) view.findViewById(R.id.et_sitting_list_title);
        }
    }

    public SittingListAdapter(Context context, ArrayList<String> sittingList, ArrayList<String> sittingListUrls) {
        this.context = context;
        this.sittingList = sittingList;
        this.sittingListUrls = sittingListUrls;
    }

    public void updateEntry(@NonNull ArrayList<String> msittingList, @NonNull ArrayList<String> msittingListUrls) {

        if(this.sittingList!=null&& this.sittingList.size()>0){
            this.sittingList.clear();
        }
        if(this.sittingListUrls!=null&& this.sittingListUrls.size()>0){
            this.sittingListUrls.clear();
        }
        this.sittingList.addAll(msittingList);
        this.sittingListUrls.addAll(msittingListUrls);
    }
}
