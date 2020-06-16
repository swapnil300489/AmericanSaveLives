package com.example.myapplication.userView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Config.PermissionUtility;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.APIClient;
import com.example.myapplication.Retrofit.APIInterface;
import com.example.myapplication.pojo.BookingCab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends AppCompatActivity {

    private String source, source_lat, source_longi, drop, drop_lat, drop_longi, bookingDate, no_of_people;
    private ArrayList<String> catArrayList;
    private ImageView id_img1, id_img2, id_img3, id_img4;
    private EditText id_descriptionEd;
    private Uri str_img1, str_img2, str_img3, str_img4;
    private String driverID = "", userID = "";
    private String description;
    private TextView id_Confirm_btn;
    private File file1, file2, file3, file4, f;
    private APIInterface apiInterface;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private String priPath = "NA";
    private String priPath2 = "NA";
    private String priPath3 = "NA";
    private String priPath4 = "NA";
    private ProgressDialog progressBar;

    private static final int EDIT_PROFILE_REQUEST_IMAGE1 = 1;
    private static final int EDIT_PROFILE_REQUEST_IMAGE11 = 11;

    private static final int EDIT_PROFILE_REQUEST_IMAGE2 = 2;
    private static final int EDIT_PROFILE_REQUEST_IMAGE22 = 22;

    private static final int EDIT_PROFILE_REQUEST_IMAGE3 = 3;
    private static final int EDIT_PROFILE_REQUEST_IMAGE33 = 33;

    private static final int EDIT_PROFILE_REQUEST_IMAGE4 = 4;
    private static final int EDIT_PROFILE_REQUEST_IMAGE44 = 44;

    private PermissionUtility putility;
    private ArrayList<String> permission_list;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        runTimePermission();

        init();

        id_Confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = id_descriptionEd.getText().toString();

                if(description.length()!=0){


                    if (str_img1 != null || str_img2 != null ||  str_img3 != null ||  str_img4 != null)
                    {



                        bookCab(driverID, userID,
                                source, source_lat, source_longi,
                                drop, drop_lat, drop_longi,
                                bookingDate, no_of_people, catArrayList,
                                str_img1, str_img2, str_img3, str_img4, description);


                    }else {
                        Log.e("URI_1", "null");
                        Toast.makeText(getApplicationContext(), "Please upload atleast one Prescription", Toast.LENGTH_LONG).show();

                    }

                }else {
                    id_descriptionEd.requestFocus();
                    id_descriptionEd.setError("Enter Description");
                }
            }
        });

        id_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage1();

            }
        });

        id_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage2();

            }
        });

        id_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage3();

            }
        });

        id_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage4();

            }
        });

    }

        private void bookCab(String driverID, String userID, String source, String source_lat, String source_longi,
                         String drop, String drop_lat, String drop_longi, String bookingDate, String no_of_people,
                         ArrayList<String> catArrayList, Uri str_img1, Uri str_img2, Uri str_img3, Uri str_img4,
                         String description) {

        progressBar.setMessage("Please wait ...");
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        MultipartBody.Part body1 = null, body2 = null, body3 = null, body4 = null;

        if(str_img1 != null){

           File file1 = new File(priPath);
            RequestBody requestFile1 =
                    RequestBody.create(MediaType.parse("multipart/form-data"),file1);

            body1 = MultipartBody.Part.createFormData("img1", file1.getName(), requestFile1);

        }
        if(str_img2 != null){

            File file2 = new File(priPath2);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"),file2);

            body2 = MultipartBody.Part.createFormData("img2", file2.getName(), requestFile2);
        }
        if(str_img3 != null){

            File file3 = new File(priPath3);
            RequestBody requestFile3 =
                    RequestBody.create(MediaType.parse("multipart/form-data"),file3);

            body3 = MultipartBody.Part.createFormData("img3", file3.getName(), requestFile3);
        }
        if(str_img4 != null){

            File file4 = new File(priPath4);
            RequestBody requestFile4 =
                    RequestBody.create(MediaType.parse("multipart/form-data"),file4);

           body4 = MultipartBody.Part.createFormData("img4", file4.getName(), requestFile4);
        }

        RequestBody driver_ID =
                RequestBody.create(MediaType.parse("multipart/form-data"), driverID);

        RequestBody user_ID =
                RequestBody.create(MediaType.parse("multipart/form-data"), userID);

        RequestBody source_ =
                RequestBody.create(MediaType.parse("multipart/form-data"), source);
        RequestBody sourcelat =
                RequestBody.create(MediaType.parse("multipart/form-data"), source_lat);

        RequestBody sourcelongi =
                RequestBody.create(MediaType.parse("multipart/form-data"), source_longi);

        RequestBody drop_ =
                RequestBody.create(MediaType.parse("multipart/form-data"),drop);

        RequestBody droplat =
                RequestBody.create(MediaType.parse("multipart/form-data"), drop_lat);

        RequestBody droplongi =
                RequestBody.create(MediaType.parse("multipart/form-data"), drop_longi);

        RequestBody booking_Date =
                RequestBody.create(MediaType.parse("multipart/form-data"), bookingDate);

        RequestBody noofpeople =
                RequestBody.create(MediaType.parse("multipart/form-data"), no_of_people);

        RequestBody description_ =
                RequestBody.create(MediaType.parse("multipart/form-data"), description);


        RequestBody catArrayList_ =
                RequestBody.create(MediaType.parse("multipart/form-data"), catArrayList.toString());



        Call<BookingCab> cabCall = apiInterface.BOOKING_CAB_CALL(driver_ID, user_ID, source_, sourcelat, sourcelongi,
                drop_, droplat, droplongi, booking_Date, noofpeople,
                 catArrayList_,description_, body1, body2, body3, body4);



        cabCall.enqueue(new Callback<BookingCab>() {
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
                Toast.makeText(getApplicationContext(),"Server Error.", Toast.LENGTH_LONG).show();
            }
        });


    }


        private void runTimePermission() {

            putility = new PermissionUtility(this);
            permission_list = new ArrayList<String>();
            permission_list.add(Manifest.permission.INTERNET);
            permission_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            permission_list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permission_list.add(Manifest.permission.CAMERA);

            putility.setListner(new PermissionUtility.OnPermissionCallback() {
                @Override
                public void OnComplete(boolean is_granted) {
                    Log.i("OnPermissionCallback", "is_granted = " + is_granted);
                    if (is_granted) {

                    } else {
                        putility.checkPermission(permission_list);
                    }
                }
            });
            putility.checkPermission(permission_list);
        }

        private void selectImage1() {

            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE1);*/

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE1);

                    }
                    else if (options[item].equals("Choose from Gallery"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE11);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();


        }

        private void selectImage2() {

            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE2);*/

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE2);
                    }
                    else if (options[item].equals("Choose from Gallery"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE22);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }

        private void selectImage3() {
            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE3);*/

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE3);
                    }
                    else if (options[item].equals("Choose from Gallery"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE33);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

        private void selectImage4() {
            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo"))
                    {
                        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE4);*/
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE4);
                    }
                    else if (options[item].equals("Choose from Gallery"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, EDIT_PROFILE_REQUEST_IMAGE44);
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

        private void init() {
        id_img1 = findViewById(R.id.id_img1);
        id_img2 = findViewById(R.id.id_img2);
        id_img3 = findViewById(R.id.id_img3);
        id_img4 = findViewById(R.id.id_img4);
        id_Confirm_btn = findViewById(R.id.id_Confirm_btn);
        id_descriptionEd = findViewById(R.id.id_descriptionEd);
        progressBar =  new ProgressDialog(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        driverID        = getIntent().getStringExtra("drvierID");
        userID          = getIntent().getStringExtra("userID");
        source          = getIntent().getStringExtra("source");
        source_lat      = getIntent().getStringExtra("source_lat");
        source_longi    = getIntent().getStringExtra("source_longi");
        drop            = getIntent().getStringExtra("drop");
        drop_lat        = getIntent().getStringExtra("drop_lat");
        drop_longi      = getIntent().getStringExtra("drop_longi");
        bookingDate     = getIntent().getStringExtra("bookingDate");
        no_of_people    = getIntent().getStringExtra("no_of_people");
        catArrayList    = (ArrayList<String>) getIntent().getSerializableExtra("catArrayList");


        Log.e("DriverID ", driverID+"  UserID "+userID);
        Log.e("Screen ", "PrescriptionActivity");
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

        public String saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = null;
        File wallpaperDirectory = null;
        try {

            bytes = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            wallpaperDirectory = new File(
                    Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            // have the object build the directory structure, if needed.
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.e("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e("IOEXCEP", e1.toString());

        }
        return "";
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            if (resultCode == RESULT_OK) {
                if (requestCode == EDIT_PROFILE_REQUEST_IMAGE1) {


                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    id_img1.setImageBitmap(thumbnail);
                    priPath = saveImage(thumbnail);
                    Log.d("IMAGE_PATH", priPath + "");
                    str_img1 = getImageUri(getApplicationContext(), thumbnail);

                } else if (requestCode == EDIT_PROFILE_REQUEST_IMAGE11) {
                    str_img1 = data.getData();
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    Log.e("file_Gallery", picturePath);

                    priPath = picturePath;

                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    id_img1.setImageBitmap(thumbnail);
                }
                //--------------------------------
                if (requestCode == EDIT_PROFILE_REQUEST_IMAGE2) {


                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    id_img2.setImageBitmap(thumbnail);
                    priPath2 = saveImage(thumbnail);
                    Log.d("IMAGE_PATH", priPath2 + "");
                    str_img2 = getImageUri(getApplicationContext(), thumbnail);

                } else if (requestCode == EDIT_PROFILE_REQUEST_IMAGE22) {
                    str_img2 = data.getData();
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    priPath2 = picturePath;
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    id_img2.setImageBitmap(thumbnail);
                }

                //--------------------------------
                if (requestCode == EDIT_PROFILE_REQUEST_IMAGE3) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    id_img3.setImageBitmap(thumbnail);
                    priPath3 = saveImage(thumbnail);
                    Log.d("IMAGE_PATH", priPath3 + "");
                    str_img3 = getImageUri(getApplicationContext(), thumbnail);

                } else if (requestCode == EDIT_PROFILE_REQUEST_IMAGE33) {
                    str_img3 = data.getData();
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    priPath3 = picturePath;
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    id_img3.setImageBitmap(thumbnail);
                }

                //--------------------------------
                if (requestCode == EDIT_PROFILE_REQUEST_IMAGE4) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    id_img4.setImageBitmap(thumbnail);
                    priPath4 = saveImage(thumbnail);
                    Log.d("IMAGE_PATH", priPath4 + "");
                    str_img4 = getImageUri(getApplicationContext(), thumbnail);
                } else if (requestCode == EDIT_PROFILE_REQUEST_IMAGE44) {
                    str_img4 = data.getData();
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    priPath4 = picturePath;
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    id_img4.setImageBitmap(thumbnail);
                }

            }
        }
        private Uri getImageUri(Context inContext, Bitmap bitmap) {

            Bitmap OutImage = Bitmap.createScaledBitmap(bitmap, 1000, 1000,true);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
            return Uri.parse(path);

    }


    }
