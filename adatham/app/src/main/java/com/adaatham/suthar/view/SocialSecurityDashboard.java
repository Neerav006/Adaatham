package com.adaatham.suthar.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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

public class SocialSecurityDashboard extends AppCompatActivity {
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private LinearLayout llFirstrow;
    private CardView cvApply;
    private CardView cvPayPremium;
    private CardView cvPremiumNotice;
    private CardView cvUpdateChalan;
    private CardView cvYourCollection;
    private CardView cvEditDetail;
    private String id;
    private String user_name;
    private String name;
    private boolean isLogin;
    private boolean isReg;
    private boolean isSSy;
    private String role;
    private String mem_id;
    private RequestForSSY requestForSSY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_security_dashboard);

        requestForSSY = requestForSSY(Constants.BASE_URL);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, SocialSecurityDashboard.this);
        id = sharedPreferences.getString(Constants.ID, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        isSSy = sharedPreferences.getBoolean(Constants.IS_SSY, false);
        mem_id = sharedPreferences.getString(Constants.MEMBER_ID, "");


        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llFirstrow = (LinearLayout) findViewById(R.id.llFirstrow);
        cvApply = (CardView) findViewById(R.id.cvApply);
        cvApply.setVisibility(View.GONE);
        cvPayPremium = (CardView) findViewById(R.id.cvPayPremium);
        cvPremiumNotice = (CardView) findViewById(R.id.cvPremiumNotice);
        cvUpdateChalan = (CardView) findViewById(R.id.cvUpdateChalan);
        cvYourCollection = (CardView) findViewById(R.id.cvYourCollection);
        cvEditDetail = findViewById(R.id.cvEditDetail);


        if (role.equals("2")) {
            cvPayPremium.setVisibility(View.VISIBLE);
            cvUpdateChalan.setVisibility(View.VISIBLE);
            cvPremiumNotice.setVisibility(View.VISIBLE);
            cvYourCollection.setVisibility(View.VISIBLE);
           // cvEditDetail.setVisibility(View.VISIBLE);


        } else if (role.equals("3")) {
            cvPayPremium.setVisibility(View.GONE);
            cvUpdateChalan.setVisibility(View.GONE);
            cvYourCollection.setVisibility(View.GONE);
           // cvEditDetail.setVisibility(View.GONE);

            if (isSSy) {
                cvPremiumNotice.setVisibility(View.VISIBLE);
                cvApply.setVisibility(View.GONE);
            } else {
                cvApply.setVisibility(View.VISIBLE);
                cvPremiumNotice.setVisibility(View.VISIBLE);
            }


        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();

            }
        });

        cvPayPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SocialSecurityDashboard.this, PaymentListActivity.class);
                startActivity(intent);

            }
        });

        cvPremiumNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SocialSecurityDashboard.this, PayNoticeActivity.class);
                startActivity(intent);
            }
        });

        cvUpdateChalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialSecurityDashboard.this, UpdateChalanActivity.class);
                startActivity(intent);
            }
        });

        cvYourCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialSecurityDashboard.this, YourCollectionActivity.class);
                startActivity(intent);
            }
        });

        cvEditDetail.setOnClickListener(v -> {


        });


    }


    RequestForSSY requestForSSY(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(RequestForSSY.class);
    }

    public void showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(SocialSecurityDashboard.this);


        dialogBuilder.setMessage("Are you sure to apply?");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();


                cvApply.setEnabled(false);
                requestForSSY.requesttossy(mem_id).enqueue(new Callback<MyRes>() {
                    @Override
                    public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                        cvApply.setEnabled(true);

                        if (response.isSuccessful()) {
                            if (response.body().getMsg().equalsIgnoreCase("true")) {

                                SharedPreferences.Editor editor = Utils.writeToPreference(Constants.MY_PREF, SocialSecurityDashboard.this);
                                editor.putBoolean(Constants.IS_SSY, true);

                                cvApply.setVisibility(View.GONE);
                                Toast.makeText(SocialSecurityDashboard.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                            } else if (response.body().getMsg().equalsIgnoreCase("false")) {
                                Toast.makeText(SocialSecurityDashboard.this, "Not Requested", Toast.LENGTH_SHORT).show();
                            } else if (response.body().getMsg().equalsIgnoreCase("xxx")) {

                                Toast.makeText(SocialSecurityDashboard.this, "Already Requested", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyRes> call, Throwable t) {
                        cvApply.setEnabled(true);
                    }
                });


            }


        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass


            }
        });
        androidx.appcompat.app.AlertDialog b = dialogBuilder.create();
        b.show();
    }


    interface RequestForSSY {
        @POST("ssy/requestssyapi/")
        @FormUrlEncoded
        Call<MyRes> requesttossy(@Field("m_id") String m_id);

    }

}
