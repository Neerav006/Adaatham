package com.adaatham.suthar.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.AlarmReceiver;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.AllMember;
import com.adaatham.suthar.model.Memberlist;
import com.adaatham.suthar.model.MobileRegistration;
import com.adaatham.suthar.model.MyRes;
import com.adaatham.suthar.model.VillageListData;
import com.adaatham.suthar.model.Villagelist;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RegistrationActivity extends AppCompatActivity {

    // role       2-->agent         3-->user

    private final long DOUBLE_TAP = 1500;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private TextInputEditText edtName;
    private TextInputEditText edtMobile;
    private TextInputEditText edtAddr;
    private TextInputEditText edtNativePlace;
    private TextInputEditText edtCity;
    private TextInputEditText edtEmail;
    private RadioGroup radioGroup;
    private RadioButton rbExecutive;
    private RadioButton rbUser;
    private Button btnRegister;
    private long lastclick = 0;
    private RegisterMobile registerMobile;
    private ProgressDialog progressDialog;
    private TextInputEditText edtMemNo;
    private ArrayList<Memberlist> memberlistArrayList;
    private SearchMember searchMember;
    private LinearLayout llIsMem;
    private RadioGroup rgYesOrNo;
    private RadioButton rbYes;
    private RadioButton rbNo;
    private Button ivGo;
    private TextInputLayout txtInName;
    private TextInputLayout txtInAddr;
    private TextInputLayout txtInNative;
    private TextInputLayout txtInCity;
    private TextInputLayout txtInEmail;
    private TextInputLayout txtInMobile;
    private AppCompatSpinner appCompatSpinner;
    private GetVillageListApi getVillageListApi;
    private String villageList;
    private TextView lableSelectNativePlace;
    private ArrayList<Villagelist> villagelists;
    private ScrollView scrollContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        searchMember = getSearchedMember(Constants.BASE_URL);
        registerMobile = registerMobileUser(Constants.BASE_URL);
        villagelists = new ArrayList<Villagelist>();

        getVillageListApi = RetrofitClient.getClient(Constants.BASE_URL).create(GetVillageListApi.class);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edtName = (TextInputEditText) findViewById(R.id.edtName);
        edtMobile = (TextInputEditText) findViewById(R.id.edtMobile);
        edtAddr = (TextInputEditText) findViewById(R.id.edtAddr);
        edtNativePlace = (TextInputEditText) findViewById(R.id.edtNativePlace);
        edtCity = (TextInputEditText) findViewById(R.id.edtCity);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        radioGroup = (RadioGroup) findViewById(R.id.rgReg);
        rbExecutive = (RadioButton) findViewById(R.id.rbAgent);
        rbUser = (RadioButton) findViewById(R.id.rbUser);
        edtMemNo = (TextInputEditText) findViewById(R.id.edtMemNo);
        llIsMem = (LinearLayout) findViewById(R.id.llIsMem);
        rgYesOrNo = (RadioGroup) findViewById(R.id.rgYesOrNo);
        rbYes = (RadioButton) findViewById(R.id.rbYes);
        rbNo = (RadioButton) findViewById(R.id.rbNo);
        ivGo = (Button) findViewById(R.id.ivGo);
        txtInName = (TextInputLayout) findViewById(R.id.txtInName);
        txtInAddr = (TextInputLayout) findViewById(R.id.txtInAddr);
        txtInNative = (TextInputLayout) findViewById(R.id.txtInNative);
        txtInCity = (TextInputLayout) findViewById(R.id.txtInCity);
        txtInEmail = (TextInputLayout) findViewById(R.id.txtInEmail);
        txtInMobile = (TextInputLayout) findViewById(R.id.txtInMobile);
        appCompatSpinner = findViewById(R.id.spVillageList);
        lableSelectNativePlace = findViewById(R.id.lableSelectNativePlace);
        scrollContainer = findViewById(R.id.scrollContainer);

        edtCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    scrollContainer.scrollTo(0,txtInCity.getTop());
                }
            }
        });
        edtAddr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    scrollContainer.scrollTo(0,txtInAddr.getTop());
                }
            }
        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtMemNo.setVisibility(View.GONE);
        ivGo.setVisibility(View.GONE);

        edtMemNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().isEmpty()) {
                    // TODO call member detail if success full fill up otherwise clear

                    edtName.setText("");
                    edtAddr.setText("");
                    edtNativePlace.setText("");
                    edtCity.setText("");
                    edtEmail.setText("");
                    edtMobile.setText("");
                }
            }
        });


        getVillageListApi.getVillageList().enqueue(new Callback<VillageListData>() {
            @Override
            public void onResponse(Call<VillageListData> call, Response<VillageListData> response) {


                if (response.isSuccessful()) {

                    villagelists = (ArrayList<Villagelist>) response.body().getVillagelist();

                    if (villagelists.size() > 0) {

                        MyCustomAdapter66 myCustomAdapter66 = new MyCustomAdapter66(RegistrationActivity.this,
                                R.layout.row_custom_spinner, villagelists);
                        appCompatSpinner.setAdapter(myCustomAdapter66);


                    }


                }
            }

            @Override
            public void onFailure(Call<VillageListData> call, Throwable t) {

            }
        });


        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem() != null) {
                    villageList = ((Villagelist) parent.getItemAtPosition(position)).getName();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ivGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMemNo.getText().toString().trim().isEmpty()) {

                } else {

                    showProgressDialog();

                    searchMember.getMemberDetail("1", edtMemNo.getText().toString().trim())
                            .enqueue(new Callback<AllMember>() {
                                @Override
                                public void onResponse(Call<AllMember> call, Response<AllMember> response) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    if (response.isSuccessful()) {
                                        memberlistArrayList = (ArrayList<Memberlist>) response.body().getMemberlist();

                                        if (memberlistArrayList != null && memberlistArrayList.size() > 0) {

                                            btnRegister.setVisibility(View.VISIBLE);

                                            edtName.setVisibility(View.VISIBLE);
                                            edtAddr.setVisibility(View.VISIBLE);
                                            edtNativePlace.setVisibility(View.GONE);
                                            appCompatSpinner.setVisibility(View.VISIBLE);
                                            edtCity.setVisibility(View.VISIBLE);
                                            edtEmail.setVisibility(View.VISIBLE);
                                            edtMobile.setVisibility(View.VISIBLE);

                                            txtInAddr.setVisibility(View.VISIBLE);
                                            txtInCity.setVisibility(View.VISIBLE);
                                            txtInEmail.setVisibility(View.VISIBLE);
                                            txtInMobile.setVisibility(View.VISIBLE);
                                            txtInName.setVisibility(View.VISIBLE);
                                            txtInNative.setVisibility(View.VISIBLE);

                                            edtMemNo.setError(null);
                                            edtEmail.setError(null);
                                            edtCity.setError(null);
                                            edtNativePlace.setError(null);
                                            edtMobile.setError(null);
                                            edtName.setError(null);
                                            edtAddr.setError(null);

                                            edtName.setText(memberlistArrayList.get(0).getName());
                                            edtAddr.setText(memberlistArrayList.get(0).getAddress());
                                            edtNativePlace.setText(memberlistArrayList.get(0).getNativePlace());
                                            lableSelectNativePlace.setVisibility(View.VISIBLE);


                                            if (villagelists != null) {

                                                for (int i = 0; i < villagelists.size(); i++) {

                                                    if (memberlistArrayList.get(0).getNativePlace().equals(villagelists.get(i).getName())) {

                                                        appCompatSpinner.setSelection(i);

                                                        break;
                                                    }
                                                }


                                            }


                                            edtCity.setText(memberlistArrayList.get(0).getCity());
                                            edtEmail.setText(memberlistArrayList.get(0).getEmail());
                                            edtMobile.setText(memberlistArrayList.get(0).getMobile());


                                            edtName.setInputType(InputType.TYPE_NULL);
                                            edtName.setKeyListener(null);

                                            edtMobile.setInputType(InputType.TYPE_NULL);
                                            edtMobile.setKeyListener(null);


                                        }

                                    }

                                }

                                @Override
                                public void onFailure(Call<AllMember> call, Throwable t) {

                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }

                                    txtInAddr.setVisibility(View.VISIBLE);
                                    txtInCity.setVisibility(View.VISIBLE);
                                    txtInEmail.setVisibility(View.VISIBLE);
                                    txtInMobile.setVisibility(View.VISIBLE);
                                    txtInName.setVisibility(View.VISIBLE);
                                    txtInNative.setVisibility(View.VISIBLE);


                                    edtMemNo.setVisibility(View.GONE);
                                    ivGo.setVisibility(View.GONE);
                                    edtName.setVisibility(View.VISIBLE);
                                    edtAddr.setVisibility(View.VISIBLE);
                                    edtNativePlace.setVisibility(View.GONE);
                                    edtCity.setVisibility(View.VISIBLE);
                                    edtEmail.setVisibility(View.VISIBLE);
                                    edtMobile.setVisibility(View.VISIBLE);
                                    edtName.setText("");
                                    edtAddr.setText("");
                                    edtNativePlace.setText("");
                                    edtCity.setText("");
                                    edtEmail.setText("");
                                    edtMobile.setText("");
                                }
                            });
                }
            }
        });

        rgYesOrNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (group.getCheckedRadioButtonId() == R.id.rbYes) {

                    btnRegister.setVisibility(View.GONE);

                    edtMemNo.setError(null);
                    edtEmail.setError(null);
                    edtCity.setError(null);
                    edtNativePlace.setError(null);
                    edtMobile.setError(null);
                    edtName.setError(null);
                    edtAddr.setError(null);


                    edtMemNo.setVisibility(View.VISIBLE);
                    ivGo.setVisibility(View.VISIBLE);
                    edtName.setVisibility(View.GONE);
                    edtAddr.setVisibility(View.GONE);
                    edtNativePlace.setVisibility(View.GONE);
                    appCompatSpinner.setVisibility(View.GONE);
                    lableSelectNativePlace.setVisibility(View.GONE);
                    edtCity.setVisibility(View.GONE);
                    edtEmail.setVisibility(View.GONE);
                    edtMobile.setVisibility(View.GONE);

                    txtInAddr.setVisibility(View.GONE);
                    txtInCity.setVisibility(View.GONE);
                    txtInEmail.setVisibility(View.GONE);
                    txtInMobile.setVisibility(View.GONE);
                    txtInName.setVisibility(View.GONE);
                    txtInNative.setVisibility(View.GONE);


                } else if (group.getCheckedRadioButtonId() == R.id.rbNo) {

                    btnRegister.setVisibility(View.VISIBLE);
                    edtMemNo.setError(null);
                    edtEmail.setError(null);
                    edtCity.setError(null);
                    edtNativePlace.setError(null);
                    edtMobile.setError(null);
                    edtName.setError(null);
                    edtAddr.setError(null);

                    edtMemNo.setVisibility(View.GONE);
                    ivGo.setVisibility(View.GONE);
                    edtName.setVisibility(View.VISIBLE);
                    edtAddr.setVisibility(View.VISIBLE);
                    appCompatSpinner.setVisibility(View.VISIBLE);
                    appCompatSpinner.setSelection(0);
                    lableSelectNativePlace.setVisibility(View.VISIBLE);

                    edtCity.setVisibility(View.VISIBLE);
                    edtEmail.setVisibility(View.VISIBLE);
                    edtMobile.setVisibility(View.VISIBLE);


                    txtInAddr.setVisibility(View.VISIBLE);
                    txtInCity.setVisibility(View.VISIBLE);
                    txtInEmail.setVisibility(View.VISIBLE);
                    txtInMobile.setVisibility(View.VISIBLE);
                    txtInName.setVisibility(View.VISIBLE);
                    txtInNative.setVisibility(View.VISIBLE);

                    edtMobile.setInputType(InputType.TYPE_CLASS_NUMBER);
                    edtCity.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtName.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtNativePlace.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtAddr.setInputType(InputType.TYPE_CLASS_TEXT);


                    edtName.setText("");
                    edtAddr.setText("");
                    edtNativePlace.setText("");
                    edtCity.setText("");
                    edtEmail.setText("");
                    edtMobile.setText("");
                }

            }
        });


