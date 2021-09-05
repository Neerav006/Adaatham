package com.adaatham.suthar.view;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.adaatham.suthar.R;
import com.adaatham.suthar.Utils.Utils;
import com.adaatham.suthar.common.RetrofitClient;
import com.adaatham.suthar.cons.Constants;
import com.adaatham.suthar.fragment.AgentDashBoardFragment;
import com.adaatham.suthar.fragment.UserDashboardFragment;
import com.adaatham.suthar.model.MyRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 520;
    private final String root = "50e423ff4b2b6fd7d61055c4a80bb55d0b6fdbe8fa2a4ad6459087e729d2a11c";

    private String id;
    private String mem_id;
    private boolean isLogin;
    private String name;
    private String user_name;
    private String role;
    private boolean isReg;
    private NavigationView navigationView;
    private CounterTotal counterTotal;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        checkPermission();

        counterTotal = getCountAPI(Constants.BASE_URL);

        SharedPreferences sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, HomeActivity.this);
        id = sharedPreferences.getString(Constants.ID, null);
        mem_id = sharedPreferences.getString(Constants.MEMBER_ID, null);
        user_name = sharedPreferences.getString(Constants.USER_NAME, null);
        name = sharedPreferences.getString(Constants.NAME, null);
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false);
        role = sharedPreferences.getString(Constants.ROLE, null);
        isReg = sharedPreferences.getBoolean(Constants.IS_REGISTERED, false);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (isReg) {
            navigationView.getMenu().findItem(R.id.nav_edit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_edit).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(false);
        }

        if (role != null) {

            if (role.equals("2")) {
                replaceFragment(new AgentDashBoardFragment());
            } else if (role.equals("3")) {
                replaceFragment(new UserDashboardFragment());
            }

        }


        View mHeaderView = navigationView.getHeaderView(0);

        TextView textView = navigationView.findViewById(R.id.tvAboutDeveloper);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, AboutDevActivity.class));

            }
        });


        if (mHeaderView != null) {
            TextView tvName = (TextView) mHeaderView.findViewById(R.id.tvUserName);
            tvName.setText(name);

        }


        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getFragmentManager().getBackStackEntryCount() > 0) {

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                    }


                } else {
                    //show hamburger

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                drawer.openDrawer(GravityCompat.START);
                            }
                        });
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        if (isLogin) {
            getMenuInflater().inflate(R.menu.home, menu);

            final View notificaitons = menu.findItem(R.id.notification).getActionView();

            final TextView counterTv = (TextView) notificaitons.findViewById(R.id.txtCount);
            counterTv.setVisibility(View.GONE);

            counterTotal.getCount(mem_id).enqueue(new Callback<MyRes>() {
                @Override
                public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                    if (response.isSuccessful()) {

                        count = response.body().getCount();

                        if (count > 0) {
                            counterTv.setVisibility(View.VISIBLE);
                            counterTv.setText(String.valueOf(count));
                        } else {
                            counterTv.setVisibility(View.GONE);
                        }
                    }

                }

                @Override
                public void onFailure(Call<MyRes> call, Throwable t) {

                }
            });


            //dummy test
            notificaitons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterTv.setVisibility(View.GONE);

                    Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                    startActivity(intent);


                }
            });


            return true;
        }

        return false;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
            // clear preference and navigate to login screen
            // clear state

            SharedPreferences.Editor editor = Utils.writeToPreference(Constants.MY_PREF, HomeActivity.this);
            editor.putBoolean(Constants.IS_LOGIN, false);

            editor.putString(Constants.USER_NAME, null);
            editor.putString(Constants.NAME, null);
            editor.putString(Constants.ID, null);
            editor.putString(Constants.ROLE, null);
            editor.putBoolean(Constants.IS_REGISTERED, false);
            editor.putString(Constants.MEMBER_ID, null);
            editor.putString(Constants.MEMBER_NO, null);

            editor.apply();


            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        } else if (id == R.id.action_changePwd) {

            Intent intent = new Intent(HomeActivity.this, ChangePwdActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
            if (role != null) {

                if (role.equals("2")) {
                    replaceFragment(new AgentDashBoardFragment());
                } else if (role.equals("3")) {
                    replaceFragment(new UserDashboardFragment());
                }

            }

        } else if (id == R.id.nav_edit) {
            Intent intent = new Intent(HomeActivity.this, EditDetailActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(HomeActivity.this, EditImageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment toReplace) {

        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {

            getFragmentManager().popBackStack();
        }

        if (toReplace.getClass().getSimpleName().equalsIgnoreCase(AgentDashBoardFragment.class.getSimpleName())

                || toReplace.getClass().getSimpleName().equalsIgnoreCase(UserDashboardFragment.class.getSimpleName())
                ) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_home, toReplace, toReplace.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container_home, toReplace, toReplace.getClass().getSimpleName())
                    .addToBackStack(toReplace.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }


    }

    CounterTotal getCountAPI(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(CounterTotal.class);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReg) {
            navigationView.getMenu().findItem(R.id.nav_edit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(true);
        }


    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(HomeActivity.this, "Storage permission required", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        } else {


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();

                    Toast.makeText(getApplicationContext(), "Storage permission required", Toast.LENGTH_LONG).show();

                    finish();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    interface CounterTotal {
        @POST("notification/countnotificationapi/")
        @FormUrlEncoded
        Call<MyRes> getCount(@Field("m_id") String m_id);
    }


}
