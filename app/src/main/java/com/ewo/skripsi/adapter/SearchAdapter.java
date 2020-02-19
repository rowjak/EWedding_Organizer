package com.ewo.skripsi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ewo.skripsi.R;
import com.ewo.skripsi.activities.DetailActivity;
import com.ewo.skripsi.objek.PengusahaModel;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<PengusahaModel> contacts;
    private Context context;

    public SearchAdapter(List<PengusahaModel> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_search, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.id_usaha = contacts.get(position).getIdUsaha();
        holder.tv_nama_usaha.setText(contacts.get(position).getNamaUsaha());
        holder.tv_alamat.setText(contacts.get(position).getAlamat());
        holder.tv_kategori.setText(contacts.get(position).getKategori());
        holder.no_wa = contacts.get(position).getNoWa();
        holder.deskripsi = contacts.get(position).getDeskripsi();
        holder.lat = contacts.get(position).getLat();
        holder.lng = contacts.get(position).getLng();
        holder.jarak = contacts.get(position).getJarak();

        String url = holder.itemView.getResources().getString(R.string.pathfoto)+contacts.get(position).getFoto();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.loading_elips).centerCrop())
                .into(holder.iv_foto);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_usaha, tv_alamat, tv_kategori;
        ImageView iv_foto;
        String id_usaha, no_wa, deskripsi, lat, lng, jarak;

        public MyViewHolder(View view) {
            super(view);

            tv_nama_usaha = view.findViewById(R.id.tv_nama_usaha);
            tv_alamat= view.findViewById(R.id.tv_alamat);
            tv_kategori = view.findViewById(R.id.tv_kategori);
            iv_foto = view.findViewById(R.id.iv_foto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("id_usaha",id_usaha);
                    intent.putExtra("nama_usaha",tv_nama_usaha.getText());
                    intent.putExtra("jarak",jarak);
                    intent.putExtra("id_usaha",id_usaha);
                    intent.putExtra("alamat",tv_alamat.getText());
                    intent.putExtra("no_wa",no_wa);
                    intent.putExtra("deskripsi",deskripsi);
                    intent.putExtra("kategori",tv_kategori.getText());
                    intent.putExtra("lat",lat);
                    intent.putExtra("lng",lng);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
