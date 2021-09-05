package com.adaatham.suthar.view;

import androidx.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.DeathMemberPaid;
import com.adaatham.suthar.model.NoticeList;
import com.adaatham.suthar.model.PaymentDetail;
import com.adaatham.suthar.model.Premiumlist;
import com.adaatham.suthar.model.Premiumnotice;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PayNoticeActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/Addatham22";
    private static final String FILE_NAME = "pnotice";
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private GetPendingMember pendingMember;
    private ProgressBar progressBar;
    private String id;
    private String user_name;
    private String name;
    private boolean isLogin;
    private boolean isReg;
    private boolean isSSy;
    private String role;
    private String mem_id;
    private CustomAdapter customAdapter;
    private ArrayList<Premiumnotice> premiumnoticeArrayList;
    private EditText edtSearch;
    private Button btnSearch;
    private ArrayList<DeathMemberPaid> deathMemberPaidArrayList;
    private BroadcastReceiver broadcastReceiver;
    private ArrayList<String> nameOfMemberList;
    private ArrayList<String> memNoList;
    private ArrayList<String> amountList;
    private GetPaymentDetail getPaymentDetail;
    private ArrayList<Premiumlist> premiumlistArrayList;


    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);

        deleteDir(new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY));

        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY, "failed to create directory");
                return null;
            }
        }

        // Create a media file name

        File mediaFile = null;


        try {
            mediaFile = File.createTempFile(FILE_NAME, ".pdf", mediaStorageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mediaFile;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_pay_notice);
        premiumnoticeArrayList = new ArrayList<>();
        deathMemberPaidArrayList = new ArrayList<>();
        nameOfMemberList = new ArrayList<>();
        memNoList = new ArrayList<>();
        amountList = new ArrayList<>();

        getPaymentDetail = getPremiumPay(Constants.BASE_URL);


        // ------------------------------------ PAID DEATH MEMBER------------------------------//


        final DeathMemberPaid deathMemberPaid1 = new DeathMemberPaid();

        deathMemberPaid1.setId("1");
        deathMemberPaid1.setClaim_no("51");
        deathMemberPaid1.setName("Naynaben Nareshbhai Suthar");
        deathMemberPaid1.setDeath_date("24-04-2018");
        deathMemberPaid1.setPaid_amount("100120");
        deathMemberPaid1.setMember_id("2429");


        final DeathMemberPaid deathMemberPaid2 = new DeathMemberPaid();

        deathMemberPaid2.setId("2");
        deathMemberPaid2.setClaim_no("52");
        deathMemberPaid2.setName("Minaben BhavaniShankar Suthar");
        deathMemberPaid2.setDeath_date("28-04-2018");
        deathMemberPaid2.setPaid_amount("100180");
        deathMemberPaid2.setMember_id("183");


        final DeathMemberPaid deathMemberPaid3 = new DeathMemberPaid();

        deathMemberPaid3.setId("3");
        deathMemberPaid3.setClaim_no("53");
        deathMemberPaid3.setName("SanjayKumar Manubhai Mevada");
        deathMemberPaid3.setDeath_date("05-07-2018");
        deathMemberPaid3.setPaid_amount("100640");
        deathMemberPaid3.setMember_id("757");

        final DeathMemberPaid deathMemberPaid4 = new DeathMemberPaid();

        deathMemberPaid4.setId("4");
        deathMemberPaid4.setClaim_no("54");
        deathMemberPaid4.setName("Kanaiyalal Chunilal Suthar");
        deathMemberPaid4.setDeath_date("09-07-2018");
        deathMemberPaid4.setPaid_amount("100640");
        deathMemberPaid4.setMember_id("2039");


        final DeathMemberPaid deathMemberPaid5 = new DeathMemberPaid();

        deathMemberPaid5.setId("5");
        deathMemberPaid5.setClaim_no("55");
        deathMemberPaid5.setName("MadhuBen Keshavlal Suthar");
        deathMemberPaid5.setDeath_date("21-08-2018");
        deathMemberPaid5.setPaid_amount("101080");
        deathMemberPaid5.setMember_id("947");


        final DeathMemberPaid deathMemberPaid6 = new DeathMemberPaid();

        deathMemberPaid6.setId("6");
        deathMemberPaid6.setClaim_no("56");
        deathMemberPaid6.setName("Jayantilal Manilal Suthar");
        deathMemberPaid6.setDeath_date("24-09-2018");
        deathMemberPaid6.setPaid_amount("101160");
        deathMemberPaid6.setMember_id("16");


        final DeathMemberPaid deathMemberPaid7 = new DeathMemberPaid();

        deathMemberPaid7.setId("7");
        deathMemberPaid7.setClaim_no("57");
        deathMemberPaid7.setName("JagdishChandra Maganlal Suthar");
        deathMemberPaid7.setDeath_date("24-09-2018");
        deathMemberPaid7.setPaid_amount("101160");
        deathMemberPaid7.setMember_id("1412");


        deathMemberPaidArrayList.add(deathMemberPaid1);
        deathMemberPaidArrayList.add(deathMemberPaid2);
        deathMemberPaidArrayList.add(deathMemberPaid3);
        deathMemberPaidArrayList.add(deathMemberPaid4);
        deathMemberPaidArrayList.add(deathMemberPaid5);
        deathMemberPaidArrayList.add(deathMemberPaid6);
        deathMemberPaidArrayList.add(deathMemberPaid7);


        //-------------------------------------------END PAID MEMBER---------------------------//

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, PayNoticeActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        isSSy = sharedPreferences.getBoolean(Constants.IS_SSY, false);
        mem_id = sharedPreferences.getString(Constants.MEMBER_ID, "");

        pendingMember = getSearchedMember(Constants.BASE_URL);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PayNoticeActivity.this, DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(new LinearLayoutManager(PayNoticeActivity.this));
        rvList.addItemDecoration(dividerItemDecoration);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (role.equals("3")) {
//            edtSearch.setVisibility(View.GONE);
//            btnSearch.setVisibility(View.GONE);
        }

        // TODO call death paid member list api


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtSearch.getText().toString().trim().isEmpty()) {
                    pendingMember.getMemberDetail("1", "", edtSearch.getText().toString().trim(), "").enqueue(new Callback<NoticeList>() {
                        @Override
                        public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {
                                premiumnoticeArrayList = (ArrayList<Premiumnotice>) response.body().getPremiumnotice();

                                if (premiumnoticeArrayList.size() > 0) {

                                    getMembersPayDetail(premiumnoticeArrayList.get(0).getMemberId());

                                    customAdapter = new CustomAdapter(premiumnoticeArrayList);
                                    rvList.setAdapter(customAdapter);
                                } else {
                                    Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                                    customAdapter = new CustomAdapter(premiumnoticeArrayList);
                                    rvList.setAdapter(customAdapter);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<NoticeList> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        });


        //TODO wrong id for user
        if (role.equals("3")) {
            pendingMember.getMemberDetail("2", "", id, "").enqueue(new Callback<NoticeList>() {
                @Override
                public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        premiumnoticeArrayList = (ArrayList<Premiumnotice>) response.body().getPremiumnotice();

                        if (premiumnoticeArrayList.size() > 0) {
                            getMembersPayDetail(premiumnoticeArrayList.get(0).getMemberId());

                            customAdapter = new CustomAdapter(premiumnoticeArrayList);
                            rvList.setAdapter(customAdapter);
                        } else {
                            Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                            customAdapter = new CustomAdapter(premiumnoticeArrayList);
                            rvList.setAdapter(customAdapter);
                        }
                    }

                }

                @Override
                public void onFailure(Call<NoticeList> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            pendingMember.getMemberDetail("2", role, "", id).enqueue(new Callback<NoticeList>() {
                @Override
                public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        premiumnoticeArrayList = (ArrayList<Premiumnotice>) response.body().getPremiumnotice();

                        if (premiumnoticeArrayList.size() > 0) {
                            getMembersPayDetail(premiumnoticeArrayList.get(0).getMemberId());

                            customAdapter = new CustomAdapter(premiumnoticeArrayList);
                            rvList.setAdapter(customAdapter);
                        } else {
                            Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                            customAdapter = new CustomAdapter(premiumnoticeArrayList);
                            rvList.setAdapter(customAdapter);
                        }
                    }

                }

                @Override
                public void onFailure(Call<NoticeList> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PayNoticeActivity.this, "No result found", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_notice, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_pdf_view) {

            //TODO PDF view

//            try {
//                generatePDF();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            if (premiumnoticeArrayList.size() > 0)
                copyAssets();


        } else if (item.getItemId() == R.id.action_pdf_view1) {
            if (premiumnoticeArrayList.size() > 0)

                copyAssets();
        }


        return super.onOptionsItemSelected(item);
    }

    GetPendingMember getSearchedMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetPendingMember.class);
    }

    public void generatePDF() throws FileNotFoundException {

        File file = getOutputMediaFile();

        if (file != null) {
            Document document = new Document(PageSize.A4);
            // step 2
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file.getAbsolutePath()));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            // step 3
            document.open();
            // step 4
            try {

                Paragraph paragraph = new Paragraph();

                float fntSize, lineSpacing;
                fntSize = 10f;
                lineSpacing = 30;
//                paragraph = new Paragraph(new Phrase(lineSpacing,"",
//                        FontFactory.getFont(FontFactory.TIMES_BOLD, 16)));
                ByteArrayOutputStream byteArrayOutputStream = loadImageFrom(0);

                Image i = Image.getInstance(byteArrayOutputStream.toByteArray());
                i.scalePercent(100f);
                paragraph.add(i);
                paragraph.setPaddingTop(5f);
                paragraph.setSpacingAfter(30f);

                Paragraph pDate = new Paragraph(new Phrase(lineSpacing, "Oct-2018",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20)));
                pDate.setSpacingAfter(30);


                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100f);
                table.setWidths(new int[]{1, 2, 6, 3, 3, 2});
                table.setHorizontalAlignment(Element.ALIGN_CENTER);


                for (int aw = 0; aw < 6; aw++) {

                    switch (aw) {

                        case 0:
                            table.addCell(new Phrase(lineSpacing, "Sr No",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));

                            break;
                        case 1:
                            table.addCell(new Phrase(lineSpacing, "Claim No",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 2:
                            table.addCell(new Phrase(lineSpacing, "Name",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 3:
                            table.addCell(new Phrase(lineSpacing, "Death Date",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 4:
                            table.addCell(new Phrase(lineSpacing, "Credit Amt",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 5:
                            table.addCell(new Phrase(lineSpacing, "Member No",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;


                    }


                }


                //add data
                for (int aw = 0; aw < deathMemberPaidArrayList.size(); aw++) {

                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getId(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getClaim_no(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getName(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getDeath_date(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getPaid_amount(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                    table.addCell(new Phrase(lineSpacing, deathMemberPaidArrayList.get(aw).getMember_id(),
                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));


                }


                Paragraph paragraphSign = new Paragraph();
                paragraphSign.setAlignment(Element.ALIGN_RIGHT);


                ByteArrayOutputStream byteArrayOutputStream22 = loadImageFrom(1);

                Image i2 = Image.getInstance(byteArrayOutputStream22.toByteArray());
                i2.scalePercent(100f);
                paragraphSign.add(i2);
                paragraphSign.setPaddingTop(5f);


                document.add(paragraph);
                document.add(pDate);
                document.add(table);
                document.add(paragraphSign);

            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // step 5
            document.close();

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }

        }
    }

    ByteArrayOutputStream loadImageFrom(int type) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if (type == 0) {
            try {
                // get input stream
                InputStream ims = getAssets().open("rsz_adatitle_2.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);

                bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);

            } catch (IOException ex) {
                return null;
            }
        } else {
            try {
                // get input stream
                InputStream ims = getAssets().open("rsz_signadatham.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);

                bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);

            } catch (IOException ex) {
                return null;
            }
        }


        return stream;

    }


    private void copyAssets() {
        AssetManager assetManager = getAssets();
        File filePath = null;
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {

                if (filename.equals("circular.pdf")) {

                    in = assetManager.open(filename);
                    File folder = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

                    boolean isPresent = true;

                    if (!folder.exists()) {
                        isPresent = folder.mkdir();
                    }

                    if (isPresent) {

                        filePath = new File(folder.getAbsolutePath(), "notice.pdf");
                        out = new FileOutputStream(filePath);
                        copyFile(in, out);


                        /**
                         *  Enque work request
                         */

                        OneTimeWorkRequest compressionWork =
                                new OneTimeWorkRequest.Builder(com.adaatham.suthar.common.PdfWriter.class)
                                        .setInputData(createInputDataForUri(filePath))
                                        .build();
                        WorkManager.getInstance().enqueue(compressionWork);


                        File finalFilePath = filePath;
                        Context context = this;
                        progressBar.setVisibility(View.VISIBLE);

                        WorkManager.getInstance().getWorkInfoByIdLiveData(compressionWork.getId())
                                .observe(this, new Observer<WorkInfo>() {
                                    @Override
                                    public void onChanged(@Nullable WorkInfo workInfo) {
                                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                                            progressBar.setVisibility(View.GONE);
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                Uri uri = null;
                                                // So you have to use Provider
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".fileprovider", finalFilePath);

                                                    // Add in case of if We get Uri from fileProvider.
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                } else {
                                                    uri = Uri.fromFile(finalFilePath);
                                                }

                                                intent.setDataAndType(uri, "application/pdf");
                                                startActivity(intent);
                                            } catch (RuntimeException e) {

                                            }
                                        }
                                    }
                                });


                        /**
                         *  work manager end request
                         */


                    }


                }

            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }


    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private Data createInputDataForUri(File pdfFile) {
        Data.Builder builder = new Data.Builder();
        if (pdfFile != null) {
            builder.putString("file_path", pdfFile.getAbsolutePath());
            builder.putString("name", premiumnoticeArrayList.get(0).getName());
            builder.putString("number", premiumnoticeArrayList.get(0).getMId());
            builder.putString("amount", premiumnoticeArrayList.get(0).getAmount());
            builder.putStringArray("name_arr", nameOfMemberList.toArray(new String[]{}));
            builder.putStringArray("mem_no_arr", memNoList.toArray(new String[]{}));
            builder.putStringArray("amount_arr", amountList.toArray(new String[]{}));


        }
        return builder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    /*
     * Step 2: Register the broadcast Receiver in the activity
     * */
    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e("received", "received");
                /*
                 * Step 3: We can update the UI of the activity here
                 * */
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("com.adaatham.suthar.pdfc"));
    }

    GetPaymentDetail getPremiumPay(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(GetPaymentDetail.class);
    }

    private void getMembersPayDetail(String mem_id) {
        progressBar.setVisibility(View.VISIBLE);
        nameOfMemberList.clear();
        memNoList.clear();
        amountList.clear();
        getPaymentDetail.getMemberPay(id, mem_id, "1").enqueue(new Callback<PaymentDetail>() {
            @Override
            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();


                    if (premiumlistArrayList.size() > 0) {
                        double total = 0;
                        double subtotal = 0;

                        ArrayList<Double> subtotalArray = new ArrayList<>();

                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

                            nameOfMemberList.add(premiumlistArrayList.get(i).getName());
                            memNoList.add(premiumlistArrayList.get(i).getMemberId());
                            amountList.add(premiumlistArrayList.get(i).getAmount());

                            if (premiumlistArrayList.get(i).getPanalty().isEmpty()) {
                                subtotal = Double.parseDouble(premiumlistArrayList.get(i).getAmount());
                            } else {
                                subtotal = Double.parseDouble(premiumlistArrayList.get(i).getAmount()) +
                                        Double.parseDouble(premiumlistArrayList.get(i).getPanalty());

                            }
                            subtotalArray.add(subtotal);
                        }


                        for (int i = 0; i < subtotalArray.size(); i++) {
                            total = total + subtotalArray.get(i);
                        }

                    }


//                    try {
//                        generatePDF();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }

            }

            @Override
            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    interface GetPendingMember {

        @POST("ssy/premiumnoticeapi/")
        @FormUrlEncoded()
        Call<NoticeList> getMemberDetail(@Field("type") String type, @Field("role") String role, @Field("member_no") String m_no, @Field("l_id") String l_id);


    }

    interface GetPaymentDetail {
        @POST("ssy/premiumdetailsapi/")
        @FormUrlEncoded
        Call<PaymentDetail> getMemberPay(@Field("l_id") String l_id, @Field("member_no") String mid, @Field("type") String type);

    }

    /**
     * Provide views to RecyclerView with data from mDataSet.
     */
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private ArrayList<Premiumnotice> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public CustomAdapter(ArrayList<Premiumnotice> dataSet) {
            mDataSet = dataSet;
        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_family_member, viewGroup, false);


            return new ViewHolder(v);
        }

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {


            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.tvMemberName.setText(mDataSet.get(position).getName());
            viewHolder.tvMemberId.setText(mDataSet.get(position).getMemberId());
            viewHolder.tvStatus.setVisibility(View.GONE);

            if (role.equals("3")) {


                if (mDataSet.get(position).getStatus().equals("0")) {
                    viewHolder.tvStatus.setText("Pending");
                } else {
                    viewHolder.tvStatus.setText("Premium Collected ");

                }

                viewHolder.btnMore.setVisibility(View.VISIBLE);
                viewHolder.tvAmount.setVisibility(View.VISIBLE);
                viewHolder.tvAmount.setText(mDataSet.get(position).getAmount());
                if (!mDataSet.get(position).getPanalty().isEmpty()) {
                    viewHolder.tvLablePenalty.setVisibility(View.VISIBLE);
                    viewHolder.tvPenalty.setText(mDataSet.get(position).getPanalty());
                } else {
                    viewHolder.tvLablePenalty.setVisibility(View.GONE);
                    viewHolder.tvPenalty.setText("");
                }

            }


            switch (mDataSet.get(position).getPMonth()) {

                case "1":
                    viewHolder.tvMonth.setText("Jan  ".concat(mDataSet.get(position).getPYear()));
                    break;

                case "2":
                    viewHolder.tvMonth.setText("Feb  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "3":
                    viewHolder.tvMonth.setText("March  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "4":
                    viewHolder.tvMonth.setText("April  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "5":
                    viewHolder.tvMonth.setText("May  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "6":
                    viewHolder.tvMonth.setText("June  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "7":
                    viewHolder.tvMonth.setText("July  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "8":
                    viewHolder.tvMonth.setText("Aug  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "9":
                    viewHolder.tvMonth.setText("Sep  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "10":
                    viewHolder.tvMonth.setText("Oct  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "11":
                    viewHolder.tvMonth.setText("Nov  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "12":
                    viewHolder.tvMonth.setText("Dec  ".concat(mDataSet.get(position).getPYear()));
                    break;


            }


        }
        // END_INCLUDE(recyclerViewOnCreateViewHolder)

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
        // END_INCLUDE(recyclerViewOnBindViewHolder)

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvMemberName;
            private TextView tvAmount;
            private TextView tvMonth;
            private TextView tvPenalty;
            private TextView tvAmountLable;
            private TextView tvTotal;
            private TextView tvStatus;
            private TextView tvLablePenalty;
            private Button btnMore;
            private TextView tvMemberId;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.

                tvMemberName = (TextView) v.findViewById(R.id.tvMemberName);
                tvAmount = (TextView) v.findViewById(R.id.tvAmount);
                tvMonth = (TextView) v.findViewById(R.id.tvMonth);
                tvPenalty = (TextView) v.findViewById(R.id.tvPenalty);
                tvAmountLable = (TextView) v.findViewById(R.id.labelAmount);
                tvTotal = (TextView) v.findViewById(R.id.tvTotal);
                tvMemberId = (TextView) v.findViewById(R.id.tvMemberId);
                tvLablePenalty = (TextView) v.findViewById(R.id.labelPenalty);
                tvStatus = (TextView) v.findViewById(R.id.tvStatus);
                btnMore = (Button) v.findViewById(R.id.btnViewMore);
                btnMore.setVisibility(View.VISIBLE);
                tvAmountLable.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);


                btnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PayNoticeActivity.this, PremiumDetailActivity.class);
                        intent.putExtra("m_no", mDataSet.get(getAdapterPosition()).getMemberId());
                        startActivity(intent);
                    }
                });
            }


        }
    }

}
