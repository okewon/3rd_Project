package kr.hnu.ock.messaging_app;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class Sub_Fragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private LinearLayout first_Container, fragment_Container;
    private static TextView major, name;
    private Intent intent;
    private Member loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_fragment);

        intent = getIntent();
        loginUser = (Member) intent.getSerializableExtra("loginUser");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        major = (TextView) header.findViewById(R.id.textViewNavMajor);
        name = (TextView) header.findViewById(R.id.textViewNavName);

        LoginUser_Info(loginUser);

        first_Container = findViewById(R.id.first_container);
        fragment_Container = findViewById(R.id.fragment_container);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_send) {
            getSupportActionBar().setTitle("메세지 작성");
            first_Container.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragment_container, new Send_Message_Fragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_read) {
            getSupportActionBar().setTitle("받은 메세지 함");
            first_Container.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragment_container, new Message_Box_Fragment());
            fragmentTransaction.commit();
        }else if(id == R.id.nav_send_mail){
            getSupportActionBar().setTitle("보낸 메세지 함");
            first_Container.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragment_container, new Sent_Message_Fragment());
            fragmentTransaction.commit();
        }else if (id == R.id.nav_setting) {
            getSupportActionBar().setTitle("개인 정보 설정");
            first_Container.setVisibility(View.GONE);
            first_Container.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragment_container, new Member_Infoset());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_exit) {
            getSupportActionBar().setTitle("로그아웃");
            // 로그아웃
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void LoginUser_Info(Member member){
        major.setText(member.getMajor());
        name.setText(member.getName());
    }
}
