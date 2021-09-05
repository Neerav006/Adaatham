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
import android.provider.MediaStore;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.MyRes;
import com.adaatham.suthar.model.Pay;
import com.adaatham.suthar.model.PaymentDetail;
import com.adaatham.suthar.model.Premiumlist;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class UpdateChalanActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ///photo upload
    private static final String IMAGE_DIRECTORY = "/Addatham22";
    private static final String FILE_NAME = "chalan";
    private final int GALLERY = 1, CAMERA = 2;
    private final long DOUBLE_TAP = 1500;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private EditText edtAmount;
    private EditText edtChalan;
    private Button btnUpload;
    private TextView tvPath;
    private RecyclerView rvList;
    private Button btnSubmit;
    private String mem_no = "";
    private ArrayList<Premiumlist> premiumlistArrayList;
    private GetPaymentDetail getPaymentDetail;
    private ProgressBar progressBar;
    private double total = 0;
    private CustomAdapter customAdapter;
    private String id;
    private Uri fileUri;
    private String path;
    private ProgressDialog progressDialog;
    private UploadImageToServer uploadImageToServer;
    private TextView tvTotal;
    private TextView tvTotal11;
    private double totalAmount = 0.0;
    private CheckBox cbAllpay;
    private TextView tvChallanFillDate;
    private boolean isAllSelected = false;
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


            tvChallanFillDate.setText(sdf.format(endCalender.getTime()));
