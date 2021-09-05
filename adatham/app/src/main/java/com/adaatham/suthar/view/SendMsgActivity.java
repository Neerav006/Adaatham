package com.adaatham.suthar.view;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.MyRes;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class SendMsgActivity extends AppCompatActivity {

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextInputEditText edtMsg;
    private Button btnSubmit;
    private SendMsg sendMsg;
    private ProgressDialog progressDialog;
    private String m_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        sendMsg = sendMsgToAdmin(Constants.BASE_URL);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, SendMsgActivity.this);
        m_id = sharedPreferences.getString(Constants.MEMBER_ID, null);


        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtMsg = (TextInputEditText) findViewById(R.id.edtMsg);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtMsg.getText().toString().trim().isEmpty()) {

                    showProgressDialog();
                    sendMsg.sendMyMsg(m_id, edtMsg.getText().toString()).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(retrofit2.Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {
                                if (response.body().getMsg().equalsIgnoreCase("true")) {
                                    Toast.makeText(SendMsgActivity.this, "Successfully sent", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(retrofit2.Call<MyRes> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(SendMsgActivity.this, "Not Successfully sent", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

    }

    SendMsg sendMsgToAdmin(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SendMsg.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(SendMsgActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    interface SendMsg {
        @POST("inquiry/addinquiryapi/")
        @FormUrlEncoded
        retrofit2.Call<MyRes> sendMyMsg(@Field("m_id") String m_id, @Field("msg") String msg);
    }
}
