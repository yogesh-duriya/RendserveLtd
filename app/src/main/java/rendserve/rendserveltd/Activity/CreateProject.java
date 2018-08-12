package rendserve.rendserveltd.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rendserve.rendserveltd.Pojo.LoginPojo;
import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.Pojo.StatePojo;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.adapter.StatesRecyclerViewAdapter;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;
import utils.ImageLoadingUtils;
import utils.RendserveConstant;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreateProject extends BaseActivity implements View.OnClickListener {

    private TextView tv_reset, tv_save, tv_end_date, tv_start_date, tv_state, cross_dialog, tv_cancel;
    //RecyclerView rv_states;
    private EditText et_recepient_ids, et_zip, et_address, et_project_name, constructor_name, ManaerName;
    private ImageView iv_logo,imash;
    private RelativeLayout rl_map;
    private final static int READ_EXTERNAL_STORAGE = 101;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected;
    private SharedPreferences sharedPreferences;
    private Dialog image_picker_dialog, state_dialog, new_state_dialog;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 456;
    private final int SELECT_PHOTO = 457;
    private final int CHOOSE_MAP = 458;
    private static final String CAMERA_DIR = "/dcim/";
    private String mCurrentPhotoPath;
    private CreateProject ctx = this;
    private String photoPath = "";
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private boolean checkCalendar;
    private boolean mLocationPermissionGranted = false;
    private boolean isLocationService = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    private static final int REQUEST_CHECK_SETTINGS = 1234;
    private String TAG = "CreateProect";
    private List<StatePojo> stateList;
    private StatesRecyclerViewAdapter recyclerViewAdapter;
    private String projectName, startDate, endDate, lat = "", lon = "", address, zip, email, user_id, state = "";
    File f;
    private String project_id = "";
    private ProjectDataPojo pdj ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //pdj = new ProjectDataPojo();
        Intent intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        if (project_id != null) {
            for (int i = 0; i < RendserveConstant.projectList.size(); i++) {
                if (project_id.equals(RendserveConstant.projectList.get(i).getId())) {
                    pdj = RendserveConstant.projectList.get(i);
                    break;
                }
            }
        }
        initialize();
        setListener();
    }

    private void initialize() {
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_state = (TextView) findViewById(R.id.tv_state);
        et_recepient_ids = (EditText) findViewById(R.id.et_recepient_ids);
        et_zip = (EditText) findViewById(R.id.et_zip);
        et_address = (EditText) findViewById(R.id.et_address);
        et_project_name = (EditText) findViewById(R.id.et_project_name);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        rl_map = (RelativeLayout) findViewById(R.id.rl_map);
        constructor_name = (EditText) findViewById(R.id.et_constructor_name);
        ManaerName = (EditText) findViewById(R.id.et_manager_name);
        imash = (ImageView)findViewById(R.id.imash);
        tv_reset.setVisibility(View.GONE);

        stateList = getBrands();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) ;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (project_id != null){
            et_project_name.setText(pdj.getProject_name());
            constructor_name.setText(pdj.getConstructor_name());
            ManaerName.setText(pdj.getManager_name());
            tv_start_date.setText(pdj.getStart_date());
            tv_end_date.setText(pdj.getEnd_date());
            et_address.setText(pdj.getAddress());
            tv_state.setText(pdj.getState());
            et_zip.setText(pdj.getZipcode());
            et_recepient_ids.setText(pdj.getEmails());
            Picasso.with(this).load(RendserveConstant.BASE_IMAGE_URL+pdj.getLogo()).into(iv_logo);
        }
    }

    private void setListener() {
        tv_reset.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        rl_map.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (MapActivity.bitmap != null) {
            imash.setImageBitmap(MapActivity.bitmap);
        }*/
        if (RendserveConstant.map_bitmap!=null)
            imash.setImageBitmap(RendserveConstant.map_bitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPermissionDialogMarshMallow();
                } else {
                    showChooserDialog();
                }
                break;
            case R.id.tv_reset:

                break;
            case R.id.tv_save:
                validate();
                break;
            case R.id.tv_start_date:
                checkCalendar = true;
                hideSoftKeyboard();
                showDatePicker();
                break;
            case R.id.tv_end_date:
                checkCalendar = false;
                hideSoftKeyboard();
                showDatePicker();
                break;
            case R.id.tv_state:
                showStateDialoge();
                break;
            case R.id.rl_map:
                getLocationPermission();

                break;
            case R.id.rl_state:
                int abc = (int) view.getTag(R.string.state_name);
                for (int i = 0; i < stateList.size(); i++) {
                    stateList.get(i).setCheck(false);
                }
                stateList.get(abc).setCheck(true);
                state = stateList.get(abc).getStateName();
                recyclerViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void validate() {
        projectName = et_project_name.getText().toString().trim();
        startDate = tv_start_date.getText().toString().trim();
        endDate = tv_end_date.getText().toString().trim();
        address = et_address.getText().toString().trim();
        zip = et_zip.getText().toString().trim();
        String cons_name = constructor_name.getText().toString();
        String manager_name = ManaerName.getText().toString();
        email = et_recepient_ids.getText().toString().trim();
        if (projectName.length() == 0) {
            showToast(getString(R.string.some_request_field_empty));
        } else if (cons_name.isEmpty()) {
            showToast("Please enter constructor name");
        } else if (manager_name.isEmpty()) {
            showToast("Please enter manager name");
        } else if (email.length() == 0) {
            showToast("Some required fields are empty");
        } else if (!email.equals("") && !isValidEmail(email)) {
            showToast("Please enter a valid Email Id");
        } else if (f == null &&  project_id == null) {
            showToast("Please choose a logo");
        } else {
            lat = String.valueOf(MapActivity.lat);
            lon = String.valueOf(MapActivity.lon);
            save(projectName, startDate, endDate, lat, lon, address, state, zip, email, cons_name, manager_name);
        }
    }

    private void save(String project_name, String start_date, String end_date, String lats, String longs, String address, String state, String zipcode, String emails, String constuctorname, String Managername) {
        final ProgressDialog d = CustomProgressDialog.showLoading(CreateProject.this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginPojo> call;
        MultipartBody.Part logo =null ;
        if (f != null) {
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            logo = MultipartBody.Part.createFormData("logo", f.getName(), mFile);
        }
        if (project_id == null) {
            call = apiService.addProject(getMultiPartData(getFromPrefs(RendserveConstant.USER_ID)), getMultiPartData(project_name),
                    getMultiPartData(start_date), getMultiPartData(end_date), getMultiPartData(lats),
                    getMultiPartData(longs), getMultiPartData(address), getMultiPartData(state),
                    getMultiPartData(zipcode), getMultiPartData(emails), getMultiPartData(constuctorname), getMultiPartData(Managername), logo);
        } else {
            if (f == null) {
                call = apiService.editProject(getMultiPartData(getFromPrefs(RendserveConstant.USER_ID)), getMultiPartData(project_id),
                        getMultiPartData(project_name), getMultiPartData(start_date), getMultiPartData(end_date),
                        getMultiPartData(lats), getMultiPartData(longs), getMultiPartData(address),
                        getMultiPartData(state), getMultiPartData(zipcode), getMultiPartData(emails),
                        getMultiPartData(constuctorname), getMultiPartData(Managername));
            }else {

                call = apiService.editProject(getMultiPartData(getFromPrefs(RendserveConstant.USER_ID)), getMultiPartData(project_id),
                        getMultiPartData(project_name), getMultiPartData(start_date), getMultiPartData(end_date),
                        getMultiPartData(lats), getMultiPartData(longs), getMultiPartData(address),
                        getMultiPartData(state), getMultiPartData(zipcode), getMultiPartData(emails),
                        getMultiPartData(constuctorname), getMultiPartData(Managername), logo);
            }
        }

        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                if (response.body().getStatus().equals("1")) {
                    finish();
                } else
                    showToast(response.body().getMessage());
                d.dismiss();
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                // Log error here since request failed
                System.out.println("retrofit hh failure" + t.getMessage());
                d.dismiss();
            }
        });
    }

    private RequestBody getMultiPartData(String value) {
        RequestBody multipart = RequestBody.create(MediaType.parse("multipart/form-data"), value);

        return multipart;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        isLocationService = true;
                        Log.i(TAG, "All location settings are satisfied.");
                        showDialogeNxtActivity();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(CreateProject.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isLocationService = true;
            chooseLocationService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    private void chooseLocationService() {
        if (!mLocationPermissionGranted) {
            //check location service is on or not if it is on then next activit
            // otherwise call displayLocationSettingsRequest
            displayLocationSettingsRequest(this);
            if (isLocationService) {
            }

        } else {
            showDialogeNxtActivity();
        }
        //startActivity(new Intent(CreateProject.this, MapActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                chooseLocationService();
            }
        }
        //updateLocationUI();
    }

    public void showDialogeNxtActivity() {
        image_picker_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        image_picker_dialog.setContentView(R.layout.custom_imageupload_dialog);
        image_picker_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView camera = (TextView) image_picker_dialog.findViewById(R.id.cameraText);
        TextView gallery = (TextView) image_picker_dialog.findViewById(R.id.galleryText);
        cross_dialog = (TextView) image_picker_dialog.findViewById(R.id.cancel);
        cross_dialog.setVisibility(View.GONE);
        camera.setText("Choose Location");
        gallery.setText("Clear");
        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //clear layout of map
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CreateProject.this, MapActivity.class), CHOOSE_MAP);
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });
        image_picker_dialog.show();
    }

    private void addPermissionDialogMarshMallow() {
        ArrayList<String> permissions = new ArrayList<>();
        int resultCode = 0;

        permissions.add(WRITE_EXTERNAL_STORAGE);
        resultCode = READ_EXTERNAL_STORAGE;

        //filter out the permissions we have already accepted
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        permissionsRejected = findRejectedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0) {//we need to ask for permissions
                //but have we already asked for them?
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), resultCode);
                //mark all these as asked..
                for (String perm : permissionsToRequest) {
                    markAsAsked(perm, sharedPreferences);
                }
            } else {
                //show the success banner
                if (permissionsRejected.size() < permissions.size()) {
                    //this means we can show success because some were already accepted.
                    //permissionSuccess.setVisibility(View.VISIBLE);
                    showChooserDialog();
                }

                if (permissionsRejected.size() > 0) {
                    //we have none to request but some previously rejected..tell the user.
                    //It may be better to show a dialog here in a prod application

                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), resultCode);
                    //mark all these as asked..
                    for (String perm : permissionsToRequest) {
                        markAsAsked(perm, sharedPreferences);
                    }
                }
            }
        }
    }

    public void showChooserDialog() {
        image_picker_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        image_picker_dialog.setContentView(R.layout.custom_imageupload_dialog);
        image_picker_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView camera = (TextView) image_picker_dialog.findViewById(R.id.cameraText);
        TextView gallery = (TextView) image_picker_dialog.findViewById(R.id.galleryText);
        cross_dialog = (TextView) image_picker_dialog.findViewById(R.id.cancel);
        cross_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_picker_dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, SELECT_PHOTO);
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // give the image a name so we can store it in the phone's
                // default location
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "bmh" + timeStamp + "_";
                    File albumF = getAlbumDir();
                    File imageF = File.createTempFile(imageFileName, "bmh", albumF);

                    mCurrentPhotoPath = imageF.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(ctx, getApplicationContext().getPackageName() + ".my.package.name.provider", imageF);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });
        image_picker_dialog.show();
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraPicture");
            } else {
                storageDir = new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + "CameraPicture");
            }

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }
        } else {
//		Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        System.out.println("xhxhxhx  " + storageDir);
        return storageDir;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath1 = cursor.getString(columnIndex);

                f = new File(compressImage(imagePath1, 1));

                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                iv_logo.setImageBitmap(myBitmap);
                iv_logo.setBackground(getResources().getDrawable(R.drawable.image_background));
                photoPath = f.getAbsolutePath();

            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                f = new File(compressImage(mCurrentPhotoPath, 0));
                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                iv_logo.setImageBitmap(myBitmap);

                photoPath = f.getAbsolutePath();
            } else if (requestCode == REQUEST_CHECK_SETTINGS) {
                chooseLocationService();
            }/* else if (requestCode == CHOOSE_MAP) {
                lat = data.getStringExtra("lat");
                lon = data.getStringExtra("lon");
                System.out.println("lattitude and longitude are : " + lat + "  " + lon);
            }*/
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                showDialogeNxtActivity();
            }
        }
    }

    public String compressImage(String imageUri, int flag) {
        String filePath;
        if (flag == 1) {
            filePath = getRealPathFromURI(imageUri);
        }

        filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        ImageLoadingUtils utils = new ImageLoadingUtils(this);
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                calendar.set(year, month, day);
                ctx.year = year;
                ctx.month = month;
                ctx.day = day;
                updateLabel();
            }
        }, year, month, day);
        //dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = getDateFormat();
        if (checkCalendar)
            tv_start_date.setText(sdf.format(calendar.getTime()));
        else
            tv_end_date.setText(sdf.format(calendar.getTime()));
    }

    public SimpleDateFormat getDateFormat() {
        String myFormat = "dd/MM/yyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf;
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showStateDialoge() {
        state_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        state_dialog.setContentView(R.layout.custom_state_dialoge);
        state_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView clear_selection = (TextView) state_dialog.findViewById(R.id.clear_selection);
        TextView tv_enter_new_option = (TextView) state_dialog.findViewById(R.id.tv_enter_new_option);
        TextView tv_ok = (TextView) state_dialog.findViewById(R.id.tv_ok);
        tv_cancel = (TextView) state_dialog.findViewById(R.id.tv_cancel);
        RecyclerView rv_states = (RecyclerView) state_dialog.findViewById(R.id.rv_states);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, (int) (height / 1.5));
        rv_states.setLayoutParams(parms);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        rv_states.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_states.getContext(),
                recyclerLayoutManager.getOrientation());
        rv_states.addItemDecoration(dividerItemDecoration);

        recyclerViewAdapter = new StatesRecyclerViewAdapter(stateList, this);
        rv_states.setAdapter(recyclerViewAdapter);


        clear_selection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                state = "";
                for (int i = 0; i < stateList.size(); i++) {
                    stateList.get(i).setCheck(false);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //clear layout of map
                if (!state.equals("")) {
                    tv_state.setText(state);
                }
                state_dialog.dismiss();
                state_dialog.hide();
            }
        });

        tv_enter_new_option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                state_dialog.hide();
                state_dialog.dismiss();
                addNewState();

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = "";
                state_dialog.dismiss();
            }
        });
        state_dialog.show();
    }

    private void addNewState() {

        new_state_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        new_state_dialog.setContentView(R.layout.custom_new_state);
        new_state_dialog.setCancelable(true);

        final EditText et_new_state = (EditText) new_state_dialog.findViewById(R.id.et_new_state);
        final TextView tv_ok = (TextView) new_state_dialog.findViewById(R.id.tv_ok);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = et_new_state.getText().toString().trim();
                tv_state.setText(state);
                stateList.add(new StatePojo(et_new_state.getText().toString().trim(), stateList.size()));
                recyclerViewAdapter.notifyDataSetChanged();
                new_state_dialog.hide();
                new_state_dialog.dismiss();
            }
        });

        et_new_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String stateNew = et_new_state.getText().toString();
                if (stateNew.equals("")) {
                    tv_ok.setFocusable(false);
                    tv_ok.setClickable(false);
                    tv_ok.setTextColor(getResources().getColor(R.color.black_overlay));
                } else {
                    tv_ok.setFocusable(true);
                    tv_ok.setClickable(true);
                    tv_ok.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        new_state_dialog.show();
    }

    private List<StatePojo> getBrands() {
        List<StatePojo> modelList = new ArrayList<StatePojo>();
        modelList.add(new StatePojo("AL", 1));
        modelList.add(new StatePojo("AK", 2));
        modelList.add(new StatePojo("AZ", 3));
        modelList.add(new StatePojo("AR", 4));
        modelList.add(new StatePojo("AR", 5));
        modelList.add(new StatePojo("CA", 6));
        modelList.add(new StatePojo("CO", 7));
        modelList.add(new StatePojo("CT", 8));
        modelList.add(new StatePojo("DE", 9));
        modelList.add(new StatePojo("FL", 10));
        modelList.add(new StatePojo("GA", 11));
        modelList.add(new StatePojo("HI", 12));
        modelList.add(new StatePojo("ID", 13));
        modelList.add(new StatePojo("IN", 14));
        modelList.add(new StatePojo("IA", 15));
        modelList.add(new StatePojo("KS", 16));
        modelList.add(new StatePojo("KY", 17));
        modelList.add(new StatePojo("ME", 18));
        modelList.add(new StatePojo("MI", 19));
        modelList.add(new StatePojo("MN", 20));
        modelList.add(new StatePojo("MS", 21));
        return modelList;
    }


}
