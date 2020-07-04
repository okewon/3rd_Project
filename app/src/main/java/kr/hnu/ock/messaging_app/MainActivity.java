package kr.hnu.ock.messaging_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import kr.hnu.ock.messaging_app.request.Login_Request;

public class MainActivity extends AppCompatActivity {

    EditText txt_id, txt_pw;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        txt_id = (EditText) findViewById(R.id.editText_Id);
        txt_pw = (EditText) findViewById(R.id.editText_Pw);
        checkBox = (CheckBox) findViewById(R.id.saveLogin);
        SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String id = pref.getString("ID", "");
        String pw = pref.getString("PW", "");
        if(id.length() != 0){
            txt_id.setText(id);
            txt_pw.setText(pw);
        }
    }

    public void Login(View view) {
        //select * from member where id = ? and pw = ?
        String id = txt_id.getText().toString();
        String pw = txt_pw.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    final JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("success");
                    if(isSuccess){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(jsonResponse.getString("id") + "님 환영합니다!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(checkBox.isChecked()){
                                    SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("ID", txt_id.getText().toString());
                                    editor.putString("PW", txt_pw.getText().toString());
                                    editor.commit();
                                }else{
                                    SharedPreferences pref = getSharedPreferences("userInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();
                                    editor.commit();
                                }

                                dialogInterface.dismiss();
                                Intent intent = new Intent(MainActivity.this, Sub_Fragment.class);

                                //login된 user에 대한 정보를 intent에 담아 sub_Fragment에 넘겨줌
                                try {
                                    String id = jsonResponse.getString("id");
                                    String pw = jsonResponse.getString("pw");
                                    String name = jsonResponse.getString("name");
                                    String major = jsonResponse.getString("major");
                                    String image_src = jsonResponse.getString("image_src");

                                    intent.putExtra("loginUser", new Member(id, pw, name, major, image_src));
                                    startActivity(intent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        builder.create().show();

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("잘못된 ID 혹은 PW입니다.").setNegativeButton("Retry", null).create().show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        if(!id.equals("") && !pw.equals("")){
            Login_Request login_request = new Login_Request(id, pw, responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(login_request);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("ID 혹은 PW에 빈칸이 존재합니다");
            builder.setNegativeButton("Retry", null).create().show();
        }
    }

    public void mOnSign(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
