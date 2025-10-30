package com.example.product.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.product.R;
import com.example.product.fragment.PostListFragment;

public class PostListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Posts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_post, new PostListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
