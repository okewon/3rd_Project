package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import kr.hnu.ock.messaging_app.Message;

public class Select_By_Major extends StringRequest {

    final static private String URL = "http://okewon.dothome.co.kr/3rd_Project/select_by_major.php";
    private Map<String, String> parameters;

    public Select_By_Major(String major, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Select by major", "Select by major");
            }
        });
        parameters = new HashMap<>();
        parameters.put("major", major);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
