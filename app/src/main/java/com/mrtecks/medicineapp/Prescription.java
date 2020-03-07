package com.mrtecks.medicineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.medicineapp.ordersPOJO.Datum;
import com.mrtecks.medicineapp.ordersPOJO.ordersBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Prescription extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progress;
    RecyclerView grid;
    OrdersAdapter adapter;
    List<Datum> list;
    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar3);
        progress = findViewById(R.id.progressBar3);
        grid = findViewById(R.id.grid);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("My Orders");

        adapter = new OrdersAdapter(list, this);

        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

    }

    void loadCart() {
        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);



        Call<ordersBean> call = cr.getPres(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<ordersBean>() {
            @Override
            public void onResponse(Call<ordersBean> call, Response<ordersBean> response) {

                if (response.body().getData().size() > 0) {


                    adapter.setgrid(response.body().getData());

                } else {
                    adapter.setgrid(response.body().getData());
                    finish();
                    Toast.makeText(Prescription.this, "No order found", Toast.LENGTH_SHORT).show();

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ordersBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        OrdersAdapter(List<Datum> list, Context context) {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.order_list_item3, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);


            if (item.getType().equals("pres"))
            {
                viewHolder.txn.setText("#" + item.getTxn() + " (PRES)");
            }
            else
            {
                viewHolder.txn.setText("#" + item.getTxn());
            }

            viewHolder.date.setText(item.getCreated());
            viewHolder.status.setText(item.getStatus());
            viewHolder.name.setText(item.getName());
            viewHolder.address.setText(item.getAddress());
            viewHolder.pay.setText(item.getPay_mode());
            viewHolder.slot.setText(item.getSlot());
            viewHolder.amount.setText("\u20B9 " + item.getAmount());

            viewHolder.deldate.setText(item.getDelivery_date());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , OrderDetails.class);
                    intent.putExtra("oid" , item.getId());
                    startActivity(intent);

                }
            });


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getPrescription() , viewHolder.pres , options);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txn , date , status , name , address , amount , pay , slot , deldate;
            ImageView pres;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txn = itemView.findViewById(R.id.textView27);
                date = itemView.findViewById(R.id.textView28);
                status = itemView.findViewById(R.id.textView35);
                name = itemView.findViewById(R.id.textView32);
                address = itemView.findViewById(R.id.textView34);
                amount = itemView.findViewById(R.id.textView30);
                pay = itemView.findViewById(R.id.textView40);
                slot = itemView.findViewById(R.id.textView62);
                deldate = itemView.findViewById(R.id.textView42);
                pres = itemView.findViewById(R.id.imageView8);


            }
        }
    }

}
