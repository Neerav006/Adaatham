package com.adaatham.suthar.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.BusCategoryList;
import com.adaatham.suthar.model.BusinessDetail;
import com.adaatham.suthar.model.Category;
import com.adaatham.suthar.model.Categorylist;
import com.adaatham.suthar.model.FamilyList;
import com.adaatham.suthar.model.FamilyListOF;
import com.adaatham.suthar.model.MyRes;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AddBusinessActivity extends AppCompatActivity {
    private GetCategory getCategory;
    private ArrayList<Categorylist> categorylists;
    private MyCustomAdapter myCustomAdapter;
    private TextInputEditText edtBusinessName;
    private TextInputEditText edtContact;
    private TextInputEditText edtAddr;
    private TextInputEditText edtWeb;
    private Spinner spCategory;
    private Button btnsave;
    private String id;
    private ProgressDialog progressDialog;
    private AddBusiness addBusiness;
    private ViewBusiness viewBusiness;
    private TextInputEditText edtDesg;
    private TextInputEditText edtEmail;
    private TextInputEditText edtBusinessDesc;
    private TextInputEditText edtCity;
    private TextInputEditText edtState;
    private TextInputEditText edtPin;
    private String gor;
    private ProgressBar progressBar;
    private Spinner spOwnerName;
    private ArrayList<BusCategoryList> busCategoryLists;
    private ArrayList<FamilyList> memberlistArrayList;
    private SearchMember searchMember;
    private String familyId=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchMember = getSearchedMember(Constants.BASE_URL);
        memberlistArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, AddBusinessActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_ID, null);
        // gor = sharedPreferences.getString(Constants.GOR, null);
        familyId = id;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        edtBusinessName = findViewById(R.id.edtBusinessName);
        edtAddr = findViewById(R.id.edtBusinessAdr);
        edtContact = findViewById(R.id.edtContactNo);
        edtWeb = findViewById(R.id.edtWebsite);
        spCategory = findViewById(R.id.spJobCategory);
        btnsave = findViewById(R.id.btnSave);
        // edtDesg = (TextInputEditText) findViewById(R.id.edtDesg);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtBusinessDesc = (TextInputEditText) findViewById(R.id.edtBusinessDesc);
        edtCity = (TextInputEditText) findViewById(R.id.edtCity);
        edtState = (TextInputEditText) findViewById(R.id.edtState);
        edtPin = (TextInputEditText) findViewById(R.id.edtPin);
        progressBar = findViewById(R.id.progressBar);
        spOwnerName = findViewById(R.id.spOwnerName);
        categorylists = new ArrayList<>();
        addBusiness = addBusinessDetails(Constants.BASE_URL);
        viewBusiness = getBusinessDetails(Constants.BASE_URL);
        getCategory = getCategoryProfession(Constants.BASE_URL);

        progressBar.setVisibility(View.VISIBLE);




        searchMember.getMemberDetail(familyId).enqueue(new Callback<FamilyListOF>() {
            @Override
            public void onResponse(Call<FamilyListOF> call, Response<FamilyListOF> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body().getFamilyList() != null && response.body().getFamilyList().size() > 0) {

                      memberlistArrayList  =(ArrayList<FamilyList>) response.body().getFamilyList();

                        MyCustomAdapter22 customAdapter22 = new MyCustomAdapter22(AddBusinessActivity.this, R.layout.row_custom_spinner, (ArrayList<FamilyList>) response.body().getFamilyList());
                        spOwnerName.setAdapter(customAdapter22);

                       for(int i = 0; i< memberlistArrayList.size() ;i++){

                           if(id.equals(memberlistArrayList.get(i).getId())){

                               spOwnerName.setSelection(memberlistArrayList.indexOf(memberlistArrayList.get(i)));


                           }

                       }

                    }

                }
            }

            @Override
            public void onFailure(Call<FamilyListOF> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


        spOwnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem()!=null){

                    familyId = ((FamilyList)parent.getItemAtPosition(position)).getId();

                    progressBar.setVisibility(View.VISIBLE);
                    viewBusiness.getBusinessList(familyId).enqueue(new Callback<BusinessDetail>() {
                        @Override
                        public void onResponse(Call<BusinessDetail> call, Response<BusinessDetail> response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {
                                if (response.body() != null) {

                                    if (response.body().getMsg() != null && response.body().getMsg().equalsIgnoreCase("false")) {

                                        edtBusinessName.setText("");
                                        edtAddr.setText("");
                                        edtWeb.setText("");
                                        edtContact.setText("");
                                        edtBusinessDesc.setText("");
                                        edtEmail.setText("");
                                        edtCity.setText("");
                                        edtPin.setText("");
                                        edtState.setText("");

                                        btnsave.setText("Save");

                                    } else {
                                        edtBusinessName.setText(response.body().getName());
                                        edtAddr.setText(response.body().getAddr());
                                        edtWeb.setText(response.body().getWeb());
                                        edtContact.setText(response.body().getMobile());
                                        edtBusinessDesc.setText(response.body().getBusinessDesc());
                                        edtEmail.setText(response.body().getEmail());
                                        edtCity.setText(response.body().getCity());
                                        edtPin.setText(response.body().getPin());
                                        edtState.setText(response.body().getState());
                                        // edtDesg.setText(response.body().getDesg());

                                        for (int i = 0; i < categorylists.size(); i++) {
                                            if (response.body().getCategory().equals(categorylists.get(i).getId())) {
                                                spCategory.setSelection(i);
                                                break;
                                            }
                                        }

                                        btnsave.setText("Update");


                                    }

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BusinessDetail> call, Throwable t) {
                            btnsave.setVisibility(View.GONE);
                        }
                    });



                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCategory.getCatList().enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    categorylists = (ArrayList<Categorylist>) response.body().getCategorylist();
                    if (categorylists.size() > 0) {

                        myCustomAdapter = new MyCustomAdapter(AddBusinessActivity.this, R.layout.row_custom_spinner, categorylists);
                        spCategory.setAdapter(myCustomAdapter);

                        progressBar.setVisibility(View.VISIBLE);
                        viewBusiness.getBusinessList(id).enqueue(new Callback<BusinessDetail>() {
                            @Override
                            public void onResponse(Call<BusinessDetail> call, Response<BusinessDetail> response) {
                                progressBar.setVisibility(View.GONE);

                                if (response.isSuccessful()) {
                                    if (response.body() != null) {

                                        if (response.body().getMsg() != null && response.body().getMsg().equalsIgnoreCase("false")) {
                                            btnsave.setText("Save");

                                        } else {
                                            edtBusinessName.setText(response.body().getName());
                                            edtAddr.setText(response.body().getAddr());
                                            edtWeb.setText(response.body().getWeb());
                                            edtContact.setText(response.body().getMobile());
                                            edtBusinessDesc.setText(response.body().getBusinessDesc());
                                            edtEmail.setText(response.body().getEmail());
                                            edtCity.setText(response.body().getCity());
                                            edtPin.setText(response.body().getPin());
                                            edtState.setText(response.body().getState());
                                            // edtDesg.setText(response.body().getDesg());

                                            for (int i = 0; i < categorylists.size(); i++) {
                                                if (response.body().getCategory().equals(categorylists.get(i).getId())) {
                                                    spCategory.setSelection(i);
                                                    break;
                                                }
                                            }

                                            btnsave.setText("Update");


                                        }

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BusinessDetail> call, Throwable t) {
                                btnsave.setVisibility(View.GONE);
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spCategory.getSelectedItem() == null) {
                    Toast.makeText(AddBusinessActivity.this, "Business category not selected", Toast.LENGTH_SHORT).show();
                }
                if(spOwnerName.getSelectedItem() == null){
                    Toast.makeText(AddBusinessActivity.this, "Family member not selected..", Toast.LENGTH_SHORT).show();

                }
                else if (edtBusinessName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddBusinessActivity.this, "Enter business name", Toast.LENGTH_SHORT).show();
                    edtBusinessName.requestFocus();
                } else if (edtBusinessDesc.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddBusinessActivity.this, "Enter business Description", Toast.LENGTH_SHORT).show();
                    edtBusinessDesc.requestFocus();
                } else if (edtAddr.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddBusinessActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                    edtAddr.requestFocus();

                } else if (edtCity.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddBusinessActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                    edtCity.requestFocus();
                } else if (edtState.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddBusinessActivity.this, "Enter State Name", Toast.LENGTH_SHORT).show();
                    edtState.requestFocus();
                } else if (edtContact.getText().toString().trim().length() != 10) {
                    Toast.makeText(AddBusinessActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    edtContact.requestFocus();

                } else {

                    JSONObject jsonObject = new JSONObject();
                    showProgressDialog();
                    final BusinessDetail businessDetail = new BusinessDetail();
                    businessDetail.setName(edtBusinessName.getText().toString().trim());
                    businessDetail.setAddr(edtAddr.getText().toString().trim());
                    businessDetail.setMobile(edtContact.getText().toString().trim());
                    businessDetail.setId(familyId);
                    businessDetail.setGor(gor);
                    businessDetail.setBusinessDesc(edtBusinessDesc.getText().toString().trim());
                    businessDetail.setEmail(edtEmail.getText().toString().trim());
                    businessDetail.setCity(edtCity.getText().toString().trim());
                    businessDetail.setState(edtState.getText().toString().trim());
                    businessDetail.setPin(edtPin.getText().toString().trim());
                    // businessDetail.setDesg(edtDesg.getText().toString().trim());
                    businessDetail.setCategory(((Categorylist) spCategory.getSelectedItem()).getId());
                    businessDetail.setWeb(edtWeb.getText().toString().trim());


//                        jsonObject.put("id", id);
//                        jsonObject.put("category", ((Categorylist) spCategory.getSelectedItem()).getId());
//                        jsonObject.put("addr", edtAddr.getText().toString().trim());
//                        jsonObject.put("mobile", edtContact.getText().toString().trim());
//                        jsonObject.put("web", edtWeb.getText().toString().trim());
//                        jsonObject.put("name", edtBusinessName.getText().toString().trim());

                    Log.e("business", new Gson().toJson(businessDetail));

                    addBusiness.addBusiness(businessDetail).enqueue(new Callback<MyRes>() {
                        @Override
                        public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            if (response.isSuccessful()) {

                                if (response.body().getMsg().equalsIgnoreCase("true")) {
                                    Toast.makeText(AddBusinessActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (response.body().getMsg().equalsIgnoreCase("false")) {
                                    Toast.makeText(AddBusinessActivity.this, "Error occur", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRes> call, Throwable t) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(AddBusinessActivity.this, "Error occur", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });


    }

    GetCategory getCategoryProfession(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetCategory.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(AddBusinessActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    AddBusiness addBusinessDetails(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(AddBusiness.class);
    }

    ViewBusiness getBusinessDetails(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(ViewBusiness.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.hideSoftKeyBoard(this.getCurrentFocus(), AddBusinessActivity.this);
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    SearchMember getSearchedMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SearchMember.class);
    }

    interface GetCategory {
        @POST("directory/categorylistapi/")
        Call<Category> getCatList();
    }

    interface AddBusiness {
        @POST("directory/addbusinessapi/")
        Call<MyRes> addBusiness(@Body BusinessDetail json);
    }

    interface ViewBusiness {
        @POST("directory/viewbusinessapi/")
        @FormUrlEncoded
        Call<BusinessDetail> getBusinessList(@Field("id") String id);
    }


    interface SearchMember {

        @POST("user/totalfamilylistapi/")
        @FormUrlEncoded()
        Call<FamilyListOF> getMemberDetail(@Field("id") String id);


    }

    public class MyCustomAdapter extends ArrayAdapter<Categorylist> {

        private ArrayList<Categorylist> categorylists;

        MyCustomAdapter(Context context, int textViewResourceId,
                        ArrayList<Categorylist> objects) {
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

    public class MyCustomAdapter22 extends ArrayAdapter<FamilyList> {

        private ArrayList<FamilyList> categorylists;

        MyCustomAdapter22(Context context, int textViewResourceId,
                          ArrayList<FamilyList> objects) {
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
