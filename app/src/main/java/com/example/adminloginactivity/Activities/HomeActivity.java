package com.example.adminloginactivity.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adminloginactivity.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1 ,btn2;
    TextView firstName,email;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference profileUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth= FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid=currentUser.getUid();
            profileUserRef = FirebaseDatabase.getInstance().getReference().child("admin").child(uid);            //User is Logged in
        }else{
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }



        btn1=findViewById(R.id.button_profile);
        btn2=findViewById(R.id.button_classfellow);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        navigationView=findViewById(R.id.nav);
        View headerView =navigationView.getHeaderView(0);
        firstName = (TextView)headerView.findViewById(R.id.Textview_header_name);
        email = (TextView)headerView.findViewById(R.id.Textview_header_email);

        drawerLayout=findViewById(R.id.drawar);

       toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
       drawerLayout.addDrawerListener(toggle);
       toggle.syncState();


       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
           {
               switch (menuItem.getItemId()){
                   case R.id.home_scr:
                       startActivity(new Intent(HomeActivity.this, AnnouncementsActivity.class));
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case R.id.permission_scr:
                       startActivity(new Intent(HomeActivity.this, ApprovalActivity.class));
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case R.id.reject_scr:
                       startActivity(new Intent(HomeActivity.this, RejectedStudentsActivity.class));
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case R.id.classfellow_scr:
                       startActivity(new Intent(HomeActivity.this, StudentActivity.class));
                       drawerLayout.closeDrawer(GravityCompat.START);
                       break;
                   case R.id.logout_scr:
                       SharedPreferences preferences = getSharedPreferences("admin", Context.MODE_PRIVATE);
                       SharedPreferences.Editor editor = preferences.edit();
                       editor.putString("isadminLogin","adminLogout");
                       editor.commit();
                       auth.getInstance().signOut();
                       Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                       startActivity(intent);
                       break;
               }

               return true;
           }
       });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_profile){
            startActivity(new Intent(HomeActivity.this,AnnouncementsActivity.class));
        }
        else if (id == R.id.button_classfellow){
            startActivity(new Intent(HomeActivity.this, StudentActivity.class));
        }
    }

    }

