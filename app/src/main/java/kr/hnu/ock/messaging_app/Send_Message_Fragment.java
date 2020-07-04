package kr.hnu.ock.messaging_app;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.hnu.ock.messaging_app.request.Modify_Reply_Status;
import kr.hnu.ock.messaging_app.request.Send_Message_Request;

public class Send_Message_Fragment extends Fragment {

    Intent intent;
    Member member;
    Button btn_cancel, btn_send, btn_search;
    EditText title, content, receiver;
    SimpleDateFormat simpleDateFormat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.send_message, container, false);

        intent = ((Sub_Fragment)getActivity()).getIntent();
        member = (Member) intent.getSerializableExtra("loginUser");
        simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        btn_cancel = (Button) root.findViewById(R.id.buttonCancle);
        btn_send = (Button) root.findViewById(R.id.buttonSend);
        btn_search = (Button) root.findViewById(R.id.btn_search);
        title = (EditText) root.findViewById(R.id.editTextTitle);
        content = (EditText) root.findViewById(R.id.editTextContent);
        receiver = (EditText) root.findViewById(R.id.receiver);

        SharedPreferences pref = getContext().getSharedPreferences("userInfo", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String pref_receiver = pref.getString("receiver", "");

        if(pref_receiver.length() != 0){
            receiver.setText(pref_receiver);
            editor.remove("receiver");
            editor.commit();
        }

        String pref_resend_receiver = pref.getString("resend_receiver", "");
        String pref_resend_title = pref.getString("resend_title", "");
        if(pref_resend_receiver.length() != 0){
            receiver.setText(pref_resend_receiver);
            title.setText(pref_resend_title);
            editor.remove("resend_receiver");
            editor.remove("resend_title");
            editor.commit();
        }

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Select_Receiver());
                fragmentTransaction.commit();
                ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("수신자 선택");

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!receiver.getText().toString().equals("")){
                    if(!title.getText().toString().equals("") && !content.getText().toString().equals("")){
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Boolean isSuccess = jsonResponse.getBoolean("success");
                                    if(isSuccess) {
                                        if(title.getText().toString().contains("[답장]")){
                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response);
                                                        boolean Success = jsonResponse.getBoolean("success");
                                                        if(Success){
                                                            //Toast.makeText(getContext(), "답장완료!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            Message selectedMsg = (Message) intent.getSerializableExtra("selectedMsg");
                                            selectedMsg.setReply_status("1");
                                            Modify_Reply_Status modify_reply_status = new Modify_Reply_Status(selectedMsg, responseListener);
                                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                            requestQueue.add(modify_reply_status);
                                        }else {
                                            Toast.makeText(getContext(), "발송완료!", Toast.LENGTH_SHORT).show();
                                        }

                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, new Message_Box_Fragment());
                                        fragmentTransaction.commit();
                                        ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("받은 메세지 함");
                                    }else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setMessage("발송실패.").setNegativeButton("Retry", null).create().show();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        };
                        Message message = new Message(receiver.getText().toString(), member.getId(), title.getText().toString(), content.getText().toString(), simpleDateFormat.format(new Date()), "0", "0");
                        Send_Message_Request send_message_request = new Send_Message_Request(message, responseListener);
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(send_message_request);
                    }else{
                        Toast.makeText(getContext(), "제목 혹은 내용을 입력해주세요.", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getContext(), "받는이를 입력해주세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("receiver", "");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Message_Box_Fragment());
                fragmentTransaction.commit();
                ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("받은 메세지 함");
            }
        });

        return root;
    }

}
