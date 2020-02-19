package com.ewo.skripsi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.ewo.skripsi.R;
import com.ewo.skripsi.api.Client;
import com.ewo.skripsi.api.Service;
import com.ewo.skripsi.utilities.SharedPrefManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    String TAG = "LoginActivity -/ ";
    String login_code;

    SharedPrefManager sharedPrefManager;

    FlatButton btnLogin;

    MaterialEditText username, password;
    MaterialDialog materialDialog;
    TextView btnDaftar;

    ImageView imageTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getSPSudahLogin()){
            Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
            startActivity(intent);
            finish();

        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnDaftar = findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UsulanActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") | password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Username/ Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    materialDialog = new MaterialDialog.Builder(LoginActivity.this)
                            .content("Sedang Memuat...")
                            .progress(true, 0)
                            .cancelable(false)
                            .progressIndeterminateStyle(true)
                            .show();
                    login();
                }

            }
        });


        imageTest = findViewById(R.id.imageTest);

    }

    private void login(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> login = serviceApi.login(username.getText().toString(), password.getText().toString());
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                materialDialog.dismiss();

                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        if (jsonRESULTS.getString("status").equals("berhasil")){
                            
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, jsonRESULTS.getString("id_usaha"));
                            Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toasty.error(LoginActivity.this, t.getMessage());
            }
        });
    }

}
