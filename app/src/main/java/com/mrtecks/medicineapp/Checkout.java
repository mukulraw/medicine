package com.mrtecks.medicineapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.medicineapp.checkPromoPOJO.checkPromoBean;
import com.mrtecks.medicineapp.checkoutPOJO.checkoutBean;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Checkout extends AppCompatActivity {

    Toolbar toolbar;
    EditText name, address, area, city, pin;
    Button proceed;
    ProgressBar progress;
    String amm , gtotal;
    String tslot = "";
    String paymode;
    RadioGroup group;
    String oid;
    TextView amount , grand;
    String dd = "";
    List<String> ts;

    String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ts = new ArrayList<>();

        amm = getIntent().getStringExtra("amount");

        toolbar = findViewById(R.id.toolbar4);
        name = findViewById(R.id.editText2);
        address = findViewById(R.id.editText3);
        proceed = findViewById(R.id.button6);
        progress = findViewById(R.id.progressBar4);

        group = findViewById(R.id.radioButton);
        area = findViewById(R.id.editText5);
        city = findViewById(R.id.editText6);
        pin = findViewById(R.id.editText7);

        amount = findViewById(R.id.textView49);
        grand = findViewById(R.id.textView51);




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
        toolbar.setTitle("Checkout");



        amount.setText("₹ " + amm);

        float gt = Float.parseFloat(amm) + 40;

        grand.setText("₹ " + gt);

        gtotal = String.valueOf(gt);














        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String a = address.getText().toString();
                String ar = area.getText().toString();
                String c = city.getText().toString();
                String p = pin.getText().toString();

                if (n.length() > 0) {

                    if (a.length() > 0) {


                        if (ar.length() > 0)
                        {

                            if (c.length() > 0)
                            {

                                if (p.length() > 0)
                                {

                                    int iidd = group.getCheckedRadioButtonId();

                                    if (iidd > -1) {

                                        RadioButton cb = group.findViewById(iidd);

                                        paymode = cb.getText().toString();


                                        oid = String.valueOf(System.currentTimeMillis());

                                        if (paymode.equals("Cash on Delivery")) {
                                            progress.setVisibility(View.VISIBLE);

                                            Bean b = (Bean) getApplicationContext();

                                            String adr = a + ", " + ar + ", " + c + ", " + p;

                                            Log.d("addd" , adr);

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.baseurl)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                            Call<checkoutBean> call = cr.buyVouchers(
                                                    SharePreferenceUtils.getInstance().getString("userId"),
                                                    gtotal,
                                                    oid,
                                                    n,
                                                    adr,
                                                    paymode,
                                                    tslot,
                                                    dd,
                                                    pid
                                            );
                                            call.enqueue(new Callback<checkoutBean>() {
                                                @Override
                                                public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                                                    Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                    progress.setVisibility(View.GONE);

                                                    Dialog dialog = new Dialog(Checkout.this, R.style.DialogCustomTheme);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setCancelable(true);
                                                    dialog.setContentView(R.layout.success_popup);
                                                    dialog.show();

                                                    TextView oi = dialog.findViewById(R.id.textView57);
                                                    TextView au = dialog.findViewById(R.id.textView58);

                                                    oi.setText(oid);
                                                    au.setText("₹ " + gtotal);

                                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                        @Override
                                                        public void onCancel(DialogInterface dialog) {

                                                            dialog.dismiss();
                                                            finish();

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onFailure(Call<checkoutBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });
                                        } else {


                                                   /* Intent intent = new Intent(Checkout.this, WebViewActivity.class);
                                                    intent.putExtra(AvenuesParams.ACCESS_CODE, "AVOL70EE77BF91LOFB");
                                                    intent.putExtra(AvenuesParams.MERCHANT_ID, "133862");
                                                    intent.putExtra(AvenuesParams.ORDER_ID, oid);
                                                    intent.putExtra(AvenuesParams.CURRENCY, "INR");
                                                    intent.putExtra(AvenuesParams.AMOUNT, String.valueOf(gtotal));
                                                    //intent.putExtra(AvenuesParams.AMOUNT, "1");
                                                    intent.putExtra("pid", SharePreferenceUtils.getInstance().getString("userid"));

                                                    intent.putExtra(AvenuesParams.REDIRECT_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.CANCEL_URL, "https://mrtecks.com/grocery/api/pay/ccavResponseHandler.php");
                                                    intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://mrtecks.com/grocery/api/pay/GetRSA.php");

                                                    startActivityForResult(intent, 12);*/


                                        }

                                    } else {
                                        Toast.makeText(Checkout.this, "Please select a Payment Mode", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(Checkout.this, "Please select a valid PIN Code", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(Checkout.this, "Please select a valid City", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(Checkout.this, "Please select a valid Locality/ Area/ District", Toast.LENGTH_SHORT).show();
                        }





                    } else {
                        Toast.makeText(Checkout.this, "Please enter a valid House/ Apartment No.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Checkout.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {

            /*progress.setVisibility(View.VISIBLE);

            Bean b = (Bean) getApplicationContext();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(b.baseurl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

            String n = name.getText().toString();
            String a = address.getText().toString();
            String ar = area.getText().toString();
            String c = city.getText().toString();
            String p = pin.getText().toString();

            String adr = a + ", " + ar + ", " + c + ", " + p;

            Log.d("addd" , adr);


            Call<checkoutBean> call = cr.buyVouchers(
                    SharePreferenceUtils.getInstance().getString("userId"),
                    amm,
                    oid,
                    n,
                    adr,
                    "online",
                    tslot,
                    dd,
                    pid
            );
            call.enqueue(new Callback<checkoutBean>() {
                @Override
                public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                    Toast.makeText(Checkout.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    progress.setVisibility(View.GONE);



                    Dialog dialog = new Dialog(Checkout.this, R.style.DialogCustomTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.success_popup);
                    dialog.show();


                    TextView oi = dialog.findViewById(R.id.textView57);
                    TextView au = dialog.findViewById(R.id.textView58);

                    oi.setText(oid);
                    au.setText("₹ " + gtotal);


                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                            dialog.dismiss();
                            finish();

                        }
                    });


                }

                @Override
                public void onFailure(Call<checkoutBean> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                }
            });*/

        }

    }


}
