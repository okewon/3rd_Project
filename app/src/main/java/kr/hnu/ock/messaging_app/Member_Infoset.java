package kr.hnu.ock.messaging_app;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import kr.hnu.ock.messaging_app.request.Modify_User_Info_Request;

public class Member_Infoset extends Fragment {

    Intent intent;
    Member member;
    Uri selected_Image_URI;
    String image_src = null;
    private ImageButton img_btn;
    private TextView txt_id;
    private EditText txt_pw, txt_name;
    private Spinner sp_major;
    private Button update_btn;
    private final static int PICK_IMAGE_REQUEST = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.infoset, container, false);

        img_btn = (ImageButton) root.findViewById(R.id.btn_image_set);
        txt_id = (TextView) root.findViewById(R.id.textViewMyId);
        txt_pw = (EditText) root.findViewById(R.id.editText_newPw);
        txt_name = (EditText) root.findViewById(R.id.editText_newName);
        sp_major = (Spinner) root.findViewById(R.id.sp_infoset);
        update_btn = (Button) root.findViewById(R.id.btn_change);

        intent = ((Sub_Fragment)getActivity()).getIntent();
        member = (Member) intent.getSerializableExtra("loginUser");
        sp_major.setAdapter(ArrayAdapter.createFromResource(root.getContext(), R.array.data_spinner, R.layout.support_simple_spinner_dropdown_item));

        txt_id.setText(member.getId());
        txt_pw.setText(member.getPw());
        txt_name.setText(member.getName());

        byte[] byteArray = getBlob(member.getImg_src());
        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        img_btn.setImageBitmap(bm);

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!txt_pw.getText().toString().isEmpty() && !txt_name.getText().toString().isEmpty()){
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean isSuccess = jsonResponse.getBoolean("success");
                                if(isSuccess){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("회원정보 수정 성공!").setPositiveButton("OK", null).create().show();

                                    member.setMajor(sp_major.getItemAtPosition(sp_major.getSelectedItemPosition()).toString());
                                    member.setPw(txt_pw.getText().toString());
                                    member.setName(txt_name.getText().toString());
                                    member.setImg_src(image_src);

                                    intent.putExtra("loginUser", member);

                                    Sub_Fragment.LoginUser_Info(member);

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new Message_Box_Fragment());
                                    fragmentTransaction.commit();
                                    ((Sub_Fragment) getActivity()).getSupportActionBar().setTitle("받은 메세지 함");
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("회원정보 수정 실패.").setNegativeButton("Retry", null).create().show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };

                    Modify_User_Info_Request modify_user_info_request = new Modify_User_Info_Request(new Member(txt_id.getText().toString(), txt_pw.getText().toString(), txt_name.getText().toString(), sp_major.getItemAtPosition(sp_major.getSelectedItemPosition()).toString(), ""), responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(modify_user_info_request);
                }else{
                    Toast.makeText(getContext(), "pw 혹은 이름이 공백입니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE_REQUEST:
                if(resultCode == getActivity().RESULT_OK){
                    selected_Image_URI = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContext().getContentResolver().openInputStream(selected_Image_URI);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    Bitmap seletedImage = BitmapFactory.decodeStream(imageStream);
                    seletedImage = Bitmap.createScaledBitmap(seletedImage, 350, 350, true);
                    img_btn.setImageBitmap(seletedImage);

                    image_src = SignUpActivity.getFilePathFromURI(getContext(), selected_Image_URI);

                }
            break;
        }
    }

    private byte[] getBlob(String image){
        ByteArrayBuffer baf = new ByteArrayBuffer(500);
        try{
            String FILE_PATH1 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath();
            File file = new File(image);
            InputStream is = new FileInputStream(file);

            BufferedInputStream bis = new BufferedInputStream(is);
            int current = 0;
            while((current = bis.read()) != -1){
                baf.append((byte)current);
            }
            return baf.toByteArray();
        }catch (Exception e){
            Log.d("ImageManager", "Error : " + e.toString());
        }
        return baf.toByteArray();
    }
}
