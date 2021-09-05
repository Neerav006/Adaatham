package com.adaatham.suthar.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.MyAdapter;
import com.adaatham.suthar.common.OnImagePressedListener;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.model.Banner;
import com.adaatham.suthar.model.MyBanner;
import com.adaatham.suthar.view.AddBusinessActivity;
import com.adaatham.suthar.view.DirectoryDashboardActivity;
import com.adaatham.suthar.view.DownLoadFormActivity;
import com.adaatham.suthar.view.GroupViseFamily;
import com.adaatham.suthar.view.HappyBirthdayActivity;
import com.adaatham.suthar.view.HomeActivity;
import com.adaatham.suthar.view.LatestNewsActivity;
import com.adaatham.suthar.view.MemberRegistrationActivity;
import com.adaatham.suthar.view.SearchMemberActivity;
import com.adaatham.suthar.view.SendMsgActivity;
import com.adaatham.suthar.view.SocialSecurityDashboard;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;


public class UserDashboardFragment extends Fragment {


    private static final int REQ_CODE = 12;
    private ViewPager mPager;
    private int currentPage = 0;
    private Context context;
    private CardView cvMemberReg;
    private CardView cvMemberInfo;
    private CardView cvSocialSchema;
    private CardView cvDownloadForm;
    private CardView cvSendMsg;
    private CardView cvBirthDay;
    private CardView cvNews;
    private LinearLayout llFirstrow;
    private LinearLayout llSecondrow;
    private LinearLayout llthirdrow;
    private String id;
    private String user_name;
    private String name;
    private boolean isLogin;
    private boolean isReg;
    private boolean isSSy;
    private String role;
    private CardView cvDirectory;
    private ArrayList<String> XMENArray = new ArrayList<String>();
    private GetBanner getBanner;
    private View view;
    private Handler handler;
    private CardView cvMyFamily;
    private CardView cvAddBusiness;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBanner = showBanner(Constants.BASE_URL);
        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, getActivity());
        id = sharedPreferences.getString(Constants.ID, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        isSSy = sharedPreferences.getBoolean(Constants.IS_SSY, false);

        getBanner.getBanner().enqueue(new Callback<MyBanner>() {
            @Override
            public void onResponse(Call<MyBanner> call, Response<MyBanner> response) {

                if (response.isSuccessful()) {

                    ArrayList<Banner> bannerArrayList = (ArrayList<Banner>) response.body().getBanner();


                    if (bannerArrayList != null && bannerArrayList.size() > 0) {

                        for (int i = 0; i < bannerArrayList.size(); i++) {
                            XMENArray.add(Constants.BASE_URL.concat("banner/").concat(bannerArrayList.get(i).getImage()));
                        }

                        if (view != null)
                            init(view);
                    }


                }

            }

            @Override
            public void onFailure(Call<MyBanner> call, Throwable t) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((HomeActivity) (context)).setTitle("Dashboard");
        return inflater.inflate(R.layout.dashboard_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;


        cvMemberReg = (CardView) view.findViewById(R.id.cvMemberReg);
        cvMemberInfo = (CardView) view.findViewById(R.id.cvMemberInfo);
        cvSocialSchema = (CardView) view.findViewById(R.id.cvSocialSchema);
        cvDownloadForm = (CardView) view.findViewById(R.id.cvDownloadForm);
        cvSendMsg = (CardView) view.findViewById(R.id.cvSendMsg);
        cvBirthDay = (CardView) view.findViewById(R.id.cvBirthDay);
        cvNews = (CardView) view.findViewById(R.id.cvNews);
        cvDirectory = (CardView) view.findViewById(R.id.cvDirectory);
        cvMyFamily = view.findViewById(R.id.cvMyFamily);
        cvAddBusiness = view.findViewById(R.id.cvAddBusinessDirectory);


        llFirstrow = (LinearLayout) view.findViewById(R.id.llFirstrow);
        llSecondrow = (LinearLayout) view.findViewById(R.id.llSecondrow);
        llthirdrow = (LinearLayout) view.findViewById(R.id.llThirdrow);

        llFirstrow.removeAllViews();
        llSecondrow.removeAllViews();
        llthirdrow.removeAllViews();


        llFirstrow.addView(cvMemberInfo);
        llFirstrow.addView(cvMyFamily);
        llFirstrow.addView(cvSocialSchema);
        llFirstrow.addView(cvSendMsg);
        llSecondrow.addView(cvBirthDay);
        llSecondrow.addView(cvNews);
        llSecondrow.addView(cvAddBusiness);
        llSecondrow.addView(cvDownloadForm);
        llthirdrow.addView(cvDirectory);


        cvMemberInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchMemberActivity.class);
                startActivity(intent);
            }
        });

        cvDownloadForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), DownLoadFormActivity.class);
                startActivity(intent);
            }
        });

        cvSocialSchema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SocialSecurityDashboard.class);
                startActivity(intent);
            }
        });

        cvSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SendMsgActivity.class);
                startActivity(intent);
            }
        });

        cvBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HappyBirthdayActivity.class);
                startActivity(intent);
            }
        });

        cvMemberReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MemberRegistrationActivity.class);
                startActivity(intent);
            }
        });

        cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LatestNewsActivity.class);
                startActivity(intent);
            }
        });

        cvDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DirectoryDashboardActivity.class);
                startActivity(intent);
            }
        });

        cvMyFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GroupViseFamily.class);
                startActivity(intent);
            }
        });

        cvAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddBusinessActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        ((HomeActivity) (context)).setTitle("Dashboard");
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, getActivity());
        boolean isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);
        llFirstrow.removeAllViews();
        llSecondrow.removeAllViews();
        llthirdrow.removeAllViews();

//        if (isReg) {
        llFirstrow.addView(cvMemberInfo);
        llFirstrow.addView(cvMyFamily);
        llFirstrow.addView(cvSocialSchema);
        llFirstrow.addView(cvSendMsg);
        llSecondrow.addView(cvBirthDay);
        llSecondrow.addView(cvNews);
        llSecondrow.addView(cvAddBusiness);
        llSecondrow.addView(cvDownloadForm);
        llthirdrow.addView(cvDirectory);

//        } else {
//            llFirstrow.addView(cvMemberReg);
//            llFirstrow.addView(cvMemberInfo);
//            llFirstrow.addView(cvMyFamily);
//            llFirstrow.addView(cvDownloadForm);
//            llSecondrow.addView(cvSendMsg);
//            llSecondrow.addView(cvBirthDay);
//            llSecondrow.addView(cvNews);
//            llSecondrow.addView(cvAddBusiness);
//       }


    }

    private void init(View view) {

        mPager = (ViewPager) view.findViewById(R.id.pager);
        // Auto start of viewpager
        handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMENArray.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 2000);
            }
        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);

        handler.post(Update);


        mPager.setAdapter(new MyAdapter(getActivity(), XMENArray, new OnImagePressedListener() {
            @Override
            public void onImagePressed(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    handler.removeCallbacksAndMessages(null);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                    handler.post(Update);


                }


            }
        }));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (handler != null) {
            handler.removeCallbacks(null);
        }


    }

    GetBanner showBanner(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetBanner.class);
    }

    interface GetBanner {

        @POST("banner/bannerlistapi/")
        Call<MyBanner> getBanner();

    }
}