//        edtMemNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
//                } else {
//
//
//                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtMemNo.setError(null);
                edtEmail.setError(null);
                edtCity.setError(null);
                edtNativePlace.setError(null);
                edtMobile.setError(null);
                edtName.setError(null);
                edtAddr.setError(null);

                if (SystemClock.elapsedRealtime() - lastclick < DOUBLE_TAP) {
                    Log.e("double tap", "");
                    return;
                }
                lastclick = SystemClock.elapsedRealtime();


                final String mem_id = edtMemNo.getText().toString().trim();
                final String name = edtName.getText().toString().trim();
                final String mobile = edtMobile.getText().toString().trim();
                final String addr = edtAddr.getText().toString().trim();
                final String city = edtCity.getText().toString().trim();
                final String nativePlace = villageList;
                final String email = edtEmail.getText().toString().trim();

                if (rgYesOrNo.getCheckedRadioButtonId() == R.id.rbYes && mem_id.isEmpty()) {

                    edtMemNo.setError("Enter Member No.");

                } else if (name.isEmpty()) {

                    edtName.setError("Enter Name");

                } else if (mobile.isEmpty() || mobile.length() < 10) {

                    edtMobile.setError("Enter mobile Number");
                } else if (addr.isEmpty()) {

                    edtAddr.setError("Enter Address");
                } else if (nativePlace == null || nativePlace.isEmpty()) {

                    edtNativePlace.setError("Enter your native place");
                } else if (city.isEmpty()) {

                    edtCity.setError("Enter city name");
                } else if (!email.isEmpty() && !Utils.isValidEmail(email)) {
                    edtEmail.setError("Enter valid email");

                } else {
                    // all ok let's register
                    final String mem_id2 = edtMemNo.getText().toString().trim();

                    MobileRegistration mobileRegistration = new MobileRegistration();

                    if (rgYesOrNo.getCheckedRadioButtonId() == R.id.rbYes) {
                        mobileRegistration.setMem_no(mem_id2);
                    } else {
                        mobileRegistration.setMem_no("0");
                    }


                    mobileRegistration.setName(name);
                    mobileRegistration.setAddr(addr);
                    mobileRegistration.setEmail(email);
                    mobileRegistration.setCity(city);
                    mobileRegistration.setNPlace(nativePlace);
                    mobileRegistration.setMobile(mobile);

                    if (radioGroup.getCheckedRadioButtonId() == R.id.rbUser) {
                        mobileRegistration.setRole("3");
                    } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbAgent) {
                        mobileRegistration.setRole("2");
                    }


                    Log.e("json", new Gson().toJson(mobileRegistration));


                    SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, getApplicationContext());
                    String user_name = sharedPreferences.getString("ok", "");
                    String reg_in_progress = sharedPreferences.getString(Constants.REG_IN_PROGRESS, "");

                    if (!user_name.isEmpty() && !reg_in_progress.isEmpty()) {

                        Intent intent = new Intent(RegistrationActivity.this, ForgotPwd22Activity.class);
                        intent.putExtra(Constants.ID, reg_in_progress);
                        intent.putExtra(Constants.TYPE, "reg");
                        startActivity(intent);


                    } else {

                        showProgressDialog();
                        registerMobile.MakeRegister(mobileRegistration).enqueue(new Callback<MyRes>() {
                            @Override
                            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                                if (progressDialog.isShowing()) progressDialog.dismiss();

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equalsIgnoreCase("true")) {

                                        SharedPreferences.Editor editor = Utils.writeToPreference(Constants.MY_PREF, getApplicationContext());
                                        editor.putString(Constants.REG_IN_PROGRESS, response.body().getId());
                                        editor.putString("ok", response.body().getMsg());
                                        editor.apply();

                                        scheduleAlarm();

                                        Intent intent = new Intent(RegistrationActivity.this, ForgotPwd22Activity.class);
                                        intent.putExtra(Constants.ID, response.body().getId());
                                        intent.putExtra(Constants.TYPE, "reg");
                                        startActivity(intent);

                                    } else if (response.body().getMsg().equalsIgnoreCase("xxx")) {
                                        Toast.makeText(RegistrationActivity.this, "Already registered", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {

                                        Toast.makeText(RegistrationActivity.this, "Not successfully saved", Toast.LENGTH_SHORT).show();
                                    }

                                }


                            }

                            @Override
                            public void onFailure(Call<MyRes> call, Throwable t) {
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Not successfully saved", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }


                }


            }
        });

    }

    RegisterMobile registerMobileUser(String baseUrl) {

        return RetrofitClient.getClient(baseUrl).create(RegisterMobile.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    SearchMember getSearchedMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SearchMember.class);
    }

    public void scheduleAlarm() {
        // The time at which the alarm will be scheduled. Here the alarm is scheduled for 1 day from the current time.
        // We fetch the current time in milliseconds and add 1 day's time
        // i.e. 24*60*60*1000 = 86,400,000 milliseconds in a day.
        Long time = new GregorianCalendar().getTimeInMillis() + 24 * 60 * 60 * 1000;
        Long time2 = new GregorianCalendar().getTimeInMillis() + 2 * 60 * 1000;

        Calendar cal = Calendar.getInstance();
        // add 30 seconds to the calendar object
        cal.add(Calendar.MILLISECOND, 60 * 1000);
        // Create an Intent and set the class that will execute when the Alarm triggers. Here we have
        // specified AlarmReceiver in the Intent. The onReceive() method of this class will execute when the broadcast from your alarm is received.
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);

        // Get the Alarm Service.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Set the alarm for a particular time.
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), PendingIntent.getBroadcast(this, 1, intentAlarm, 0));
        //Toast.makeText(this, "Alarm Scheduled for Tomorrow", Toast.LENGTH_LONG).show();
    }

    interface RegisterMobile {
        @POST("home/registrationapi/")
        Call<MyRes> MakeRegister(@Body MobileRegistration mobileRegistration);
    }

    interface SearchMember {

        @POST("member/searchdetailsapi/")
        @FormUrlEncoded()
        Call<AllMember> getMemberDetail(@Field("type") String type, @Field("search") String search);


    }

    interface GetVillageListApi {
        @POST("user/villagelistapi/")
        Call<VillageListData> getVillageList();
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
