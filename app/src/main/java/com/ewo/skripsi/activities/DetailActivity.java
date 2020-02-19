package com.ewo.skripsi.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.dd.processbutton.FlatButton;
import com.ewo.skripsi.R;
import com.ewo.skripsi.adapter.DetailAdapter;
import com.ewo.skripsi.adapter.PengusahaAdapter;
import com.ewo.skripsi.api.Client;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.objek.CarouselModel;
import com.ewo.skripsi.objek.PengusahaModel;
import com.ewo.skripsi.utilities.GPSTracker;
import com.ewo.skripsi.utilities.SharedPrefManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    TextView dt_nama_usaha, dt_kategori, dt_alamat, dt_jarak, dt_deskripsi;
    FlatButton btn_map, btn_wa;
    Toolbar toolbar;
    SliderLayout sliderLayout;

    private List<CarouselModel> carousels= new ArrayList<>();

    RecyclerView recyclerView;
    List<PengusahaModel> movies;
    DetailAdapter adapter;
    Service api;
    private List<PengusahaModel> barang_list;

    GPSTracker gpsTracker;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton btnPost, btnLogout;

    String id_usaha;

    int REQ_CODE = 101;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        sharedPrefManager = new SharedPrefManager(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gpsTracker = new GPSTracker(this);

        dt_nama_usaha = findViewById(R.id.dt_nama_usaha);
        dt_kategori = findViewById(R.id.dt_kategori);
        dt_alamat = findViewById(R.id.dt_alamat);
        dt_jarak = findViewById(R.id.dt_jarak);
        dt_deskripsi = findViewById(R.id.dt_deskripsi);
        btnPost = findViewById(R.id.btnPost);
        btnLogout = findViewById(R.id.btnLogout);


        btn_map = findViewById(R.id.btn_map);
        btn_wa = findViewById(R.id.btn_wa);

        if (getIntent().hasExtra("detail")){
            id_usaha = getIntent().getStringExtra("id_usaha");
            fetch_detail(getIntent().getStringExtra("id_usaha"));
        }else{
            fetch_detail(sharedPrefManager.getSPID());
            id_usaha = sharedPrefManager.getSPID();
            btnPost.setVisibility(View.VISIBLE);
            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, PostActivity.class);
                    startActivityForResult(intent,REQ_CODE);
                }
            });
            btnLogout.setVisibility(View.VISIBLE);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN,false);
                    finish();
                }
            });
        }


        recyclerView = findViewById(R.id.recyclerview_list_data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        api = Client.getClient();

        fetchData();
        initimageSlider();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE){
            recreate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fetch_detail(String id_usaha){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> login = serviceApi.detail_pengusaha(id_usaha,gpsTracker.getLatitude(), gpsTracker.getLongitude());
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try{
                    JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        dt_nama_usaha.setText(jsonRESULTS.getString("nama_usaha"));
                        getSupportActionBar().setTitle(dt_nama_usaha.getText().toString());
                        dt_kategori.setText(jsonRESULTS.getString("kategori"));
                        dt_alamat.setText(jsonRESULTS.getString("alamat"));
                        dt_jarak.setText(jsonRESULTS.getString("jarak"));
                        Spanned sp = Html.fromHtml(jsonRESULTS.getString("deskripsi"));
                        dt_deskripsi.setText(sp);

                        final String lat = jsonRESULTS.getString("lat");
                        final String lng = jsonRESULTS.getString("lng");
                        final String no_wa = jsonRESULTS.getString("no_wa");

                        btn_map.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uri = "http://maps.google.com/maps?&daddr=" +lat+","+lng;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent);
                            }
                        });


                        btn_wa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Date date = new Date();
                                SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("HHmm", Locale.getDefault());
                                int currentDate = Integer.valueOf(dateFormatWithZone.format(date));
                                String jam;

                                if (currentDate >= 0501 && currentDate <= 1000){
                                    jam = "Selamat Pagi";
                                }else if(currentDate >= 1001 && currentDate <= 1500){
                                    jam = "Selamat Siang";
                                }else if(currentDate >= 1501 && currentDate <= 1800){
                                    jam = "Selamat Sore";
                                }else if(currentDate >= 1801 && currentDate <= 1900){
                                    jam = "Selamat Petang";
                                }else{
                                    jam = "Selamat Malam";
                                }

                                String salam = jam+", dengan "+dt_nama_usaha.getText()+" ?";
                                try{
                                    PackageManager packageManager = DetailActivity.this.getPackageManager();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    String url = "https://api.whatsapp.com/send?phone="+no_wa+"&text=" + URLEncoder.encode(salam, "UTF-8");
                                    i.setPackage("com.whatsapp");
                                    i.setData(Uri.parse(url));
                                    if (i.resolveActivity(packageManager) != null) {
                                        startActivity(i);
                                    }else {
                                        Toasty.error(DetailActivity.this, "Smartphone Anda Tidak Terinstall Aplikasi Whatsapp").show();
                                    }
                                } catch(Exception e) {
                                    Toasty.error(DetailActivity.this, e.getMessage()).show();
                                }
                            }
                        });

                }catch (Exception e){
                    Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(DetailActivity.this, t.getMessage()).show();
            }
        });
    }

    public void fetchData(){

        api = Client.getClient();

        Call<List<PengusahaModel>> call = api.getPost(id_usaha);
        call.enqueue(new Callback<List<PengusahaModel>>() {
            @Override
            public void onResponse(Call<List<PengusahaModel>> call, Response<List<PengusahaModel>> response) {

                barang_list = response.body();
                adapter = new DetailAdapter(barang_list, DetailActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PengusahaModel>> call, Throwable t) {

//                Toast.makeText(DetailActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    void initimageSlider(){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadFile = serviceAPI.getFoto(id_usaha);
        loadFile.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<List<CarouselModel>>() {}.getType();
                carousels = getTeamListFromJson(faskesString, listType);

                sliderLayout = findViewById(R.id.imageslider);

                HashMap<String,String> url_maps = new HashMap<String, String>();

                for(int i=0; i<carousels.size(); i++){
                    String path = getString(R.string.pathfoto)+carousels.get(i).getNamaFile();
                    url_maps.put(carousels.get(i).getId(),path);
                }

                for(String name : url_maps.keySet()){
                    DefaultSliderView textSliderView = new DefaultSliderView(DetailActivity.this);
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                            .setOnSliderClickListener(DetailActivity.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    sliderLayout.addSlider(textSliderView);
                }

                // you can change animasi, time page and anythink.. read more on github
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.setDuration(4000);
                sliderLayout.startAutoCycle();
                sliderLayout.stopAutoCycle();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }


    @Override
    public void onStop() {
//        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
