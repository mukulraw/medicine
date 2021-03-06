package com.mrtecks.medicineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.medicineapp.addressPOJO.Datum;
import com.mrtecks.medicineapp.addressPOJO.addressBean;
import com.mrtecks.medicineapp.checkoutPOJO.checkoutBean;
import com.mrtecks.medicineapp.seingleProductPOJO.singleProductBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Checkout2 extends AppCompatActivity {

    Toolbar toolbar;
    EditText name, address, area, city, pin;
    Button proceed;
    ProgressBar progress;
    RadioGroup group;
    String oid;
    String dd = "";
    List<String> ts;
    Spinner addr;
    List<String> list;
    String pid;
    List<Datum> adlist;
    String paymode;
    String isnew = "1";
    ImageView image;
    Button upload;
    File f1;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout2);

        ts = new ArrayList<>();
        list = new ArrayList<>();
        adlist = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar4);
        name = findViewById(R.id.editText2);
        address = findViewById(R.id.editText3);
        proceed = findViewById(R.id.button6);
        progress = findViewById(R.id.progressBar4);
        addr = findViewById(R.id.spinner2);
        image = findViewById(R.id.textView37);
        upload = findViewById(R.id.button3);

        group = findViewById(R.id.radioButton);
        area = findViewById(R.id.editText5);
        city = findViewById(R.id.editText6);
        pin = findViewById(R.id.editText7);





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
        toolbar.setTitle("Upload Prescription");









        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


        Call<addressBean> call = cr.getAddress(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<addressBean>() {
            @Override
            public void onResponse(Call<addressBean> call, Response<addressBean> response) {

                list.clear();
                adlist.clear();

                list.add("New Address");

                if (response.body().getData().size() > 0) {

                    adlist.addAll(response.body().getData());

                    for (int i = 0 ; i < response.body().getData().size() ; i++)
                    {
                        list.add(response.body().getData().get(i).getName());
                    }


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Checkout2.this,
                        android.R.layout.simple_list_item_1, list);

                addr.setAdapter(adapter);



                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<addressBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        addr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0)
                {


                    isnew = "0";
                    Datum item = adlist.get(position - 1);
                    name.setText(item.getName());
                    address.setText(item.getHouse());
                    area.setText(item.getArea());
                    city.setText(item.getCity());
                    pin.setText(item.getPin());


                }
                else
                {
                    isnew = "1";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Checkout2.this, "This is for demo purpose only", Toast.LENGTH_SHORT).show();

                /*String n = name.getText().toString();
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

                                            MultipartBody.Part body = null;

                                            try {

                                                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                                                body = MultipartBody.Part.createFormData("pres", f1.getName(), reqFile1);


                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(b.baseurl)
                                                        .addConverterFactory(ScalarsConverterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();

                                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                                Call<checkoutBean> call = cr.uploadPres(
                                                        SharePreferenceUtils.getInstance().getString("userId"),
                                                        body,
                                                        oid,
                                                        n,
                                                        adr,
                                                        paymode,
                                                        "",
                                                        dd,
                                                        pid,
                                                        a,
                                                        ar,
                                                        c,
                                                        p,
                                                        isnew
                                                );
                                                call.enqueue(new Callback<checkoutBean>() {
                                                    @Override
                                                    public void onResponse(Call<checkoutBean> call, Response<checkoutBean> response) {

                                                        Toast.makeText(Checkout2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                        progress.setVisibility(View.GONE);

                                                        Dialog dialog = new Dialog(Checkout2.this, R.style.DialogCustomTheme);
                                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        dialog.setCancelable(true);
                                                        dialog.setContentView(R.layout.success_popup2);
                                                        dialog.show();

                                                        TextView oi = dialog.findViewById(R.id.textView57);
                                                        TextView au = dialog.findViewById(R.id.textView58);

                                                        oi.setText(oid);
                                                        //au.setText("₹ " + gtotal);

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


                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }


                                        } else {


                                                   *//* Intent intent = new Intent(Checkout.2this, WebViewActivity.class);
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

                                                    startActivityForResult(intent, 12);*//*


                                        }

                                    } else {
                                        Toast.makeText(Checkout2.this, "Please select a Payment Mode", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(Checkout2.this, "Please select a valid PIN Code", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(Checkout2.this, "Please select a valid City", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(Checkout2.this, "Please select a valid Locality/ Area/ District", Toast.LENGTH_SHORT).show();
                        }





                    } else {
                        Toast.makeText(Checkout2.this, "Please enter a valid House/ Apartment No.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Checkout2.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Checkout2.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f1 = new File(file);
                            try {
                                f1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(Checkout2.this), BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                            dialog.dismiss();
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                            dialog.dismiss();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri" , String.valueOf(uri));

            String ypath = getPath(Checkout2.this , uri);
            assert ypath != null;
            f1 = new File(ypath);

            image.setImageURI(uri);

            /*Log.d("path" , ypath);

            MultipartBody.Part body = null;

            try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("pres", f1.getName(), reqFile1);


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

                Call<singleProductBean> call = cr.uploadPres(SharePreferenceUtils.getInstance().getString("userId") , body , versionName);

                call.enqueue(new Callback<singleProductBean>() {
                    @Override
                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e1) {
                e1.printStackTrace();
            }*/





        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Log.d("uri" , String.valueOf(uri));

            image.setImageURI(uri);
            /*try {

                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("pres", f1.getName(), reqFile1);

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

                Call<singleProductBean> call = cr.uploadPres(SharePreferenceUtils.getInstance().getString("userId") , body , versionName);

                call.enqueue(new Callback<singleProductBean>() {
                    @Override
                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                        Toast.makeText(MainActivity.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });

            } catch (Exception e1) {
                e1.printStackTrace();
            }*/



        }


    }

    private static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }

}
