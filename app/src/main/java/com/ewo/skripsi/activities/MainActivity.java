package com.ewo.skripsi.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ewo.skripsi.R;
import com.ewo.skripsi.adapter.PengusahaAdapter;
import com.ewo.skripsi.adapter.SearchAdapter;
import com.ewo.skripsi.adapter.UsahaAdapter;
import com.ewo.skripsi.api.Client;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.objek.KategoriModel;
import com.ewo.skripsi.objek.PengusahaModel;
import com.ewo.skripsi.utilities.GPSTracker;
import com.ewo.skripsi.utilities.GpsUtils;
import com.ewo.skripsi.utilities.SharedPrefManager;
import com.ewo.skripsi.utilities.VerticalLineDecorator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.inforoeste.mocklocationdetector.MockLocationDetector;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    RecyclerView recyclerView;
    List<PengusahaModel> movies;
    PengusahaAdapter adapter;
    Service api;
    String TAG = "Usaha Activity /- ";
    Context context;
    Toolbar dataztoolbar;
    ImageView ic_livesearch, ic_kategori, ic_usulan, ic_chat;

    String lat="", lng="";

    private boolean isGPS = false;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean mockon;

    SharedPrefManager sharedPrefManager;

    String kategori = "Semua";

    private List<KategoriModel> list_kategori = new ArrayList<>();
    ArrayList<String> array_kategori=new ArrayList<>();
    SpinnerDialog spinnerKategori;

    private List<PengusahaModel> barang_list;



    @Override
    public void onBackPressed() {
        finish();
        Bungee.fade(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        recyclerView = findViewById(R.id.recyclerview_list_data);
        movies = new ArrayList<>();

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
        sharedPrefManager = new SharedPrefManager(MainActivity.this);

        dataztoolbar = findViewById(R.id.dataztoolbar);

        load_kategori();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ic_livesearch = findViewById(R.id.ic_livesearch);
        ic_livesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent livesearch = new Intent(MainActivity.this, LiveSearchActivity.class);
                startActivity(livesearch);
                Bungee.slideUp(MainActivity.this);
            }
        });

//        adapter = new PengusahaAdapter(this, movies);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
        api = Client.getClient();

        check_gps();

        if (isGPS){
            if (mockon){
                Toasty.error(MainActivity.this, "Silahkan Non Aktifkan Fake GPS Terlebih Dahulu").show();
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        load(0);
                        fetchData();
                    }
                }, 3000);
            }
        }else{
            lat = String.valueOf(gpsTracker.getLatitude());
            lng = String.valueOf(gpsTracker.getLongitude());
//            load(0);s
            fetchData();
        }

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                kategori = "Semua";
                barang_list.clear();
//                load(0);
                fetchData();
            }
        });


        ic_kategori = findViewById(R.id.ic_kategori);
        ic_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerKategori.showSpinerDialog();
            }
        });

        ic_usulan = findViewById(R.id.ic_usulan);
        ic_usulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ic_chat = findViewById(R.id.ic_chat);
        ic_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });

    }

    public void fetchData(){

        api = Client.getClient();

        Call<List<PengusahaModel>> call = api.getPengusaha(kategori, lat, lng);
        call.enqueue(new Callback<List<PengusahaModel>>() {
            @Override
            public void onResponse(Call<List<PengusahaModel>> call, Response<List<PengusahaModel>> response) {

                barang_list = response.body();
                adapter = new PengusahaAdapter(barang_list, MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<PengusahaModel>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

//    private void load(int index){
//        Call<List<PengusahaModel>> call = api.getUsaha(index,kategori, lat, lng);
//        call.enqueue(new Callback<List<PengusahaModel>>() {
//            @Override
//            public void onResponse(Call<List<PengusahaModel>> call, Response<List<PengusahaModel>> response) {
//                loading.setVisibility(View.GONE);
//                if(response.isSuccessful()){
//                    List<PengusahaModel> result = response.body();
//                    if(result.size()<=0){
//                        Toasty.error(MainActivity.this,"Maaf, Saat Ini Belum ada Data pada Kategori Tersebut");
//                    }else{
//                        movies.addAll(response.body());
//                        adapter.notifyDataChanged();
//                    }
//                    swipeRefreshLayout.setRefreshing(false);
//                }else{
//                    Log.e(TAG," Response Error "+ String.valueOf(response.code()));
//                    Toasty.error(MainActivity.this,"Maaf, Tidak ada Data pada Kategori ini");
//                }
//
//                adapter.setLoadMoreListener(new UsahaAdapter.OnLoadMoreListener() {
//                    @Override
//                    public void onLoadMore() {
//                        recyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                int index = movies.size() - 1;
//                                loadMore(index);
//                            }
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<List<PengusahaModel>> call, Throwable t) {
//                loading.setVisibility(View.GONE);
//                Toasty.error(MainActivity.this,"Maaf, Tidak ada Data pada Kategori ini");
//            }
//        });
//    }
//
//    private void loadMore(int index){
//        movies.add(new PengusahaModel("load"));
//        adapter.notifyItemInserted(movies.size()-1);
//
//        Call<List<PengusahaModel>> call = api.getUsaha(index,kategori, lat, lng);
//        call.enqueue(new Callback<List<PengusahaModel>>() {
//            @Override
//            public void onResponse(Call<List<PengusahaModel>> call, Response<List<PengusahaModel>> response) {
//                if(response.isSuccessful()){
//                    movies.remove(movies.size()-1);
//
//                    List<PengusahaModel> result = response.body();
//
//                    if(result.size()>1){
//                        movies.remove(movies.size()-1);
//                        movies.addAll(result);
//                    }else{
//                        adapter.setMoreDataAvailable(false);
//                    }
//
//                    adapter.notifyDataChanged();
//
//                }else{
//                    Log.e(TAG," Load More Response Error "+ String.valueOf(response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<PengusahaModel>> call, Throwable t) {
//                Log.e(TAG," Load More Response Error "+t.getMessage());
//            }
//        });
//    }

    public void load_kategori(){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadKategori();

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String kecamatanString = response.body().toString();

                Type listType = new TypeToken<List<KategoriModel>>() {}.getType();
                list_kategori = getTeamListFromJson(kecamatanString, listType);

                array_kategori.add("Semua");
                for (int i=0; i<list_kategori.size(); i++){
                    array_kategori.add(list_kategori.get(i).getKategori());
                }

                spinnerKategori = new SpinnerDialog(MainActivity.this,array_kategori,"Kategori",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation

                spinnerKategori.setCancellable(true); // for cancellable
                spinnerKategori.setShowKeyboard(false);// for open keyboard by default

                spinnerKategori.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        kategori = item;
                        Toasty.info(MainActivity.this, "Menampilkan Kategori "+item).show();
//                        movies.clear();
                        barang_list.clear();
//                        adapter.notifyDataChanged();

//                        load(0);
                        fetchData();
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        createLocationRequest();
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        boolean isMock = MockLocationDetector.isLocationFromMockProvider(MainActivity.this, mCurrentLocation);
        if (isMock) {
            mockon = true;
        } else {
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lng = String.valueOf(mCurrentLocation.getLongitude());
            mockon = false;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void check_gps(){

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        new GpsUtils(MainActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;

            }
        });
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}
