package com.adaatham.suthar.view;

import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.AllMember;
import com.adaatham.suthar.model.Memberlist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PaymentListActivity extends AppCompatActivity {

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private Button btnSearch;
    private RecyclerView rvList;
    private EditText edtSearch;
    private CustomAdapter customAdapter;
    private ArrayList<Memberlist> memberlistArrayList;
    private SearchMemberActivity.SearchMember searchMember;
    private Spinner spType;
    private ProgressBar progressBar;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);


        searchMember = getSearchedMember(Constants.BASE_URL);
        memberlistArrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, PaymentListActivity.this);
        role = sharedPreferences.getString(Constants.ROLE, null);

        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        rvList = (RecyclerView) findViewById(R.id.rvList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(PaymentListActivity.this, DividerItemDecoration.VERTICAL);
        rvList.setLayoutManager(new LinearLayoutManager(PaymentListActivity.this));
        rvList.addItemDecoration(dividerItemDecoration);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        spType = (Spinner) findViewById(R.id.spType);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtSearch.getText().toString().trim().isEmpty()) {

                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    btnSearch.setEnabled(false);
                    searchMember.getMemberDetail(String.valueOf(spType.getSelectedItemPosition() + 1), edtSearch.getText().toString().trim())
                            .enqueue(new Callback<AllMember>() {
                                @Override
                                public void onResponse(Call<AllMember> call, Response<AllMember> response) {
                                    btnSearch.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    if (response.isSuccessful()) {
                                        memberlistArrayList = (ArrayList<Memberlist>) response.body().getMemberlist();

                                        if (memberlistArrayList.size() > 0) {
                                            customAdapter = new CustomAdapter(memberlistArrayList);
                                            rvList.setAdapter(customAdapter);

                                        } else {
                                            customAdapter = new CustomAdapter(memberlistArrayList);
                                            rvList.setAdapter(customAdapter);
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<AllMember> call, Throwable t) {
                                    btnSearch.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);

                                }
                            });


                }
            }
        });

    }

    SearchMemberActivity.SearchMember getSearchedMember(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SearchMemberActivity.SearchMember.class);
    }

    interface SearchMember {

        @POST("member/searchdetailsapi/")
        @FormUrlEncoded()
        Call<AllMember> getMemberDetail(@Field("type") String type, @Field("search") String search);


    }

    /**
     * Provide views to RecyclerView with data from mDataSet.
     */
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        private ArrayList<Memberlist> mDataSet;

        // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
         */
        CustomAdapter(ArrayList<Memberlist> dataSet) {
            mDataSet = dataSet;
        }
        // END_INCLUDE(recyclerViewSampleViewHolder)

        // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
        // Create new views (invoked by the layout manager)
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_member_info, viewGroup, false);

            return new CustomAdapter.ViewHolder(v);
        }

        // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");

            // Get element from your dataset at this position and replace the contents of the view
            // with that element


            viewHolder.tvName.setText(mDataSet.get(position).getName());
            viewHolder.tvMemNo.setText(mDataSet.get(position).getMemberId());
            viewHolder.tvCity.setText(mDataSet.get(position).getCity());

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
        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView tvName;
            private final TextView tvCity;
            private final TextView tvMemNo;
            private final ImageView ivView;
            private final ImageView ivEdit;
            private final ImageView ivEditProfile;


            ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.


                tvName = (TextView) v.findViewById(R.id.tvMemberName);
                tvCity = (TextView) v.findViewById(R.id.tvCity);
                tvMemNo = (TextView) v.findViewById(R.id.tvMemberCode);
                ivView = (ImageView) v.findViewById(R.id.ivView);
                ivEdit = (ImageView) v.findViewById(R.id.ivEdit);
                ivEditProfile = (ImageView) v.findViewById(R.id.ivEditProfile);

                ivEdit.setVisibility(View.GONE);
                ivEditProfile.setVisibility(View.GONE);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PaymentListActivity.this, PaymentDetailActivity.class);
                        intent.putExtra("m_id", mDataSet.get(getAdapterPosition()).getMemberId());
                        startActivity(intent);
                    }
                });


            }


        }
    }
}
