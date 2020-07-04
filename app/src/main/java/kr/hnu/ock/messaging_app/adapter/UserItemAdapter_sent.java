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

public class UserItemAdapter_sent extends BaseAdapter {

    private Context context;
    private List<Message> message_List;
    private static ImageView img;
    private Activity parentActivity;

    public UserItemAdapter_sent(Context context, List<Message> message_List, Activity parentActivity) {
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

        TextView sender = (TextView) v.findViewById(R.id.mailreceiver);
        TextView title = (TextView) v.findViewById(R.id.mailTitle);
        TextView date = (TextView) v.findViewById(R.id.mailTime);
        img = (ImageView) v.findViewById(R.id.imageViewMail);

        img.setImageResource(R.drawable.mail2);
        title.setText(message_List.get(position).getTitle());
        sender.setText(message_List.get(position).getSender());
        date.setText(message_List.get(position).getDate());

        return v;
    }
}