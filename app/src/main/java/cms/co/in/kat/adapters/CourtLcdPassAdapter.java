package cms.co.in.kat.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cms.kat.cws.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cms.co.in.kat.customview.demono.adapter.InfinitePagerAdapter;

/**
 * Created by subham_naik on 22-Jun-17.
 */
public class CourtLcdPassAdapter extends InfinitePagerAdapter {

    private List<JSONObject> data = new ArrayList<JSONObject>();
    private LayoutInflater inflater;
    private Context context;

    public CourtLcdPassAdapter(Activity ctx, List<JSONObject> data) {
        this.data = data;
        context = ctx;
        inflater = ctx.getWindow().getLayoutInflater();

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
//        LinearLayout linr1 = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.court_lcd_next, parent, false);
            viewHolder.top = (TextView) convertView.findViewById(R.id.txt_top);
            viewHolder.bottom = (TextView) convertView.findViewById(R.id.txt_bottom);
//            linr1 = (LinearLayout) convertView.findViewById(R.id.linr1);


            viewHolder.child1 = (LinearLayout) convertView.findViewById(R.id.child1);
            viewHolder.txt_child1 = (TextView) convertView.findViewById(R.id.txt_child1);
            viewHolder.txt1_child1 = (TextView) convertView.findViewById(R.id.txt1_child1);

            viewHolder.child2 = (LinearLayout) convertView.findViewById(R.id.child2);
            viewHolder.txt_child2 = (TextView) convertView.findViewById(R.id.txt_child2);
            viewHolder.txt1_child2 = (TextView) convertView.findViewById(R.id.txt1_child2);

            viewHolder.child3 = (LinearLayout) convertView.findViewById(R.id.child3);
            viewHolder.txt_child3 = (TextView) convertView.findViewById(R.id.txt_child3);
            viewHolder.txt1_child3 = (TextView) convertView.findViewById(R.id.txt1_child3);

            viewHolder.child4 = (LinearLayout) convertView.findViewById(R.id.child4);
            viewHolder.txt_child4 = (TextView) convertView.findViewById(R.id.txt_child4);
            viewHolder.txt1_child4 = (TextView) convertView.findViewById(R.id.txt1_child4);

            viewHolder.child5 = (LinearLayout) convertView.findViewById(R.id.child5);
            viewHolder.txt_child5 = (TextView) convertView.findViewById(R.id.txt_child5);
            viewHolder.txt1_child5 = (TextView) convertView.findViewById(R.id.txt1_child5);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.top.setText("SL NO");
        viewHolder.bottom.setText("CASE NUMBER");

        try {
            viewHolder.name.setText("" + data.get(position).getString("courtName"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonResponse = new JSONArray(data.get(position).getString("passedOut"));
            int size = jsonResponse.length();
            Log.e("**", "** size " + size);
            if (size == 0) {
                viewHolder.child1.setVisibility(View.GONE);
                viewHolder.child2.setVisibility(View.GONE);
                viewHolder.child3.setVisibility(View.GONE);
                viewHolder.child4.setVisibility(View.GONE);
                viewHolder.child5.setVisibility(View.GONE);

            } else if (size == 1) {
                viewHolder.child1.setVisibility(View.VISIBLE);
                viewHolder.child2.setVisibility(View.GONE);
                viewHolder.child3.setVisibility(View.GONE);
                viewHolder.child4.setVisibility(View.GONE);
                viewHolder.child5.setVisibility(View.GONE);
//                Log.e("**", "**   " + jsonResponse.getJSONObject(i).getString("caseNo"));
                viewHolder.txt_child1.setText(""+ jsonResponse.getJSONObject(0).getString("caseNo"));
                viewHolder.txt1_child1.setText(""+ jsonResponse.getJSONObject(0).getString("slNo"));


            } else if (size == 2) {
                viewHolder.child1.setVisibility(View.VISIBLE);
                viewHolder.child2.setVisibility(View.VISIBLE);
                viewHolder.child3.setVisibility(View.GONE);
                viewHolder.child4.setVisibility(View.GONE);
                viewHolder.child5.setVisibility(View.GONE);
                viewHolder.txt_child1.setText(""+ jsonResponse.getJSONObject(0).getString("caseNo"));
                viewHolder.txt1_child1.setText(""+ jsonResponse.getJSONObject(0).getString("slNo"));
                viewHolder.txt_child2.setText(""+ jsonResponse.getJSONObject(1).getString("caseNo"));
                viewHolder.txt1_child2.setText(""+ jsonResponse.getJSONObject(1).getString("slNo"));


            } else if (size == 3) {
                viewHolder.child1.setVisibility(View.VISIBLE);
                viewHolder.child2.setVisibility(View.VISIBLE);
                viewHolder.child3.setVisibility(View.VISIBLE);
                viewHolder.child4.setVisibility(View.GONE);
                viewHolder.child5.setVisibility(View.GONE);
                viewHolder.txt_child1.setText(""+ jsonResponse.getJSONObject(0).getString("caseNo"));
                viewHolder.txt1_child1.setText(""+ jsonResponse.getJSONObject(0).getString("slNo"));
                viewHolder.txt_child2.setText(""+ jsonResponse.getJSONObject(1).getString("caseNo"));
                viewHolder.txt1_child2.setText(""+ jsonResponse.getJSONObject(1).getString("slNo"));
                viewHolder.txt_child3.setText(""+ jsonResponse.getJSONObject(2).getString("caseNo"));
                viewHolder.txt1_child3.setText(""+ jsonResponse.getJSONObject(2).getString("slNo"));

            } else if (size == 4) {
                viewHolder.child1.setVisibility(View.VISIBLE);
                viewHolder.child2.setVisibility(View.VISIBLE);
                viewHolder.child3.setVisibility(View.VISIBLE);
                viewHolder.child4.setVisibility(View.VISIBLE);
                viewHolder.child5.setVisibility(View.GONE);
                viewHolder.txt_child1.setText(""+ jsonResponse.getJSONObject(0).getString("caseNo"));
                viewHolder.txt1_child1.setText(""+ jsonResponse.getJSONObject(0).getString("slNo"));
                viewHolder.txt_child2.setText(""+ jsonResponse.getJSONObject(1).getString("caseNo"));
                viewHolder.txt1_child2.setText(""+ jsonResponse.getJSONObject(1).getString("slNo"));
                viewHolder.txt_child3.setText(""+ jsonResponse.getJSONObject(2).getString("caseNo"));
                viewHolder.txt1_child3.setText(""+ jsonResponse.getJSONObject(2).getString("slNo"));
                viewHolder.txt_child4.setText(""+ jsonResponse.getJSONObject(3).getString("caseNo"));
                viewHolder.txt1_child4.setText(""+ jsonResponse.getJSONObject(3).getString("slNo"));

            } else{
                viewHolder.child1.setVisibility(View.VISIBLE);
                viewHolder.child2.setVisibility(View.VISIBLE);
                viewHolder.child3.setVisibility(View.VISIBLE);
                viewHolder.child4.setVisibility(View.VISIBLE);
                viewHolder.child5.setVisibility(View.VISIBLE);
                viewHolder.txt_child1.setText(""+ jsonResponse.getJSONObject(0).getString("caseNo"));
                viewHolder.txt1_child1.setText(""+ jsonResponse.getJSONObject(0).getString("slNo"));
                viewHolder.txt_child2.setText(""+ jsonResponse.getJSONObject(1).getString("caseNo"));
                viewHolder.txt1_child2.setText(""+ jsonResponse.getJSONObject(1).getString("slNo"));
                viewHolder.txt_child3.setText(""+ jsonResponse.getJSONObject(2).getString("caseNo"));
                viewHolder.txt1_child3.setText(""+ jsonResponse.getJSONObject(2).getString("slNo"));
                viewHolder.txt_child4.setText(""+ jsonResponse.getJSONObject(3).getString("caseNo"));
                viewHolder.txt1_child4.setText(""+ jsonResponse.getJSONObject(3).getString("slNo"));
                viewHolder.txt_child5.setText(""+ jsonResponse.getJSONObject(4).getString("caseNo"));
                viewHolder.txt1_child5.setText(""+ jsonResponse.getJSONObject(4).getString("slNo"));
            }
            
            
            
//            View v;
//            TextView caseNo, slNo;
//            for (int i = 0; i < jsonResponse.length(); i++) {
////                Log.e("**", "**" + i);
////                Log.e("**", "**   " + jsonResponse.getJSONObject(i).getString("caseNo"));
////                Log.e("**", "**   " + jsonResponse.getJSONObject(i).getString("slNo"));
//                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT);
//                p.weight = 1;
//                v = inflater.inflate(R.layout.court_pass_over_row, null, false);
//                caseNo = (TextView) v.findViewById(R.id.case_number);
//                slNo = (TextView) v.findViewById(R.id.sl_no);
//                caseNo.setText("" + jsonResponse.getJSONObject(i).getString("caseNo"));
//                slNo.setText(""+jsonResponse.getJSONObject(i).getString("slNo"));
//                linr1.addView(v,p);
//            }
//            linr1.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void updateValue(List<JSONObject> nextHearingList) {
        this.data = nextHearingList;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView name,top,bottom;
        public LinearLayout  linr4, linr5;
        public LinearLayout linr3, child1, child2, child3, child4, child5;
        public TextView txt_child1,txt_child2,txt_child3,txt_child4,txt_child5,
                txt1_child1,txt1_child2,txt1_child3,txt1_child4,txt1_child5;
    }
}