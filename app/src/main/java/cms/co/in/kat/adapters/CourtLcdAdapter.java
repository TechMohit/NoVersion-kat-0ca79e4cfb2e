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

import java.util.List;

import cms.co.in.kat.customview.demono.adapter.InfinitePagerAdapter;

/**
 * Created by subham_naik on 22-Jun-17.
 */
public class CourtLcdAdapter extends InfinitePagerAdapter {

    private List<JSONArray> data;
    private LayoutInflater inflater;
    private Context context;

    public CourtLcdAdapter(Activity ctx, List<JSONArray> data) {
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
        LinearLayout linr1 = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.court_lcd, parent, false);
            linr1 = (LinearLayout) convertView.findViewById(R.id.linr);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {

            for (int i = 0; i < data.get(0).length(); i++) {

                View v;
                TextView courtHall, slNo, caseNo, caseLevel;

                Log.e("**", "**" + data.get(0).getJSONObject(i));
                if (data.get(0).getJSONObject(i).getString("status").equalsIgnoreCase("Active")) {
                    v = inflater.inflate(R.layout.court_lcd_main_row, null, false);
                    courtHall = (TextView) v.findViewById(R.id.court_hall);
                    slNo = (TextView) v.findViewById(R.id.sl_no);
                    caseLevel = (TextView) v.findViewById(R.id.case_level);
                    caseNo = (TextView) v.findViewById(R.id.case_no);

                    courtHall.setText("" + data.get(0).getJSONObject(i).getString("courtName"));
                    slNo.setText("" + data.get(0).getJSONObject(i).getString("currentSlNo"));
                    caseLevel.setText("" + data.get(0).getJSONObject(i).getString("currentCaseLevel"));
                    caseNo.setText("" + data.get(0).getJSONObject(i).getString("currentCaseNo"));

                    linr1.addView(v);
                    linr1.invalidate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        Category category = (Category) getItem(position);
//        viewHolder.textTile.setText(data.get(position));
        return convertView;
    }

    private class ViewHolder {
        //        public TextView textTile;

    }
}