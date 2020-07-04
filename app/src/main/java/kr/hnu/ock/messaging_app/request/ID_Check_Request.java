package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ID_Check_Request extends StringRequest {

    final static private String ID_CHECK_URL = "http://okewon.dothome.co.kr/3rd_Project/id_check.php";
    private Map<String, String> parameters;

    public ID_Check_Request(String id, Response.Listener<String> listener){
        super(Method.POST, ID_CHECK_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ID Check Request", "ID Check Request");
            }
        });
        setShouldCache(false);
        parameters = new HashMap<>();
        parameters.put("id", id);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
