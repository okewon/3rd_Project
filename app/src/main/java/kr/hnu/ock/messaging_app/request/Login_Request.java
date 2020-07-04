package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login_Request extends StringRequest {

    final static private String ID_CHECK_URL = "http://okewon.dothome.co.kr/3rd_Project/login.php";
    private Map<String, String> parameters;

    public Login_Request(String id, String pw, Response.Listener<String> listener){
        super(Method.POST, ID_CHECK_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Login Request", "Login Request");
            }
        });
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pw", pw);
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
