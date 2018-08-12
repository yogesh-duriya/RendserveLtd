package rendserve.rendserveltd.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rendserve.rendserveltd.Pojo.BasePojo;
import rendserve.rendserveltd.Pojo.DailyReportPojo;
import rendserve.rendserveltd.Pojo.LoginDataPojo;
import rendserve.rendserveltd.Pojo.LoginPojo;
import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.Pojo.ProjectPojo;
import rendserve.rendserveltd.Pojo.ReportDataPojo;
import rendserve.rendserveltd.adapter.ProjectAdapter;
import rendserve.rendserveltd.fragment.CurrentProject;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.fragment.ProjectDetailFragment;
import rendserve.rendserveltd.fragment.SavedLogsFragment;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;
import utils.RendserveConstant;

public class MainDeshboardActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;
    public ArrayList<ReportDataPojo> dailyReportsList;
    private Dialog pdf_picker_dialog;
    private TextView edusername,edemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_deshboard2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        dailyReportsList = new ArrayList<>();

    }
    public void initNavigationDrawer() {

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame,new CurrentProject(),"current_fragment").commit();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
      //  final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                Fragment fragment = null;
                String tag ="";
                switch (id){
                    case R.id.current_proect:
                        fragment = new CurrentProject();
                        tag = "current_fragment";
                        break;
                    /*case R.id.nav_movies:
                        //fragment = new CurrentProject();

                        break;
                    case R.id.nav_notifications:
                        Toast.makeText(getApplicationContext(),"Trash",Toast.LENGTH_SHORT).show();
                        //fragment = new CurrentProject();
                        break;
                    case R.id.nav_settings:
                        //fragment = new CurrentProject();
                        break;*/
                    case R.id.nav_logout:
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                        //fragment = new CurrentProject();
                        break;

                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment,tag);
                    fragmentTransaction.addToBackStack(null).commit();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);

        edusername = (TextView)header.findViewById(R.id.username);
        edemail = (TextView)header.findViewById(R.id.et_email);
        edusername.setText(getFromPrefs("username"));
        edemail.setText(getFromPrefs("email"));

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.rl_project:
                Log.d("xhxhxh", "ha ha ha");
                //Toast.makeText(this, "here is toast" , Toast.LENGTH_SHORT);
                String state_id = String.valueOf(view.getTag(R.string.state_id));
                String project_name = String.valueOf(view.getTag(R.string.project_name));
                /*Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = ProjectDetailFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ProjectDetailFragment fragmentt =  ProjectDetailFragment.newInstance(state_id,project_name);
                fragmentTransaction.replace(R.id.frame, fragmentt,"project_detail");
                fragmentTransaction.commit();
                break;
            case R.id.rl_saved_log:
                String savedLogId = (String) view.getTag(R.string.save_log_id);
                //startActivity(new Intent(MainDeshboardActivity.this, NewDailyReport.class));
                break;
            case R.id.iv_delete_log:
                String id = (String) view.getTag(R.string.save_log_id);
                deleteReport(id);
                SavedLogsFragment fragment1 = (SavedLogsFragment) getSupportFragmentManager().findFragmentByTag("saved");
                fragment1.notifyAdapter(id);
                //startActivity(new Intent(MainDeshboardActivity.this, NewDailyReport.class));
                break;
            case R.id.iv_delete:
                String project_id = String.valueOf(view.getTag(R.string.state_id));
                deleteProject(project_id);
                CurrentProject fragment = (CurrentProject) getSupportFragmentManager().findFragmentByTag("current_fragment");
                fragment.notifyAdapter(project_id);
                break;
            case R.id.rl_pdf:
                String pdfId = (String) view.getTag(R.string.pdf_id);
                String pdf_link = (String) view.getTag(R.string.pdf_link);
                showPdfDialoge(pdf_link);
                //startActivity(new Intent(MainDeshboardActivity.this, NewDailyReport.class));
                break;
        }
    }

    public void currentProject(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CurrentProject fragment =  new CurrentProject();
        fragmentTransaction.replace(R.id.frame, fragment,"current_fragment");
        fragmentTransaction.commit();
    }

    private void deleteProject(String id ){
        final ProgressDialog d = CustomProgressDialog.showLoading(this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<BasePojo> call = apiService.deleteProject(getFromPrefs(RendserveConstant.USER_ID),id);
        call.enqueue(new Callback<BasePojo>() {
            @Override
            public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                if(response.body().getStatus().equals("1")){


                }else
                    showToast(response.body().getMessage());
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

    private void deleteReport(String id ){
        final ProgressDialog d = CustomProgressDialog.showLoading(this);
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<BasePojo> call = apiService.deleteReport(getFromPrefs(RendserveConstant.USER_ID),id);
        call.enqueue(new Callback<BasePojo>() {
            @Override
            public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                if(response.body().getStatus().equals("1")){


                }else
                    showToast(response.body().getMessage());
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

    public void showPdfDialoge(final String pdf_link) {
        pdf_picker_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        pdf_picker_dialog.setContentView(R.layout.custom_imageupload_dialog);
        pdf_picker_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView camera = (TextView) pdf_picker_dialog.findViewById(R.id.cameraText);
        TextView gallery = (TextView) pdf_picker_dialog.findViewById(R.id.galleryText);
        TextView cancel = (TextView) pdf_picker_dialog.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        camera.setText("View");
        gallery.setText("Share");

        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //startActivity(new Intent(MainDeshboardActivity.this,WebviewActivity.class).putExtra("Url",pdf_link));
                Intent i =new Intent(Intent.ACTION_VIEW);
                String link = /*"http://docs.google.com/gview?embedded=true&url="+*/ RendserveConstant.PDF_URL+pdf_link;
                i.setDataAndType(Uri.parse(link),"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                pdf_picker_dialog.hide();
                pdf_picker_dialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    i.putExtra(Intent.EXTRA_TEXT, RendserveConstant.PDF_URL+pdf_link);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                pdf_picker_dialog.hide();
                pdf_picker_dialog.dismiss();
            }
        });
        pdf_picker_dialog.show();
    }

    public ArrayList<ProjectDataPojo> getProjectList(){
        return RendserveConstant.projectList;
    }
}

