package com.adaatham.suthar.common;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PdfWriter extends Worker {
    File pdfFile;
    Context context;

    public PdfWriter(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        //this.pdfFile = pdffile;
        this.context = context;
    }


    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();

        try {
            PdfWriterUtils pdfWriterUtils = new PdfWriterUtils();

            String resourceUri = getInputData().getString("file_path");
            pdfFile = new File(resourceUri);
            String name = getInputData().getString("name");
            String number = getInputData().getString("number");
            String amount = getInputData().getString("amount");
            String[] nameArr = getInputData().getStringArray("name_arr");
            String[] memNoArr = getInputData().getStringArray("mem_no_arr");
            String[] amountArr = getInputData().getStringArray("amount_arr");
            pdfWriterUtils.overWritePdf(pdfFile, applicationContext, name, number, amount
                    , nameArr, memNoArr, amountArr);

            Intent intent = new Intent("com.adaatham.suthar.pdfc");
            context.sendBroadcast(intent);


            return Result.success();
        } catch (Throwable throwable) {

            return Result.failure();
        }


    }
}
