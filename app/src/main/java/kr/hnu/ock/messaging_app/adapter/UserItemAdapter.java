package kr.hnu.ock.messaging_app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.hnu.ock.messaging_app.Message;
import kr.hnu.ock.messaging_app.R;

public class UserItemAdapter extends BaseAdapter {

    private Context context;
    private List<Message> message_List;
    private static ImageView img;
    private Activity parentActivity;

    public UserItemAdapter(Context context, List<Message> message_List, Activity parentActivity) {
        this.context = context;
        this.message_List = message_List;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return message_List.size();
    }

    @Override
    public Object getItem(int i) {
        return message_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view.inflate(context, R.layout.message, null);
        //보낸메세지함/받은메세지함 구분하기

        TextView receiver = (TextView) v.findViewById(R.id.mailreceiver);
        TextView title = (TextView) v.findViewById(R.id.mailTitle);
        TextView date = (TextView) v.findViewById(R.id.mailTime);
        img = (ImageView) v.findViewById(R.id.imageViewMail);

        if(message_List.get(position).getReply_status().equals("1")){
            img.setImageResource(R.drawable.ic_reply_black_24dp);
        }else{
            if(message_List.get(position).getRead_status().equals("1")){
                img.setImageResource(R.drawable.mail2);
            }else{
                img.setImageResource(R.drawable.mail);
            }
        }

        title.setText(message_List.get(position).getTitle());
        receiver.setText(message_List.get(position).getReceiver());
        date.setText(message_List.get(position).getDate());

        return v;
    }
}