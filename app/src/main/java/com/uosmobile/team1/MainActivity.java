package com.uosmobile.team1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.uosmobile.team1.DB.DBHelper;
import com.uosmobile.team1.support.PermissionSupport;

public class MainActivity extends AppCompatActivity {

    //프래그먼트
    Fragment fragment0, fragment1;
    
    //권한
    PermissionSupport permission;
    
    //바텀 네비게이션
    BottomNavigationView bottomNavigationView;

    //DB
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //프래그먼트 생성
        fragment0 = new Fragment0();
        fragment1 = new Fragment1();

        //DB함수 실행
        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 0, 1);
        dbHelper.insertContentsInformation("개구리 왕자", 18);
        
        //바텀 네비게이션
        bottomNavigationView = findViewById(R.id.mainBottomNavigationView);

        //상단탭 리스너 설정
        TabLayout tabs = findViewById(R.id.BookList);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    selected = fragment0;
                } else if (position == 1){
                    selected = fragment1;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {            }
        });

        //바텀 네비게이션 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.goToContents:
                        break;
                    case R.id.goToStamps:
                        break;
                }
                return true;
            }
        });

        //권한 체크
        permissionCheck();

        //텍스트 프래그먼트 우선 배치
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment0).commit();
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT>=23){
            permission = new PermissionSupport(this, this);
            if(!permission.checkPermission()){
                permission.requestPermisssion();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermisssion();
        }
    }
}