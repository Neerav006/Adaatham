package com.adaatham.suthar.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ChangePwdActivity extends AppCompatActivity {

    private final String BASE_URL = "https://ssy.adaathamwelfare.org/";
    private final String root = "50e423ff4b2b6fd7d61055c4a80bb55d0b6fdbe8fa2a4ad6459087e729d2a11c";
    private TextInputEditText edtOld;
    private TextInputEditText edtNew;
    private TextInputEditText edtConf;
    private Button btnSubmit;
    private String id;
    private boolean isLogin;
    private String name;
    private String user_name;
    private ChangePassword changePassword;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePassword = getAPIService(BASE_URL);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, ChangePwdActivity.this);
        id = sharedPreferences.getString(Constants.ID, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(user_name, false);

        edtOld = (TextInputEditText) findViewById(R.id.activity_change_edtOld);
        edtNew = (TextInputEditText) findViewById(R.id.activity_change_edtNew);
        edtConf = (TextInputEditText) findViewById(R.id.activity_change_edtNewCon);

        btnSubmit = (Button) findViewById(R.id.activity_forgot_btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old = edtOld.getText().toString().trim();
                String newPwd = edtNew.getText().toString().trim();
                String newConf = edtConf.getText().toString().trim();


                if (!edtOld.getText().toString().trim().isEmpty()) {

                    if (!edtNew.getText().toString().trim().isEmpty() && !edtConf.getText().toString().trim().isEmpty()) {


                        if (edtNew.getText().toString().trim().equals(edtConf.getText().toString().trim())) {

                            //TODO call change

                            showProgressDialog();
                            changePassword.changePwd(id, edtOld.getText().toString().trim()
                                    , edtNew.getText().toString().trim(), root).enqueue(new Callback<MyRes>() {
                                @Override
                                public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        if (response.body().getMsg().equalsIgnoreCase("true")) {
                                            Toast.makeText(ChangePwdActivity.this, "Successfully changed", Toast.LENGTH_SHORT).show();


                                            SharedPreferences.Editor editor = Utils.writeToPreference(Constants.MY_PREF, ChangePwdActivity.this);
                                            editor.putBoolean(user_name, false);

                                            editor.putString(Constants.USER_NAME, null);
                                            editor.putString(Constants.NAME, null);
                                            editor.putString(Constants.ID, null);

                                            editor.apply();

                                            finishAffinity();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);


                                        } else {
                                            Toast.makeText(ChangePwdActivity.this, "Not changed successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ChangePwdActivity.this, "Connection error", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                                    Toast.makeText(ChangePwdActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(ChangePwdActivity.this, "Enter correct password", Toast.LENGTH_LONG).show();
                            edtConf.requestFocus();
                        }

                    } else {

                        if (edtNew.getText().toString().trim().isEmpty()) {
                            Toast.makeText(ChangePwdActivity.this, "Enter New password", Toast.LENGTH_LONG).show();
                            edtNew.requestFocus();

                        } else {
                            Toast.makeText(ChangePwdActivity.this, "ReEnter password", Toast.LENGTH_LONG).show();
                            edtConf.requestFocus();
                        }


                    }


                } else {
                    edtOld.requestFocus();
                    Toast.makeText(ChangePwdActivity.this, "Enter valid password", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    ChangePassword getAPIService(String baseUrl) {

        return RetrofitClient.getClient(baseUrl).create(ChangePassword.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(ChangePwdActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    interface ChangePassword {

        @POST("home/changepassword_api/")
        @FormUrlEncoded
        Call<MyRes> changePwd(@Field("id") String id, @Field("old_pwd") String old_pwd, @Field("new_pwd") String new_pwd, @Field("root") String root);

    }
}
