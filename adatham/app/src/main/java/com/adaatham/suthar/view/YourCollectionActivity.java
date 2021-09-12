package com.adaatham.suthar.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.PaymentDetail;
import com.adaatham.suthar.model.Premiumlist;
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
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class YourCollectionActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/Addatham22";
    private static final String FILE_NAME = "circular";
    private RadioGroup rgCOC;
    private RadioButton rbCash;
    private RadioButton rbCheque;
    private RadioButton rbAll;
    private RecyclerView rvList;
    private TextView tvTotal;
    private String id;
    private GetPaymentDetail getPaymentDetail;
    private ArrayList<Premiumlist> premiumlistArrayList;
    private ProgressBar progressBar;
    private CustomAdapter customAdapter;
    private String name;

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
        setContentView(R.layout.activity_your_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, YourCollectionActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);
        name = sharedPreferences.getString(Constants.NAME, null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        premiumlistArrayList = new ArrayList<>();
        getPaymentDetail = getPremiumPay(Constants.BASE_URL);
        toolbar.setTitle("Your Collection");

        rgCOC = (RadioGroup) findViewById(R.id.rgCOC);
        rbCash = (RadioButton) findViewById(R.id.rbCash);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);
        rbAll = (RadioButton) findViewById(R.id.rbAll);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(YourCollectionActivity.this, DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(dividerItemDecoration);
        rvList.setLayoutManager(new LinearLayoutManager(YourCollectionActivity.this));

        getPaymentDetail.getMemberPay(id, "0").enqueue(new Callback<PaymentDetail>() {
            @Override
            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {

                double total_amount = 0.0;
                double total_penalty = 0.0;

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();

                    if (premiumlistArrayList.size() > 0) {


                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

                            total_amount = total_amount + Double.parseDouble(premiumlistArrayList.get(i).getAmount());

                            if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {

                                total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());


                            }

                        }

                        double total = total_amount + total_penalty;
                        tvTotal.setText("Total- ".concat(String.valueOf(total)));

                    }


                    customAdapter = new CustomAdapter(premiumlistArrayList);
                    rvList.setAdapter(customAdapter);


                }

            }

            @Override
            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(YourCollectionActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

            }
        });


        rgCOC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {


                    case R.id.rbCash:
                        getPaymentDetail.getMemberPay(id, "1").enqueue(new Callback<PaymentDetail>() {
                            @Override
                            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {

                                double total_amount = 0.0;
                                double total_penalty = 0.0;
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();

                                    for (int i = 0; i < premiumlistArrayList.size(); i++) {

                                        total_amount = total_amount + Double.parseDouble(premiumlistArrayList.get(i).getAmount());

                                        if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {

                                            total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());


                                        }

                                    }

                                    double total = total_amount + total_penalty;
                                    tvTotal.setText("Total- ".concat(String.valueOf(total)));

                                    customAdapter = new CustomAdapter(premiumlistArrayList);
                                    rvList.setAdapter(customAdapter);


                                }

                            }

                            @Override
                            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(YourCollectionActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                            }
                        });


                        break;
                    case R.id.rbCheque:
                        getPaymentDetail.getMemberPay(id, "2").enqueue(new Callback<PaymentDetail>() {
                            @Override
                            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {

                                double total_amount = 0.0;
                                double total_penalty = 0.0;
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();

                                    for (int i = 0; i < premiumlistArrayList.size(); i++) {

                                        total_amount = total_amount + Double.parseDouble(premiumlistArrayList.get(i).getAmount());

                                        if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {

                                            total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());


                                        }

                                    }

                                    double total = total_amount + total_penalty;
                                    tvTotal.setText("Total- ".concat(String.valueOf(total)));

                                    customAdapter = new CustomAdapter(premiumlistArrayList);
                                    rvList.setAdapter(customAdapter);


                                }

                            }

                            @Override
                            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(YourCollectionActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                            }
                        });

                        break;
                    case R.id.rbAll:
                        getPaymentDetail.getMemberPay(id, "all").enqueue(new Callback<PaymentDetail>() {
                            @Override
                            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {
                                double total_amount = 0.0;
                                double total_penalty = 0.0;
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();

                                    for (int i = 0; i < premiumlistArrayList.size(); i++) {

                                        total_amount = total_amount + Double.parseDouble(premiumlistArrayList.get(i).getAmount());

                                        if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {

                                            total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());


                                        }

                                    }

                                    double total = total_amount + total_penalty;
                                    tvTotal.setText("Total- ".concat(String.valueOf(total)));

                                    customAdapter = new CustomAdapter(premiumlistArrayList);
                                    rvList.setAdapter(customAdapter);


                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(YourCollectionActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

                            }
                        });

                        break;


                }


            }
        });


    }

    GetPaymentDetail getPremiumPay(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(GetPaymentDetail.class);
    }

    public void generatePDF() throws FileNotFoundException {

        File file = getOutputMediaFile();
        double total_amt = 0.0;
        double total_penalty = 0.0;

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

                Paragraph pDate = new Paragraph(new Phrase(lineSpacing, "",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20)));
                pDate.setSpacingBefore(30);
                pDate.setSpacingAfter(10);

                Paragraph pAgentName = new Paragraph(new Phrase(lineSpacing, "",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20)));
                pAgentName.setSpacingAfter(30);
                pAgentName.setAlignment(Element.ALIGN_CENTER);
                pAgentName.add(name);

                PdfPTable table = new PdfPTable(5);
                table.setWidths(new int[]{2, 4, 2, 2, 3});
                table.setHorizontalAlignment(Element.ALIGN_RIGHT);


                for (int aw = 0; aw < 5; aw++) {

                    switch (aw) {

                        case 0:
                            table.addCell(new Phrase(lineSpacing, "Member No",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));

                            break;
                        case 1:
                            table.addCell(new Phrase(lineSpacing, "Member Name",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 2:
                            table.addCell(new Phrase(lineSpacing, "Amount",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 3:
                            table.addCell(new Phrase(lineSpacing, "Penalty",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;
                        case 4:
                            table.addCell(new Phrase(lineSpacing, "Status",
                                    FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                            break;


                    }


                }

                for (int k = 0; k < premiumlistArrayList.size(); k++) {

                    total_amt = total_amt + Double.parseDouble(premiumlistArrayList.get(k).getAmount());

                    if (!premiumlistArrayList.get(k).getPanalty().isEmpty()) {
                        total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(k).getPanalty());
                    }


                    for (int j = 0; j < 5; j++) {

                        switch (j) {

                            case 0:
                                table.addCell(new Phrase(lineSpacing, premiumlistArrayList.get(k).getMemberId(),
                                        FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));

                                break;
                            case 1:
                                table.addCell(new Phrase(lineSpacing, premiumlistArrayList.get(k).getName(),
                                        FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                break;
                            case 2:
                                table.addCell(new Phrase(lineSpacing, premiumlistArrayList.get(k).getAmount(),
                                        FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                break;
                            case 3:
                                table.addCell(new Phrase(lineSpacing, premiumlistArrayList.get(k).getPanalty(),
                                        FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                break;
                            case 4:

                                if (premiumlistArrayList.get(k).getPayment_type().equals("0")) {
                                    table.addCell(new Phrase(lineSpacing, "Cash",
                                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                }
                                else if(premiumlistArrayList.get(k).getPayment_type().equals("2")){
                                    table.addCell(new Phrase(lineSpacing, "Online",
                                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                }
                                else {
                                    table.addCell(new Phrase(lineSpacing, "Cheque",
                                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                }


                                break;


                        }


                    }
                }

                Paragraph pTotal = new Paragraph(new Phrase(lineSpacing, "",
                        FontFactory.getFont(FontFactory.TIMES_BOLD, 20)));
                pTotal.setAlignment(Element.ALIGN_RIGHT);
                pTotal.setSpacingBefore(30);
                pTotal.setSpacingAfter(60);
                pTotal.add("Total Amount:-  ".concat(String.valueOf(total_amt)));
                pTotal.add("\n");
                pTotal.add("Total Penalty :-  ".concat(String.valueOf(total_penalty)));
                pTotal.add("\n");
                pTotal.add(new LineSeparator());
                pTotal.add("\n");
                pTotal.add("Final Amount:-  ".concat(String.valueOf(total_amt + total_penalty)));


                Paragraph paragraphSign = new Paragraph();
                paragraphSign.setAlignment(Element.ALIGN_RIGHT);


//                paragraph = new Paragraph(new Phrase(lineSpacing,"",
//                        FontFactory.getFont(FontFactory.TIMES_BOLD, 16)));
                ByteArrayOutputStream byteArrayOutputStream22 = loadImageFrom(1);

                Image i2 = Image.getInstance(byteArrayOutputStream22.toByteArray());
                i2.scalePercent(100f);
                paragraphSign.add(i2);
                paragraphSign.setPaddingTop(5f);

//rsz_signadatham


                switch (premiumlistArrayList.get(0).getPMonth()) {

                    case "1":

                        pDate.add("Jan  ".concat(premiumlistArrayList.get(0).getPYear()));

                        break;

                    case "2":
                        pDate.add("Feb  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "3":
                        pDate.add("March  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "4":
                        pDate.add("April  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "5":
                        pDate.add("May  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "6":
                        pDate.add("June  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "7":
                        pDate.add("July  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "8":
                        pDate.add("Aug  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "9":
                        pDate.add("Sep  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "10":
                        pDate.add("Oct  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "11":
                        pDate.add("Nov  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;
                    case "12":
                        pDate.add("Dec  ".concat(premiumlistArrayList.get(0).getPYear()));
                        break;


                }


//                if (Utils.getMilliSeconds(premiumlistArrayList.get(0).getUpdate_time()) != 0) {
//                    String updatedDate = Utils.getDate(Utils.getMilliSeconds(mDataSet.get(position).getUpdate_time()));
//                    if (updatedDate != null) {
//                        viewHolder.tvPremiumDate.setText(updatedDate);
//                    }
//                }

                document.add(paragraph);
                document.add(pDate);
                document.add(pAgentName);
                document.add(table);
                document.add(pTotal);
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

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Uri uri = null;
                // So you have to use Provider
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);

                    // Add in case of if We get Uri from fileProvider.
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(file);
                }

                intent.setDataAndType(uri, "application/pdf");
                startActivity(intent);
            } catch (RuntimeException e) {

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_view, menu);

        MenuItem menuItem = menu.findItem(R.id.action_pdf_view);
        MenuItem menuItem1 = menu.findItem(R.id.action_pdf_view_11);
        menuItem.setVisible(true);
        menuItem1.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_pdf_view && premiumlistArrayList != null && premiumlistArrayList.size() > 0) {
            try {
                generatePDF();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (item.getItemId() == R.id.action_pdf_view_11 && premiumlistArrayList != null && premiumlistArrayList.size() > 0) {
            try {
                generatePDF();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);


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


    interface GetPaymentDetail {
        @POST("ssy/premiumcollectionapi/")
        @FormUrlEncoded
        Call<PaymentDetail> getMemberPay(@Field("l_id") String l_id, @Field("type") String type);

    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private ArrayList<Premiumlist> mDataSet;


        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public CustomAdapter(ArrayList<Premiumlist> dataSet) {
            mDataSet = dataSet;


        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_payment_detail, viewGroup, false);

            return new ViewHolder(v);
        }

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

//           viewHolder.edtBankName.setText(mDataSet.get(position).getBank_name());
//           viewHolder.edtChequeNo.setText(mDataSet.get(position).getC_no());


//             if(hashMap.get(position)!=null && hashMap.get(position)==viewHolder.rbCheque.getId()){
//                 viewHolder.edtChequeNo.setVisibility(View.VISIBLE);
//                 viewHolder.edtBankName.setVisibility(View.VISIBLE);
//
//
//             }
//             else if(hashMap.get(position)!=null && hashMap.get(position)==viewHolder.rbCash.getId()) {
//                 viewHolder.edtChequeNo.setVisibility(View.GONE);
//                 viewHolder.edtBankName.setVisibility(View.GONE);
//
//
//             }


            if (mDataSet.get(position).getPanalty().isEmpty()) {
                viewHolder.tvPenalty.setVisibility(View.GONE);
                viewHolder.lablePenalty.setVisibility(View.GONE);
            } else {
                viewHolder.tvPenalty.setVisibility(View.VISIBLE);
                viewHolder.lablePenalty.setVisibility(View.VISIBLE);

                viewHolder.tvPenalty.setText(mDataSet.get(position).getPanalty());
            }


//            viewHolder.rgCOC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                    if (group.getCheckedRadioButtonId() == R.id.rbCheque) {
//
//                        hashMap.put(position,viewHolder.rbCheque.getId());
//                        viewHolder.edtBankName.setVisibility(View.VISIBLE);
//                        viewHolder.edtChequeNo.setVisibility(View.VISIBLE);
//                        mDataSet.get(position).setBank_name(viewHolder.edtBankName.getText().toString().trim());
//                        mDataSet.get(position).setC_no(viewHolder.edtChequeNo.getText().toString().trim());
//
//
//
//                    } else {
//                        hashMap.put(position,viewHolder.rbCash.getId());
//
//                        viewHolder.edtChequeNo.setVisibility(View.GONE);
//                        viewHolder.edtBankName.setVisibility(View.GONE);
//                        viewHolder.edtBankName.getText().clear();
//                        viewHolder.edtChequeNo.getText().clear();
//
//
//                    }
//
//                }
//            });


//             viewHolder.edtBankName.addTextChangedListener(new TextWatcher() {
//                 @Override
//                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                 }
//
//                 @Override
//                 public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                 }
//
//                 @Override
//                 public void afterTextChanged(Editable s) {
//
//                     mDataSet.get(position).setBank_name(viewHolder.edtBankName.getText().toString().trim());
//                 }
//             });
//
//             viewHolder.edtChequeNo.addTextChangedListener(new TextWatcher() {
//                 @Override
//                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                 }
//
//                 @Override
//                 public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                 }
//
//                 @Override
//                 public void afterTextChanged(Editable s) {
//                     mDataSet.get(position).setC_no(viewHolder.edtChequeNo.getText().toString().trim());
//                 }
//             });


            viewHolder.tvName.setText(mDataSet.get(position).getName());
            viewHolder.tvNo.setText(mDataSet.get(position).getMId());
            viewHolder.tvPremiumAmount.setText(mDataSet.get(position).getAmount());

            if (!mDataSet.get(position).getUpdate_time().equals("0000-00-00 00:00:00") && Utils.getMilliSeconds(mDataSet.get(position).getUpdate_time()) != 0) {
                String updatedDate = Utils.getDate(Utils.getMilliSeconds(mDataSet.get(position).getUpdate_time()));
                if (updatedDate != null) {
                    viewHolder.tvPremiumDate.setText(updatedDate);
                }
            }


//            switch (mDataSet.get(position).getPMonth()) {
//
//                case "1":
//                    viewHolder.tvPremiumDate.setText("Jan  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//
//                case "2":
//                    viewHolder.tvPremiumDate.setText("Feb  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "3":
//                    viewHolder.tvPremiumDate.setText("March  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "4":
//                    viewHolder.tvPremiumDate.setText("April  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "5":
//                    viewHolder.tvPremiumDate.setText("May  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "6":
//                    viewHolder.tvPremiumDate.setText("June  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "7":
//                    viewHolder.tvPremiumDate.setText("July  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "8":
//                    viewHolder.tvPremiumDate.setText("Aug  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "9":
//                    viewHolder.tvPremiumDate.setText("Sep  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "10":
//                    viewHolder.tvPremiumDate.setText("Oct  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "11":
//                    viewHolder.tvPremiumDate.setText("Nov  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//                case "12":
//                    viewHolder.tvPremiumDate.setText("Dec  ".concat(mDataSet.get(position).getPYear()));
//                    break;
//
//
//            }
            // Get element from your dataset at this position and replace the contents of the view
            // with that element

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

            private final TextView tvNo;
            private final TextView tvName;
            private final TextView tvPremiumDate;
            private final TextView lablePay;
            private final TextView tvPremiumAmount;
            private final CheckBox cbPay;
            private final TextView lablePenalty;
            private final TextView tvPenalty;
            private final TextView tvStatus;
            private final TextView tvStatusCOC;
            private final LinearLayout llCheque;
            private final RadioGroup rgCOC;
            private final RadioButton rbCash;
            private final RadioButton rbCheque;
            private final EditText edtBankName;
            private final EditText edtChequeNo;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                tvNo = (TextView) v.findViewById(R.id.tvNo);
                tvName = (TextView) v.findViewById(R.id.tvName);
                tvPremiumDate = (TextView) v.findViewById(R.id.tvPremiumDate);
                lablePay = (TextView) v.findViewById(R.id.lablePay);
                tvPremiumAmount = (TextView) v.findViewById(R.id.tvPremiumAmount);
                cbPay = (CheckBox) v.findViewById(R.id.cbPay);
                lablePenalty = (TextView) v.findViewById(R.id.lablePenalty);
                tvPenalty = (TextView) v.findViewById(R.id.tvPenalty);
                llCheque = (LinearLayout) v.findViewById(R.id.llCheque);
                rgCOC = (RadioGroup) v.findViewById(R.id.rgCOC);
                rbCash = (RadioButton) v.findViewById(R.id.rbCash);
                rbCheque = (RadioButton) v.findViewById(R.id.rbCheque);
                edtBankName = (EditText) v.findViewById(R.id.edtBankName);
                edtChequeNo = (EditText) v.findViewById(R.id.edtChequeNo);
                llCheque.setVisibility(View.VISIBLE);
                tvStatus = (TextView) v.findViewById(R.id.tvStatus);
                tvStatusCOC = (TextView) v.findViewById(R.id.tvStatusCOC);

                cbPay.setVisibility(View.GONE);

            }


        }
    }


}
