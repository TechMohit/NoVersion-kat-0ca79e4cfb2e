package cms.co.in.kat.utils.chart;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter
{
    private ArrayList<String> mMonths=new ArrayList<>();


//    protected String[] mMonths = new String[]{
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
//    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, ArrayList<String> mMonths) {
        this.chart = chart;
        this.mMonths=mMonths;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return  mMonths.get((int)value % mMonths.size());
    }
}