//            dob = sdf.format(endCalender.getTime());


        }

    };
    private File filePath;

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri() {
        filePath = getOutputMediaFile();
        return Uri.fromFile(filePath);
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_chalan);

        uploadImageToServer = uploadImage(Constants.BASE_URL);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, UpdateChalanActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);

        getPaymentDetail = getPremiumPay(Constants.BASE_URL);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        edtChalan = (EditText) findViewById(R.id.edtChalan);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        tvPath = (TextView) findViewById(R.id.tvPath);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTotal11 = (TextView) findViewById(R.id.tvTotal11);
        tvChallanFillDate = (TextView) findViewById(R.id.tvChalanFillDate);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cbAllpay = (CheckBox) findViewById(R.id.cbAllPay);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(UpdateChalanActivity.this, DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(new LinearLayoutManager(UpdateChalanActivity.this));
        rvList.addItemDecoration(dividerItemDecoration);


        tvChallanFillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateChalanActivity.this, endDate, Calendar.getInstance()
                        .get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });


        cbAllpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                total = 0.0;

                if (customAdapter != null && premiumlistArrayList != null && premiumlistArrayList.size() > 0) {
                    if (isChecked) {

                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

                            premiumlistArrayList.get(i).setChecked(true);


                            if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {
                                total = total + Double.parseDouble(premiumlistArrayList.get(i).getAmount())
                                        + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());
                            } else {
                                total = total + Double.parseDouble(premiumlistArrayList.get(i).getAmount());
                            }


                        }


                        tvTotal.setText(String.valueOf(total));
                        isAllSelected = true;
                        customAdapter = new CustomAdapter(premiumlistArrayList);
                        rvList.setAdapter(customAdapter);


                    } else {
                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

                            premiumlistArrayList.get(i).setChecked(false);


//                            if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {
//                                total = total - Double.parseDouble(premiumlistArrayList.get(i).getAmount())
//                                        - Double.parseDouble(premiumlistArrayList.get(i).getPanalty());
//                            } else {
//                                total = total - Double.parseDouble(premiumlistArrayList.get(i).getAmount());
//                            }


                        }

                        tvTotal.setText(String.valueOf(total));
                        isAllSelected = false;
                        customAdapter = new CustomAdapter(premiumlistArrayList);
                        rvList.setAdapter(customAdapter);

                    }
                }

                totalAmount = total;


            }
        });


        getPaymentDetail.getMemberPay(id, mem_no, "2").enqueue(new Callback<PaymentDetail>() {
            @Override
            public void onResponse(Call<PaymentDetail> call, Response<PaymentDetail> response) {

                double total_amount = 0.0;
                double total_penalty = 0.0;

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    premiumlistArrayList = (ArrayList<Premiumlist>) response.body().getPremiumlist();
                    customAdapter = new CustomAdapter(premiumlistArrayList);
                    rvList.setAdapter(customAdapter);


                    if (premiumlistArrayList.size() > 0) {


                        for (int i = 0; i < premiumlistArrayList.size(); i++) {

                            total_amount = total_amount + Double.parseDouble(premiumlistArrayList.get(i).getAmount());

                            if (!premiumlistArrayList.get(i).getPanalty().isEmpty()) {

                                total_penalty = total_penalty + Double.parseDouble(premiumlistArrayList.get(i).getPanalty());


                            }

                        }

                        double total = total_amount + total_penalty;
                        tvTotal11.setText("Total Cash Collection:- ".concat(String.valueOf(total)));

                    }


                    if (premiumlistArrayList.size() == 0) {
                        Toast.makeText(UpdateChalanActivity.this, "No premium to update chalan", Toast.LENGTH_SHORT).show();
                        edtAmount.setVisibility(View.GONE);
                        edtChalan.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);
                        btnUpload.setVisibility(View.GONE);
                        tvTotal11.setVisibility(View.GONE);
                        tvChallanFillDate.setVisibility(View.GONE);
                        cbAllpay.setVisibility(View.GONE);

                    }


                }

            }

            @Override
            public void onFailure(Call<PaymentDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                t.printStackTrace();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean ischecked = false;
                if (!edtAmount.getText().toString().isEmpty() && !edtChalan.getText().toString().isEmpty()) {

                    if (Double.parseDouble(edtAmount.getText().toString().trim()) != totalAmount) {
                        Toast.makeText(UpdateChalanActivity.this, "Amount not match", Toast.LENGTH_SHORT).show();

                    }


                    com.adaatham.suthar.model.MakePay makePay = new com.adaatham.suthar.model.MakePay();
                    ArrayList<Pay> payArrayList = new ArrayList<>();
                    for (int i = 0; i < premiumlistArrayList.size(); i++) {

                        if (premiumlistArrayList.get(i).isChecked()) {
                            Pay pay = new Pay();
                            pay.setMId(premiumlistArrayList.get(i).getId());
                            payArrayList.add(pay);
                            ischecked = true;

                        }
                    }

                    makePay.setPay(payArrayList);
                    Log.e("json:-", new Gson().toJson(makePay));
                    if (ischecked && path != null) {
                        if (Double.parseDouble(edtAmount.getText().toString().trim()) == totalAmount) {
                            showAlertDialog(edtAmount.getText().toString().trim(), edtChalan.getText().toString(), new Gson().toJson(makePay));
                        } else {
                            Toast.makeText(UpdateChalanActivity.this, "Amount not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (path == null) {
                            Toast.makeText(UpdateChalanActivity.this, "Select Photo", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(UpdateChalanActivity.this, "Amount and chalan no both required..", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    GetPaymentDetail getPremiumPay(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(GetPaymentDetail.class);
    }

    public void showAlertDialog(final String amount, final String j_no, final String json) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(UpdateChalanActivity.this);


        dialogBuilder.setMessage("Are you sure to continue");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();


                if (path != null) {

                    File file = new File(path);

                    showProgressDialog();


                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                    RequestBody amountbody = RequestBody.create(MediaType.parse("text/plain"), amount);
                    RequestBody chalanBody = RequestBody.create(MediaType.parse("text/plain"), j_no);
                    RequestBody jsonbody = RequestBody.create(MediaType.parse("text/plain"), json);
                    RequestBody idbody = RequestBody.create(MediaType.parse("text/plain"), id);

                    uploadImageToServer.uploadFile(fileToUpload, filename, idbody, chalanBody, amountbody, jsonbody).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            if (response.isSuccessful()) {
                                if (response.body().getMsg().equalsIgnoreCase("true")) {
                                    Toast.makeText(UpdateChalanActivity.this, "Successfully paid", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(UpdateChalanActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(UpdateChalanActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            Toast.makeText(UpdateChalanActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });


                } else {
                    Toast.makeText(UpdateChalanActivity.this, "Select image before upload", Toast.LENGTH_SHORT).show();
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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(UpdateChalanActivity.this);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", filePath));
            startActivityForResult(intent, CAMERA);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UpdateChalanActivity.this.getContentResolver(), contentURI);
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
            if (fileUri != null)

            {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());

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
            MediaScannerConnection.scanFile(UpdateChalanActivity.this,
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_member, menu);

        final MenuItem item = menu.findItem(R.id.action_search);


        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    private List<Premiumlist> filter(List<Premiumlist> models, String query) {
        query = query.toLowerCase().trim();
        final List<Premiumlist> filteredModelList = new ArrayList<>();
        for (Premiumlist model : models) {
            final String name = model.getName().toLowerCase();
            final String memberId = model.getMemberId();
            final String mobile = model.getMobile().toLowerCase();
            if (name.contains(query) || memberId.contains(query) || mobile.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(UpdateChalanActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    UploadImageToServer uploadImage(String baseurl) {
        return RetrofitClient.getClient(baseurl).create(UploadImageToServer.class);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (customAdapter != null) {

            final List<Premiumlist> filteredModelList = filter(premiumlistArrayList, newText);

            customAdapter.setFilter(filteredModelList);

        }

        return true;
    }

    interface GetPaymentDetail {
        @POST("ssy/paidpremiumapi/")
        @FormUrlEncoded
        Call<PaymentDetail> getMemberPay(@Field("l_id") String l_id, @Field("member_no") String mid, @Field("type") String type);

    }

    public interface UploadImageToServer {
        @Multipart
        @POST("ssy/uploaddetailsapi/")
        Call<MyRes> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("l_id") RequestBody id, @Part("j_no") RequestBody j_no,
                               @Part("amount") RequestBody amount, @Part("pay") RequestBody pay);
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


            viewHolder.cbPay.setOnCheckedChangeListener(null);
            viewHolder.cbPay.setChecked(mDataSet.get(position).isChecked());


            if (mDataSet.get(position).getPanalty().isEmpty()) {
                viewHolder.tvPenalty.setVisibility(View.GONE);
                viewHolder.lablePenalty.setVisibility(View.GONE);
            } else {
                viewHolder.tvPenalty.setVisibility(View.VISIBLE);
                viewHolder.lablePenalty.setVisibility(View.VISIBLE);

                viewHolder.tvPenalty.setText(mDataSet.get(position).getPanalty());
            }

            viewHolder.cbPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mDataSet.get(position).setChecked(isChecked);


                    if (mDataSet.get(position).isChecked()) {

                        if (!mDataSet.get(position).getPanalty().isEmpty()) {
                            total = total + Double.parseDouble(mDataSet.get(position).getAmount())
                                    + Double.parseDouble(mDataSet.get(position).getPanalty());
                        } else {
                            total = total + Double.parseDouble(mDataSet.get(position).getAmount());
                        }


                    } else {

                        if (!mDataSet.get(position).getPanalty().isEmpty()) {
                            total = total - Double.parseDouble(mDataSet.get(position).getAmount())
                                    - Double.parseDouble(mDataSet.get(position).getPanalty());
                        } else {
                            total = total - Double.parseDouble(mDataSet.get(position).getAmount());
                        }


                    }

                    totalAmount = total;

                    tvTotal.setText("Amount:-".concat(String.valueOf(total)));
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

        void setFilter(List<Premiumlist> empInfo) {
            mDataSet = new ArrayList<>();
            mDataSet.addAll(empInfo);
            notifyDataSetChanged();
        }

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


            }


        }
    }

}
