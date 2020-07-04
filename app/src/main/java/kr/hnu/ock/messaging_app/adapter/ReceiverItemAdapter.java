package kr.hnu.ock.messaging_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import kr.hnu.ock.messaging_app.Member;
import kr.hnu.ock.messaging_app.OnItemClick;
import kr.hnu.ock.messaging_app.R;
import kr.hnu.ock.messaging_app.Sub_Fragment;

public class ReceiverItemAdapter extends BaseAdapter {

    private List<Member> list;
    private Context context;
    private Activity parentActivity;
    private OnItemClick listener;

    public ReceiverItemAdapter(List<Member> list, Context context, Activity parentActivity, OnItemClick listener){
        this.context = context;
        this.list = list;
        this.parentActivity = parentActivity;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view.inflate(context, R.layout.receiver, null);

        TextView id = (TextView) v.findViewById(R.id.receiver_id);
        Button choice = (Button) v.findViewById(R.id.receiver_choice);
        id.setText(list.get(i).getId() + " (" + list.get(i).getName() + ")");


        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("receiver", list.get(i).getId());
                editor.commit();
                listener.onClick();
            }
        });

        return v;
    }
}
