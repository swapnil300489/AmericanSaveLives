package com.example.myapplication.userView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Config.GPSTracker;
import com.example.myapplication.Config.KEY;
import com.example.myapplication.Config.Utility;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.APIClient;
import com.example.myapplication.Retrofit.APIInterface;
import com.example.myapplication.UserLogin_Activity;
import com.example.myapplication.pojo.ShowTaxi;
import com.example.myapplication.pojo.TokenSave;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private CardView card_source, card_destination;
    private TextView id_pickLocation_txt, id_dropLocation_txt, id_minus_txt, id_add_txt;
    private static final int SOURCE_REQUEST_CODE = 111;
    private static final int DROP_REQUEST_CODE = 222;
    private static final String GOOGLE_PLACES_API_KEY = "AIzaSyBc_2_IFihiG7pNUJzC0fJsBab_36fvKmg";
    private LatLng srcLatLong, dropLatLong, defaultLatLong;
    private ImageView id_dateTime_txt, id_message_txt, id_iNeed_txt;
    private TextView id_count_edTxt;
    private int no_of_people = 0;
    private AlertDialog alertDialog;
    private CharSequence[] values = {"My Location","Places","Point On Map"};
    private String[] catArray = {"Accept Credit Card","Air Conditioner","Non-Smoking", "Smoking Allow", "Extra Luggage Space", "Child Seat", "Disable Passenger"};
    private List<String> catArrayList;
    private String mapSelctionMode = "", location = "", userId = "";
    private TextView id_showCab_txt;
    private APIInterface apiInterface;
    private Utility utility;
    private ProgressDialog progressBar;
    private GPSTracker gpsTracker;
    private Double latitude;
    private Double longitude;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private String bookingDate = "", bookingFLAG = "", source = "", drop = "", dragMarker = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

        card_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                location = "source";

                selectChoice(location);

            }
        });

        card_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                location = "drop";

                selectChoice(location);

            }
        });

        id_add_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookingFLAG = "1"; // Schedule Booking


            }
        });

        id_message_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                utility.setLoginPreferences(KEY.USER_LOGIN, false);

                                Intent intent = new Intent(MapsActivity.this, UserLogin_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });

                alertDialog = builder.create();
                alertDialog.show();


            }
        });

        id_iNeed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                needDialog();
            }
        });

        id_minus_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                no_of_people = no_of_people - 1;

                if (no_of_people < 0){
                    no_of_people = 0;
                    id_count_edTxt.setText(no_of_people+"");
                }else {
                    id_count_edTxt.setText(no_of_people+"");
                }

            }
        });

        id_add_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_of_people = no_of_people + 1;

                if (no_of_people < 0){
                    no_of_people = 0;
                    id_count_edTxt.setText(no_of_people+"");
                }else {
                    id_count_edTxt.setText(no_of_people+"");
                }
            }
        });

        id_showCab_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (no_of_people == 0){
                    id_minus_txt.requestFocus();
                    id_minus_txt.setError("Please set no. of People");
                }else if(dropLatLong == null){

                    Toast.makeText(getApplicationContext(), "Please add drop location.", Toast.LENGTH_LONG).show();

                } else {
                    showTaxi(userId, srcLatLong);
                }

            }
        });
    }

    private void showTaxi(String userId, LatLng srcLatLong) {
        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        Call<ShowTaxi> showTaxiCall = apiInterface.SHOW_TAXI_CALL(srcLatLong.latitude+"", srcLatLong.longitude+"", userId);

        showTaxiCall.enqueue(new Callback<ShowTaxi>() {
            @Override
            public void onResponse(Call<ShowTaxi> call, Response<ShowTaxi> response) {

                progressBar.dismiss();

                ShowTaxi showTaxi = response.body();
                List<ShowTaxi.Detail> detailList;

                if (showTaxi.getMessageCode() == 1){

                    detailList = showTaxi.getDetails();

                    if (detailList.size() != 0){

                        for (int i = 0; i<detailList.size() ; i++){

                            drawTaxi(detailList.get(i).getLatitude(), detailList.get(i).getLongitude(), detailList.get(i).getDname(), detailList.get(i).getId());

                        }
                        
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(detailList.get(0).getLatitude()),Double.parseDouble( detailList.get(0).getLongitude()))).zoom(12).tilt(30).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }else {
                        Toast.makeText(getApplicationContext(), "Cab Not Found In This Location", Toast.LENGTH_LONG).show();
                    }

                }else {

                    Toast.makeText(getApplicationContext(), "Cab Not Found In This Location", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<ShowTaxi> call, Throwable t) {
                progressBar.dismiss();

                Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void drawTaxi(String latitude, String longitude, String dname, String id) {


        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(id);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_mini_top));
        mMap.addMarker(markerOptions);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               // Log.e("MarkerID ",marker.getTitle());

                source = id_pickLocation_txt.getText().toString().trim();
                drop   = id_dropLocation_txt.getText().toString().trim();


                bookingDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(Calendar.getInstance().getTime());

                Log.e("bookingDate", bookingDate);
                Log.e("drvierID___", marker.getTitle());

                Intent intent = new Intent(MapsActivity.this, DriverLocation_Activity.class);
                intent.putExtra("userID", userId);
                intent.putExtra("drvierID",marker.getTitle());

                intent.putExtra("source",source);
                intent.putExtra("source_lat",srcLatLong.latitude+"");
                intent.putExtra("source_longi",srcLatLong.longitude+"");

                intent.putExtra("drop",drop);
                intent.putExtra("drop_lat",dropLatLong.latitude+"");
                intent.putExtra("drop_longi",dropLatLong.longitude+"");

                intent.putExtra("bookingDate",bookingDate);
                intent.putExtra("no_of_people", no_of_people+"");

                intent.putExtra("catArrayList", (Serializable) catArrayList);

                intent.putExtra("screen","MAP");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });

    }

    private void needDialog() {

        if(catArrayList.size() != 0){
            catArrayList.clear();

            id_showCab_txt.setVisibility(View.GONE);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("I Need")
                .setMultiChoiceItems(catArray, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {

                                Log.i("Dialogos", "Item :" + catArray[item]+" isChecked : "+isChecked);

                                if (isChecked){
                                    addCategory(catArray[item], item);
                                }else {
                                    removeCategory(catArray[item], item);
                                }

                            }
                        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

               showList();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void showList() {

        id_showCab_txt.setVisibility( View.VISIBLE);

    }

    private void removeCategory(String s, int item) {
        catArrayList.remove(s);
    }

    private void addCategory(String s, int item) {

        catArrayList.add(s);
    }

    private void selectChoice(String location) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {


                mapSelctionMode = values[item].toString();

                Toast.makeText(MapsActivity.this, mapSelctionMode, Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                locationSelection(location, mapSelctionMode);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void locationSelection(String location, String mapSelctionMode) {

        switch (location){

            case "source":

                switch (mapSelctionMode){

                    case "My Location":

                        mMap.addMarker(new MarkerOptions().position(srcLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)).draggable(true));

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(srcLatLong).zoom(12).tilt(30).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        setLocation(location, srcLatLong);

                        break;

                    case "Places":
                        sourceAddress();
                        break;

                    case "Point On Map":
                        markerDrag(location);
                        break;


                }

                break;

            case "drop":

                switch (mapSelctionMode){

                    case "My Location":


                       if (dropLatLong == null){
                           dropLatLong = defaultLatLong;

                           if (dropLatLong != srcLatLong){
                               mMap.addMarker(new MarkerOptions().position(dropLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)).draggable(true));

                               CameraPosition cameraPosition = new CameraPosition.Builder().target(dropLatLong).zoom(12).tilt(30).build();
                               mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                               setLocation(location, dropLatLong);
                           }else {

                               AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                               builder.setTitle("Warning !!!");
                               builder.setMessage("Both Location are same, \nplease select another drop loction.")
                                       .setCancelable(false)
                                       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) {
                                               dialog.cancel();

                                           }
                                       });

                               alertDialog = builder.create();
                               alertDialog.show();
                           }


                       }else {
                           if (dropLatLong != srcLatLong){
                               mMap.addMarker(new MarkerOptions().position(dropLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)).draggable(true));

                               CameraPosition cameraPosition = new CameraPosition.Builder().target(dropLatLong).zoom(12).tilt(30).build();
                               mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                               setLocation(location, dropLatLong);
                           }else {

                               AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                               builder.setTitle("Warning !!!");
                               builder.setMessage("Both Location are same, \nplease select another drop loction.")
                                       .setCancelable(false)
                                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) {
                                               dialog.cancel();

                                           }
                                       })
                                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int id) {

                                               dialog.cancel();

                                           }
                                       });

                               alertDialog = builder.create();
                               alertDialog.show();
                           }

                       }



                        break;

                    case "Places":
                        dropAddress();
                        break;

                    case "Point On Map":
                        markerDrag(location);
                        break;


                }
                break;
        }

    }

    private void markerDrag(String location) {

        switch (location){

            case "source":

                dragMarker = location;
                mMap.addMarker(new MarkerOptions().position(srcLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)).draggable(true));


                break;

            case "drop":

                dragMarker = location;

                mMap.addMarker(new MarkerOptions().position(defaultLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_drop)).draggable(true));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(defaultLatLong).zoom(12).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                break;
        }



    }

    private void dropAddress() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(MapsActivity.this);
        startActivityForResult(intent, DROP_REQUEST_CODE);
    }

    private void sourceAddress() {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(MapsActivity.this);
        startActivityForResult(intent, SOURCE_REQUEST_CODE);
    }

    private void init() {
        Places.initialize(getApplicationContext(), GOOGLE_PLACES_API_KEY);

        gpsTracker = new GPSTracker(getApplicationContext());


        if (!gpsTracker.canGetLocation()) {
            showSettingsAlert();
        } else {
            latitude  = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e("LatitudeLongitude", "Latitude:" + gpsTracker.getLatitude() + ", Longitude:" + gpsTracker.getLongitude());

        }

        card_source      = findViewById(R.id.card_source);
        card_destination = findViewById(R.id.card_destination);

        id_pickLocation_txt = findViewById(R.id.id_pickLocation);
        id_dropLocation_txt = findViewById(R.id.id_dropLocation);

        id_dateTime_txt = findViewById(R.id.id_dateTime_txt);
        id_message_txt  = findViewById(R.id.id_message_txt);
        id_iNeed_txt    = findViewById(R.id.id_iNeed_txt);

        id_minus_txt   =  findViewById(R.id.id_minus_txt);
        id_add_txt     =  findViewById(R.id.id_add_txt);
        id_count_edTxt =  findViewById(R.id.id_count_edTxt);

        id_showCab_txt = findViewById(R.id.id_showCab_txt);
        id_showCab_txt.setVisibility(View.GONE);

        catArrayList = new ArrayList<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        utility      = new Utility(getApplicationContext());
        progressBar = new ProgressDialog(this);
        userId = utility.getPreferences(KEY.USER_ID);

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        firebase_registrationtoken();
    }

    private void firebase_registrationtoken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();

                        Log.e("FirebaseToken", token);

                       // Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();

                        sendRegistrationToServer(userId, "Android", token);


                    }
                });

    }

    private void sendRegistrationToServer(String userId, String deviceType, String token) {

        Call<TokenSave> tokenSaveCall = apiInterface.TOKEN_SAVE_CALL(userId, deviceType, token);

        tokenSaveCall.enqueue(new Callback<TokenSave>() {
            @Override
            public void onResponse(Call<TokenSave> call, Response<TokenSave> response) {
                TokenSave tokenSave = response.body();

                if (tokenSave.getMessageCode() == 1){
                    Log.e("TokenSave",tokenSave.getMessage());
                }else {
                    Log.e("TokenSave",tokenSave.getMessage());
                }

            }

            @Override
            public void onFailure(Call<TokenSave> call, Throwable t) {
                Log.e("TokenSave",t.getMessage());
            }
        });


    }


    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SOURCE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.e("SOURCE", "Place: " + place.getName() + ", " + place.getLatLng());
                id_pickLocation_txt.setText(place.getName());

                srcLatLong = place.getLatLng();

                mMap.addMarker(new MarkerOptions().position(srcLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)));


                CameraPosition cameraPosition = new CameraPosition.Builder().target(srcLatLong).zoom(12).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("SOURCE", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

                Log.e("SOURCE", "RESULT_CANCELED");
            }
        }else if(requestCode == DROP_REQUEST_CODE){

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.e("DROP", "Place: " + place.getName() + ", " + place.getLatLng());
                id_dropLocation_txt.setText(place.getName());

                dropLatLong = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(dropLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_drop)));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(srcLatLong).zoom(12).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("DROP", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

                Log.e("DROP", "RESULT_CANCELED");
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        if (gpsTracker.getLatitude() == 0.0){

        }else {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            srcLatLong = latLng;
        }



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


     mMap.setOnMarkerDragListener(this);

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();


                srcLatLong = new LatLng(lat, longi);

                defaultLatLong = srcLatLong;
                CameraPosition cameraPosition = new CameraPosition.Builder().target(srcLatLong).zoom(12).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            srcLatLong.latitude, srcLatLong.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());

                        String S1 = "", S2 = "", S3 = "", S4 = "";

                        S1 = address.getAddressLine(0);

                        String[] strArray = S1.split(" ");

                        for (int i = 0; i < strArray.length; i++) {
                            if (i <= 2) {
                                S2 = S2 + " " + strArray[i];
                            } else {
                                S3 = S3 + " " + strArray[i];
                            }
                        }


                        id_pickLocation_txt.setText(S2+"\n"+S3);


                        Log.e("Current_Address==>", S2+"\n"+S3);
                    }
                } catch (IOException e) {
                    Log.e("LocationException", "Unable connect to Geocoder", e);
                }



                Log.e("Your Location: " , "\n" + "Latitude: " + lat + "\n" + "Longitude: " + lat);
            } else {
                //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();

                //srcLatLong = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                defaultLatLong = srcLatLong;
                CameraPosition cameraPosition = new CameraPosition.Builder().target(srcLatLong).zoom(12).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            srcLatLong.latitude, srcLatLong.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());

                        String S1 = "", S2 = "", S3 = "", S4 = "";

                        S1 = address.getAddressLine(0);

                        String[] strArray = S1.split(" ");

                        for (int i = 0; i < strArray.length; i++) {
                            if (i <= 2) {
                                S2 = S2 + " " + strArray[i];
                            } else {
                                S3 = S3 + " " + strArray[i];
                            }
                        }


                        id_pickLocation_txt.setText(S2+"\n"+S3);


                        Log.e("Current_Address==>", S2+"\n"+S3);
                    }
                } catch (IOException e) {
                    Log.e("LocationException", "Unable connect to Geocoder", e);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Eixt");
        builder.setMessage("Are you sure want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

       // mMap.addMarker(new MarkerOptions().position(marker.getPosition()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_drop)));

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

        switch (dragMarker){

            case "source":

                srcLatLong = marker.getPosition();
              //  mMap.addMarker(new MarkerOptions().position(srcLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)));

               setLocation(dragMarker, srcLatLong);



                break;

            case "drop":

                dropLatLong = marker.getPosition();
            //    mMap.addMarker(new MarkerOptions().position(srcLatLong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_drop)));

                setLocation(dragMarker, dropLatLong);
                break;


        }



    }

    private void setLocation(String dragMarker, LatLng Latlong) {

      //  mMap.addMarker(new MarkerOptions().position(Latlong).icon(BitmapDescriptorFactory.fromResource(R.mipmap.apex_source)));


        CameraPosition cameraPosition = new CameraPosition.Builder().target(Latlong).zoom(12).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    Latlong.latitude, Latlong.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());

                String S1 = "", S2 = "", S3 = "", S4 = "";

                S1 = address.getAddressLine(0);

                String[] strArray = S1.split(" ");

                for (int i = 0; i < strArray.length; i++) {
                    if (i <= 2) {
                        S2 = S2 + " " + strArray[i];
                    } else {
                        S3 = S3 + " " + strArray[i];
                    }
                }


                switch (dragMarker){

                    case "source":
                        id_pickLocation_txt.setText(S2+"\n"+S3);
                        break;

                    case "drop":
                        id_dropLocation_txt.setText(S2+"\n"+S3);
                        break;

                }






            }
        } catch (IOException e) {
            Log.e("LocationException", "Unable connect to Geocoder", e);
        }
    }
}
