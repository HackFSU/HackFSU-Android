package com.hackfsu.android.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hackfsu.android.app.R;
import com.hackfsu.android.app.ui.TouchImageView;

public class MapViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TouchImageView mapImage = (TouchImageView) findViewById(R.id.iv_map_image);

        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("map");

        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        mapImage.setImageBitmap(bmp);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
