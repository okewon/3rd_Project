package kr.hnu.ock.messaging_app.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import kr.hnu.ock.messaging_app.Message;

public class Delete_Message_Request extends StringRequest {

    final static private String URL = "http://okewon.dothome.co.kr/3rd_Project/delete_message.php";
    private Map<String, String> parameters;

    public Delete_Message_Request(Message message, Response.Listener<String> listener){
        super(Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Delete Message Request", "Delete Message Request");
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
