package com.adaatham.suthar.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.MyRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class ForgotPwdActivity extends AppCompatActivity {
    private final String BASE_URL = "http://ssy.adaathamwelfare.org/";
    private final long DOUBLE_TAP = 1500;
    private TextInputLayout tiMobile;
    private TextInputEditText edtMobile;
    private Button btnSubmit;
    private Toolbar toolbar;
    private CheckMobile checkMobile;
    private long lastclick = 0;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        checkMobile = getAPIService(BASE_URL);


        tiMobile = (TextInputLayout) findViewById(R.id.tiMobile);
        edtMobile = (TextInputEditText) findViewById(R.id.edtMobile);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

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

                if (!edtMobile.getText().toString().trim().isEmpty()) {
                    //forgot password

                    showProgressDialog();

                    checkMobile.verifymobile(edtMobile.getText().toString().trim()).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {

                                if (response.body().getMsg().equalsIgnoreCase("true")) {
                                    Intent intent = new Intent(ForgotPwdActivity.this, ForgotPwd22Activity.class);
                                    intent.putExtra(Constants.ID, response.body().getId());
                                    intent.putExtra(Constants.TYPE, "for");
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(ForgotPwdActivity.this,"Wrong mobile",Toast.LENGTH_SHORT).show();
                                }
                            } else {

                                Toast.makeText(ForgotPwdActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            Toast.makeText(ForgotPwdActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });

                } else {
                    Toast.makeText(ForgotPwdActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    CheckMobile getAPIService(String baseUrl) {

        return RetrofitClient.getClient(baseUrl).create(CheckMobile.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(ForgotPwdActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    interface CheckMobile {

        @POST("home/checkmobileapi/")
        @FormUrlEncoded
        Call<MyRes> verifymobile(@Field("mobile") String mobile

        );
    }

}
