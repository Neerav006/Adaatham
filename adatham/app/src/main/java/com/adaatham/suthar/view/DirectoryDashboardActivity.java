package com.adaatham.suthar.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.adaatham.suthar.R;

public class DirectoryDashboardActivity extends AppCompatActivity {

    private LinearLayout llFirstrow;
    private CardView cvDeathMember;
    private CardView cvYuvatiInfo;
    private CardView cvProfessionalInfo;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_dashboard);

        llFirstrow = (LinearLayout) findViewById(R.id.llFirstrow);
        cvDeathMember = (CardView) findViewById(R.id.cvDeathMember);
        cvYuvatiInfo = (CardView) findViewById(R.id.cvYuvatiInfo);
        cvProfessionalInfo = (CardView) findViewById(R.id.cvProfessionalInfo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvDeathMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectoryDashboardActivity.this, DeathMemberListActivity.class);
                startActivity(intent);
            }
        });

        cvYuvatiInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectoryDashboardActivity.this, MatrimonialActivity.class);
                startActivity(intent);

            }
        });

        cvProfessionalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectoryDashboardActivity.this, BusinessDirectoryListActivity.class);
                startActivity(intent);
            }
        });
    }
}
