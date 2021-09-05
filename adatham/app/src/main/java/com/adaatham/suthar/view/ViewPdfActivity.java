package com.adaatham.suthar.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.adaatham.suthar.R;

public class ViewPdfActivity extends AppCompatActivity {


    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);


        if(getIntent()!=null){

            path=getIntent().getStringExtra("path");
        }


    }
}
