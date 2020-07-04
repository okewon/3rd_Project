package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import kr.hnu.ock.messaging_app.Message;

public class Modify_Read_Status extends StringRequest {

    final static private String URL = "http://okewon.dothome.co.kr/3rd_Project/modify_read_status.php";
    private Map<String, String> parameters;

    public Modify_Read_Status(Message message, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Modify_Read_Status", "Modify_Read_Status");
            }
        });
        parameters = new HashMap<>();
        parameters.put("title", message.getTitle());
        parameters.put("content", message.getContent());
        parameters.put("date", message.getDate());
        parameters.put("sender", message.getSender());
        parameters.put("receiver", message.getReceiver());
        parameters.put("reply_status", message.getReply_status());
        parameters.put("read_status", message.getRead_status());
    }

    @Override
    protected Map<String, String> getParams() {
        return parameters;
    }
}
