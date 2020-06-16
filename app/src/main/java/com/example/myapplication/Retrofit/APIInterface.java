package com.example.myapplication.Retrofit;

import com.example.myapplication.pojo.BookingCab;
import com.example.myapplication.pojo.DriverDetails;
import com.example.myapplication.pojo.Login;
import com.example.myapplication.pojo.Register;
import com.example.myapplication.pojo.ShowTaxi;
import com.example.myapplication.pojo.TokenSave;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @POST("register")
    @FormUrlEncoded
    Call<Register> REGISTER_CALL (@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("mobile_no") String mobile_no,
                                  @Field("password") String password,
                                  @Field("user_type") String user_type);


    @POST("login")
    @FormUrlEncoded
    Call<Login> LOGIN_CALL (@Field("email") String email,
                            @Field("password") String password,
                            @Field("user_type") String user_type);

    @POST("show_taxi")
    @FormUrlEncoded
    Call<ShowTaxi> SHOW_TAXI_CALL (@Field("source_lat") String source_lat,
                                   @Field("source_long") String source_long,
                                   @Field("users_id") String user_type);

    @POST("get_single_driver")
    @FormUrlEncoded
    Call<DriverDetails> DRIVER_DETAILS_CALL (@Field("users_id") String user_type);



    /*@POST("add_booking")
    @FormUrlEncoded
    Call<BookingCab> BOOKING_CAB_CALL (@Field("DriverID") String DriverID,
                                @Field("UserID") String UserID,
                                @Field("source") String source,
                                @Field("source_lat") String source_lat,
                                @Field("source_longi") String source_longi,
                                @Field("drop") String drop,
                                @Field("drop_lat") String drop_lat,
                                @Field("drop_longi") String drop_longi,
                                @Field("bookingDate") String bookingDate,
                                @Field("no_of_people") String no_of_people,
                                @Field("catArrayList") ArrayList<String> catArrayList);*/

    @Multipart
    @POST("add_booking")
    Call<BookingCab> BOOKING_CAB_CALL(
            @Part("DriverID") RequestBody DriverID,
            @Part("UserID")RequestBody  UserID,
            @Part("source")RequestBody  source,
            @Part("source_lat") RequestBody source_lat,
            @Part("source_longi")RequestBody  source_longi,
            @Part("drop")RequestBody  drop,
            @Part("drop_lat")RequestBody  drop_lat,
            @Part("drop_longi")RequestBody  drop_longi,
            @Part("bookingDate")RequestBody  bookingDate,
            @Part("no_of_people")RequestBody  no_of_people,
            @Part("catArrayList")RequestBody  catArrayList,
            @Part("descr")RequestBody  descr,
            @Part MultipartBody.Part  img1,
            @Part MultipartBody.Part  img2,
            @Part MultipartBody.Part  img3,
            @Part MultipartBody.Part  img4
    );


    @POST("saveToken")
    @FormUrlEncoded
    Call<TokenSave> TOKEN_SAVE_CALL(@Field("userId") String userId,
                                    @Field("DeviceType") String DeviceType,
                                    @Field("DeviceToken") String DeviceToken);
}
