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


public class UsahaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<PengusahaModel> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

//    SharedPrefManager sharedPrefManager;

    public UsahaAdapter(Context context, List<PengusahaModel> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new DatazHolder(inflater.inflate(R.layout.recyclerview_item_data,parent,false));

        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((DatazHolder)holder).bindData(movies.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!movies.get(position).getIdUsaha().equalsIgnoreCase("")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class DatazHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_usaha, tv_jarak;
        ImageView iv_foto;
        String id_usaha, alamat, no_wa, deskripsi, kategori, lat, lng;

        public DatazHolder(final View view){
            super(view);
            tv_nama_usaha = view.findViewById(R.id.tv_nama_usaha);
            tv_jarak = view.findViewById(R.id.tv_jarak);
            iv_foto = view.findViewById(R.id.iv_foto);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("id_usaha",id_usaha);
                        intent.putExtra("nama_usaha",tv_nama_usaha.getText());
                        intent.putExtra("jarak",tv_jarak.getText());
                        intent.putExtra("id_usaha",id_usaha);
                        intent.putExtra("alamat",alamat);
                        intent.putExtra("no_wa",no_wa);
                        intent.putExtra("deskripsi",deskripsi);
                        intent.putExtra("kategori",kategori);
                        intent.putExtra("lat",lat);
                        intent.putExtra("lng",lng);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }

        void bindData(PengusahaModel dataz){

            tv_nama_usaha.setText(dataz.getNamaUsaha());
            tv_jarak.setText(dataz.getJarak());

            String url = itemView.getResources().getString(R.string.pathfoto)+dataz.getFoto();
            Glide.with(itemView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(R.drawable.loading_elips).centerCrop())
                    .into(iv_foto);

            id_usaha = dataz.getIdUsaha();
            alamat = dataz.getAlamat();
            no_wa = dataz.getNoWa();
            deskripsi = dataz.getDeskripsi();
            kategori = dataz.getKategori();
            lat = dataz.getLat();
            lng = dataz.getLng();
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}
