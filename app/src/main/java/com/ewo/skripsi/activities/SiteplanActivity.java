package com.ewo.skripsi.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ewo.skripsi.R;
import com.github.chrisbanes.photoview.PhotoView;

public class SiteplanActivity extends AppCompatActivity {
    PhotoView iv_siteplan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siteplan);

        iv_siteplan = findViewById(R.id.iv_siteplan);

    }
}
