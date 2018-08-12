package rendserve.rendserveltd.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rendserve.rendserveltd.Pojo.BasePojo;
import rendserve.rendserveltd.Pojo.DailyReportPojo;
import rendserve.rendserveltd.Pojo.DocDataPojo;
import rendserve.rendserveltd.Pojo.DocPojo;
import rendserve.rendserveltd.Pojo.ImagePojo;
import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.Pojo.StatePojo;
import rendserve.rendserveltd.Pojo.UploadDocPojo;
import rendserve.rendserveltd.adapter.ProjectAdapter;
import rendserve.rendserveltd.adapter.StatesRecyclerViewAdapter;
import rendserve.rendserveltd.adapter.WorkLogsAdapter;
import rendserve.rendserveltd.fragment.Pager;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.fragment.PhotosFragment;
import rendserve.rendserveltd.fragment.Textsave;
import rendserve.rendserveltd.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;
import utils.FilePath;
import utils.ImageLoadingUtils;
import utils.RendserveConstant;
import utils.RequestBodyUtil;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NewDailyReport extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout, tabLayout2, tabLayout3, tabLayout4, tabLayout5, tabLayout6, tabLayout7,
            tabLayout8, tabLayout9, tabLayout10;
    private ViewPager viewPager, viewPager2, viewPager3, viewPager4, viewPager5, viewPager6, viewPager7,
            viewPager8, viewPager9, viewPager10;
    private TextView tv_cancel, firstText, secondtext, thirdtext, fourthdtext, fivehdtext, sixhdtext,
            sevenhdtext, eighttext, ninetext, tentext, cross_dialog,tv_add_labor,tv_add_constructor,
            tv_add_rental, current_date;
    private RelativeLayout firstLayout, secondLayout, thirdLayout, fourLayout, fiveLayout, sixLayout, sevenLayout, eightLayout, nineLayout, tenLayout;
    private RecyclerView rv_labour, rv_constructor, rv_rental;
    private EditText et_temp_type, et_temp, et_weather, et_humidity/*, et_pressure*/ ,et_email, et_time_of_visit;
    private Button save_send, button, button2, button3;
    private Dialog temp_dialog;
    private StatesRecyclerViewAdapter recyclerViewAdapter;
    private List<StatePojo> stateList;
    private String temp;
    private CheckBox cb_enter_weather;
    private String cameraFrom;
    private String id;
    private final static int READ_EXTERNAL_STORAGE = 101;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected;
    private SharedPreferences sharedPreferences;
    private Dialog image_picker_dialog;
    private final int SELECT_PHOTO = 457;
    private final int SELECT_DOC = 458;

    private String mCurrentPhotoPath;
    private NewDailyReport ctx = this;
    private MainDeshboardActivity main_ctx;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 456;
    private static final String CAMERA_DIR = "/dcim/";
    private Bitmap myBitmap;
    public ArrayList<ImagePojo> img_doc_arr;
    public ArrayList<DocDataPojo> doc_arr;
    private ArrayList<File> files;
    private Pager viewPagerAdapter1, viewPagerAdapter2, viewPagerAdapter3, viewPagerAdapter4, viewPagerAdapter5,
            viewPagerAdapter6, viewPagerAdapter7, viewPagerAdapter8, viewPagerAdapter9, viewPagerAdapter10;
    private ConstraintLayout cl;
    private String project_id = "";
    private final int ADD_LABOUR = 777;
    private final int ADD_SUBCONTRACT = 888;
    private final int ADD_EQUIPMENT = 999;
    private JSONArray workforccelabour, workforcesubconstractor, rentalscost;
    private WorkLogsAdapter workLogsAdapter;
    //private DocPojo doc_list;

    //private RecyclerView rv_list;
    //DailyReportAdapter adapter;
    //private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_daily_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("New Daily Report");

        Intent intent = new Intent();
        intent = getIntent();
        project_id = intent.getStringExtra("project_id");
        //imagePojo = (ImagePojo)getArguments().getSerializable(ARG_PARAM2)
        initialize();
        setListener();

    }

    private void setListener() {
        et_temp_type.setOnClickListener(this);
        save_send.setOnClickListener(this);
        cb_enter_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                templayout(v);
            }
        });

        firstText.setOnClickListener(this);
        secondtext.setOnClickListener(this);
        thirdtext.setOnClickListener(this);
        fourthdtext.setOnClickListener(this);
        fivehdtext.setOnClickListener(this);
        sixhdtext.setOnClickListener(this);
        sevenhdtext.setOnClickListener(this);
        eighttext.setOnClickListener(this);
        ninetext.setOnClickListener(this);
        tentext.setOnClickListener(this);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    private void templayout(View v) {
        if (((CheckBox) v).isChecked()) {
            et_temp_type.setClickable(true);
            et_temp.setClickable(true);
            et_weather.setClickable(true);
            et_time_of_visit.setClickable(true);
            et_humidity.setClickable(true);
            //et_pressure.setClickable(true);
            et_temp_type.setText("");
            et_temp.setText("");
            et_weather.setText("");
            et_time_of_visit.setText("");
            et_humidity.setText("");
            //et_pressure.setText("");
            et_temp_type.setFocusable(true);
            et_temp.setFocusable(true);
            et_weather.setFocusable(true);
            et_time_of_visit.setFocusable(true);
            et_humidity.setFocusable(true);
            //et_pressure.setFocusable(true);
        } else {
            et_temp_type.setClickable(true);
            et_temp.setClickable(true);
            et_weather.setClickable(true);
            et_time_of_visit.setClickable(true);
            et_humidity.setClickable(true);
            //et_pressure.setClickable(false);
            et_temp_type.setText("℃");
            et_temp.setText("24");
            et_weather.setText("Mist");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            String currentTime = sdf.format(new Date());
            et_time_of_visit.setText(currentTime);
            et_humidity.setText("29");
            //et_pressure.setText("1010");
            et_temp_type.setFocusable(true);
            et_temp.setFocusable(true);
            et_weather.setFocusable(true);
            et_time_of_visit.setFocusable(true);
            et_humidity.setFocusable(true);
            //et_pressure.setFocusable(false);
        }
    }

    private void initialize() {
       // main_ctx = ((MainDeshboardActivity)this);
        sharedPreferences = getSharedPreferences(RendserveConstant.PREF_NAME, Activity.MODE_PRIVATE);
        img_doc_arr = new ArrayList<>();
        doc_arr = new ArrayList<>();
        for (int k =0;k<10;k++) {
            DocDataPojo ddp = new DocDataPojo();
            ddp.setDoc1("");
            ddp.setDoc2("");
            ddp.setDoc3("");
            doc_arr.add(ddp);
        }

        //rv_list = (RecyclerView) findViewById(R.id.rv_list);
        et_temp_type = (EditText) findViewById(R.id.et_temp_type);
        et_temp = (EditText) findViewById(R.id.et_temp);
        et_weather = (EditText) findViewById(R.id.et_weather);
        et_time_of_visit = (EditText) findViewById(R.id.et_pressure);
        et_humidity = (EditText) findViewById(R.id.et_humidity);
        //et_pressure = (EditText) findViewById(R.id.et_pressure);
        et_email = (EditText) findViewById(R.id.editemail);
        cb_enter_weather = (CheckBox) findViewById(R.id.cb_enter_weather);
        save_send = (Button) findViewById(R.id.save_send);
        // initialize
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout3 = (TabLayout) findViewById(R.id.tabLayout3);
        tabLayout4 = (TabLayout) findViewById(R.id.tabLayout4);
        tabLayout5 = (TabLayout) findViewById(R.id.tabLayout5);
        tabLayout6 = (TabLayout) findViewById(R.id.tabLayout6);
        tabLayout7 = (TabLayout) findViewById(R.id.tabLayout7);
        tabLayout8 = (TabLayout) findViewById(R.id.tabLayout8);
        tabLayout9 = (TabLayout) findViewById(R.id.tabLayout9);
        tabLayout10 = (TabLayout) findViewById(R.id.tabLayout10);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager2 = (ViewPager) findViewById(R.id.pager2);
        viewPager3 = (ViewPager) findViewById(R.id.pager3);
        viewPager4 = (ViewPager) findViewById(R.id.pager4);
        viewPager5 = (ViewPager) findViewById(R.id.pager5);
        viewPager6 = (ViewPager) findViewById(R.id.pager6);
        viewPager7 = (ViewPager) findViewById(R.id.pager7);
        viewPager8 = (ViewPager) findViewById(R.id.pager8);
        viewPager9 = (ViewPager) findViewById(R.id.pager9);
        viewPager10 = (ViewPager) findViewById(R.id.pager10);

        firstText = (TextView) findViewById(R.id.firstText);
        secondtext = (TextView) findViewById(R.id.secondtext);
        thirdtext = (TextView) findViewById(R.id.thirdtext);
        fourthdtext = (TextView) findViewById(R.id.fourthdtext);
        fivehdtext = (TextView) findViewById(R.id.fivehdtext);
        sixhdtext = (TextView) findViewById(R.id.sixhdtext);
        sevenhdtext = (TextView) findViewById(R.id.sevenhdtext);
        eighttext = (TextView) findViewById(R.id.eighttext);
        ninetext = (TextView) findViewById(R.id.ninetext);
        tentext = (TextView) findViewById(R.id.tentext);
        tv_add_labor = (TextView) findViewById(R.id.tv_add_labor);
        tv_add_rental = (TextView) findViewById(R.id.tv_add_rental);
        tv_add_constructor = (TextView) findViewById(R.id.tv_add_constructor);
        current_date = (TextView) findViewById(R.id.textView22);

        firstLayout = (RelativeLayout) findViewById(R.id.firstLayout);
        secondLayout = (RelativeLayout) findViewById(R.id.SecondLayout);
        thirdLayout = (RelativeLayout) findViewById(R.id.thirdLayout);
        fourLayout = (RelativeLayout) findViewById(R.id.fourLayout);
        fiveLayout = (RelativeLayout) findViewById(R.id.fiveLayout);
        sixLayout = (RelativeLayout) findViewById(R.id.sixLayout);
        sevenLayout = (RelativeLayout) findViewById(R.id.sevenLayout);
        eightLayout = (RelativeLayout) findViewById(R.id.eightLayout);
        nineLayout = (RelativeLayout) findViewById(R.id.nineLayout);
        tenLayout = (RelativeLayout) findViewById(R.id.tenLayout);

        rv_labour = (RecyclerView) findViewById(R.id.rv_labour);
        rv_constructor = (RecyclerView) findViewById(R.id.rv_constructor);
        rv_rental = (RecyclerView) findViewById(R.id.rv_rental);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        cl = (ConstraintLayout) findViewById(R.id.cl);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        String currentTime = sdf.format(new Date());
        et_time_of_visit.setText(currentTime);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        current_date.setText(formattedDate);

        boolean ch = false;
        if (img_doc_arr.size() != 0) {
            for (int j = 0; j < img_doc_arr.size(); j++) {
                if (img_doc_arr.get(j).getId().equals("1")) {
                    viewPagerAdapter1 = new Pager(getSupportFragmentManager(), "1");
                    viewPager.setAdapter(viewPagerAdapter1);
                    ch = true;
                    break;
                }
            }
        }
        if (!ch) {
            viewPagerAdapter1 = new Pager(getSupportFragmentManager(), "1");
            viewPager.setAdapter(viewPagerAdapter1);
        }

        viewPagerAdapter2 = new Pager(getSupportFragmentManager(), "2");
        viewPager2.setAdapter(viewPagerAdapter2);
        viewPagerAdapter3 = new Pager(getSupportFragmentManager(), "3");
        viewPager3.setAdapter(viewPagerAdapter3);
        viewPagerAdapter4 = new Pager(getSupportFragmentManager(), "4");
        viewPager4.setAdapter(viewPagerAdapter4);
        viewPagerAdapter5 = new Pager(getSupportFragmentManager(), "5");
        viewPager5.setAdapter(viewPagerAdapter5);
        viewPagerAdapter6 = new Pager(getSupportFragmentManager(), "6");
        viewPager6.setAdapter(viewPagerAdapter6);
        viewPagerAdapter7 = new Pager(getSupportFragmentManager(), "7");
        viewPager7.setAdapter(viewPagerAdapter7);
        viewPagerAdapter8 = new Pager(getSupportFragmentManager(), "8");
        viewPager8.setAdapter(viewPagerAdapter8);
        viewPagerAdapter9 = new Pager(getSupportFragmentManager(), "9");
        viewPager9.setAdapter(viewPagerAdapter9);
        viewPagerAdapter10 = new Pager(getSupportFragmentManager(), "10");
        viewPager10.setAdapter(viewPagerAdapter10);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout2.setupWithViewPager(viewPager2);
        tabLayout3.setupWithViewPager(viewPager3);
        tabLayout4.setupWithViewPager(viewPager4);
        tabLayout5.setupWithViewPager(viewPager5);
        tabLayout6.setupWithViewPager(viewPager6);
        tabLayout7.setupWithViewPager(viewPager7);
        tabLayout8.setupWithViewPager(viewPager8);
        tabLayout9.setupWithViewPager(viewPager9);
        tabLayout10.setupWithViewPager(viewPager10);

        stateList = getBrands();

        ArrayList<String> titles = new ArrayList();
        titles.add("ANY DELAYS CAUSED BY WEATHER");
        titles.add("WORK PERFORMED TODAY");
        titles.add("SUBCONTRACTOR'S PROGRESS");
        titles.add("ANY ACCIDENTS ON SITE TODAY?");
        titles.add("EXTRA WORK REQUESTS");
        titles.add("MATERIAL PURCHASE/RECIEVED");
        titles.add("QUALITY CONTROL");
        titles.add("SAFETY OBSERVATIONS");
        titles.add("ANY SCHEDULES SELAYS?");
        titles.add("WHAT ELSE?");

        files = new ArrayList<>();

        workforccelabour = new JSONArray();
        workforcesubconstractor = new JSONArray();
        rentalscost = new JSONArray();
        /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(mLayoutManager);
        rv_list.setItemAnimator(new DefaultItemAnimator());
        adapter = new DailyReportAdapter(this,titles);
        rv_list.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.et_temp_type:
                showTempDialoge();
                break;

            case R.id.rl_state:
                int abc = (int) view.getTag(R.string.state_name);
                for (int i = 0; i < stateList.size(); i++) {
                    stateList.get(i).setCheck(false);
                }
                stateList.get(abc).setCheck(true);
                temp = stateList.get(abc).getStateName();
                recyclerViewAdapter.notifyDataSetChanged();
                break;

            case R.id.firstText:
                expandLayout(firstLayout, firstText);
                break;

            case R.id.secondtext:
                expandLayout(secondLayout, secondtext);
                break;

            case R.id.thirdtext:
                expandLayout(thirdLayout, thirdtext);
                break;

            case R.id.fourthdtext:
                expandLayout(fourLayout, fourthdtext);
                break;

            case R.id.fivehdtext:
                expandLayout(fiveLayout, fivehdtext);
                break;

            case R.id.sixhdtext:
                expandLayout(sixLayout, sixhdtext);
                break;

            case R.id.sevenhdtext:
                expandLayout(sevenLayout, sevenhdtext);
                break;

            case R.id.eighttext:
                expandLayout(eightLayout, eighttext);
                break;

            case R.id.ninetext:
                expandLayout(nineLayout, ninetext);
                break;

            case R.id.tentext:
                expandLayout(tenLayout, tentext);
                break;

            case R.id.img1:
                cameraFrom = String.valueOf(view.getTag(R.string.first_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;

            case R.id.img2:
                cameraFrom = String.valueOf(view.getTag(R.string.second_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;

            case R.id.img3:
                cameraFrom = String.valueOf(view.getTag(R.string.third_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img4:
                cameraFrom = String.valueOf(view.getTag(R.string.fourth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img5:
                cameraFrom = String.valueOf(view.getTag(R.string.fivth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img6:
                cameraFrom = String.valueOf(view.getTag(R.string.sixth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img7:
                cameraFrom = String.valueOf(view.getTag(R.string.seventh_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img8:
                cameraFrom = String.valueOf(view.getTag(R.string.eighth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img9:
                cameraFrom = String.valueOf(view.getTag(R.string.ninth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img10:
                cameraFrom = String.valueOf(view.getTag(R.string.tenth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img11:
                cameraFrom = String.valueOf(view.getTag(R.string.eleventh_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;
            case R.id.img12:
                cameraFrom = String.valueOf(view.getTag(R.string.twelfth_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseImage();
                break;

            case R.id.doc_img_1:
                cameraFrom = String.valueOf(view.getTag(R.string.first_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseDoc();
                break;

            case R.id.doc_img_2:
                cameraFrom = String.valueOf(view.getTag(R.string.second_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseDoc();
                break;

            case R.id.doc_img_3:
                cameraFrom = String.valueOf(view.getTag(R.string.third_camera));
                id = String.valueOf(view.getTag(R.string.id));
                chooseDoc();
                break;
            case R.id.save_send:
                SaveAndSend();
                //submit();

                //addDocument();
                break;
            case R.id.button:
                Intent addIntent = new Intent(NewDailyReport.this,AddLabourActivity.class);
                startActivityForResult(addIntent,ADD_LABOUR);
                break;
            case R.id.button2:
                Intent contractorIntent = new Intent(NewDailyReport.this,AddSubContractActivity.class);
                startActivityForResult(contractorIntent,ADD_SUBCONTRACT);
                break;
            case R.id.button3:
                Intent addRental = new Intent(NewDailyReport.this,AddRentalsActivity.class);
                startActivityForResult(addRental,ADD_EQUIPMENT);
                break;
        }
    }



    private void showTempDialoge() {
        temp_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        temp_dialog.setContentView(R.layout.custom_state_dialoge);
        temp_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView clear_selection = (TextView) temp_dialog.findViewById(R.id.clear_selection);
        TextView tv_enter_new_option = (TextView) temp_dialog.findViewById(R.id.tv_enter_new_option);
        TextView tv_ok = (TextView) temp_dialog.findViewById(R.id.tv_ok);
        tv_cancel = (TextView) temp_dialog.findViewById(R.id.tv_cancel);
        RecyclerView rv_states = (RecyclerView) temp_dialog.findViewById(R.id.rv_states);

        tv_enter_new_option.setVisibility(View.GONE);

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
                temp = "";
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
                if (!temp.equals("")) {
                    et_temp_type.setText(temp);
                }
                temp_dialog.dismiss();
                temp_dialog.hide();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = "";
                temp_dialog.dismiss();
            }
        });
        temp_dialog.show();
    }

    private void expandLayout(RelativeLayout layout, TextView textView) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_righttt, 0);
        } else {
            layout.setVisibility(View.VISIBLE);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
        }
    }

    private List<StatePojo> getBrands() {
        List<StatePojo> modelList = new ArrayList<StatePojo>();
        modelList.add(new StatePojo(" \u2103", 1));
        modelList.add(new StatePojo(" \u2109", 2));
        return modelList;
    }

    private void chooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissionDialogMarshMallow();
        } else {
            showChooserDialog();
        }
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

//                File f =new File(compressImage(imagePath1, 1));
                File f = new File(compressImage(imagePath1, 1));

                myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                /*BitmapDrawable background = new BitmapDrawable(myBitmap);
                cl.setBackgroundDrawable(background);*/
                addData(myBitmap);

                files.add(f);

                /*profile_image.setImageBitmap(myBitmap);
                photoPath = f.getAbsolutePath();*/

            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                File f = new File(compressImage(mCurrentPhotoPath, 0));
                myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                /*BitmapDrawable background = new BitmapDrawable(myBitmap);
                cl.setBackgroundDrawable(background);*/
                addData(myBitmap);
                //img_doc_arr.add(myBitmap);
                files.add(f);
            }else if (requestCode == SELECT_DOC) {

                Uri PathHolder = data.getData();
               // Toast.makeText(this, PathHolder , Toast.LENGTH_LONG).show();
                addDoc(PathHolder.toString());

            }else if (requestCode == ADD_LABOUR){
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name",data.getStringExtra("name"));
                    obj.put("labour_type",data.getStringExtra("labour"));
                    obj.put("workhours",data.getStringExtra("work_hour"));
                    obj.put("travelhour",data.getStringExtra("travel_hour"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                workforccelabour.put(obj);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                rv_labour.setLayoutManager(mLayoutManager);
                rv_labour.setItemAnimator(new DefaultItemAnimator());
                workLogsAdapter = new WorkLogsAdapter(workforccelabour, this,"labor");
                rv_labour.setAdapter(workLogsAdapter);
                tv_add_labor.setVisibility(View.GONE);
            }else if (requestCode == ADD_SUBCONTRACT){
                Intent i = getIntent();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("name",data.getStringExtra("name"));
                    obj.put("labour_type",data.getStringExtra("subcontract"));
                    obj.put("workhours",data.getStringExtra("work_hour"));
                    obj.put("travelhour",data.getStringExtra("travel_hour"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                workforcesubconstractor.put(obj);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                rv_constructor.setLayoutManager(mLayoutManager);
                rv_constructor.setItemAnimator(new DefaultItemAnimator());
                workLogsAdapter = new WorkLogsAdapter(workforcesubconstractor, this,"constructor");
                rv_constructor.setAdapter(workLogsAdapter);
                tv_add_constructor.setVisibility(View.GONE);
            }else if ( requestCode == ADD_EQUIPMENT ){
                JSONObject obj = new JSONObject();
                try {
                    obj.put("equipment",data.getStringExtra("equipment"));
                    obj.put("costhour",data.getStringExtra("cost"));
                    obj.put("hour",data.getStringExtra("work_hour"));
                    obj.put("totalcost",data.getStringExtra("total_cost"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rentalscost.put(obj);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                rv_rental.setLayoutManager(mLayoutManager);
                rv_rental.setItemAnimator(new DefaultItemAnimator());
                workLogsAdapter = new WorkLogsAdapter(rentalscost, this,"rental");
                rv_rental.setAdapter(workLogsAdapter);
                tv_add_rental.setVisibility(View.GONE);
            }
                /*profile_image.setImageBitmap(myBitmap);
                photoPath = f.getAbsolutePath();*/
        }
    }

    private void addData(Bitmap bitmap) {
        Boolean check = false;
        if (cameraFrom.equals("camera_one")) {
            if (img_doc_arr.size() == 0) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap1(bitmap);
                img_doc_arr.add(ip);
                check = true;
            } else {
                for (int i = 0; i < img_doc_arr.size(); i++) {
                    if (img_doc_arr.get(i).getId().equals(id)) {
                        img_doc_arr.get(i).setImageBitmap1(bitmap);
                        check = true;
                        break;
                    } else {
                        check = false;
                    }
                }
            }

            if (!check) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap1(bitmap);
                img_doc_arr.add(ip);
            }
        }

        if (cameraFrom.equals("camera_two")) {
            if (img_doc_arr.size() == 0) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap2(bitmap);
                img_doc_arr.add(ip);
                check = true;
            } else {
                for (int i = 0; i < img_doc_arr.size(); i++) {
                    if (img_doc_arr.get(i).getId().equals(id)) {
                        img_doc_arr.get(i).setImageBitmap2(bitmap);
                        check = true;
                        break;
                    } else {
                        check = false;
                    }
                }
            }

            if (!check) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap2(bitmap);
                img_doc_arr.add(ip);
            }
        }

        if (cameraFrom.equals("camera_three")) {
            if (img_doc_arr.size() == 0) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap3(bitmap);
                img_doc_arr.add(ip);
                check = true;
            } else {
                for (int i = 0; i < img_doc_arr.size(); i++) {
                    if (img_doc_arr.get(i).getId().equals(id)) {
                        img_doc_arr.get(i).setImageBitmap3(bitmap);
                        check = true;
                        break;
                    } else {
                        check = false;
                    }
                }
            }

            if (!check) {
                ImagePojo ip = new ImagePojo();
                ip.setId(id);
                ip.setImageBitmap3(bitmap);
                img_doc_arr.add(ip);
            }
        }

        if (cameraFrom.equals("camera_four"))
            addCamera4(bitmap);

        if (cameraFrom.equals("camera_five"))
            addCamera5(bitmap);

        if (cameraFrom.equals("camera_six"))
            addCamera6(bitmap);

        if (cameraFrom.equals("camera_seven"))
            addCamera7(bitmap);

        if (cameraFrom.equals("camera_eight"))
            addCamera8(bitmap);

        if (cameraFrom.equals("camera_nine"))
            addCamera9(bitmap);

        if (cameraFrom.equals("camera_ten"))
            addCamera10(bitmap);

        if (cameraFrom.equals("camera_eleven"))
            addCamera11(bitmap);

        if (cameraFrom.equals("camera_twelve"))
            addCamera12(bitmap);

        if (img_doc_arr.size() != 0) {
            for (int j = 0; j < img_doc_arr.size(); j++) {
                if (img_doc_arr.get(j).getId().equals("1")) {
                    viewPagerAdapter1 = new Pager(getSupportFragmentManager(), "1");
                    viewPager.setAdapter(viewPagerAdapter1);

                } else if (img_doc_arr.get(j).getId().equals("2")) {
                    viewPagerAdapter2 = new Pager(getSupportFragmentManager(), "2");
                    viewPager2.setAdapter(viewPagerAdapter2);

                } else if (img_doc_arr.get(j).getId().equals("3")) {
                    viewPagerAdapter3 = new Pager(getSupportFragmentManager(), "3");
                    viewPager3.setAdapter(viewPagerAdapter3);

                } else if (img_doc_arr.get(j).getId().equals("4")) {
                    viewPagerAdapter4 = new Pager(getSupportFragmentManager(), "4");
                    viewPager4.setAdapter(viewPagerAdapter4);

                } else if (img_doc_arr.get(j).getId().equals("5")) {
                    viewPagerAdapter5 = new Pager(getSupportFragmentManager(), "5");
                    viewPager5.setAdapter(viewPagerAdapter5);

                } else if (img_doc_arr.get(j).getId().equals("6")) {
                    viewPagerAdapter6 = new Pager(getSupportFragmentManager(), "6");
                    viewPager6.setAdapter(viewPagerAdapter6);

                } else if (img_doc_arr.get(j).getId().equals("7")) {
                    viewPagerAdapter7 = new Pager(getSupportFragmentManager(), "7");
                    viewPager7.setAdapter(viewPagerAdapter7);

                } else if (img_doc_arr.get(j).getId().equals("8")) {
                    viewPagerAdapter8 = new Pager(getSupportFragmentManager(), "8");
                    viewPager8.setAdapter(viewPagerAdapter8);

                } else if (img_doc_arr.get(j).getId().equals("9")) {
                    viewPagerAdapter9 = new Pager(getSupportFragmentManager(), "9");
                    viewPager9.setAdapter(viewPagerAdapter9);

                } else if (img_doc_arr.get(j).getId().equals("10")) {
                    viewPagerAdapter10 = new Pager(getSupportFragmentManager(), "10");
                    viewPager10.setAdapter(viewPagerAdapter10);

                }
            }
        }
    }

    private void addCamera4(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap4(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap4(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap4(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera5(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap5(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap5(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap5(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera6(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap6(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap6(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap6(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera7(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap7(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap7(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap7(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera8(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap8(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap8(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap8(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera9(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap9(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap9(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap9(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera10(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap10(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap10(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap10(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera11(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap11(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap11(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap11(bitmap);
            img_doc_arr.add(ip);
        }
    }

    private void addCamera12(Bitmap bitmap) {
        boolean check = false;
        if (img_doc_arr.size() == 0) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap12(bitmap);
            img_doc_arr.add(ip);
            check = true;
        } else {
            for (int i = 0; i < img_doc_arr.size(); i++) {
                if (img_doc_arr.get(i).getId().equals(id)) {
                    img_doc_arr.get(i).setImageBitmap12(bitmap);
                    check = true;
                    break;
                } else {
                    check = false;
                }
            }
        }

        if (!check) {
            ImagePojo ip = new ImagePojo();
            ip.setId(id);
            ip.setImageBitmap12(bitmap);
            img_doc_arr.add(ip);
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
//        options.inPurgeable = true;
//        options.inInputShareable = true;
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

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
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

    private String SaveAndSend() {
        List<String> list = new ArrayList<>();
        list.add("delayscausedbyweather");
        list.add("workperformtoday");
        list.add("subconstractorprogress");
        list.add("accidentonsite");
        list.add("extraworkrequire");
        list.add("materialpurchase");
        list.add("qualitycontrol");
        list.add("safetyobservations");
        list.add("scheduledelays");
        list.add("whatelse");
        final ProgressDialog d = CustomProgressDialog.showLoading(NewDailyReport.this);
        d.setCanceledOnTouchOutside(false);
        d.show();
        JSONObject data = new JSONObject();

        for (int j = 1; j < 11; j++) {
            JSONObject delayscausedbyweather = new JSONObject();
            try {
                //Textsave mFragment = Pager.getItem(viewPager.getCurrentItem());
                //Textsave myFragment = null;
                if (j==1) {
                    //Pager adapter = ((Pager)viewPager.getAdapter());
                    Textsave myFragment = (Textsave) viewPagerAdapter1.instantiateItem(viewPager, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==2) {
                    Textsave myFragment2 = (Textsave) viewPagerAdapter2.instantiateItem(viewPager2, 0);
                    String text2 =  myFragment2.getText();
                    delayscausedbyweather.put("text", text2);
                }else if (j==3) {
                    Textsave myFragment3 = (Textsave) viewPagerAdapter3.instantiateItem(viewPager3, 0);
                    String text3 =  myFragment3.getText();
                    delayscausedbyweather.put("text", text3);
                }else if (j==4) {
                    Textsave myFragment = (Textsave) viewPagerAdapter4.instantiateItem(viewPager4, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==5) {
                    Textsave  myFragment = (Textsave) viewPagerAdapter5.instantiateItem(viewPager5, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==6) {
                    Textsave  myFragment = (Textsave) viewPagerAdapter6.instantiateItem(viewPager6, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==7) {
                    Textsave myFragment = (Textsave) viewPagerAdapter7.instantiateItem(viewPager7, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==8) {
                    Textsave  myFragment = (Textsave) viewPagerAdapter8.instantiateItem(viewPager8, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==9) {
                    Textsave  myFragment = (Textsave) viewPagerAdapter9.instantiateItem(viewPager9, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }else if (j==10) {
                    Textsave  myFragment = (Textsave) viewPagerAdapter10.instantiateItem(viewPager10, 0);
                    String text =  myFragment.getText();
                    delayscausedbyweather.put("text", text);
                }
                boolean check = false;
                JSONArray image = new JSONArray();
                JSONArray document = new JSONArray();
                for (int i = 0; i < img_doc_arr.size(); i++) {
                    if (img_doc_arr.get(i).getId().equals("" + j) ) {
                        if (img_doc_arr.get(i).getImageBitmap1() != null) {
                            //String name = new LongOperation().execute("");
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap1()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap2() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap2()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap3() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap3()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap4() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap4()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap5() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap5()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap6() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap6()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap7() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap7()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap8() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap8()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap9() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap9()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap10() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap10()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap11() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap11()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        if (img_doc_arr.get(i).getImageBitmap12() != null) {
                            LoginProcess task = new LoginProcess();
                            String img1 = task.execute(new Bitmap[]{img_doc_arr.get(i).getImageBitmap12()}).get();
                            image.put(new JSONObject().put("imageurl", img1));
                        }
                        delayscausedbyweather.put("image", image);

                        if (!doc_arr.get(i).getDoc1().equals("")){

                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(i).getDoc1()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }
                        if (!doc_arr.get(i).getDoc2().equals("")){
                            //File f = new File(doc_arr.get(i).getDoc1());
                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(i).getDoc2()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }
                        if (!doc_arr.get(i).getDoc3().equals("")){
                            //File f = new File(doc_arr.get(i).getDoc1());
                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(i).getDoc3()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }

                        delayscausedbyweather.put("document", document);
                        check = true;
                    }
                }
                if (!check) {
                    delayscausedbyweather.put("image", image);
                    //for (int l =0;l<doc_arr.size();l++){
                    int l = j-1;
                        if (!doc_arr.get(l).getDoc1().equals("")){
                            //File f = new File(doc_arr.get(l).getDoc1());
                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(l).getDoc1()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }
                        if (!doc_arr.get(l).getDoc2().equals("")){
                           // File f = new File(doc_arr.get(l).getDoc1());
                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(l).getDoc2()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }
                        if (!doc_arr.get(l).getDoc3().equals("")){
                            //File f = new File(doc_arr.get(l).getDoc1());
                            DocProcess docProcess = new DocProcess();
                            String doc1 = docProcess.execute(doc_arr.get(l).getDoc3()).get();
                            document.put(new JSONObject().put("docurl", doc1));
                        }

                        delayscausedbyweather.put("document", document);
                    //}

                    // delayscausedbyweather.put("document", document);
                }
                data.put(list.get(j - 1), delayscausedbyweather);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        try {
            data.put("workforccelabour",workforccelabour);
            data.put("workforcesubconstractor",workforcesubconstractor);
            data.put("rentalscost",rentalscost);
            Log.d("SaveAndSend : ", "SaveAndSend: " + data.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        d.dismiss();
        submit(data.toString());
        return data.toString();
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void submit(String obj) {
        final ProgressDialog d = CustomProgressDialog.showLoading(NewDailyReport.this);
        d.setCanceledOnTouchOutside(false);
        String state="",start_date="",end_date="",address="",constructor_name="",manager_name="";
        for (int i=0;i<RendserveConstant.projectList.size();i++){
            if (project_id.equals(RendserveConstant.projectList.get(i).getId())){
                state = RendserveConstant.projectList.get(i).getState();
                address = RendserveConstant.projectList.get(i).getAddress();
                start_date = RendserveConstant.projectList.get(i).getStart_date();
                end_date = RendserveConstant.projectList.get(i).getEnd_date();
                manager_name = RendserveConstant.projectList.get(i).getManager_name();
                constructor_name = RendserveConstant.projectList.get(i).getConstructor_name();
            }
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<BasePojo> call = apiService.pdfProject(getFromPrefs(RendserveConstant.USER_ID), project_id, end_date,
                state, start_date, address, et_temp.getText().toString()+et_temp_type.getText().toString(),
                et_weather.getText().toString(), et_time_of_visit.getText().toString(), et_humidity.getText().toString(),
                constructor_name, manager_name, et_email.getText().toString().trim(),  obj);
        call.enqueue(new Callback<BasePojo>() {
            @Override
            public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                try {
                        if (response.body().getStatus().equals("1")) {

                        finish();
                    } else
                        showToast(response.body().getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                d.dismiss();
            }

            @Override
            public void onFailure(Call<BasePojo> call, Throwable t) {
                // Log error here since request failed
                System.out.println("retrofit hh failure " + t.getMessage());
                d.dismiss();
            }
        });

    }

    private String addDocument(Bitmap bitmap) {

        final String[] name = {""};
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, "image" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        final ProgressDialog d = CustomProgressDialog.showLoading(NewDailyReport.this);
        d.setCanceledOnTouchOutside(false);

        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part logo = MultipartBody.Part.createFormData("logo", imageFile.getName(), mFile);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<UploadDocPojo> call = apiService.addDocument(logo);
        call.enqueue(new Callback<UploadDocPojo>() {
            @Override
            public void onResponse(Call<UploadDocPojo> call, Response<UploadDocPojo> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                        name[0] = response.body().getData().toString();
                    } else
                        showToast(response.body().getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                d.dismiss();
            }

            @Override
            public void onFailure(Call<UploadDocPojo> call, Throwable t) {
                System.out.println("retrofit hh failure " + t.getMessage());
                d.dismiss();
            }
        });
        return name[0];
    }

    class LoginProcess extends AsyncTask<Bitmap, Integer, String> {

        //private ProgressDialog pDialog;
        //final ProgressDialog d = CustomProgressDialog.showLoading(NewDailyReport.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //d.setCanceledOnTouchOutside(false);
            //d.show();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            final String[] name = {""};
            File filesDir = getFilesDir();
            File imageFile = new File(filesDir, "image" + ".jpg");
            okhttp3.Response response =null;
            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                params[0].compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }
            OkHttpClient client = new OkHttpClient();
            String message = null;
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            //MultipartBody.Part logo = MultipartBody.Part.createFormData("logo", imageFile.getName(), mFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(),
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                    .build();
            try {
                Request request = new Request.Builder()
                        .url(RendserveConstant.BASE_URL + "/api/mobile/addDocument")
                        .post(requestBody)
                        .build();
                response = client.newCall(request).execute();
                //System.out.println("response is :" + response.body().string().toString());
                String str = response.body().string().toString();
                JSONObject localJSONObject1 = new JSONObject(str);
                message = localJSONObject1.getString("data");
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
         //   d.dismiss();
            Intent i = null;
            if (result != null) {
                //d.dismiss();
                /*try {

                    JSONObject localJSONObject1 = new JSONObject(result);
                    String message = localJSONObject1.getString("Message");
                    showToast(message);
                    System.out.println("message is ;" + message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    d.dismiss();
                    // System.out.println("Response is:"+e.getMessage());
                }*/
            } else {
               // d.dismiss();
                //displayToast( "Login failed.."+ " Please enter correct username and password");
                //System.out.println("cancel");
            }
        }

    }

    class DocProcess extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            final String[] name = {""};
            File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Download/yogesh.pdf");
            String filesDir = params[0];
            String path = FilePath.getPath(NewDailyReport.this, Uri.parse(filesDir));
            File file = new File(path);
            Uri pa = Uri.fromFile(file);
            Uri photoURI = FileProvider.getUriForFile(NewDailyReport.this, getApplicationContext().getPackageName() + ".my.package.name.provider", file);

            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(photoURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            String uploadId = UUID.randomUUID().toString();
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(NewDailyReport.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }*/
            okhttp3.Response response =null;
            /*byte[] byteArray=null;
            try {
                InputStream inputStream = new FileInputStream(path);
                String inputStreamToString = inputStream.toString();
                byteArray = inputStreamToString.getBytes();
                inputStream.close();
            } catch (FileNotFoundException e) {
                System.out.println("File Not found"+e);
            } catch (IOException e) {
                System.out.println("IO Ex"+e);
            }*/
            OkHttpClient client = new OkHttpClient();
            String message = null;
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            //MultipartBody.Part logo = MultipartBody.Part.createFormData("logo", imageFile.getName(), mFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("doc", file.getName(), RequestBody.create(MediaType.parse("application/pdf"), file))
                    .build();
           /* final MediaType MEDIA_TYPE_PLAINTEXT = MediaType
                    .parse("text/plain; charset=utf-8");
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_PLAINTEXT, byteArray);*/
            try {
                Request request = new Request.Builder()
                        .url(RendserveConstant.BASE_URL + "/api/mobile/addDocument")
                        .post(requestBody)
                        .build();
                response = client.newCall(request).execute();
                //System.out.println("response is :" + response.body().string().toString());
                String str = response.body().string().toString();
                Log.d("response :" , str);
                JSONObject localJSONObject1 = new JSONObject(str);
                message = localJSONObject1.getString("data");
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return message;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Intent i = null;
            if (result != null) {

            } else {
            }
        }

    }

    private void chooseDoc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissionDialogMarshMallows();
        } else {
            showDocChooserDialog();
        }
    }

    private void addPermissionDialogMarshMallows() {
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
                    showDocChooserDialog();
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

    private void showDocChooserDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //File file = new File( filename  );
        intent.setType( "application/pdf" );
        startActivityForResult(intent, SELECT_DOC);
    }

    private void addDoc(String doc){

        if (id.equals("1")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(0).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(0).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(0).setDoc3(doc);
            }
            viewPagerAdapter1 = new Pager(getSupportFragmentManager(), "1");
            viewPager.setAdapter(viewPagerAdapter1);
        }else if (id.equals("2")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(1).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(1).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(1).setDoc3(doc);
            }
            viewPagerAdapter2 = new Pager(getSupportFragmentManager(), "2");
            viewPager2.setAdapter(viewPagerAdapter2);
        }else if (id.equals("3")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(2).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(2).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(2).setDoc3(doc);
            }
            viewPagerAdapter3 = new Pager(getSupportFragmentManager(), "3");
            viewPager3.setAdapter(viewPagerAdapter3);
        }else if (id.equals("4")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(3).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(3).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(3).setDoc3(doc);
            }
            viewPagerAdapter4 = new Pager(getSupportFragmentManager(), "4");
            viewPager4.setAdapter(viewPagerAdapter4);
        }else if (id.equals("5")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(4).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(4).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(4).setDoc3(doc);
            }
            viewPagerAdapter5 = new Pager(getSupportFragmentManager(), "5");
            viewPager5.setAdapter(viewPagerAdapter5);
        }else if (id.equals("6")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(5).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(5).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(5).setDoc3(doc);
            }
            viewPagerAdapter6 = new Pager(getSupportFragmentManager(), "6");
            viewPager6.setAdapter(viewPagerAdapter6);
        }else if (id.equals("7")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(6).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(6).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(6).setDoc3(doc);
            }
            viewPagerAdapter7 = new Pager(getSupportFragmentManager(), "7");
            viewPager7.setAdapter(viewPagerAdapter7);
        }else if (id.equals("8")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(7).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(7).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(7).setDoc3(doc);
            }
            viewPagerAdapter8 = new Pager(getSupportFragmentManager(), "8");
            viewPager8.setAdapter(viewPagerAdapter8);
        }else if (id.equals("9")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(8).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(8).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(8).setDoc3(doc);
            }
            viewPagerAdapter9 = new Pager(getSupportFragmentManager(), "9");
            viewPager9.setAdapter(viewPagerAdapter9);
        }else if (id.equals("10")){
            if (cameraFrom.equals("camera_one")){
                doc_arr.get(9).setDoc1(doc);
            }else if (cameraFrom.equals("camera_two")){
                doc_arr.get(9).setDoc2(doc);
            }else if (cameraFrom.equals("camera_three")){
                doc_arr.get(9).setDoc3(doc);
            }
            viewPagerAdapter10 = new Pager(getSupportFragmentManager(), "10");
            viewPager10.setAdapter(viewPagerAdapter10);
        }

        /*if (doc_arr.size() != 0) {
            for (int j = 0; j < doc_arr.size(); j++) {
                if (doc_arr.get(j).getId().equals("1")) {


                } else if (doc_arr.get(j).getId().equals("2")) {


                } else if (doc_arr.get(j).getId().equals("3")) {


                } else if (doc_arr.get(j).getId().equals("4")) {


                } else if (doc_arr.get(j).getId().equals("5")) {


                } else if (doc_arr.get(j).getId().equals("6")) {


                } else if (doc_arr.get(j).getId().equals("7")) {


                } else if (doc_arr.get(j).getId().equals("8")) {


                } else if (doc_arr.get(j).getId().equals("9")) {


                } else if (doc_arr.get(j).getId().equals("10")) {

                }
            }
        }*/

    }
}
