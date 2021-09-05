package com.adaatham.suthar.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.adaatham.suthar.model.Part2;
import com.adaatham.suthar.model.Part3;
import com.adaatham.suthar.model.SubCat;
import com.adaatham.suthar.model.VillageListData;
import com.adaatham.suthar.model.Villagelist;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MemberRegistrationActivity extends AppCompatActivity {
    private final long DOUBLE_TAP = 1500;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private LinearLayout llFirst;
    private TextInputEditText edtPart1Name;
    private TextInputEditText edtPart1Addr;
    private TextInputEditText edtPart1Phone;
    private TextInputEditText edtpart1Mobile;
    private TextInputEditText edtpart1FamilyCode;
    private TextInputEditText edtPart1Native;
    private TextInputEditText edtPart1Email;
    private TextView tvpart1DOB;
    private TextInputEditText edtPart1City;
    private TextInputEditText edtPart1PinCode;
    private TextInputEditText edtPart1Taluko;
    private TextInputEditText edtPart1District;
    private TextInputEditText edtPart1State;
    private Button btnPart1SelectBirth;
    private TextInputEditText edtStudy;
    private Button btnNext11;
    private LinearLayout llSecond;
    private TextInputEditText edtPart2Name;
    private TextInputEditText edtPart2age;
    private TextInputEditText edtPart2Relation;
    private TextInputEditText edtPart2Guardian;

    private Button btnBack22;
    private Button btnNext22;
    private LinearLayout llThird;
    private TextInputEditText edtPart3Name;
    private TextInputEditText edtPart3Age;
    private TextInputEditText edtPart3Relation;
    private TextInputEditText edtSabhasad11Name;
    private TextInputEditText edtSabhasad11Code;
    private TextInputEditText edtSabhasad22Name;
    private TextInputEditText edtSabhasad22Code;

    private String nativePlace = null;

    private CheckBox cbCheque;
    private TextInputLayout txtBankName;
    private TextInputEditText edtBankName;
    private TextInputLayout txtChequeNo;
    private TextInputEditText edtChequeNo;
    private TextInputEditText edtAmount;
    private RadioGroup rgCOC;
    private RadioButton rbCash;
    private RadioButton rbCheque;
    private RadioGroup rgGender;
    private RadioGroup rgMarriageStatus;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbMarried;
    private RadioButton rbUnMarried;
    private Spinner spNativePlace;
    private Button btnBack33;
    private Button btnNext33;
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
    private GetVillageListApi getVillageListApi;
    private String main_cat_id = "";
    private String sub_cat_id = "";
    private String stydy_id = "";
    private String study = "";
    private Spinner spSubCategory;
    private Spinner spStudy;
    private Spinner spCategory;
    private MyCustomAdapter44 myCustomAdapter44;
    private MyCustomAdapter55 myCustomAdapter55;
    private ArrayList<BusCategoryList> copybusCategoryLists;
    private MyCustomAdapter33 myCustomAdapter33;
    private GetBusinessCategory getBusinessCategory;
    private ArrayList<BusCategoryList> busCategoryLists;
    private ProgressBar progressBar;
    private String gender = "1";  // 1 male 2 female
    private String marriedStatus = "1"; // 1 unmarried 2 married
    private ProgressDialog progressDialog;
    private String l_id;
    private MemberRegAPi memberRegAPi;
    private MemberRegistration memberRegistration;

    /**
     * Create a file Uri for saving an image or video
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        copybusCategoryLists = new ArrayList<>();
        busCategoryLists = new ArrayList<>();
        getVillageListApi = RetrofitClient.getClient(Constants.BASE_URL).create(GetVillageListApi.class);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, MemberRegistrationActivity.this);
        l_id = sharedPreferences.getString(Constants.ID, null);

        setContentView(R.layout.activity_member_registration);
        memberRegAPi = addMember(Constants.BASE_URL);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        memberRegistration = new MemberRegistration();
        progressBar = findViewById(R.id.progressBar);

        llFirst = (LinearLayout) findViewById(R.id.llFirst);
        edtPart1Name = (TextInputEditText) findViewById(R.id.edtPart1Name);
        edtPart1Addr = (TextInputEditText) findViewById(R.id.edtPart1Addr);
        edtPart1Phone = (TextInputEditText) findViewById(R.id.edtPart1Phone);
        edtpart1Mobile = (TextInputEditText) findViewById(R.id.edtpart1Mobile);
        edtpart1FamilyCode = (TextInputEditText) findViewById(R.id.edtPart1FamilyCode);
        edtPart1Email = (TextInputEditText) findViewById(R.id.edtpart1Email);
        edtPart1Native = (TextInputEditText) findViewById(R.id.edtPart1Native);
        tvpart1DOB = (TextView) findViewById(R.id.tvpart1DOB);
        edtPart1City = (TextInputEditText) findViewById(R.id.edtPart1City);
        edtPart1PinCode = (TextInputEditText) findViewById(R.id.edtPart1PinCode);
        edtPart1Taluko = (TextInputEditText) findViewById(R.id.edtPart1Taluko);
        edtPart1District = (TextInputEditText) findViewById(R.id.edtPart1District);
        edtPart1State = (TextInputEditText) findViewById(R.id.edtPart1State);
        getBusinessCategory = getCategoryByBusiness(Constants.BASE_URL);
        edtStudy = findViewById(R.id.edtStudy);
        btnPart1SelectBirth = (Button) findViewById(R.id.btnPart1SelectBirth);
        spNativePlace = findViewById(R.id.spNativePlace);


        txtBankName = (TextInputLayout) findViewById(R.id.txtBankName);
        edtBankName = (TextInputEditText) findViewById(R.id.edtBankName);
        txtChequeNo = (TextInputLayout) findViewById(R.id.txtChequeNo);
        edtChequeNo = (TextInputEditText) findViewById(R.id.edtChequeNo);


        edtAmount = (TextInputEditText) findViewById(R.id.edtAmount);
        rgCOC = (RadioGroup) findViewById(R.id.rgCOC);
        rbCash = (RadioButton) findViewById(R.id.rbCash);
        rbCash.setChecked(true);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);

        rgGender = findViewById(R.id.rgGender);
        rgMarriageStatus = findViewById(R.id.rgMarriedStatus);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFeMale);
        rbMarried = findViewById(R.id.rbMarried);
        rbUnMarried = findViewById(R.id.rbUnMarried);


        txtBankName.setVisibility(View.GONE);
        txtChequeNo.setVisibility(View.GONE);

        spCategory = findViewById(R.id.spCategory);
        spSubCategory = findViewById(R.id.spSubCategory);
        spStudy = findViewById(R.id.spCategoryStudy);


        rgCOC.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbCash) {
                    txtBankName.setVisibility(View.GONE);
                    txtChequeNo.setVisibility(View.GONE);
                    edtBankName.getText().clear();
                    edtChequeNo.getText().clear();
                } else {
                    txtBankName.setVisibility(View.VISIBLE);
                    txtChequeNo.setVisibility(View.VISIBLE);
                }
            }
        });


        btnNext11 = (Button) findViewById(R.id.btnNext11);
        llSecond = (LinearLayout) findViewById(R.id.llSecond);
        edtPart2Name = (TextInputEditText) findViewById(R.id.edtPart2Name);
        edtPart2age = (TextInputEditText) findViewById(R.id.edtPart2age);
        edtPart2Relation = (TextInputEditText) findViewById(R.id.edtPart2Relation);
        edtPart2Guardian = (TextInputEditText) findViewById(R.id.edtPart2Guardian);

        btnBack22 = (Button) findViewById(R.id.btnBack22);
        btnNext22 = (Button) findViewById(R.id.btnNext22);
        llThird = (LinearLayout) findViewById(R.id.llThird);
        edtPart3Name = (TextInputEditText) findViewById(R.id.edtPart3Name);
        edtPart3Age = (TextInputEditText) findViewById(R.id.edtPart3Age);
        edtPart3Relation = (TextInputEditText) findViewById(R.id.edtPart3Relation);
        edtSabhasad11Name = (TextInputEditText) findViewById(R.id.edtSabhasad11Name);
        edtSabhasad11Code = (TextInputEditText) findViewById(R.id.edtSabhasad11Code);
        edtSabhasad22Name = (TextInputEditText) findViewById(R.id.edtSabhasad22Name);
        edtSabhasad22Code = (TextInputEditText) findViewById(R.id.edtSabhasad22Code);

        btnBack33 = (Button) findViewById(R.id.btnBack33);
        btnNext33 = (Button) findViewById(R.id.btnNext33);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llFirst.getVisibility() == View.VISIBLE) {
                    finish();
                }

            }
        });


        progressBar.setVisibility(View.VISIBLE);
        getVillageListApi.getVillageList().enqueue(new Callback<VillageListData>() {
            @Override
            public void onResponse(Call<VillageListData> call, Response<VillageListData> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    ArrayList<Villagelist> villagelists = (ArrayList<Villagelist>) response.body().getVillagelist();

                    if (villagelists.size() > 0) {

                        MyCustomAdapter66 myCustomAdapter66 = new MyCustomAdapter66(MemberRegistrationActivity.this,
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


        rgMarriageStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group.getCheckedRadioButtonId() == R.id.rbMarried) {

                    marriedStatus = "2";
                } else if (group.getCheckedRadioButtonId() == R.id.rbUnMarried) {

                    marriedStatus = "1";
                }

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

                            myCustomAdapter55 = new MyCustomAdapter55(MemberRegistrationActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) busCategoryLists.get(i).getSubCat());
                            spStudy.setAdapter(myCustomAdapter55);

                            spStudy.setSelection(((ArrayList<SubCat>) busCategoryLists.get(i).getSubCat()).size() - 1);


//                            for (SubCat subCat : busCategoryLists.get(i).getSubCat()) {
//                                if (subCat.getId().equals(stydy_id)) {
//                                    spStudy.setSelection(busCategoryLists.get(i).getSubCat().indexOf(subCat));
//                                   // edtStudy.setText(study);
//                                    break;
//                                }
//                            }


                        } else {
                            copybusCategoryLists.add(busCategoryLists.get(i));
                        }


                    }

                    if (copybusCategoryLists.size() > 0) {


                        myCustomAdapter33 = new MyCustomAdapter33(MemberRegistrationActivity.this, R.layout.row_custom_spinner, copybusCategoryLists);
                        spCategory.setAdapter(myCustomAdapter33);


                        for (BusCategoryList busCategoryList : copybusCategoryLists) {
                            if (busCategoryList.getMainCat().getId().equals(main_cat_id)) {
                                spCategory.setSelection(copybusCategoryLists.indexOf(busCategoryList));

                                for (SubCat subCat : busCategoryList.getSubCat()) {
                                    if (subCat.getId().equals(sub_cat_id)) {
                                        myCustomAdapter44 = new MyCustomAdapter44(MemberRegistrationActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) busCategoryList.getSubCat());
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


        btnPart1SelectBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(MemberRegistrationActivity.this, endDate, Calendar.getInstance()
                        .get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }
        });


        btnNext11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPart1Valid()) {
                    llFirst.setVisibility(View.GONE);
                    llSecond.setVisibility(View.VISIBLE);
                }
            }
        });


        btnNext22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPart2Valid()) {
                    llFirst.setVisibility(View.GONE);
                    llSecond.setVisibility(View.GONE);
                    llThird.setVisibility(View.VISIBLE);
                }


            }
        });


        btnNext33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//         Submit all data
                Part3 part3 = new Part3();
                part3.setSnName(edtPart3Name.getText().toString().trim());
                part3.setSnAge(edtPart3Age.getText().toString().trim());
                part3.setSnRelation(edtPart3Relation.getText().toString().trim());
                part3.setId1Name(edtSabhasad11Name.getText().toString().trim());
                part3.setId1No(edtSabhasad11Code.getText().toString().trim());
                part3.setId2Name(edtSabhasad22Name.getText().toString().trim());
                part3.setId2No(edtSabhasad22Code.getText().toString().trim());

                memberRegistration.setPart3(part3);

                Log.e("josn", new Gson().toJson(memberRegistration));
                showProgressDialog();

                memberRegAPi.registerMember(memberRegistration).enqueue(new Callback<MyRes>() {
                    @Override
                    public void onResponse(Call<MyRes> call, Response<MyRes> response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (response.isSuccessful()) {

                            if (response.body().getMsg().equalsIgnoreCase("true")) {

                                Toast.makeText(MemberRegistrationActivity.this, Constants.SUCCESS, Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, MemberRegistrationActivity.this);
                                sharedPreferences.edit().putBoolean(Constants.IS_REGISTERED, true).apply();
                                sharedPreferences.edit().putString(Constants.MEMBER_ID, response.body().getId()).apply();

                                String idof = response.body().getId();
                                Intent intent = new Intent(MemberRegistrationActivity.this, PhotoUploadActivity.class);
                                intent.putExtra(Constants.ID, idof);

                                if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {
                                    intent.putExtra("c_no", true);

                                } else {
                                    intent.putExtra("c_no", false);
                                }

                                startActivity(intent);
                                finish();


                            } else if (response.body().getMsg().equalsIgnoreCase("xxx")) {
                                Toast.makeText(MemberRegistrationActivity.this, "Already registered..", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MemberRegistrationActivity.this, "Not inserted..", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MemberRegistrationActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onFailure(Call<MyRes> call, Throwable t) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MemberRegistrationActivity.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        btnBack22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llFirst.setVisibility(View.VISIBLE);
                llSecond.setVisibility(View.GONE);
                llThird.setVisibility(View.GONE);
            }
        });

        btnBack33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llThird.setVisibility(View.GONE);
                llFirst.setVisibility(View.GONE);
                llSecond.setVisibility(View.VISIBLE);


            }
        });


        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                main_cat_id = ((BusCategoryList) parent.getItemAtPosition(position)).getMainCat().getId();

                myCustomAdapter44 = new MyCustomAdapter44(MemberRegistrationActivity.this, R.layout.row_custom_spinner, (ArrayList<SubCat>) ((BusCategoryList) parent.getItemAtPosition(position)).getSubCat());
                spSubCategory.setAdapter(myCustomAdapter44);

                if (((BusCategoryList) parent.getItemAtPosition(position)).getMainCat().getId().equals("10")) {
                    // edtBusinessAddr.setVisibility(View.GONE);
                } else {
                    // edtBusinessAddr.setVisibility(View.VISIBLE);

                }

                for (SubCat subCat : ((BusCategoryList) parent.getItemAtPosition(position)).getSubCat()) {
                    if (subCat.getId().equals(sub_cat_id)) {
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


        spStudy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
     * ***********************************************************
     */


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
        final String familyCode = edtpart1FamilyCode.getText().toString().trim();
        final String bankName = edtBankName.getText().toString().trim();
        final String chequeNo = edtChequeNo.getText().toString().trim();
        final String dob = tvpart1DOB.getText().toString().trim();
        final String amount = edtAmount.getText().toString().trim();


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
            Toast.makeText(MemberRegistrationActivity.this, "Native place not selected", Toast.LENGTH_LONG).show();

            return false;
        } else if (dob.isEmpty()) {
            Toast.makeText(MemberRegistrationActivity.this, "Please Select birth date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtAmount.getText().toString().trim().isEmpty()) {
            edtAmount.setError("Enter amount");
            return false;
        } else if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque && (edtBankName.getText().toString().isEmpty() || edtChequeNo.getText().toString().trim().isEmpty())) {
            edtBankName.requestFocus();
            Toast.makeText(MemberRegistrationActivity.this, "Please enter Bank Name and Cheque No.", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            Part1 part1 = new Part1();
            part1.setName(name);
            part1.setFamilyCode(familyCode);
            part1.setAddr(addr);
            part1.setDob(dob);
            part1.setEmail(edtPart1Email.getText().toString().trim());
            part1.setMobile(mobile);
            part1.setNPlace(nativePlace);
            part1.setPhone(phone);
            part1.setL_id(l_id);
            part1.setCity(city);
            part1.setDist(dist);
            part1.setTaluka(taluka);
            part1.setState(state);
            part1.setPin(pin);
            part1.setAmount(amount);
            part1.setGender(gender);
            part1.setStudy(edtStudy.getText().toString().trim());
            part1.setBus_cat(main_cat_id);
            part1.setBus_sub_cat(sub_cat_id);
            part1.setStudy_cat(stydy_id);
            part1.setMarried_status(marriedStatus);


            if (rgCOC.getCheckedRadioButtonId() == R.id.rbCheque) {
                part1.setPtype("2");
            } else {
                part1.setPtype("1");
            }

            part1.setBankName(edtBankName.getText().toString().trim());
            part1.setChequeNo(edtChequeNo.getText().toString().trim());

            memberRegistration.setPart1(part1);

            return true;
        }


    }

    private boolean isPart2Valid() {

        final String name = edtPart2Name.getText().toString().trim();
        final String age = edtPart2age.getText().toString().trim();
        final String relation = edtPart2Relation.getText().toString().trim();
        final String guardian = edtPart2Guardian.getText().toString().trim();
        Part2 part2 = new Part2();
        part2.setFnName(name);
        part2.setFnAge(age);
        part2.setFnRelation(relation);
        part2.setFnGrd(guardian);

        memberRegistration.setPart2(part2);


        return true;


    }

    MemberRegAPi addMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(MemberRegAPi.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(MemberRegistrationActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    GetBusinessCategory getCategoryByBusiness(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetBusinessCategory.class);
    }


    /**
     * **************************************
     */

    interface MemberRegAPi {
        @POST("member/addmemberapi/")
        Call<MyRes> registerMember(@Body MemberRegistration memberRegistration);
    }

    interface GetBusinessCategory {
        @POST("user/jobtypeapi/")
        Call<BusinessCategory> getBusinessCatList();
    }

    interface GetVillageListApi {
        @POST("user/villagelistapi/")
        Call<VillageListData> getVillageList();
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
