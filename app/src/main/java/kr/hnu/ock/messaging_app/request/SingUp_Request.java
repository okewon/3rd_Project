package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import kr.hnu.ock.messaging_app.Member;

public class SingUp_Request extends StringRequest {

    final private static String SIGNUP_URL = "http://okewon.dothome.co.kr/3rd_Project/signup.php";
    private Map<String, String> parameters;

    public SingUp_Request(Member member, Response.Listener<String> listener){
        super(Method.POST, SIGNUP_URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Sign up Request", "Sign up Request");
            }
        });
        parameters = new HashMap<>();
        parameters.put("id", member.getId());
        parameters.put("pw", member.getPw());
        parameters.put("name", member.getName());
        parameters.put("major", member.getMajor());
        parameters.put("image_src", member.getImg_src());
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
