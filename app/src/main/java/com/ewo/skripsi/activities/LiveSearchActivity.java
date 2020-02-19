package com.ewo.skripsi.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ewo.skripsi.R;
import com.ewo.skripsi.adapter.SearchAdapter;
import com.ewo.skripsi.api.Client;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.objek.PengusahaModel;
import com.ewo.skripsi.utilities.GPSTracker;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class LiveSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Service api;

    private List<PengusahaModel> barang_list;
    private SearchAdapter searchAdapter;


    Call<List<PengusahaModel>> call;

    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gpsTracker = new GPSTracker(LiveSearchActivity.this);


        recyclerView = findViewById(R.id.recyclerview_livesearch);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void fetchContact(String key){

        api = Client.getClient();

        call = api.search_usaha(key, gpsTracker.getLatitude(), gpsTracker.getLongitude());
        call.enqueue(new Callback<List<PengusahaModel>>() {
            @Override
            public void onResponse(Call<List<PengusahaModel>> call, Response<List<PengusahaModel>> response) {

                barang_list = response.body();
                searchAdapter = new SearchAdapter(barang_list, LiveSearchActivity.this);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PengusahaModel>> call, Throwable t) {

                Toast.makeText(LiveSearchActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.slideDown(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_atas, menu);
        MenuItem cari = menu.findItem(R.id.btnSearch);
        cari.setVisible(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        search.expandActionView();
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                View view = LiveSearchActivity.this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(LiveSearchActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                Bungee.slideDown(LiveSearchActivity.this);
                return true;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchContact(newText);
                return false;
            }
        });


        return true;
    }

}
