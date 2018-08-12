package rendserve.rendserveltd.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import rendserve.rendserveltd.Activity.CreateProject;
import rendserve.rendserveltd.Activity.MainDeshboardActivity;
import rendserve.rendserveltd.Activity.NewDailyReport;
import rendserve.rendserveltd.Pojo.ProjectDataPojo;
import rendserve.rendserveltd.Pojo.ProjectPojo;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.adapter.ProjectAdapter;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;
import utils.RendserveConstant;

public class CurrentProject extends Fragment implements View.OnClickListener {

    private TextView add_newProject,tv_no_project;
    private RelativeLayout newdailyreport,rl_edit_n_report;
    private RecyclerView rv_projects;

    View view;
    private ProjectAdapter adapter;
    private MainDeshboardActivity ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this rendserve.rendserveltd.fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        initializer();
        setLisener();

        return view;
    }

    private void initializer() {

        add_newProject = (TextView)view.findViewById(R.id.add_newProject);
        newdailyreport = (RelativeLayout) view.findViewById(R.id.newdailyreport);
        rv_projects = (RecyclerView) view.findViewById(R.id.rv_projects);
        ctx = ((MainDeshboardActivity)getActivity());
        RendserveConstant.projectList = new ArrayList<>();
        getProjects();
    }

    private void setLisener() {
        add_newProject.setOnClickListener(this);
        newdailyreport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.add_newProject:
                startActivity(new Intent(getActivity(),CreateProject.class));
                break;
            case R.id.newdailyreport:
                startActivity(new Intent(getActivity(), NewDailyReport.class));
                break;
           /* case R.id.rl_project:
                Log.d("xhxhxh", "ha ha ha");
                Toast.makeText(getActivity(), "here is toast" , Toast.LENGTH_SHORT);
                break;*/
        }
    }

    private void getProjects(){

        final ProgressDialog d = CustomProgressDialog.showLoading(getActivity());
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ProjectPojo> call = apiService.getProject(ctx.getFromPrefs(RendserveConstant.USER_ID));
        call.enqueue(new Callback<ProjectPojo>() {
            @Override
            public void onResponse(Call<ProjectPojo> call, Response<ProjectPojo> response) {
                if(response.body().getStatus().equals("1")){
                    RendserveConstant.projectList= response.body().getData();
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_projects.setLayoutManager(mLayoutManager);
                    rv_projects.setItemAnimator(new DefaultItemAnimator());
                    adapter = new ProjectAdapter(RendserveConstant.projectList, getActivity());
                    rv_projects.setAdapter(adapter);
                }else
                    ctx.showToast(response.body().getMessage());
                d.dismiss();
            }

            @Override
            public void onFailure(Call<ProjectPojo> call, Throwable t) {
                // Log error here since request failed
                System.out.println("retrofit hh failure " + t.getMessage());
                d.dismiss();
            }
        });

    }

    public void notifyAdapter(String project_id){
        for (int i=0;i< RendserveConstant.projectList.size();i++){
            if (RendserveConstant.projectList.get(i).getId().equals(project_id)){
                RendserveConstant.projectList.remove(i);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getProjects();
    }
}
