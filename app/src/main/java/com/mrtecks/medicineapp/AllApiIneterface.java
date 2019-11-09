package com.mrtecks.medicineapp;

import com.mrtecks.medicineapp.cartPOJO.cartBean;
import com.mrtecks.medicineapp.checkPromoPOJO.checkPromoBean;
import com.mrtecks.medicineapp.checkoutPOJO.checkoutBean;
import com.mrtecks.medicineapp.homePOJO.homeBean;
import com.mrtecks.medicineapp.loginBean;
import com.mrtecks.medicineapp.orderDetailsPOJO.orderDetailsBean;
import com.mrtecks.medicineapp.ordersPOJO.ordersBean;
import com.mrtecks.medicineapp.productsPOJO.productsBean;
import com.mrtecks.medicineapp.searchPOJO.searchBean;
import com.mrtecks.medicineapp.seingleProductPOJO.singleProductBean;
import com.mrtecks.medicineapp.subCat1POJO.subCat1Bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {



    @GET("medicine/api/getHome.php")
    Call<homeBean> getHome();

    @Multipart
    @POST("medicine/api/getSubCat1.php")
    Call<subCat1Bean> getSubCat1(
            @Part("cat") String cat
    );

    @Multipart
    @POST("medicine/api/getSubCat2.php")
    Call<subCat1Bean> getSubCat2(
            @Part("subcat1") String cat
    );

    @Multipart
    @POST("medicine/api/getProducts.php")
    Call<productsBean> getProducts(
            @Part("subcat2") String cat
    );

    @Multipart
    @POST("medicine/api/getProductById.php")
    Call<singleProductBean> getProductById(
            @Part("id") String cat
    );

    @Multipart
    @POST("medicine/api/search.php")
    Call<searchBean> search(
            @Part("query") String query
    );

    @Multipart
    @POST("medicine/api/login.php")
    Call<loginBean> login(
            @Part("phone") String phone,
            @Part("token") String token
    );

    @Multipart
    @POST("medicine/api/verify.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("medicine/api/addCart.php")
    Call<singleProductBean> addCart(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price,
            @Part("version") String version
    );

    @Multipart
    @POST("medicine/api/updateCart.php")
    Call<singleProductBean> updateCart(
            @Part("id") String id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price
    );

    @Multipart
    @POST("medicine/api/deleteCart.php")
    Call<singleProductBean> deleteCart(
            @Part("id") String id
    );

    @Multipart
    @POST("medicine/api/getRew.php")
    Call<String> getRew(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("medicine/api/clearCart.php")
    Call<singleProductBean> clearCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("medicine/api/getOrderDetails.php")
    Call<orderDetailsBean> getOrderDetails(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("medicine/api/getCart.php")
    Call<cartBean> getCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("medicine/api/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("medicine/api/checkPromo.php")
    Call<checkPromoBean> checkPromo(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("medicine/api/buyVouchers.php")
    Call<checkoutBean> buyVouchers(
            @Part("user_id") String user_id,
            @Part("amount") String amount,
            @Part("txn") String txn,
            @Part("name") String name,
            @Part("address") String address,
            @Part("pay_mode") String pay_mode,
            @Part("slot") String slot,
            @Part("date") String date,
            @Part("promo") String promo
    );
}
