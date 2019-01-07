package cms.co.in.kat.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Happy on 01-03-2017.
 */

public interface VolleyListner {

    void onVolleyRespondJSONObject(JSONObject result);

    void onVolleyRespondJSONArray(JSONArray result);

    void onVolleyRespondString(int result, String response);

    void onVolleyError(String result);
}

