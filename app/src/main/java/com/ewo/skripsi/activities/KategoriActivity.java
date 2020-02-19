package com.ewo.skripsi.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.ewo.skripsi.R;
import com.ewo.skripsi.adapter.UsahaAdapter;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.objek.PengusahaModel;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class KategoriActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<PengusahaModel> movies;
    private List<PengusahaModel> faskes = new ArrayList<>();
    UsahaAdapter adapter;
    String kategori = "", data_kategori = "";
    Service api;
    String TAG = "MainActivity - ";
    //    Double latitude,longitude;
    Context context;
    Toolbar dataztoolbar;
    ImageView ic_livesearch;
    GifImageView loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
    }
}
