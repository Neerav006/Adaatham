package com.adaatham.suthar.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;

import com.adaatham.suthar.model.paytmmodel.InitiateTransactionRes;
import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.adaatham.suthar.model.MakePay;
import com.adaatham.suthar.model.MyRes;
import com.adaatham.suthar.model.Pay;
import com.adaatham.suthar.model.PaymentDetail;
import com.adaatham.suthar.model.Premiumlist;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class PaymentDetailActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY = "/Addatham22";
    private static final String FILE_NAME = "cheque";
    private final int GALLERY = 1, CAMERA = 2;
    MakeInitialPay makemypay;
    PaytmInitTransaction paytmInitTransaction;
    String paytmSignature;
    //paytm
    Integer ActivityRequestCode = 2;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private Button btnPay;
    private ProgressBar progressBar;
    private CustomAdapter customAdapter;
    private GetPaymentDetail getPaymentDetail;
    private String mem_no;
    private ArrayList<Premiumlist> premiumlistArrayList;
    private TextView tvTotal;
    private double total = 0;
    private ProgressDialog progressDialog;
    private String id;
    private HashMap<Integer, Integer> hashMap;
    private UploadImageToServer uploadImageToServer;
    private UploadImageToServer uploadImageToServer22;
    private LinearLayout llCheque;
    private RadioGroup rgCOC;
    private RadioButton rbCash;
    private RadioButton rbCheque;
    private RadioButton rbOnline;
    private EditText edtBankName;
    private EditText edtChequeNo;
    private TextView tvDate;
    private Button btnUpload;
    private TextView tvPath;
    private Calendar endCalender;
    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            endCalender = Calendar.getInstance();
            endCalender.set(Calendar.YEAR, year);
            endCalender.set(Calendar.MONTH, monthOfYear);
            endCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


            tvDate.setText(sdf.format(endCalender.getTime()));
