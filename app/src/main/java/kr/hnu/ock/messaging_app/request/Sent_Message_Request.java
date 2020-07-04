package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Sent_Message_Request extends StringRequest {

    final static private String URL = "http://okewon.dothome.co.kr/3rd_Project/sent_message.php";
    private Map<String, String> parameters;

    public Sent_Message_Request(String sender, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Sent Message Request", "Sent Message Request");
            }
        });
        parameters = new HashMap<>();
        parameters.put("sender", sender);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
