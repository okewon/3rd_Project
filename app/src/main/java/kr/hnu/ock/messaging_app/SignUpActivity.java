package kr.hnu.ock.messaging_app;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import kr.hnu.ock.messaging_app.request.ID_Check_Request;
import kr.hnu.ock.messaging_app.request.SingUp_Request;

public class SignUpActivity extends AppCompatActivity {

    ImageButton img_btn;
    EditText txt_id, txt_pw, txt_name;
    Spinner sp_major;
    Uri selected_Image_URI;
    final static int PICK_IMAGE_REQUEST = 0;
    String image_src = null;
    Boolean id_check = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        //activity_signup.xml에 있는 view에 대한 findviewbyid
        img_btn = (ImageButton) findViewById(R.id.btn_image);
        txt_id = (EditText) findViewById(R.id.editText_mId);
        txt_pw = (EditText) findViewById(R.id.editText_mPw);
        txt_name = (EditText) findViewById(R.id.editText_mName);
        sp_major = (Spinner) findViewById(R.id.sp_major);

        //selectedUri 변수에 대한 초기화
        selected_Image_URI = Uri.EMPTY;

        //spinner에 대한 setAdapter
        sp_major.setAdapter(ArrayAdapter.createFromResource(this, R.array.data_spinner, R.layout.support_simple_spinner_dropdown_item));
    }

    public void onImageClick(View view) {
        //갤러리로 이동
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void mOnCheck(View view) {
        //select * from member where id = ?
        String id = txt_id.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("success");
                    if(isSuccess){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage("이미 존재하는 ID 입니다.");
                        builder.setNegativeButton("Retry", null).create().show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage("사용할 수 있습니다.").setPositiveButton("OK", null).create().show();
                        id_check = true;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        if(!id.equals("")){
            ID_Check_Request id_check_request = new ID_Check_Request(id, responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(id_check_request);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage("ID이 빈칸입니다.");
            builder.setNegativeButton("Retry", null).create().show();
        }
    }

    public void mOnClick(View view) {
        //insert into member values(?, ?, ?, ?, ?)

        String id = txt_id.getText().toString();
        String pw = txt_pw.getText().toString();
        String name = txt_name.getText().toString();
        String major = (String) sp_major.getItemAtPosition(sp_major.getSelectedItemPosition());

        if(!id_check){
            Toast.makeText(this, "ID 중복 체크를 수행해주세요", Toast.LENGTH_LONG).show();
        }else{
            Response.Listener<String> responseListener  = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean isSuccess = jsonResponse.getBoolean("success");
                        if(isSuccess){
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage("계정 생성 성공!");
                            builder.setPositiveButton("OK", null).create().show();
                            finish();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setMessage("계정 생성 실패!");
                            builder.setNegativeButton("Retry", null).create().show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };

            if(!pw.equals("") && !name.equals("")){
                Member member = new Member(id, pw, name, major, image_src);
                SingUp_Request singUp_request = new SingUp_Request(member, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                requestQueue.add(singUp_request);
            }else{
                Toast.makeText(this, "PW, NAME에 빈칸이 존재합니다", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnIntent);
        switch (requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    selected_Image_URI = imageReturnIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selected_Image_URI);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    Bitmap seletedImage = BitmapFactory.decodeStream(imageStream);
                    seletedImage = Bitmap.createScaledBitmap(seletedImage, 350, 350, true);
                    img_btn.setImageBitmap(seletedImage);

                    image_src = getFilePathFromURI(this, selected_Image_URI);
                }
            break;
        }
    }

    public static String getFilePathFromURI(Context context, Uri contentUri){
        String fileName = getFileName(contentUri);
        if(!TextUtils.isEmpty(fileName)){
            File copyFile = new File(context.getFilesDir() + File.separator + fileName);;
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private static String getFileName(Uri uri){
        if(uri == null)
            return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf("/");
        if(cut != -1){
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if(inputStream == null)
                return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}