//            dob = sdf.format(endCalender.getTime());


        }

    };
    private boolean isPending = true;
    private Uri fileUri;
    private String path;

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
            mediaFile = File.createTempFile(FILE_NAME, ".jpg", mediaStorageDir);
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
        hashMap = new HashMap<>();
        setContentView(R.layout.activity_payment_detail);
        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, PaymentDetailActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);
        makemypay = makeInitialPay(Constants.BASE_URL);
        uploadImageToServer = uploadImage(Constants.BASE_URL);
        uploadImageToServer22 = uploadImage22(Constants.BASE_URL);
        premiumlistArrayList = new ArrayList<>();
        getPaymentDetail = getPremiumPay(Constants.BASE_URL);
        paytmInitTransaction = payTmInitTran("https://adaathamwelfare.org/api/");


        if (getIntent() != null) {
            mem_no = getIntent().getStringExtra("m_id");
        }

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PaymentDetailActivity.this, DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(dividerItemDecoration);
        rvList.setLayoutManager(new LinearLayoutManager(PaymentDetailActivity.this));
        btnPay = (Button) findViewById(R.id.btnPay);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        llCheque = (LinearLayout) findViewById(R.id.llCheque);
        rgCOC = (RadioGroup) findViewById(R.id.rgCOC);
        rbCash = (RadioButton) findViewById(R.id.rbCash);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);
        rbOnline = findViewById(R.id.rbOnline);
        edtBankName = (EditText) findViewById(R.id.edtBankName);
        edtChequeNo = (EditText) findViewById(R.id.edtChequeNo);
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        tvPath = (TextView) findViewById(R.id.tvPath);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PaymentDetailActivity.this, endDate, Calendar.getInstance()
                        .get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        rbCash.setChecked(true);

        if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {
            edtBankName.setVisibility(View.VISIBLE);
            edtChequeNo.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.VISIBLE);
            tvPath.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
        } else {
            edtBankName.setVisibility(View.GONE);
            edtChequeNo.setVisibility(View.GONE);
            tvDate.setVisibility(View.GONE);
            tvPath.setVisibility(View.GONE);
            btnUpload.setVisibility(View.GONE);
        }

        rgCOC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {
                    edtBankName.setVisibility(View.VISIBLE);
                    edtChequeNo.setVisibility(View.VISIBLE);
                    tvDate.setVisibility(View.VISIBLE);
                    tvPath.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                } else {
                    edtBankName.setVisibility(View.GONE);
                    edtChequeNo.setVisibility(View.GONE);
                    tvDate.setVisibility(View.GONE);
                    tvPath.setVisibility(View.GONE);
                    btnUpload.setVisibility(View.GONE);
                }
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getPaymentDetail.getMemberPay(id, mem_no, "1").enqueue(new Callback<PaymentDetail>() {
            @Override
            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();

                    for (int k = 0; k < premiumlistArrayList.size(); k++) {

                        if (premiumlistArrayList.get(k).getStatus().equals("0")) {
                            isPending = true;
                            break;
                        } else {
                            isPending = false;
                        }

                        hashMap.put(k, R.id.rbCash);
                    }


                    if (isPending) {
                        llCheque.setVisibility(View.VISIBLE);
                        btnPay.setVisibility(View.VISIBLE);
                    } else {
                        llCheque.setVisibility(View.GONE);
                        btnPay.setVisibility(View.INVISIBLE);
                    }


                    if (premiumlistArrayList.size() == 0) {
                        btnPay.setVisibility(View.GONE);
                        llCheque.setVisibility(View.GONE);
                        Toast.makeText(PaymentDetailActivity.this, "No due premium", Toast.LENGTH_SHORT).show();
                    } else {
//                        btnPay.setVisibility(View.VISIBLE);
//                        llCheque.setVisibility(View.VISIBLE);

                        customAdapter = new CustomAdapter(premiumlistArrayList, hashMap);
                        rvList.setAdapter(customAdapter);


                    }

                }

            }

            @Override
            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PaymentDetailActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();

            }
        });


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {

                    if (edtBankName.getText().toString().trim().isEmpty()) {

                        edtBankName.setError("Enter Bank Name");

                    } else if (edtChequeNo.getText().toString().isEmpty()) {

                        edtChequeNo.setError("Enter Cheque NO");
                    } else if (tvDate.getText().toString().isEmpty()) {
                        Toast.makeText(PaymentDetailActivity.this, "Select cheque date", Toast.LENGTH_SHORT).show();
                    } else if (path == null) {
                        Toast.makeText(PaymentDetailActivity.this, "Select cheque photo", Toast.LENGTH_SHORT).show();

                    } else {
                        //call

                        showAlertDialog(edtBankName.getText().toString().trim(), edtChequeNo.getText().toString().trim(),
                                tvDate.getText().toString().trim());

                    }

                } else {
                    showAlertDialog("", "",
                            "");

                }


            }
        });


    }


    /***************************************************************
     * Camera code start
     *
     * ********************************************************************
     */
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(PaymentDetailActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        Log.e("file uri", fileUri.toString());


        if (fileUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Log.e("Paytm Pay--->", data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"));
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
            String paymentRes = data.getStringExtra("response");
            if (paymentRes != null && !paymentRes.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(paymentRes);
                    String mID = jsonObject.getString("MID");
                    String orderId = jsonObject.getString("ORDERID");
                    String txtStatus = jsonObject.getString("STATUS");
                    String txtMessage = jsonObject.getString("RESPMSG");
                    if (txtStatus.equalsIgnoreCase("TXN_SUCCESS")) {
                        // todo success transacton call verify transaction paytm api
                        Toast.makeText(PaymentDetailActivity.this, txtMessage, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(PaymentDetailActivity.this, txtMessage, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(PaymentDetailActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                if (data.getStringExtra("nativeSdkForMerchantMessage") != null && !data.getStringExtra("nativeSdkForMerchantMessage").isEmpty()) {
                    Toast.makeText(PaymentDetailActivity.this, data.getStringExtra("nativeSdkForMerchantMessage"), Toast.LENGTH_LONG).show();

                }
            }
        }
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(PaymentDetailActivity.this.getContentResolver(), contentURI);
                    path = saveImage(bitmap);

                    Log.e("path", path);
                    //call upload service
                    if (path != null) {
                        tvPath.setText(path);
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        } else if (requestCode == CAMERA) {
            if (fileUri != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                path = saveImage(bitmap);
                if (path != null) {
                    tvPath.setText(path);
                }

            }

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {

        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.

        deleteDir(wallpaperDirectory);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }


        try {
            File f = new File(wallpaperDirectory, FILE_NAME + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(PaymentDetailActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void showAlertDialog(final String bank_name, final String c_no, final String cdate) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(PaymentDetailActivity.this);

        dialogBuilder.setMessage("Are you sure to continue");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                boolean isChecked = false;

                com.adaatham.suthar.model.MakePay makePay = new com.adaatham.suthar.model.MakePay();
                makePay.setL_id(id);
                makePay.setP_id(mem_no);
                makePay.setT_amount(String.valueOf(total));


                ArrayList<Pay> payArrayList = new ArrayList<>();
                for (int i = 0; i < premiumlistArrayList.size(); i++) {

                    if (premiumlistArrayList.get(i).isChecked()) {
                        Pay pay = new Pay();
                        pay.setMId(premiumlistArrayList.get(i).getId());

//                        if(hashMap.get(i)==R.id.rbCheque){
//
//                            if(!premiumlistArrayList.get(i).getBank_name().isEmpty() && !premiumlistArrayList.get(i).getC_no().isEmpty()){
//
//                                pay.setBank_name(premiumlistArrayList.get(i).getBank_name());
//                                pay.setC_no(premiumlistArrayList.get(i).getC_no());
//                                pay.setP_type("1");
//                            }
//                            else {
//
////                              View view= rvList.getChildAt(i);
////
////
////                              EditText edtBankName= (EditText) view.findViewById(R.id.edtBankName);
////                              EditText edtCNo= (EditText) view.findViewById(R.id.edtChequeNo);
////
////                              edtBankName.setError("Enter bank name");
////                              edtCNo.setError("Enter cheque no");
//
//                                return;
//                            }
//
//                        }
//                        else {
//
//                            pay.setBank_name("");
//                            pay.setC_no("");
//                            pay.setP_type("0");
//
//                        }


                        payArrayList.add(pay);
                        isChecked = true;

                    }
                }


                makePay.setPay(payArrayList);

                Log.e("json:-", new Gson().toJson(makePay));


                if (isChecked) {
                    ///////////////////////test cheque api

                    if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {
                        //cheque call

                        if (path != null) {

                            File file = new File(path);

                            showProgressDialog();

                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                            RequestBody bankname = RequestBody.create(MediaType.parse("text/plain"), bank_name);
                            RequestBody cno = RequestBody.create(MediaType.parse("text/plain"), c_no);
                            RequestBody p_type = RequestBody.create(MediaType.parse("text/plain"), "1");
                            RequestBody c_date = RequestBody.create(MediaType.parse("text/plain"), cdate);
                            RequestBody temp = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(makePay));

                            uploadImageToServer.uploadFile(fileToUpload, filename, bankname, cno, p_type, c_date, temp).enqueue(new Callback<MyRes>() {
                                @Override
                                public void onResponse(Call<MyRes> call, Response<MyRes> response) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    if (response.isSuccessful()) {
                                        if (response.body().getMsg().equalsIgnoreCase("true")) {
                                            Toast.makeText(PaymentDetailActivity.this, "Successfully paid", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();

                                        }

                                    } else {
                                        Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onFailure(Call<MyRes> call, Throwable t) {
                                    Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(PaymentDetailActivity.this, "Select image before upload", Toast.LENGTH_SHORT).show();
                        }

                    } else if (rgCOC.getCheckedRadioButtonId() == R.id.rbOnline) {
                        // online payment
                        makePayTmOnlinePayment(makePay);

                    } else {
                        //cash call
                        RequestBody bankname = RequestBody.create(MediaType.parse("text/plain"), bank_name);
                        RequestBody cno = RequestBody.create(MediaType.parse("text/plain"), c_no);
                        RequestBody p_type = RequestBody.create(MediaType.parse("text/plain"), "0");
                        RequestBody c_date = RequestBody.create(MediaType.parse("text/plain"), cdate);
                        RequestBody temp = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(makePay));

                        showProgressDialog();

                        uploadImageToServer.uploadFile22(bankname, cno, p_type, c_date, temp).enqueue(new Callback<MyRes>() {
                            @Override
                            public void onResponse(Call<MyRes> call, Response<MyRes> response) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                if (response.isSuccessful()) {
                                    if (response.body().getMsg().equalsIgnoreCase("true")) {
                                        Toast.makeText(PaymentDetailActivity.this, "Successfully paid", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<MyRes> call, Throwable t) {
                                Toast.makeText(PaymentDetailActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        });

                    }


                    ////////////////////////////////////////////


//                    makemypay.makeInitPay(makePay).enqueue(new Callback<MyRes>() {
//                        @Override
//                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {
//
//                            if (progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//
//
//                            if (response.isSuccessful()) {
//                                if (response.body().getMsg().equalsIgnoreCase("true")) {
//                                    Toast.makeText(PaymentDetailActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
//                                    finish();
//                                } else {
//                                    Toast.makeText(PaymentDetailActivity.this, "Not Successfully added", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<MyRes> call, Throwable t) {
//                            if (progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            Toast.makeText(PaymentDetailActivity.this, "Not Successfully added", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });

                } else {
                }


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

    private void makePayTmOnlinePayment(MakePay makePaytmPay) {
        Log.e("m_id......", makePaytmPay.getP_id());
        showProgressDialog();
        paytmInitTransaction.initiateTransaction(makePaytmPay).enqueue(new Callback<InitiateTransactionRes>() {
            @Override
            public void onResponse(Call<InitiateTransactionRes> call, Response<InitiateTransactionRes> response) {
                progressDialog.dismiss();
                // get paytm initiate transaction api response here
                if (response.isSuccessful() && response.body() != null) {
                    InitiateTransactionRes initiateTransactionRes
                            = response.body();
                    if (initiateTransactionRes.getBody().getResultInfo().getResultCode().equals("0000")) {
                        String txtToken = initiateTransactionRes.getBody().getTxnToken();
                        paytmSignature = initiateTransactionRes.getHead().getSignature();
                        // process with paytm sdk call
                        showPayTmPaymentPage(initiateTransactionRes.getBody().getOrderId(), Constants.MERCHANT_ID, txtToken, initiateTransactionRes.getBody().getAmount());


                    } else {
                        paytmSignature = "";
                        Toast.makeText(PaymentDetailActivity.this, initiateTransactionRes.getBody().getResultInfo().getResultMsg(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    paytmSignature = "";
                    Toast.makeText(PaymentDetailActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<InitiateTransactionRes> call, Throwable t) {
                progressDialog.dismiss();
                paytmSignature = "";
                Toast.makeText(PaymentDetailActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    GetPaymentDetail getPremiumPay(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(GetPaymentDetail.class);
    }

    MakeInitialPay makeInitialPay(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(MakeInitialPay.class);
    }

    PaytmInitTransaction payTmInitTran(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(PaytmInitTransaction.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(PaymentDetailActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    UploadImageToServer uploadImage(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(UploadImageToServer.class);
    }

    UploadImageToServer uploadImage22(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(UploadImageToServer.class);
    }

    // TODO call initiate payment api for txt token and pass here
    void showPayTmPaymentPage(String orderId, String merchantId, String txtToken, String txtAmount) {
        // String callBackUrl = Constants.PAYTM_BASE_URL + "theia/paytmCallback?ORDER_ID=" + orderId;

        PaytmOrder paytmOrder = new PaytmOrder(orderId, merchantId, txtToken, txtAmount, Constants.PAYTM_CALLBACK_URL);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                Toast.makeText(PaymentDetailActivity.this, "Response (onTransactionResponse) : " + bundle.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Payment_res---->", bundle.toString());
                String mID = bundle.getString("MID");
                String orderId = bundle.getString("ORDERID");
                String txtStatus = bundle.getString("STATUS");
                String txtMessage = bundle.getString("RESPMSG");
                if (txtStatus.equalsIgnoreCase("TXN_SUCCESS")) {
                    // todo success transacton call verify transaction paytm api
                    Toast.makeText(PaymentDetailActivity.this, txtMessage, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PaymentDetailActivity.this, txtMessage, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(PaymentDetailActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onErrorProceed(String s) {
                Log.e("error_paytm", s);
                Toast.makeText(PaymentDetailActivity.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.e("error_paytm", s);
                Toast.makeText(PaymentDetailActivity.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.e("error_paytm", s);
                Toast.makeText(PaymentDetailActivity.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.e("error_paytm", s);
                Toast.makeText(PaymentDetailActivity.this, s, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Log.e("cancel_paytm", s);
                Toast.makeText(PaymentDetailActivity.this, s, Toast.LENGTH_LONG).show();

            }
        });

        transactionManager.setShowPaymentUrl(Constants.PAYTM_BASE_URL + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(PaymentDetailActivity.this, ActivityRequestCode);

    }


    interface GetPaymentDetail {
        @POST("ssy/premiumdetailsapi/")
        @FormUrlEncoded
        Call<PaymentDetail> getMemberPay(@Field("l_id") String l_id, @Field("member_no") String mid, @Field("type") String type);

    }

    interface MakeInitialPay {
        @POST("ssy/paypremiumapi/")
        Call<MyRes> makeInitPay(@Body MakePay makePay);
    }


    public interface UploadImageToServer {
        @Multipart
        @POST("ssy/paypremiumapi/")
        Call<MyRes> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("bank_name") RequestBody bank_name, @Part("c_no") RequestBody c_no,
                               @Part("p_type") RequestBody p_type, @Part("c_date") RequestBody c_date, @Part("temp") RequestBody temp);


        @Multipart
        @POST("ssy/paypremiumapi/")
        Call<MyRes> uploadFile22(@Part("bank_name") RequestBody bank_name, @Part("c_no") RequestBody c_no,
                                 @Part("p_type") RequestBody p_type, @Part("c_date") RequestBody c_date, @Part("temp") RequestBody temp);


    }


    interface PaytmInitTransaction {
        @POST("order")
        Call<InitiateTransactionRes> initiateTransaction(@Body MakePay makePay);
    }

    /**
     * Provide views to RecyclerView with data from mDataSet.
     */
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private ArrayList<Premiumlist> mDataSet;
        private HashMap<Integer, Integer> hashMap;


        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        public CustomAdapter(ArrayList<Premiumlist> dataSet, HashMap<Integer, Integer> hashMap) {
            mDataSet = dataSet;
            this.hashMap = hashMap;


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


            if (!mDataSet.get(position).getStatus().equals("0")) {

                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("Collected by  ".concat(mDataSet.get(position).getPE().concat(":- ").concat((mDataSet.get(position).getLId()))));
                viewHolder.cbPay.setVisibility(View.GONE);


                if (mDataSet.get(position).getPayment_type().equals("0")) {
                    viewHolder.tvStatusCOC.setVisibility(View.VISIBLE);
                    viewHolder.tvStatusCOC.setText("By Cash");
                } else if (mDataSet.get(position).getPayment_type().equals("2")) {
                    viewHolder.tvStatusCOC.setVisibility(View.VISIBLE);
                    viewHolder.tvStatusCOC.setText("By Online");
                } else {
                    viewHolder.tvStatusCOC.setVisibility(View.VISIBLE);
                    viewHolder.tvStatusCOC.setText("By Cheque");
                }


            } else {


                viewHolder.tvStatus.setText("");
                viewHolder.tvStatus.setVisibility(View.GONE);
                viewHolder.cbPay.setVisibility(View.VISIBLE);
            }


            viewHolder.cbPay.setOnCheckedChangeListener(null);
//            viewHolder.rgCOC.setOnCheckedChangeListener(null);
            viewHolder.cbPay.setChecked(mDataSet.get(position).isChecked());

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


            viewHolder.cbPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDataSet.get(position).setChecked(isChecked);


                    if (mDataSet.get(position).isChecked()) {
                        if (mDataSet.get(position).getPanalty().isEmpty()) {
                            total = total + Double.parseDouble(mDataSet.get(position).getAmount());

                        } else {
                            total = total + Double.parseDouble(mDataSet.get(position).getAmount()) + Double.parseDouble(mDataSet.get(position).getPanalty());
                        }
                    } else {


                        if (mDataSet.get(position).getPanalty().isEmpty()) {
                            total = total - Double.parseDouble(mDataSet.get(position).getAmount());

                        } else {
                            total = total - Double.parseDouble(mDataSet.get(position).getAmount()) - Double.parseDouble(mDataSet.get(position).getPanalty());

                        }


                    }


                    tvTotal.setText("Total Amount:-".concat(String.valueOf(total)));
                }
            });

            viewHolder.tvName.setText(mDataSet.get(position).getName());
            viewHolder.tvNo.setText(mDataSet.get(position).getMId());
            viewHolder.tvPremiumAmount.setText(mDataSet.get(position).getAmount());
            switch (mDataSet.get(position).getPMonth()) {

                case "1":
                    viewHolder.tvPremiumDate.setText("Jan  ".concat(mDataSet.get(position).getPYear()));
                    break;

                case "2":
                    viewHolder.tvPremiumDate.setText("Feb  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "3":
                    viewHolder.tvPremiumDate.setText("March  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "4":
                    viewHolder.tvPremiumDate.setText("April  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "5":
                    viewHolder.tvPremiumDate.setText("May  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "6":
                    viewHolder.tvPremiumDate.setText("June  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "7":
                    viewHolder.tvPremiumDate.setText("July  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "8":
                    viewHolder.tvPremiumDate.setText("Aug  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "9":
                    viewHolder.tvPremiumDate.setText("Sep  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "10":
                    viewHolder.tvPremiumDate.setText("Oct  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "11":
                    viewHolder.tvPremiumDate.setText("Nov  ".concat(mDataSet.get(position).getPYear()));
                    break;
                case "12":
                    viewHolder.tvPremiumDate.setText("Dec  ".concat(mDataSet.get(position).getPYear()));
                    break;


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


            }


        }
    }
}
