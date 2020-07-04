package kr.hnu.ock.messaging_app;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import kr.hnu.ock.messaging_app.adapter.ReceiverItemAdapter;
import kr.hnu.ock.messaging_app.request.Select_By_Major;

public class Select_Receiver extends Fragment implements OnItemClick{

    Intent intent;
    Button btn_cancel, btn_search;
    Spinner sp_major;
    List<Member> list;
    ReceiverItemAdapter adapter;
    ListView listview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.select_receiver, container, false);

        list = new ArrayList<Member>();

        sp_major = (Spinner) root.findViewById(R.id.sp_search);
        btn_cancel = (Button) root.findViewById(R.id.button_cancel);
        btn_search = (Button) root.findViewById(R.id.button_search);

        sp_major.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.data_spinner, R.layout.support_simple_spinner_dropdown_item));


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                String major = (String) sp_major.getItemAtPosition(sp_major.getSelectedItemPosition());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("response");
                            if(jsonArray.length() < 0){
                                Toast.makeText(getContext(), "해당하는 회원이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                            }else{
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject row = jsonArray.getJSONObject(i);
                                    String id = row.getString("id");
                                    String name = row.getString("name");
                                    Member member = new Member(id, name);
                                    list.add(member);
                                }
                                adapter = new ReceiverItemAdapter(list, getContext().getApplicationContext(), (Sub_Fragment)getActivity(), Select_Receiver.this);
                                listview = (ListView) root.findViewById(R.id.list_search_result);
                                listview.setAdapter(adapter);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                Select_By_Major select_by_major = new Select_By_Major(major, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(root.getContext());
                requestQueue.add(select_by_major);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Send_Message_Fragment());
                fragmentTransaction.commit();
                ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("메세지 작성");
            }
        });

        return root;
    }

    @Override
    public void onClick() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new Send_Message_Fragment());
        fragmentTransaction.commit();
        ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("메세지 작성");
    }
}
