package com.adaatham.suthar.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

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

public class PremiumDetailActivity extends AppCompatActivity {
    private static final String IMAGE_DIRECTORY = "/Addatham22";
    private static final String FILE_NAME = "circular";
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private TextView tvTotal;
    private String id;
    private String user_name;
    private String name;
    private boolean isLogin;
    private boolean isReg;
    private boolean isSSy;
    private String role;
    private String m_no;
    private ProgressBar progressBar;
    private GetPaymentDetail getPaymentDetail;
    private ArrayList<Premiumlist> premiumlistArrayList;
    private CustomAdapter customAdapter;
    private Button btnPayOnline;
    Integer ActivityRequestCode = 2;
    long lastClickInMillSecond = 0L;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
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
        setContentView(R.layout.activity_premium_detail);

        getPaymentDetail = getPremiumPay(Constants.BASE_URL);

        if (getIntent() != null) {
            m_no = getIntent().getStringExtra("m_no");

        }

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, PremiumDetailActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        isSSy = sharedPreferences.getBoolean(Constants.IS_SSY, false);


        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnPayOnline = findViewById(R.id.btnPayOnline);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PremiumDetailActivity.this, DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(new LinearLayoutManager(PremiumDetailActivity.this));
        rvList.addItemDecoration(dividerItemDecoration);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getPaymentDetail.getMemberPay(id, m_no, "1").enqueue(new Callback<PaymentDetail>() {
            @Override
            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();
                    customAdapter = new CustomAdapter(premiumlistArrayList);
                    rvList.setAdapter(customAdapter);

                    if (premiumlistArrayList.size() > 0) {
                        double total = 0;
                        double subtotal = 0;

                        ArrayList<Double> subtotalArray = new ArrayList<>();

                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

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

                        tvTotal.setText("Total:- ".concat(String.valueOf(total)));
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

        // Pay online here
        // TODO pay online
        btnPayOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch paytm  flow
                if(SystemClock.elapsedRealtime() - lastClickInMillSecond < 1000){
                    return;
                }
                lastClickInMillSecond = SystemClock.elapsedRealtime();
                // testing purpose dummy amount and transaction token
                showPayTmPaymentPage(String.valueOf(SystemClock.elapsedRealtime()),Constants.MERCHANT_ID,"yiDXSP32065462525229","1.0");

            }
        });


    }

    GetPaymentDetail getPremiumPay(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(GetPaymentDetail.class);
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
                pDate.setSpacingAfter(30);
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

                                if (premiumlistArrayList.get(k).getStatus().equals("0")) {
                                    table.addCell(new Phrase(lineSpacing, "Due",
                                            FontFactory.getFont(FontFactory.TIMES_BOLD, 10)));
                                } else {
                                    table.addCell(new Phrase(lineSpacing, "Collected.\n".concat(premiumlistArrayList.get(k).getPE()),
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

                document.add(paragraph);
                document.add(pDate);
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
        @POST("ssy/premiumdetailsapi/")
        @FormUrlEncoded
        Call<PaymentDetail> getMemberPay(@Field("l_id") String l_id, @Field("member_no") String mid, @Field("type") String type);

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
                    .inflate(R.layout.row_family_member, viewGroup, false);

            return new ViewHolder(v);
        }

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            viewHolder.tvMemberName.setText(mDataSet.get(position).getName());
            viewHolder.tvMemberId.setText("Member Id:- ".concat(mDataSet.get(position).getMemberId()));
            viewHolder.tvAmount.setText(mDataSet.get(position).getAmount());

            if (mDataSet.get(position).getStatus().equals("0")) {

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

                viewHolder.tvStatus.setTextColor(Color.parseColor("#E65100"));
                viewHolder.tvStatus.setText("Your Premium is due");
            } else {
                viewHolder.tvStatus.setTextColor(Color.parseColor("#00695C"));
                viewHolder.tvStatus.setText("Collected by  ".concat(mDataSet.get(position).getPE().concat(":- ").concat((mDataSet.get(position).getLId()))));


                if (!mDataSet.get(position).getUpdate_time().equals("0000-00-00 00:00:00") && Utils.getMilliSeconds(mDataSet.get(position).getUpdate_time()) != 0) {
                    String updatedDate = Utils.getDate(Utils.getMilliSeconds(mDataSet.get(position).getUpdate_time()));
                    if (updatedDate != null) {
                        viewHolder.tvMonth.setText(updatedDate);
                    }
                }

            }


            if (mDataSet.get(position).getPanalty().isEmpty()) {
                viewHolder.tvPenalty.setText("");
                viewHolder.tvLablePenalty.setVisibility(View.GONE);
            } else {
                viewHolder.tvPenalty.setText(mDataSet.get(position).getPanalty());
                viewHolder.tvLablePenalty.setVisibility(View.VISIBLE);
            }


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
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });


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
                btnMore.setVisibility(View.GONE);
                tvAmountLable.setVisibility(View.VISIBLE);

            }


        }
    }

    // TODO pay now button for pay online
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
    }

    // TODO call initiate payment api for txt token and pass here
    void showPayTmPaymentPage(String orderId,String merchantId,String txtToken,String txtAmount){
        String callBackUrl = Constants.PAYTM_BASE_URL + "theia/paytmCallback?ORDER_ID="+orderId;

        PaytmOrder paytmOrder = new PaytmOrder(orderId, merchantId, txtToken, txtAmount, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){

            @Override
            public void onTransactionResponse(Bundle bundle) {
                Toast.makeText(PremiumDetailActivity.this, "Response (onTransactionResponse) : "+bundle.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });

        transactionManager.setShowPaymentUrl(Constants.PAYTM_BASE_URL + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, ActivityRequestCode);

    }
}
