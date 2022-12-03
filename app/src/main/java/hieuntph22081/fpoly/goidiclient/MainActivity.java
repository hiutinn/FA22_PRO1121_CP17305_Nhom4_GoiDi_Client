package hieuntph22081.fpoly.goidiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import hieuntph22081.fpoly.goidiclient.adapter.MainViewPager2Adapter;
import hieuntph22081.fpoly.goidiclient.model.User;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView bottomNav;
    public static ViewPager2 mViewPager2;
    MainViewPager2Adapter adapter;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null)
            userId = bundle.getString("userId");
        getToken();

        bottomNav = findViewById(R.id.bottomNav);
        mViewPager2 = findViewById(R.id.viewPage2);
        adapter = new MainViewPager2Adapter(MainActivity.this);
        mViewPager2.setAdapter(adapter);
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;
                    case 3:
                        bottomNav.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                }
            }
        });
        bottomNav.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                mViewPager2.setCurrentItem(0);
                break;

            case R.id.nav_order:
                mViewPager2.setCurrentItem(1);
                break;

            case R.id.nav_feedback:
                mViewPager2.setCurrentItem(2);
                break;
            case R.id.nav_profile:
                mViewPager2.setCurrentItem(3);
                break;
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        // Log and toast
                        Log.e("TAG", token);
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId+"/token");
                        databaseRef.setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}