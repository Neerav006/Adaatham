package com.adaatham.suthar.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.BusCategoryList;
import com.adaatham.suthar.model.BusinessCategory;
import com.adaatham.suthar.model.MemberRegistration;
import com.adaatham.suthar.model.MyRes;
import com.adaatham.suthar.model.Part1;
import com.adaatham.suthar.model.SearchMember;
import com.adaatham.suthar.model.SubCat;
import com.adaatham.suthar.model.VillageListData;
import com.adaatham.suthar.model.Villagelist;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class AddMatrimonialActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY = "/Adatham";
    private static final String FILE_NAME = "profile";
    private final int GALLERY = 1, CAMERA = 2;
    ArrayList<Villagelist> villagelists;
    private Uri fileUri;
    private Uri output;
    private String path;
    private String cameraPath;
    private ProgressDialog progressDialog;
    private RadioGroup rgIsSSyMember;
    private RadioButton rbYes;
    private RadioButton rbNo;
    private CardView cvMemberCodeContainer;
    private TextInputEditText edtMemberNo;
    private TextInputEditText edtPart1FamilyCode;
    private TextInputEditText edtPart1Name;
    private TextInputEditText edtPart1Addr;
    private TextInputEditText edtPart1Phone;
    private TextInputEditText edtpart1Mobile;
    private TextInputEditText edtpart1Email;
    private TextInputEditText edtPart1City;
    private TextInputEditText edtPart1PinCode;
    private TextInputEditText edtPart1Taluko;
    private TextInputEditText edtPart1District;
    private TextInputEditText edtPart1State;
    private TextInputEditText edtPart1Native;
    private TextInputEditText edtYearlyIncome;
    private Spinner spNativePlace;
    private TextView tvpart1DOB;
    private Button btnPart1SelectBirth;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFeMale;
    private RadioGroup rgMarriedStatus;
    private RadioButton rbUnMarried;
    private RadioButton rbMarried;
    private TextView tvLableSelectStudy;
    private Spinner spCategoryStudy;
    private TextInputLayout txtStudy;
    private TextInputEditText edtStudy;
    private TextView tvLableSelectBusiness;
    private Spinner spCategory;
    private TextView tvLableSelectBusinessSubCat;
    private Spinner spSubCategory;
    private EditText edtOther;
    private EditText edtBusinessAddr;
    private TextInputEditText edtAmount;
    private RadioGroup rgCOC;
    private RadioButton rbCash;
    private RadioButton rbCheque;
    private TextInputLayout txtBankName;
    private TextInputEditText edtBankName;
    private TextInputLayout txtChequeNo;
    private TextInputEditText edtChequeNo;
    private Button btnNext11;
    private GetVillageListApi getVillageListApi;
    private ProgressBar progressBar;
    private String nativePlace = null;
    private MemberRegistration memberRegistration;
    private MyCustomAdapter44 myCustomAdapter44;
    private MyCustomAdapter55 myCustomAdapter55;
    private ArrayList<BusCategoryList> copybusCategoryLists;
    private MyCustomAdapter33 myCustomAdapter33;
    private GetBusinessCategory getBusinessCategory;
    private ArrayList<BusCategoryList> busCategoryLists;
    private String l_id;
    private String main_cat_id = "";
    private String sub_cat_id = "";
    private String stydy_id = "";
    private String study = "";
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


            tvpart1DOB.setText(sdf.format(endCalender.getTime()));
