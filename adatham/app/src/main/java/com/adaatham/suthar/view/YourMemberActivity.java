package com.adaatham.suthar.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.view.MenuItemCompat;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.AllMember;
import com.adaatham.suthar.model.Family;
import com.adaatham.suthar.model.Memberlist;
import com.adaatham.suthar.model.MultiPay;
import com.adaatham.suthar.model.MyRes;
import com.adaatham.suthar.model.NoticeList;
import com.adaatham.suthar.model.Pay;
import com.adaatham.suthar.model.Premiumnotice;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class YourMemberActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private GetYourMember getYourMember;
    private ProgressBar progressBar;
    private String mid;
    private ArrayList<Memberlist> memberlistArrayList;
    private CustomAdapter customAdapter;
    private String m_no;
    private TextView tvTotal;
    private Button btnPAY;
    private GetPendingMember pendingMember;
    private ArrayList<Premiumnotice> premiumnoticeArrayList;
    private ArrayList<Premiumnotice> selectedPremiumList;
    private String id;
    private String user_name;
    private String name;
    private boolean isLogin;
    private boolean isReg;
    private boolean isSSy;
    private String role;
    private String mem_id;
    private double total = 0;
    private double total222 = 0;
    private ProgressDialog progressDialog;
    private MakeMultiPay makeMultiPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_member);
        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, YourMemberActivity.this);
        id = sharedPreferences.getString(Constants.MEMBER_NO, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        isSSy = sharedPreferences.getBoolean(Constants.IS_SSY, false);
        mem_id = sharedPreferences.getString(Constants.MEMBER_ID, "");
        memberlistArrayList = new ArrayList<>();
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnPAY = (Button) findViewById(R.id.btnPAY);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pendingMember = getSearchedMember(Constants.BASE_URL);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getYourMember = getYourMember(Constants.BASE_URL);
        makeMultiPay = sendMultiPay(Constants.BASE_URL);
        premiumnoticeArrayList = new ArrayList<>();

        selectedPremiumList = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(YourMemberActivity.this, DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(dividerItemDecoration);
        rvList.setLayoutManager(new LinearLayoutManager(YourMemberActivity.this));


//        getYourMember.yourMember(m_no).enqueue(new Callback<AllMember>() {
//            @Override
//            public void onResponse(Call<AllMember> call, Response<AllMember> response) {
//
//                progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    memberlistArrayList = (ArrayList<Memberlist>) response.body().getMemberlist();
//
//                    if (memberlistArrayList.size() > 0) {
//                        customAdapter = new CustomAdapter(memberlistArrayList);
//                        rvList.setAdapter(customAdapter);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<AllMember> call, Throwable t) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });

        boolean isPayed = false;

        for (int i = 0; i < premiumnoticeArrayList.size(); i++) {

            if (!premiumnoticeArrayList.get(i).getStatus().equals("0")) {
                isPayed = true;
            } else {
                isPayed = false;
                break;
            }


        }

        if (isPayed) {
            btnPAY.setVisibility(View.INVISIBLE);
        } else {
            btnPAY.setVisibility(View.VISIBLE);
        }

        btnPAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isSelected = false;
                com.adaatham.suthar.model.MakePay makePay = new com.adaatham.suthar.model.MakePay();

                for (int i = 0; i < premiumnoticeArrayList.size(); i++) {

                    if (premiumnoticeArrayList.get(i).isChecked()) {
                        isSelected = true;
                        break;
                    } else {
                        isSelected = false;
                    }


                }

                if (isSelected) {


                    HashMap<String, Family> hashMap = new HashMap<>();
                    ArrayList<Family> families = new ArrayList<>();
                    ArrayList<Pay> ids = new ArrayList<>();


                    for (int i = 0; i < premiumnoticeArrayList.size(); i++) {


                        if (premiumnoticeArrayList.get(i).isChecked()) {

                            final Pay pay = new Pay();
                            pay.setMId(premiumnoticeArrayList.get(i).getId());
                            ids.add(pay);

                            if (!hashMap.containsKey(premiumnoticeArrayList.get(i).getGroupNo())) {

                                // reset amount
                                total = 0.0;

                                if (!premiumnoticeArrayList.get(i).getPanalty().isEmpty()) {
                                    total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount()) +
                                            Double.parseDouble(premiumnoticeArrayList.get(i).getPanalty());
                                } else {
                                    total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount());

                                }


                                Family family = new Family();
                                family.setName(premiumnoticeArrayList.get(i).getName());
                                family.setGroupId(premiumnoticeArrayList.get(i).getGroupNo());
                                family.setMobile(premiumnoticeArrayList.get(i).getMobile());
                                family.setTotal(String.valueOf(total));

                                hashMap.put(premiumnoticeArrayList.get(i).getGroupNo(), family);
                                families.add(hashMap.get(premiumnoticeArrayList.get(i).getGroupNo()));

                            } else {

                                if (!premiumnoticeArrayList.get(i).getPanalty().isEmpty()) {
                                    total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount()) +
                                            Double.parseDouble(premiumnoticeArrayList.get(i).getPanalty());
                                } else {
                                    total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount());

                                }

                                // hashMap.get(premiumnoticeArrayList.get(i).getGroupNo()).setName(premiumnoticeArrayList.get(i).getName());
                                hashMap.get(premiumnoticeArrayList.get(i).getGroupNo()).setGroupId(premiumnoticeArrayList.get(i).getGroupNo());
                                //hashMap.get(premiumnoticeArrayList.get(i).getGroupNo()).setMobile(premiumnoticeArrayList.get(i).getMobile());
                                hashMap.get(premiumnoticeArrayList.get(i).getGroupNo()).setTotal(String.valueOf(total));


                            }
                        }


                    }

                    MultiPay multiPay = new MultiPay();
                    multiPay.setL_id(id);

                    multiPay.setPayArrayList(ids);

                    multiPay.setFamily(families);

                    Log.e("json multipay", new Gson().toJson(multiPay).toString());

                    showAlertDialog(multiPay);


                }


            }
        });

        btnPAY.setVisibility(View.GONE);
        pendingMember.getMemberDetail("2", role, "", id).enqueue(new Callback<NoticeList>() {
            @Override
            public void onResponse(Call<NoticeList> call, Response<NoticeList> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    premiumnoticeArrayList = (ArrayList<Premiumnotice>) response.body().getPremiumnotice();

                    if (premiumnoticeArrayList.size() > 0) {
                        customAdapter = new CustomAdapter(premiumnoticeArrayList);
                        rvList.setAdapter(customAdapter);
                        btnPAY.setVisibility(View.VISIBLE);


                        boolean isPayed = false;
                        double total = 0.0;

                        for (int i = 0; i < premiumnoticeArrayList.size(); i++) {


                            if (premiumnoticeArrayList.get(i).getPanalty().isEmpty()) {
                                total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount());

                            } else {
                                total = total + Double.parseDouble(premiumnoticeArrayList.get(i).getAmount()) + Double.parseDouble(premiumnoticeArrayList.get(i).getPanalty());
                            }


                            if (!premiumnoticeArrayList.get(i).getStatus().equals("0")) {
                                isPayed = true;
                            } else {
                                isPayed = false;
                                break;
                            }


                        }

                        if (isPayed) {
                            tvTotal.setText("Total Amount:-".concat(String.valueOf(total)));

                            btnPAY.setVisibility(View.INVISIBLE);
                        } else {
                            btnPAY.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Toast.makeText(YourMemberActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                        customAdapter = new CustomAdapter(premiumnoticeArrayList);
                        rvList.setAdapter(customAdapter);
                        btnPAY.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<NoticeList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(YourMemberActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                btnPAY.setVisibility(View.GONE);

            }
        });


    }


    public void showAlertDialog(final MultiPay multiPay) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(YourMemberActivity.this);

        dialogBuilder.setMessage("Are you sure to continue");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                // TODO call multipay api..

                showProgressDialog();

                makeMultiPay.makeInitPay(multiPay).enqueue(new Callback<MyRes>() {
                    @Override
                    public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (response.isSuccessful()) {
                            if (response.body().getMsg().equalsIgnoreCase("true")) {
                                Toast.makeText(YourMemberActivity.this, "Successfully paid", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(YourMemberActivity.this, "Something went wrong..Try again", Toast.LENGTH_SHORT).show();

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<MyRes> call, Throwable t) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(YourMemberActivity.this, "Something went wrong..Try again", Toast.LENGTH_SHORT).show();

                    }
                });


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_member, menu);


        final MenuItem item = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    GetYourMember getYourMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetYourMember.class);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (customAdapter != null && premiumnoticeArrayList != null && premiumnoticeArrayList.size() > 0) {
            final ArrayList<Premiumnotice> dummyList = (ArrayList<Premiumnotice>) filter(premiumnoticeArrayList, newText);
            customAdapter.setFilter(dummyList);
        }


        return true;
    }

    private List<Premiumnotice> filter(List<Premiumnotice> models, String query) {
        query = query.toLowerCase().trim();
        final List<Premiumnotice> filteredModelList = new ArrayList<>();
        for (Premiumnotice model : models) {
            final String text = model.getName().toLowerCase();
            final String empId = model.getMemberId();
            if (text.contains(query) || empId.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    GetPendingMember getSearchedMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetPendingMember.class);
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(YourMemberActivity.this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    MakeMultiPay sendMultiPay(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(MakeMultiPay.class);
    }

    interface GetYourMember {
        @POST("member/searchbyagentapi/")
        @FormUrlEncoded
        Call<AllMember> yourMember(@Field("l_id") String lid);
    }


    interface GetPendingMember {

        @POST("ssy/premiumnoticeapi/")
        @FormUrlEncoded()
        Call<NoticeList> getMemberDetail(@Field("type") String type, @Field("role") String role, @Field("member_no") String m_no, @Field("l_id") String l_id);


    }

    interface MakeMultiPay {
        @POST("ssy/paymultiplepremiumapi/")
        Call<MyRes> makeInitPay(@Body MultiPay multiPay);
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private ArrayList<Premiumnotice> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        CustomAdapter(ArrayList<Premiumnotice> dataSet) {
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
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the contents of the view


            if (!mDataSet.get(position).getStatus().equals("0")) {

                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("Collected  ");
                viewHolder.cbPay.setVisibility(View.GONE);


            } else {


                viewHolder.tvStatus.setText("");
                viewHolder.tvStatus.setVisibility(View.GONE);
                viewHolder.cbPay.setVisibility(View.VISIBLE);
            }

            viewHolder.cbPay.setOnCheckedChangeListener(null);
//            viewHolder.rgCOC.setOnCheckedChangeListener(null);
            viewHolder.cbPay.setChecked(mDataSet.get(position).isChecked());


            viewHolder.tvName.setText(mDataSet.get(position).getName());
            viewHolder.tvNo.setText(mDataSet.get(position).getMId());
            viewHolder.tvPremiumAmount.setText(mDataSet.get(position).getAmount());

            if (mDataSet.get(position).getStatus().equals("0")) {
                viewHolder.tvStatus.setTextColor(Color.parseColor("#E65100"));
                viewHolder.tvStatus.setText("Your Premium is due");
            } else {
                viewHolder.tvStatus.setTextColor(Color.parseColor("#00695C"));
                viewHolder.tvStatus.setText("Collected..");
                // viewHolder.tvStatus.setText("Collected by  ".concat(mDataSet.get(position).getPE().concat(":- ").concat((mDataSet.get(position).getLId()))));

            }


            viewHolder.cbPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mDataSet.get(position).setChecked(isChecked);


                    if (mDataSet.get(position).isChecked()) {
                        if (mDataSet.get(position).getPanalty().isEmpty()) {
                            total222 = total222 + Double.parseDouble(mDataSet.get(position).getAmount());

                        } else {
                            total222 = total222 + Double.parseDouble(mDataSet.get(position).getAmount()) + Double.parseDouble(mDataSet.get(position).getPanalty());
                        }
                    } else {


                        if (mDataSet.get(position).getPanalty().isEmpty()) {
                            total222 = total222 - Double.parseDouble(mDataSet.get(position).getAmount());

                        } else {
                            total222 = total222 - Double.parseDouble(mDataSet.get(position).getAmount()) - Double.parseDouble(mDataSet.get(position).getPanalty());

                        }


                    }


                    tvTotal.setText("Total Amount:-".concat(String.valueOf(total222)));
                }
            });

            if (mDataSet.get(position).getPanalty().isEmpty()) {
                viewHolder.tvPenalty.setVisibility(View.GONE);
                viewHolder.lablePenalty.setVisibility(View.GONE);
            } else {
                viewHolder.tvPenalty.setVisibility(View.VISIBLE);
                viewHolder.lablePenalty.setVisibility(View.VISIBLE);

                viewHolder.tvPenalty.setText(mDataSet.get(position).getPanalty());
            }


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


        }
        // END_INCLUDE(recyclerViewOnCreateViewHolder)

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
        // END_INCLUDE(recyclerViewOnBindViewHolder)

        void setFilter(List<Premiumnotice> empInfo) {
            mDataSet = new ArrayList<>();
            mDataSet.addAll(empInfo);
            notifyDataSetChanged();
        }

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        class ViewHolder extends RecyclerView.ViewHolder {
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


            ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.

                tvNo = (TextView) v.findViewById(R.id.tvNo);
                tvName = (TextView) v.findViewById(R.id.tvName);
                tvPremiumDate = (TextView) v.findViewById(R.id.tvPremiumDate);
                lablePay = (TextView) v.findViewById(R.id.lablePay);
                tvPremiumAmount = (TextView) v.findViewById(R.id.tvPremiumAmount);
                cbPay = (CheckBox) v.findViewById(R.id.cbPay);
                lablePenalty = (TextView) v.findViewById(R.id.lablePenalty);
                tvPenalty = (TextView) v.findViewById(R.id.tvPenalty);
                llCheque = (LinearLayout) v.findViewById(R.id.llCheque);

                llCheque.setVisibility(View.VISIBLE);
                tvStatus = (TextView) v.findViewById(R.id.tvStatus);
                tvStatusCOC = (TextView) v.findViewById(R.id.tvStatusCOC);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent intent = new Intent(YourMemberActivity.this, MemberDetailActivity.class);
//                        intent.putExtra("detail", mDataSet.get(getAdapterPosition()));
//                        startActivity(intent);

                    }
                });

            }


        }
    }
}
