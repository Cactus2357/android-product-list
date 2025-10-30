package com.example.product.activity;

import android.Manifest;
import android.app.ComponentCaller;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.product.R;
import com.example.product.databinding.ActivityMainBinding;
import com.example.product.fragment.ProductListFragment;
import com.example.product.service.MyBoundService;
import com.example.product.service.MyUnboundService;

import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MyBoundService boundService;
    private boolean isBound = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBoundService.LocalBinder binder = (MyBoundService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;
//            Toast.makeText(MainActivity.this, "Bound to service", Toast.LENGTH_SHORT).show();
            System.out.println("MainActivity bound to service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new ProductListFragment())
                    .commit();
        }

        findViewById(R.id.btnOpenProductList).setOnClickListener(v ->
                startActivity(new Intent(this, ProductListActivity.class)));

        findViewById(R.id.btnOpenPostList).setOnClickListener(v ->
                startActivity(new Intent(this, PostListActivity.class)));

        requestPostNotificationsPermission();
        bindService();
    }

    private void bindService() {
        binding.btnBoundService.setText("Show Time (BoundService)");
        binding.btnBoundService.setOnClickListener(v -> {
            if (isBound && boundService != null) {
                String timestamp = boundService.getCurrentTimestamp();
                binding.message.setText("Current Time: " + timestamp);
            } else {
                Toast.makeText(this, "Service not bound yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBoundService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.user_profile) {
            startActivity(new Intent(this, LoginActivity.class));
//        } else if (id == R.id.product_list) {
//            startActivity(new Intent(this, ProductListActivity.class));

        } else if (id == R.id.test_permissions) {
            requestPermission();

        } else if (id == R.id.test_notification) {
            Intent intent = new Intent(this, MyUnboundService.class);
            intent.putExtra("msg", "This is a notification sent from Unbound service");
            startService(intent);
            Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.google_map) {
            startActivity(new Intent(this, MapsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(@NonNull Intent intent, @NonNull ComponentCaller caller) {
        super.onNewIntent(intent, caller);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        if (intent.hasExtra("msg")) {
            String msg = intent.getStringExtra("msg");
            binding.message.setText(msg);
        }
    }

    private void requestPermission() {
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Explain to user why permission is needed
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Request GPS Permission")
                        .setMessage("This app needs location permission to access GPS features.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();

            } else {
                // Directly request if no explanation needed
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            Toast.makeText(
                    this,
                    permissionGranted ? "Location Permission granted" : "Location Permission denied",
                    Toast.LENGTH_SHORT
            ).show();
        } else if (requestCode == 100) {
            boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(
                    this,
                    permissionGranted ? "Notification Permission granted" : "Notification Permission denied",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void requestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        }
    }
}