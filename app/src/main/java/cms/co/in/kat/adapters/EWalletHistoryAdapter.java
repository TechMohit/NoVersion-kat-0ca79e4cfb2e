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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cms.kat.cws.R;

import java.util.ArrayList;

import cms.co.in.kat.activity.CaseTrackHeader;
import cms.co.in.kat.customview.expandablelayout.ExpandableLayout;
import cms.co.in.kat.objectholders.EWalletModel;
import cms.co.in.kat.utils.Constant;

public class EWalletHistoryAdapter extends RecyclerView.Adapter<EWalletHistoryAdapter.MyViewHolder> {

    private ArrayList<EWalletModel> tranctionHistory = new ArrayList<>();
    private Context context;

    public EWalletHistoryAdapter(Context context, ArrayList<EWalletModel> tranctionHistory) {
        this.context = context;
        this.tranctionHistory = tranctionHistory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_history_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position == 0) {
            holder.expandable.expand();
            holder.arrow.setBackgroundResource(R.drawable.up_arrow);
        }
        final String caseNo = tranctionHistory.get(position).getTransactionDate();
        holder.txtDate.setText(caseNo);
        holder.txtParticular.setText("" + tranctionHistory.get(position).getParticulars());
        holder.txtTransId.setText("" + tranctionHistory.get(position).getTransectionId());

        if (tranctionHistory.get(position).getType().equalsIgnoreCase(Constant.CREDIT)) {

            holder.txtCredit.setText("Cr. " + tranctionHistory.get(position).getCredit());
            holder.txtCredit.setTextColor(ContextCompat.getColor(context, R.color.green_cs));
        } else {
            holder.txtCredit.setText("Dr. " + tranctionHistory.get(position).getDebit());
            holder.txtCredit.setTextColor(ContextCompat.getColor(context, R.color.red));

        }

        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandable.isExpanded()) {
                    holder.expandable.collapse();
                    holder.arrow.setBackgroundResource(R.drawable.down_arrow);

                } else {
                    holder.expandable.expand();
                    holder.arrow.setBackgroundResource(R.drawable.up_arrow);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (tranctionHistory == null) {
            return 0;
        }
        return tranctionHistory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDate, txtCredit, txtTransId, txtParticular;
        public ExpandableLayout expandable;
        public ImageView arrow;
        public RelativeLayout rel;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            txtCredit = (TextView) view.findViewById(R.id.txt_credit);
            txtTransId = (TextView) view.findViewById(R.id.txt_trans_id);
            txtParticular = (TextView) view.findViewById(R.id.txt_particular);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            rel = (RelativeLayout) view.findViewById(R.id.rel_top);
            expandable = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
        }
    }
}
