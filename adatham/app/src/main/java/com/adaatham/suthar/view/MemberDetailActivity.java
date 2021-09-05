package com.adaatham.suthar.view;

import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.adaatham.suthar.R;
import com.adaatham.suthar.model.Deathlist;
import com.adaatham.suthar.model.Memberlist;

public class MemberDetailActivity extends AppCompatActivity {

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextView tvMemNo;
    private TextView tvRegDate;
    private TextView tvName;
    private TextView tvAddr;
    private TextView tvCity;
    private TextView tvTaluka;
    private TextView tvDist;
    private TextView tvState;
    private TextView tvPin;
    private TextView tvMobile;
    private TextView tvEmail;
    private TextView tvBirth;
    private Memberlist memberlist;
    private Deathlist deathlist;
    private TextView lableExpDate;
    private TextView tvExpDate;
    private TextView lablePay;
    private TextView tvPayableAmt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        if (getIntent() != null) {

            if (getIntent().getParcelableExtra("detail") instanceof Memberlist) {
                memberlist = getIntent().getParcelableExtra("detail");
            } else  {
                deathlist = getIntent().getParcelableExtra("detail");
            }


        }

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvMemNo = (TextView) findViewById(R.id.tvMemNo);
        tvRegDate = (TextView) findViewById(R.id.tvRegDate);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvTaluka = (TextView) findViewById(R.id.tvTaluka);
        tvDist = (TextView) findViewById(R.id.tvDist);
        tvState = (TextView) findViewById(R.id.tvState);
        tvPin = (TextView) findViewById(R.id.tvPin);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvBirth = (TextView) findViewById(R.id.tvBirth);
        lableExpDate = (TextView) findViewById(R.id.lableExpDate);
        tvExpDate = (TextView) findViewById(R.id.tvExpDate);
        lablePay = (TextView) findViewById(R.id.lablePay);
        tvPayableAmt = (TextView) findViewById(R.id.tvPayableAmt);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (memberlist != null) {

            toolbar.setTitle(memberlist.getName());
            tvName.setText(memberlist.getName());
            tvRegDate.setText(memberlist.getJoinDate());
            tvMemNo.setText(memberlist.getMemberId());
            tvAddr.setText(memberlist.getAddress());
            tvCity.setText(memberlist.getCity());
            tvTaluka.setText(memberlist.getTaluka());
            tvEmail.setText(memberlist.getEmail());
            tvMobile.setText(memberlist.getMobile());
            tvBirth.setText(memberlist.getDob());
            tvPin.setText(memberlist.getPin());
            tvState.setText(memberlist.getState());

        } else if (deathlist != null) {
            toolbar.setTitle(deathlist.getName());
            tvName.setText(deathlist.getName());
            tvRegDate.setText(deathlist.getJoinDate());
            tvMemNo.setText(deathlist.getMemberId());
            tvAddr.setText(deathlist.getAddress());
            tvCity.setText(deathlist.getCity());
            tvTaluka.setText(deathlist.getTaluka());
            tvEmail.setText(deathlist.getEmail());
            tvMobile.setText(deathlist.getMobile());
            tvBirth.setText(deathlist.getDob());
            tvPin.setText(deathlist.getPin());
            tvState.setText(deathlist.getState());

            lableExpDate.setVisibility(View.VISIBLE);
            lablePay.setVisibility(View.VISIBLE);
            tvExpDate.setVisibility(View.VISIBLE);
            tvPayableAmt.setVisibility(View.VISIBLE);
            tvExpDate.setText(deathlist.getExpireddate());
            tvPayableAmt.setText(deathlist.getAmount());
        }
    }
}
