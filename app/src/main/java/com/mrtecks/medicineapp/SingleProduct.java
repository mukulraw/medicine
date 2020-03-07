package com.mrtecks.medicineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.medicineapp.seingleProductPOJO.Data;
import com.mrtecks.medicineapp.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.concurrent.TimeUnit;

import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleProduct extends AppCompatActivity {

    Toolbar toolbar;
    ImageView image;
    TextView discount , title , price;
    Button add;
    TextView brand , size , sdescription , seller;
    TextView description , highlights , key_ingredients , key_benefits , direction , safety , stock;
    ProgressBar progress;

    String id , name;

    RatingBar rating;
    TextView rate;

    ImageButton fav;
    boolean isFav = false;
    String pid , nv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.image);
        discount = findViewById(R.id.discount);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        add = findViewById(R.id.add);
        rating = findViewById(R.id.rating);
        rate = findViewById(R.id.rate);
        brand = findViewById(R.id.brand);
        size = findViewById(R.id.unit);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);
        sdescription = findViewById(R.id.sdescription);
        highlights = findViewById(R.id.highlights);
        key_ingredients = findViewById(R.id.key_ingredients);
        direction = findViewById(R.id.direction);
        key_benefits = findViewById(R.id.key_benefits);
        safety = findViewById(R.id.safety);
        progress = findViewById(R.id.progress);
        stock = findViewById(R.id.stock);
        fav = findViewById(R.id.fav);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pid.length() > 0)
                {
                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0)
                    {

                        final Dialog dialog = new Dialog(SingleProduct.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch  = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);



                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

                                Bean b = (Bean) getApplicationContext();


                                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                logging.level(HttpLoggingInterceptor.Level.HEADERS);
                                logging.level(HttpLoggingInterceptor.Level.BODY);

                                OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.baseurl)
                                        .client(client)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                Log.d("userid" , SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid" , pid);
                                Log.d("quantity" , String.valueOf(stepperTouch.getCount()));
                                Log.d("price" , nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId") , pid , String.valueOf(stepperTouch.getCount()), nv1 , versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1"))
                                        {
                                            //loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(SingleProduct.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingleProduct.this , Login.class);
                        startActivity(intent);

                    }
                }



            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(SingleProduct.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.rating_dialog);
                dialog.show();

                RatingBar ratt = dialog.findViewById(R.id.ratingBar);
                Button submit = dialog.findViewById(R.id.button7);
                ProgressBar bar = dialog.findViewById(R.id.progressBar5);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        float ra = ratt.getRating();

                        bar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();


                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.level(HttpLoggingInterceptor.Level.HEADERS);
                        logging.level(HttpLoggingInterceptor.Level.BODY);

                        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .client(client)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        Call<singleProductBean> call = cr.addRating(SharePreferenceUtils.getInstance().getString("userId") , pid , String.valueOf(ra));

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                bar.setVisibility(View.GONE);

                                onResume();

                                dialog.dismiss();

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }
                });


            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pid.length() > 0)
                {
                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0)
                    {

                        progress.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();


                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.level(HttpLoggingInterceptor.Level.HEADERS);
                        logging.level(HttpLoggingInterceptor.Level.BODY);

                        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .client(client)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                        int versionCode = BuildConfig.VERSION_CODE;
                        String versionName = BuildConfig.VERSION_NAME;

                        Call<singleProductBean> call = cr.addFav(SharePreferenceUtils.getInstance().getString("userId") , pid);

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                                onResume();

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(SingleProduct.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingleProduct.this , Login.class);
                        startActivity(intent);

                    }
                }



            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<singleProductBean> call = cr.getProductById(id , SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<singleProductBean>() {
            @Override
            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {


                if (response.body().getStatus().equals("1"))
                {
                    Data item = response.body().getData();

                    pid = item.getId();

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getImage() , image , options);

                    float dis = Float.parseFloat(item.getDiscount());

                    if (dis > 0)
                    {

                        float pri = Float.parseFloat(item.getPrice());
                        float dv = (dis / 100 ) * pri;

                        float nv = pri - dv;

                        nv1 = String.valueOf(nv);

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(item.getDiscount() + "% OFF");
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
                    }
                    else
                    {

                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
                    }


                    title.setText(item.getName());

                    brand.setText(item.getBrand());
                    size.setText(item.getSize());
                    sdescription.setText(item.getSizeDescription());
                    seller.setText(item.getSeller());

                    description.setText(Html.fromHtml(item.getDescription()));
                    highlights.setText(Html.fromHtml(item.getKeyFeatures()));
                    key_ingredients.setText(Html.fromHtml(item.getKeyIngredients()));
                    key_benefits.setText(Html.fromHtml(item.getKeyBenefits()));
                    direction.setText(Html.fromHtml(item.getDirectionForUse()));
                    safety.setText(Html.fromHtml(item.getSafetyInformation()));

                    if (item.getRated().equals("0"))
                    {
                        rate.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        rate.setVisibility(View.GONE);
                    }

                    if (item.getRating() != null)
                    {
                        float rr = Float.parseFloat(item.getRating());
                        float cc = Float.parseFloat(item.getRated());

                        if (cc > 0)
                        {
                            rating.setRating(rr / cc);
                        }
                        else
                        {
                            rating.setRating(0);
                        }
                    }
                    else
                    {
                        rating.setRating(0);
                    }



                    if (item.getFavourite().equals("0"))
                    {
                        isFav = false;
                        fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                    }
                    else
                    {
                        isFav = true;
                        fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_red));
                    }

                    Log.d("fav" , item.getFavourite());

                    if (item.getStock().equals("In stock"))
                    {
                        add.setEnabled(true);
                    }
                    else
                    {
                        add.setEnabled(false);
                    }

                    stock.setText(item.getStock());



                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<singleProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }
}
