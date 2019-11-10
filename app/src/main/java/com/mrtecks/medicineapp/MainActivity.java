package com.mrtecks.medicineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.mrtecks.medicineapp.cartPOJO.cartBean;
import com.mrtecks.medicineapp.homePOJO.Banners;
import com.mrtecks.medicineapp.homePOJO.Best;
import com.mrtecks.medicineapp.homePOJO.Cat;
import com.mrtecks.medicineapp.homePOJO.Member;
import com.mrtecks.medicineapp.homePOJO.homeBean;
import com.mrtecks.medicineapp.seingleProductPOJO.singleProductBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.santalu.autoviewpager.AutoViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    AutoViewPager pager;
    RecyclerView categories;

    ProgressBar progress;


    CategoryAdapter adapter6;
    List<Best> list;
    List<Banners> list1;
    List<Member> list2;
    List<Cat> list3;
    DrawerLayout drawer;

    TextView login , logout , cart , orders , count , terms , about;

    CircleIndicator indicator;

    ImageButton cart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        pager = findViewById(R.id.viewPager);


        categories = findViewById(R.id.categories);
        progress = findViewById(R.id.progress);
        login = findViewById(R.id.textView3);
        logout = findViewById(R.id.logout);
        cart = findViewById(R.id.cart);
        orders = findViewById(R.id.orders);
        indicator = findViewById(R.id.indicator);

        cart1 = findViewById(R.id.imageButton3);
        count = findViewById(R.id.count);

        terms = findViewById(R.id.terms);
        about = findViewById(R.id.about);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        adapter6 = new CategoryAdapter(this , list3);

        LinearLayoutManager manager1 = new LinearLayoutManager(this , RecyclerView.HORIZONTAL , false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this , RecyclerView.HORIZONTAL , false);
        LinearLayoutManager manager3 = new LinearLayoutManager(this , RecyclerView.VERTICAL , false);
        LinearLayoutManager manager4 = new LinearLayoutManager(this , RecyclerView.HORIZONTAL , false);
        LinearLayoutManager manager5 = new LinearLayoutManager(this , RecyclerView.VERTICAL , false);



        categories.setAdapter(adapter6);
        categories.setLayoutManager(manager5);


       /* terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , Web.class);
                intent.putExtra("title" , "Terms & Conditions");
                intent.putExtra("url" , "https://mrtecks.com/medicine/terms.php");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });*/

       /* about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , Web.class);
                intent.putExtra("title" , "About Us");
                intent.putExtra("url" , "https://mrtecks.com/medicine/about.php");
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);

            }
        });*/


       /* category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this , Category.class);
                startActivity(intent);

            }
        });*/

        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this , Search.class);
                startActivity(intent);

            }
        });*/

        final String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0)
        {
            login.setText(SharePreferenceUtils.getInstance().getString("phone"));
        }
        else
        {

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (uid.length() == 0)
                {
                    Intent intent = new Intent(MainActivity.this , Login.class);
                    startActivity(intent);
                }


            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharePreferenceUtils.getInstance().deletePref();

                Intent intent = new Intent(MainActivity.this , Splash.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0)
                {
                    Intent intent = new Intent(MainActivity.this , Cart.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0)
                {
                    Intent intent = new Intent(MainActivity.this , Cart.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uid.length() > 0)
                {
                    Intent intent = new Intent(MainActivity.this , Orders.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                }

                drawer.closeDrawer(GravityCompat.START);

            }
        });





        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.setDisplay(Display.NOTIFICATION);
        appUpdater.start();

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

        Call<homeBean> call = cr.getHome();
        call.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1"))
                {


                    BannerAdapter adapter1 = new BannerAdapter(getSupportFragmentManager() , response.body().getPbanner());
                    pager.setAdapter(adapter1);
                    indicator.setViewPager(pager);


                    adapter6.setData(response.body().getCat());

                }

                progress.setVisibility(View.GONE);

                Log.d("asdasd" , response.body().getMessage());

            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.d("asdasd" , t.toString());
            }
        });
        loadCart();


    }

    void loadCart()
    {
        String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0)
        {
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

            Call<cartBean> call2 = cr.getCart(SharePreferenceUtils.getInstance().getString("userId"));
            call2.enqueue(new Callback<cartBean>() {
                @Override
                public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                    if (response.body().getData().size() > 0) {


                        count.setText(String.valueOf(response.body().getData().size()));


                    } else {

                        count.setText("0");

                    }

                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<cartBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });



        }
        else
        {
            count.setText("0");
        }
    }

    class BannerAdapter extends FragmentStatePagerAdapter
    {

        List<Banners> blist = new ArrayList<>();

        public BannerAdapter(FragmentManager fm , List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position).getImage() , blist.get(position).getCname() , blist.get(position).getCid());
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
        }
    }


    public static class page extends Fragment
    {

        String url , tit , cid = "";

        ImageView image;

        void setData(String url , String tit , String cid)
        {
            this.url = url;
            this.tit = tit;
            this.cid = cid;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout , container , false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url , image , options);


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cid != null)
                    {
                        Intent intent = new Intent(getContext() , SubCat.class);
                        intent.putExtra("id" , cid);
                        intent.putExtra("title" , tit);
                        startActivity(intent);
                    }



                }
            });


            return view;
        }
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder>
    {

        Context context;
        List<Best> list = new ArrayList<>();

        public BestAdapter(Context context , List<Best> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Best> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Best item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage() , holder.image , options);

            float dis = Float.parseFloat(item.getDiscount());

            final String nv1;

            if (item.getStock().equals("In stock"))
            {
                holder.add.setEnabled(true);
            }
            else
            {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());

            if (dis > 0)
            {

                float pri = Float.parseFloat(item.getPrice());
                float dv = (dis / 100 ) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(item.getDiscount() + "% OFF");
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(nv) + " </b></font><strike>\u20B9 " + item.getPrice() + "</strike>"));
            }
            else
            {

                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9 " + String.valueOf(item.getPrice()) + " </b></font>"));
            }


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*Intent intent = new Intent(context , SingleProduct.class);
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("title" , item.getName());
                    context.startActivity(intent);*/

                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0)
                    {

                        final Dialog dialog = new Dialog(context);
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
                                Log.d("pid" , item.getId());
                                Log.d("quantity" , String.valueOf(stepperTouch.getCount()));
                                Log.d("price" , nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId") , item.getId() , String.valueOf(stepperTouch.getCount()), nv1 , versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1"))
                                        {
                                            //loadCart();
                                            dialog.dismiss();
                                            loadCart();
                                        }

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context , Login.class);
                        context.startActivity(intent);

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ImageView image;
            TextView price , title , discount , stock;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView63);



            }
        }
    }






    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
    {

        Context context;
        List<Cat> list = new ArrayList<>();

        public CategoryAdapter(Context context , List<Cat> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Cat> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Cat item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage() , holder.image , options);


            holder.title.setText(item.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context , SubCat.class);
                    intent.putExtra("id" , item.getId());
                    intent.putExtra("title" , item.getName());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView5);

                title = itemView.findViewById(R.id.textView18);



            }
        }
    }






}