//            dob = sdf.format(endCalender.getTime());


        }

    };
    private String resSubCat = "";
    private String resStudyCat = "";
    private LinearLayout llPhotoContainer;
    private TextView tvFilePath;
    private Button btnSelectPhoto;
    private AddMetrimonyDetails addMetrimonyDetails;
    private GetMemberBySearch getMemberBySearch;
    private String gender = "1";  // 1 male 2 female
    private String marriedStatus = "1"; // 1 unmarried 2 married

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matrimonial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getMemberBySearch = RetrofitClient.getClient(Constants.BASE_URL).create(GetMemberBySearch.class);
        addMetrimonyDetails = RetrofitClient.getClient(Constants.BASE_URL).create(AddMetrimonyDetails.class);

        copybusCategoryLists = new ArrayList<>();
        busCategoryLists = new ArrayList<>();

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, AddMatrimonialActivity.this);
        l_id = sharedPreferences.getString(Constants.ID, null);

        getVillageListApi = RetrofitClient.getClient(Constants.BASE_URL).create(GetVillageListApi.class);
        getBusinessCategory = getCategoryByBusiness(Constants.BASE_URL);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Add Matrimonial");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        rgIsSSyMember = findViewById(R.id.rgIsSSyMember);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        cvMemberCodeContainer = findViewById(R.id.cvMemberCodeContainer);
        edtMemberNo = findViewById(R.id.edtMemberNo);
        edtPart1FamilyCode = findViewById(R.id.edtPart1FamilyCode);
        edtPart1Name = findViewById(R.id.edtPart1Name);
        edtPart1Addr = findViewById(R.id.edtPart1Addr);
        edtPart1Phone = findViewById(R.id.edtPart1Phone);
        edtpart1Mobile = findViewById(R.id.edtpart1Mobile);
        edtpart1Email = findViewById(R.id.edtpart1Email);
        edtPart1City = findViewById(R.id.edtPart1City);
        edtPart1PinCode = findViewById(R.id.edtPart1PinCode);
        edtPart1Taluko = findViewById(R.id.edtPart1Taluko);
        edtPart1District = findViewById(R.id.edtPart1District);
        edtPart1State = findViewById(R.id.edtPart1State);
        edtPart1Native = findViewById(R.id.edtPart1Native);
        edtYearlyIncome = findViewById(R.id.edtYearlyIncome);
        spNativePlace = findViewById(R.id.spNativePlace);
        tvpart1DOB = findViewById(R.id.tvpart1DOB);
        btnPart1SelectBirth = findViewById(R.id.btnPart1SelectBirth);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFeMale = findViewById(R.id.rbFeMale);
        rgMarriedStatus = findViewById(R.id.rgMarriedStatus);
        rbUnMarried = findViewById(R.id.rbUnMarried);
        rbMarried = findViewById(R.id.rbMarried);
        tvLableSelectStudy = findViewById(R.id.tvLableSelectStudy);
        spCategoryStudy = findViewById(R.id.spCategoryStudy);
        txtStudy = findViewById(R.id.txtStudy);
        edtStudy = findViewById(R.id.edtStudy);
        tvLableSelectBusiness = findViewById(R.id.tvLableSelectBusiness);
        spCategory = findViewById(R.id.spCategory);
        tvLableSelectBusinessSubCat = findViewById(R.id.tvLableSelectBusinessSubCat);
        spSubCategory = findViewById(R.id.spSubCategory);
        edtOther = findViewById(R.id.edtOther);
        edtBusinessAddr = findViewById(R.id.edtBusinessAddr);
        edtAmount = findViewById(R.id.edtAmount);
        rgCOC = findViewById(R.id.rgCOC);
        rbCash = findViewById(R.id.rbCash);
        rbCheque = findViewById(R.id.rbCheque);
        txtBankName = findViewById(R.id.txtBankName);
        edtBankName = findViewById(R.id.edtBankName);
        txtChequeNo = findViewById(R.id.txtChequeNo);
        edtChequeNo = findViewById(R.id.edtChequeNo);
        btnNext11 = findViewById(R.id.btnNext11);

        llPhotoContainer = findViewById(R.id.llPhotoContainer);
        tvFilePath = findViewById(R.id.tvFilePath);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);


        btnPart1SelectBirth.setOnClickListener(v -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddMatrimonialActivity.this, endDate, Calendar.getInstance()
                    .get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();

        });

        edtMemberNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !edtMemberNo.getText().toString().trim().isEmpty() && rgIsSSyMember.getCheckedRadioButtonId() == R.id.rbYes) {

                    progressBar.setVisibility(View.VISIBLE);

                    getMemberBySearch.getMember(edtMemberNo.getText().toString().trim())
                            .enqueue(new Callback<SearchMember>() {
                                @Override
                                public void onResponse(Call<SearchMember> call, Response<SearchMember> response) {

                                    progressBar.setVisibility(View.GONE);

                                    if (response.isSuccessful()) {


                                        resSubCat = response.body().getBusSubCat();
                                        resStudyCat = response.body().getStudyCat();

                                        edtPart1FamilyCode.setText(response.body().getGroupNo());
                                        edtPart1Name.setText(response.body().getName());
                                        edtPart1Addr.setText(response.body().getAddress());
                                        edtPart1Phone.setText(response.body().getPhone());
                                        edtpart1Mobile.setText(response.body().getMobile());
                                        edtpart1Email.setText(response.body().getEmail());
                                        edtPart1City.setText(response.body().getCity());
                                        edtPart1PinCode.setText(response.body().getPin());
                                        edtPart1Taluko.setText(response.body().getTaluka());
                                        edtPart1District.setText(response.body().getDist());
                                        edtPart1State.setText(response.body().getState());
                                        edtYearlyIncome.setText(response.body().getY_income());
                                        tvpart1DOB.setText(response.body().getDob());


                                        if (response.body().getGender().equals("1")) {

                                            rbMale.setChecked(true);
                                        } else {

                                            rbFeMale.setChecked(true);
                                        }


                                        if (response.body().getMarriedStatus().equals("1")) {

                                            rbUnMarried.setChecked(true);

                                        } else {
                                            rbMarried.setChecked(true);
                                        }

                                        if (villagelists != null) {
                                            for (int i = 0; i < villagelists.size(); i++) {

                                                if (response.body().getNativePlace().equals(villagelists.get(i).getName())) {

                                                    spNativePlace.setSelection(villagelists.indexOf(villagelists.get(i)));
                                                    break;

                                                }

                                            }
                                        }


//                                        for (int i = 0; i < copybusCategoryLists.size(); i++) {
//                                            if(response.body().getBusCat().equals(copybusCategoryLists.get(i).getMainCat().getId())){
//
//                                                spCategory.setSelection(copybusCategoryLists.indexOf(copybusCategoryLists.get(i)));
//
//                                                for(int k = 0 ; k < copybusCategoryLists.get(i).getSubCat().size();k++){
//
//                                                    final SubCat subCat = copybusCategoryLists.get(i).getSubCat().get(k) ;
//
//                                                    if(subCat.getId().equals(response.body().getBusSubCat())){
//                                                        //spSubCategory.setSelection(copybusCategoryLists.get(i).getSubCat().indexOf(subCat));
//                                                    }
//                                                }
//
//                                            }
//
//
//
//
//                                        }


                                        if (copybusCategoryLists.size() > 0) {
                                            for (BusCategoryList busCategoryList : copybusCategoryLists) {
                                                if (busCategoryList.getMainCat().getId().equals(response.body().getBusCat())) {
                                                    spCategory.setSelection(copybusCategoryLists.indexOf(busCategoryList));

                                                    for (SubCat subCat : busCategoryList.getSubCat()) {
                                                        if (subCat.getId().equals(response.body().getBusSubCat())) {

                                                            int count = spSubCategory.getAdapter().getCount();

                                                            if (count > 0 && busCategoryList.getSubCat().indexOf(subCat) < count) {

                                                                myCustomAdapter44 = new MyCustomAdapter44(AddMatrimonialActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) busCategoryList.getSubCat());
                                                                spSubCategory.setAdapter(myCustomAdapter44);

                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        spSubCategory.setSelection(busCategoryList.getSubCat().indexOf(subCat));


                                                                    }
                                                                }, 100);


                                                                break;
                                                            }


                                                        }
                                                    }


                                                    break;
                                                }
                                            }


                                            for (int k = 0; k < busCategoryLists.size(); k++) {


                                                for (SubCat subCat : busCategoryLists.get(k).getSubCat()) {
                                                    if (subCat.getId().equals(resStudyCat)) {
                                                        spCategoryStudy.setSelection(busCategoryLists.get(k).getSubCat().indexOf(subCat));
                                                        // edtStudy.setText(study);
                                                        break;
                                                    }
                                                }


                                            }


                                        }


                                    }
                                }

                                @Override
                                public void onFailure(Call<SearchMember> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                }

            }
        });


        btnSelectPhoto.setOnClickListener(v -> showPictureDialog()


        );


        edtPart1FamilyCode.setEnabled(false);
        edtPart1FamilyCode.setClickable(false);

        btnNext11.setOnClickListener(v -> {


            if (rgIsSSyMember.getCheckedRadioButtonId() == R.id.rbYes) {

                if (isPart1Valid() && memberRegistration != null) {
                    //TODO  add matrimonial data to server

                    showProgressDialog();

                    addMetrimonyDetails.addMetrimony(memberRegistration).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {

                                if (response.body().getMsg().equalsIgnoreCase("true")) {
                                    Toast.makeText(AddMatrimonialActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                                    finish();
                                } else if (response.body().getMsg().equalsIgnoreCase("false")) {
                                    Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    finish();
                                }


                            } else {
                                Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                finish();
                            }


                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });


                }
            } else {

                // TODO photo upload for non member

                if (path != null && isPart1Valid() && memberRegistration != null) {

                    showProgressDialog();

                    File file = new File(path);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody requestJson = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(memberRegistration));
                    RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), file.getName());


                    addMetrimonyDetails.addMetrimonyForNonMember(fileToUpload, fileName, requestJson)
                            .enqueue(new Callback<MyRes>() {
                                @Override
                                public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }


                                    if (response.isSuccessful()) {

                                        if (response.body().getMsg().equalsIgnoreCase("true")) {
                                            Toast.makeText(AddMatrimonialActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                                            finish();

                                        } else if (response.body().getMsg().equalsIgnoreCase("false")) {
                                            Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                            finish();

                                        } else if (response.body().getMsg().equalsIgnoreCase("xxx")) {
                                            Toast.makeText(AddMatrimonialActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                                            finish();
                                        }


                                    } else {
                                        Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                        finish();
                                    }


                                }

                                @Override
                                public void onFailure(Call<MyRes> call, Throwable t) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    Toast.makeText(AddMatrimonialActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });


                }


                // RequestBody regBody = RequestBody.create(MediaType.parse("text/plain"), mid);


            }


        });


        progressBar.setVisibility(View.VISIBLE);
        getVillageListApi.getVillageList().enqueue(new Callback<VillageListData>() {
            @Override
            public void onResponse(Call<VillageListData> call, Response<VillageListData> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    villagelists = (ArrayList<Villagelist>) response.body().getVillagelist();

                    if (villagelists.size() > 0) {

                        MyCustomAdapter66 myCustomAdapter66 = new MyCustomAdapter66(AddMatrimonialActivity.this,
                                R.layout.row_custom_spinner, villagelists);
                        spNativePlace.setAdapter(myCustomAdapter66);


                    }


                }
            }

            @Override
            public void onFailure(Call<VillageListData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


        getBusinessCategory.getBusinessCatList().enqueue(new Callback<BusinessCategory>() {
            @Override
            public void onResponse(Call<BusinessCategory> call, Response<BusinessCategory> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    busCategoryLists = (ArrayList<BusCategoryList>) response.body().getCategoryList();

                    for (int i = 0; i < busCategoryLists.size(); i++) {
                        if (busCategoryLists.get(i).getMainCat().getId().equals("1")) {

                            myCustomAdapter55 = new MyCustomAdapter55(AddMatrimonialActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) busCategoryLists.get(i).getSubCat());
                            spCategoryStudy.setAdapter(myCustomAdapter55);

                            spCategoryStudy.setSelection(((ArrayList<SubCat>) busCategoryLists.get(i).getSubCat()).size() - 1);


                            for (SubCat subCat : busCategoryLists.get(i).getSubCat()) {
                                if (subCat.getId().equals(stydy_id)) {
                                    spCategoryStudy.setSelection(busCategoryLists.get(i).getSubCat().indexOf(subCat));
                                    // edtStudy.setText(study);
                                    break;
                                }
                            }


                        } else {
                            copybusCategoryLists.add(busCategoryLists.get(i));
                        }


                    }

                    if (copybusCategoryLists.size() > 0) {


                        myCustomAdapter33 = new MyCustomAdapter33(AddMatrimonialActivity.this, R.layout.row_custom_spinner, copybusCategoryLists);
                        spCategory.setAdapter(myCustomAdapter33);


                        for (BusCategoryList busCategoryList : copybusCategoryLists) {
                            if (busCategoryList.getMainCat().getId().equals(main_cat_id)) {
                                spCategory.setSelection(copybusCategoryLists.indexOf(busCategoryList));

                                for (SubCat subCat : busCategoryList.getSubCat()) {
                                    if (subCat.getId().equals(sub_cat_id)) {
                                        myCustomAdapter44 = new MyCustomAdapter44(AddMatrimonialActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) busCategoryList.getSubCat());
                                        spSubCategory.setAdapter(myCustomAdapter44);

                                        spSubCategory.setSelection(busCategoryList.getSubCat().indexOf(subCat));
                                        break;

                                    }
                                }


                                break;
                            }
                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<BusinessCategory> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


        rgIsSSyMember.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group.getCheckedRadioButtonId() == R.id.rbYes) {
                    cvMemberCodeContainer.setVisibility(View.VISIBLE);
                    edtMemberNo.requestFocus();

                    llPhotoContainer.setVisibility(View.GONE);


                } else if (group.getCheckedRadioButtonId() == R.id.rbNo) {
                    cvMemberCodeContainer.setVisibility(View.GONE);

                    edtPart1FamilyCode.setText("");
                    edtPart1Name.setText("");
                    edtPart1Addr.setText("");
                    edtPart1Phone.setText("");
                    edtpart1Mobile.setText("");
                    edtpart1Email.setText("");
                    edtPart1City.setText("");
                    edtPart1PinCode.setText("");
                    edtPart1Taluko.setText("");
                    edtPart1District.setText("");
                    edtPart1State.setText("");
                    edtYearlyIncome.setText("");
                    tvpart1DOB.setText("");
                    edtMemberNo.getText().clear();

                    llPhotoContainer.setVisibility(View.VISIBLE);

                }


            }
        });


        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group.getCheckedRadioButtonId() == R.id.rbMale) {

                    gender = "1";
                } else if (group.getCheckedRadioButtonId() == R.id.rbFeMale) {

                    gender = "2";
                }

            }
        });


        rgMarriedStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group.getCheckedRadioButtonId() == R.id.rbMarried) {

                    marriedStatus = "2";
                } else if (group.getCheckedRadioButtonId() == R.id.rbUnMarried) {

                    marriedStatus = "1";
                }

            }
        });


        spNativePlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem() != null) {
                    nativePlace = ((Villagelist) parent.getItemAtPosition(position)).getName();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                main_cat_id = ((BusCategoryList) parent.getItemAtPosition(position)).getMainCat().getId();

                myCustomAdapter44 = new MyCustomAdapter44(AddMatrimonialActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) ((BusCategoryList) parent.getItemAtPosition(position)).getSubCat());
                spSubCategory.setAdapter(myCustomAdapter44);

                if (((BusCategoryList) parent.getItemAtPosition(position)).getMainCat().getId().equals("10")) {
                    // edtBusinessAddr.setVisibility(View.GONE);
                } else {
                    // edtBusinessAddr.setVisibility(View.VISIBLE);

                }

                for (SubCat subCat : ((BusCategoryList) parent.getItemAtPosition(position)).getSubCat()) {
                    if (subCat.getId().equals(resSubCat)) {
                        spSubCategory.setSelection(((BusCategoryList) parent.getItemAtPosition(position)).getSubCat().indexOf(subCat));
                        break;
                    }
                }


                if (((BusCategoryList) parent.getItemAtPosition(position)).getMainCat().getStatus().equals("2")) {
                    //edtOtherBusiness.setVisibility(View.VISIBLE);
                    spSubCategory.setVisibility(View.GONE); // hide sub cat
                    sub_cat_id = " ";

                } else {
                    spSubCategory.setVisibility(View.VISIBLE);
                    //edtOtherBusiness.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spCategoryStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((SubCat) parent.getItemAtPosition(position)).getStatus().equals("2")) {
                    edtStudy.setVisibility(View.VISIBLE);
                    stydy_id = ((SubCat) parent.getItemAtPosition(position)).getId();

                } else {
                    stydy_id = ((SubCat) parent.getItemAtPosition(position)).getId();
                    edtStudy.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (((SubCat) parent.getItemAtPosition(position)).getStatus().equals("2")) {
                    // edtOtherBusiness.setVisibility(View.VISIBLE);
                    sub_cat_id = ((SubCat) parent.getItemAtPosition(position)).getId();
                } else {
                    sub_cat_id = ((SubCat) parent.getItemAtPosition(position)).getId();
                    //edtOtherBusiness.setVisibility(View.GONE);
                }


                //


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * **************************
     * Form validation functions
     */

    private boolean isPart1Valid() {

        final String name = edtPart1Name.getText().toString().trim();
        final String addr = edtPart1Addr.getText().toString().trim();
        final String mobile = edtpart1Mobile.getText().toString().trim();
        final String phone = edtPart1Phone.getText().toString().trim();
        final String city = edtPart1City.getText().toString().trim();
        final String taluka = edtPart1Taluko.getText().toString().trim();
        final String dist = edtPart1District.getText().toString().trim();
        final String state = edtPart1State.getText().toString().trim();
        final String pin = edtPart1PinCode.getText().toString().trim();
        final String familyCode = edtPart1FamilyCode.getText().toString().trim();
        final String yeralyIncome = edtYearlyIncome.getText().toString().trim();

        final String dob = tvpart1DOB.getText().toString().trim();


        if (name.isEmpty()) {
            edtPart1Name.setError("Enter Name");
            return false;
        } else if (addr.isEmpty()) {
            edtPart1Addr.setError("Enter address");
            return false;
        } else if (mobile.length() < 10) {
            edtpart1Mobile.setError("Enter mobile");
            return false;
        } else if (city.isEmpty()) {
            edtPart1City.setError("Enter city");
            return false;
        } else if (spNativePlace.getSelectedItem() == null) {
            Toast.makeText(AddMatrimonialActivity.this, "Native place not selected", Toast.LENGTH_LONG).show();

            return false;
        } else if (dob.isEmpty()) {
            Toast.makeText(AddMatrimonialActivity.this, "Please Select birth date", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            Part1 part1 = new Part1();
            part1.setName(name);
            part1.setFamilyCode(familyCode);
            part1.setAddr(addr);
            part1.setDob(dob);
            part1.setEmail(edtpart1Email.getText().toString().trim());
            part1.setMobile(mobile);
            part1.setNPlace(nativePlace);
            part1.setPhone(phone);
            part1.setL_id(l_id);
            part1.setCity(city);
            part1.setDist(dist);
            part1.setTaluka(taluka);
            part1.setState(state);
            part1.setPin(pin);
            part1.setY_income(yeralyIncome);

            part1.setStudy(edtStudy.getText().toString().trim());
            part1.setBus_cat(main_cat_id);
            part1.setBus_sub_cat(sub_cat_id);
            part1.setStudy_cat(stydy_id);

            if (rgMarriedStatus.getCheckedRadioButtonId() == R.id.rbUnMarried) {
                marriedStatus = "1";
            } else {
                marriedStatus = "2";
            }


            if (rgGender.getCheckedRadioButtonId() == R.id.rbMale) {
                gender = "1";
            } else {
                gender = "2";
            }

            part1.setMarried_status(marriedStatus);
            part1.setGender(gender);
            part1.setM_id(edtMemberNo.getText().toString().trim());

            memberRegistration = new MemberRegistration();

            memberRegistration.setPart1(part1);

            Log.e("member json: ", new Gson().toJson(memberRegistration));

            return true;
        }


    }

    GetBusinessCategory getCategoryByBusiness(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetBusinessCategory.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(AddMatrimonialActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * *******************************************
     * <p>
     * File and camera code
     * ********************************************
     */

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(AddMatrimonialActivity.this);
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
        File file = getOutputMediaFile();

        if (file != null) {
            cameraPath = file.getAbsolutePath();
            fileUri = FileProvider.getUriForFile(this,
                    "com.adaatham.suthar.fileprovider",
                    file);

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                fileUri = contentURI;
                path = getImageRealPath(getContentResolver(), fileUri, null);
                tvFilePath.setText(path.toString());

                // Crop.of(contentURI, output).asSquare().start(EditImageActivity.this);
            }
        } else if (requestCode == CAMERA) {
            if (fileUri != null) {

                Bitmap bitmap = BitmapFactory.decodeFile(cameraPath);
                path = saveImage(getResizedBitmap(bitmap, 200, 200), "profile");
                // Crop.of(fileUri, output).asSquare().start(EditImageActivity.this);
                tvFilePath.setText(path.toString());
            }
        }
    }

    /* Return uri represented document file real local path.*/
    private String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if (cursor != null) {
            boolean moveToFirst = cursor.moveToFirst();
            if (moveToFirst) {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA;
                } else if (uri == MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA;
                } else if (uri == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }
        if (cursor != null)
            cursor.close();

        return ret;
    }

    public String saveImage(Bitmap myBitmap, String fileName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, fileName + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(AddMatrimonialActivity.this,
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP

        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    interface GetVillageListApi {
        @POST("user/villagelistapi/")
        Call<VillageListData> getVillageList();
    }

    interface GetBusinessCategory {
        @POST("user/jobtypeapi/")
        Call<BusinessCategory> getBusinessCatList();
    }

    interface GetMemberBySearch {

        @POST("member/getmemberapi")
        @FormUrlEncoded
        Call<SearchMember> getMember(@Field("id") String id);
    }

    interface AddMetrimonyDetails {

        @POST("member/addmetrominaluserapi")
        Call<MyRes> addMetrimony(@Body MemberRegistration memberRegistration);

        @POST("member/addmetrominaluserimageapi")
        @Multipart
        Call<MyRes> addMetrimonyForNonMember(@Part MultipartBody.Part file, @Part("file") RequestBody name, @Part("data") RequestBody data);


    }

    public class MyCustomAdapter33 extends ArrayAdapter<BusCategoryList> {

        private ArrayList<BusCategoryList> categorylists;

        MyCustomAdapter33(Context context, int textViewResourceId,
                          ArrayList<BusCategoryList> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.categorylists = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row_custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(categorylists.get(position).getMainCat().getName());


            return row;
        }
    }

    public class MyCustomAdapter44 extends ArrayAdapter<SubCat> {

        private ArrayList<SubCat> categorylists;

        MyCustomAdapter44(Context context, int textViewResourceId,
                          ArrayList<SubCat> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.categorylists = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    @NonNull ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row_custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(categorylists.get(position).getSubName());


            return row;
        }
    }

    public class MyCustomAdapter55 extends ArrayAdapter<SubCat> {

        private ArrayList<SubCat> categorylists;

        MyCustomAdapter55(Context context, int textViewResourceId,
                          ArrayList<SubCat> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.categorylists = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row_custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(categorylists.get(position).getSubName());


            return row;
        }
    }

    public class MyCustomAdapter66 extends ArrayAdapter<Villagelist> {

        private ArrayList<Villagelist> categorylists;

        MyCustomAdapter66(Context context, int textViewResourceId,
                          ArrayList<Villagelist> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            this.categorylists = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row_custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvName);
            label.setText(categorylists.get(position).getName());


            return row;
        }
    }
}
