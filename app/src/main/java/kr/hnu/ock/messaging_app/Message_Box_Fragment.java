package kr.hnu.ock.messaging_app;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.hnu.ock.messaging_app.adapter.UserItemAdapter;
import kr.hnu.ock.messaging_app.request.Read_Message_Request;

public class Message_Box_Fragment extends Fragment {

    Intent intent;
    Member member;
    private ListView listView;
    private UserItemAdapter adapter;
    private List<Message> messageList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.message_box_fragment, container, false);


        intent = ((Sub_Fragment)getActivity()).getIntent();
        member = (Member) intent.getSerializableExtra("loginUser");
        messageList = new ArrayList<Message>();
        messageList.clear();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    if(jsonArray.length() == 0){
                        Toast.makeText(getContext(), "받은 메세지가 없습니다.", Toast.LENGTH_LONG).show();
                    }else{
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject row = jsonArray.getJSONObject(i);
                            String receiver = row.getString("receiver");
                            String sender = row.getString("sender");
                            String title = row.getString("title");
                            String content = row.getString("content");
                            String date = row.getString("date");
                            String reply_status = row.getString("reply_status");
                            String read_status = row.getString("read_status");
                            Message message = new Message(receiver, sender, title, content, date, reply_status, read_status);
                            messageList.add(message);
                        }

                        Log.d(null, "messageList.size : " + messageList.size());
                        adapter = new UserItemAdapter(getContext().getApplicationContext(), messageList, (Sub_Fragment) getActivity());
                        listView = root.findViewById(R.id.list_mail);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(itemClickListener);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Read_Message_Request read_message_request = new Read_Message_Request(member.getId(), responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(root.getContext());
        requestQueue.add(read_message_request);

        return root;
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Message message = (Message) adapter.getItem(i);
            intent.putExtra("selectedMsg", message);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Read_Message_Fragment());
            fragmentTransaction.commit();
        }
    };

}
