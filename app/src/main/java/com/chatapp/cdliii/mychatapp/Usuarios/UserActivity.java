package com.chatapp.cdliii.mychatapp.Usuarios;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.chatapp.cdliii.mychatapp.Inicio.LoginActivity;
import com.chatapp.cdliii.mychatapp.Preferences;
import com.chatapp.cdliii.mychatapp.R;

/**
 * Created by ricky on 06-18-18.
 */

public class UserActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        setTitle("Mensajeria");

        //Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.abrir, R.string.cerrar);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);

        tabLayout = (TabLayout) findViewById(R.id.tabUsuarios);
        viewPager = (ViewPager) findViewById(R.id.viewPagerUsuarios);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new UserAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                }else if(position==1){
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectIterDrawer(MenuItem item){
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()){
            case R.id.salir:
                Preferences.savePreferenceBoolean(UserActivity.this, false, Preferences.PREFERENCE_ESTADO);
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectIterDrawer(item);
                return true;
            }
        });
    }
}
