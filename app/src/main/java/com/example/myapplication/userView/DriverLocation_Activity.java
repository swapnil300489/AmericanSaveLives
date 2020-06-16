package com.example.myapplication.userView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.Retrofit.APIClient;
import com.example.myapplication.Retrofit.APIInterface;
import com.example.myapplication.pojo.BookingCab;
import com.example.myapplication.pojo.DriverDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLocation_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private CircleImageView id_driverProfile;
    private TextView id_driverName, id_Rate, id_Review, id_CarCompany, id_Licence;
    private APIInterface apiInterface;
    private String driverID = "", userID = "";
    private LinearLayout id_reviewLL;
    private ProgressDialog progressBar;
    private double latitude, longitude;
    private MapView mapView;
    private GoogleMap mMap;
    private Bundle mapViewBundle = null;
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyBc_2_IFihiG7pNUJzC0fJsBab_36fvKmg";
    private ImageView id_back;
    private Button id_bookButton;
    private String source, source_lat, source_longi, drop, drop_lat, drop_longi, bookingDate, no_of_people;
    private ArrayList<String> catArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_location);



        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(GOOGLE_PLACES_API_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        init();
        getDriverDetails(driverID);

        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        id_bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DriverLocation_Activity.this, PrescriptionActivity.class);

                intent.putExtra("userID", userID);
                intent.putExtra("drvierID",driverID);

                intent.putExtra("source",source);
                intent.putExtra("source_lat",source_lat);
                intent.putExtra("source_longi",source_longi);

                intent.putExtra("drop",drop);
                intent.putExtra("drop_lat",drop_lat);
                intent.putExtra("drop_longi",drop_longi);

                intent.putExtra("bookingDate",bookingDate);
                intent.putExtra("no_of_people", no_of_people);

                intent.putExtra("catArrayList", (Serializable) catArrayList);


                startActivity(intent);







                /*bookCab(driverID, userID,
                        source, source_lat, source_longi,
                        drop, drop_lat, drop_longi,
                        bookingDate, no_of_people, catArrayList);*/



            }
        });

    }

    /*private void bookCab(String driverID, String userID, String source, String source_lat, String source_longi, String drop, String drop_lat, String drop_longi, String bookingDate, String no_of_people, ArrayList<String> catArrayList) {

        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        Call<BookingCab> bookingCabCall = apiInterface.BOOKING_CAB_CALL(driverID, userID,
                                                                        source, source_lat, source_longi,
                                                                        drop, drop_lat, drop_longi,
                                                                        bookingDate, no_of_people, catArrayList );


        bookingCabCall.enqueue(new Callback<BookingCab>() {
            @Override
            public void onResponse(Call<BookingCab> call, Response<BookingCab> response) {

                progressBar.dismiss();

                BookingCab bookingCab = response.body();

                if (bookingCab.getMessageCode() == 1){
                    Toast.makeText(getApplicationContext(), bookingCab.getMessage(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Something went wrong.", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<BookingCab> call, Throwable t) {

                progressBar.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });


    }
*/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(GOOGLE_PLACES_API_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(GOOGLE_PLACES_API_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void getDriverDetails(String driverID) {

        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        Call<DriverDetails> driverDetailsCall = apiInterface.DRIVER_DETAILS_CALL(driverID);

        driverDetailsCall.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(Call<DriverDetails> call, Response<DriverDetails> response) {
                progressBar.dismiss();

                DriverDetails driverDetails = response.body();

                List<DriverDetails.Detail> details;

                if (driverDetails.getMessageCode() == 1){

                    details = driverDetails.getDetails();

                    Picasso.get().load(details.get(0).getProfileImg()).into(id_driverProfile);

                    id_driverName.setText(details.get(0).getDname());

                    id_Licence.setText(details.get(0).getLicense());

                    id_Rate.setText("$"+details.get(0).getPerHr()+" /hr");

                    id_CarCompany.setText(details.get(0).getAcType()+", "+details.get(0).getWheels()+" Wheeler");

                    if (details.get(0).getReview().equals("0")){
                        id_reviewLL.setVisibility(View.GONE);
                    }else {
                        id_Review.setText(details.get(0).getReview()+" ✰");
                    }

                    latitude = Double.parseDouble(details.get(0).getLatitude());
                    longitude = Double.parseDouble(details.get(0).getLongitude());


                    LatLng latLng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(details.get(0).getDname());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_mini_top));
                    mMap.addMarker(markerOptions);


                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude,longitude)).zoom(10).tilt(30).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else {
                    Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<DriverDetails> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void init() {

        Places.initialize(getApplicationContext(), GOOGLE_PLACES_API_KEY);

        id_driverProfile = findViewById(R.id.id_driverProfile);

        id_driverName = findViewById(R.id.id_driverName);
        id_Rate = findViewById(R.id.id_Rate);
        id_Review = findViewById(R.id.id_Review);
        id_CarCompany = findViewById(R.id.id_CarCompany);
        id_Licence = findViewById(R.id.id_Licence);
        id_back = findViewById(R.id.id_back);
        id_reviewLL = findViewById(R.id.id_reviewLL);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressBar = new ProgressDialog(this);
        id_bookButton = findViewById(R.id.id_bookButton);

        driverID = getIntent().getStringExtra("drvierID");
        userID   = getIntent().getStringExtra("userID");
        Log.e("DriverID ", driverID+"  UserID "+userID);


        source          = getIntent().getStringExtra("source");
        source_lat      = getIntent().getStringExtra("source_lat");
        source_longi    = getIntent().getStringExtra("source_longi");
        drop            = getIntent().getStringExtra("drop");
        drop_lat        = getIntent().getStringExtra("drop_lat");
        drop_longi      = getIntent().getStringExtra("drop_longi");
        bookingDate     = getIntent().getStringExtra("bookingDate");
        no_of_people    = getIntent().getStringExtra("no_of_people");
        catArrayList    = (ArrayList<String>) getIntent().getSerializableExtra("catArrayList");


        Log.e("source ", source);
        Log.e("source_lat ", source_lat);
        Log.e("source_longi ", source_longi);
        Log.e("drop ", drop);
        Log.e("drop_lat ", drop_lat);
        Log.e("drop_longi ", drop_longi);
        Log.e("bookingDate ", bookingDate);
        Log.e("no_of_people ", no_of_people);
        Log.e("catArrayList ", catArrayList+"");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        finish();
    }
}
