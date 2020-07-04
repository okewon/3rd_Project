package kr.hnu.ock.messaging_app;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import kr.hnu.ock.messaging_app.request.Delete_Message_Request;

public class Read_Sent_Message_Fragment extends Fragment {

    private Intent intent;
    private Button btn_back, btn_delete;
    private Message message;
    private TextView receiver, sender, title, content, date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.sent_message, container, false);

        intent = ((Sub_Fragment)getActivity()).getIntent();
        message = (Message) intent.getSerializableExtra("selectedMsg");

        btn_back = (Button) root.findViewById(R.id.btn_back);
        btn_delete = (Button) root.findViewById(R.id.btn_delete);
        receiver = (TextView) root.findViewById(R.id.textViewRecipient);
        sender = (TextView) root.findViewById(R.id.textViewSender);
        title = (TextView) root.findViewById(R.id.textViewTitle);
        content = (TextView) root.findViewById(R.id.textViewContent);
        date = (TextView) root.findViewById(R.id.textViewDate);

        receiver.setText(message.getReceiver());
        sender.setText(message.getSender());
        title.setText(message.getTitle());
        content.setText(message.getContent());
        date.setText(message.getDate());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Sent_Message_Fragment());
                fragmentTransaction.commit();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isSuccess = jsonResponse.getBoolean("success");
                            if (isSuccess) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, new Sent_Message_Fragment());
                                fragmentTransaction.commit();
                                ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("받은 메세지 함");
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Delete_Message_Request delete_message_request = new Delete_Message_Request(message, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(root.getContext());
                requestQueue.add(delete_message_request);
            }
        });

        return root;
    }
}
