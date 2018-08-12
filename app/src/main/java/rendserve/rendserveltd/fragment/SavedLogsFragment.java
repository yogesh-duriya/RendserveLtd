package rendserve.rendserveltd.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rendserve.rendserveltd.Activity.MainDeshboardActivity;
import rendserve.rendserveltd.Activity.NewDailyReport;
import rendserve.rendserveltd.Pojo.DailyReportPojo;
import rendserve.rendserveltd.Pojo.SavedLogoPojo;
import rendserve.rendserveltd.Pojo.UploadDocPojo;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.adapter.SavedLogsAdapter;
import rendserve.rendserveltd.model.ApiClient;
import rendserve.rendserveltd.model.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomProgressDialog;
import utils.RendserveConstant;


public class SavedLogsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String project_id;
    private String project_name;
    private Button btn_edit;
    private RecyclerView rv_logs;
    private SavedLogsAdapter adapter;
    private MainDeshboardActivity ctx;

    View view;

    public SavedLogsFragment() {
        // Required empty public constructor
    }

    public static SavedLogsFragment newInstance(String param1, String param2) {
        SavedLogsFragment fragment = new SavedLogsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project_id = getArguments().getString(ARG_PARAM1);
            project_name = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved_logs, container, false);

        initialize();
        setListener();
        return view;
    }

    private void setListener() {
        btn_edit.setOnClickListener(this);
    }

    private void initialize(){
        ctx = ((MainDeshboardActivity)getActivity());
        btn_edit = (Button)view.findViewById(R.id.btn_edit);
        rv_logs = (RecyclerView) view.findViewById(R.id.rv_logs);

        ctx.dailyReportsList = new ArrayList<>();
        getLogs();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_edit:
                if(btn_edit.getText().toString().equals("EDIT")){
                    for (int i=0;i<ctx.dailyReportsList.size();i++){
                        ctx.dailyReportsList.get(i).setEdit(true);
                        adapter.notifyDataSetChanged();
                    }
                    btn_edit.setText("DONE");
                }else {
                    for (int i=0;i<ctx.dailyReportsList.size();i++) {
                        ctx.dailyReportsList.get(i).setEdit(false);
                        adapter.notifyDataSetChanged();
                    }
                    btn_edit.setText("EDIT");
                }
                break;
        }
    }

    public void getLogs(){

        final ProgressDialog d = CustomProgressDialog.showLoading(getActivity());
        d.setCanceledOnTouchOutside(false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<DailyReportPojo> call = apiService.dailyReport(ctx.getFromPrefs(RendserveConstant.USER_ID),project_id);
        call.enqueue(new Callback<DailyReportPojo>() {
            @Override
            public void onResponse(Call<DailyReportPojo> call, Response<DailyReportPojo> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                        ctx.dailyReportsList = response.body().getData();
                        setAdapter();
                        //name[0] = response.body().getData().toString();
                    } else
                        ctx.showToast(response.body().getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                d.dismiss();
            }

            @Override
            public void onFailure(Call<DailyReportPojo> call, Throwable t) {
                // Log error here since request failed
                System.out.println("retrofit hh failure " + t.getMessage());
                d.dismiss();
            }
        });

        /*for (int i=0;i<6;i++){
            SavedLogoPojo slp = new SavedLogoPojo();
            slp.setId(String.valueOf(i));
            slp.setProject_name("Project "+i);
            slp.setDate_created("2/2/2017");
            slp.setTime_created((12+i) + ":50");
            slp.setTemperature_type("F");
            slp.setTemperature("30");
            slp.setWeather("cloudy");
            slp.setPressure("50");
            slp.setEdit(false);
            savedLogoList.add(slp);
        }*/




    }

    private void setAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_logs.setLayoutManager(mLayoutManager);
        rv_logs.setItemAnimator(new DefaultItemAnimator());
        adapter = new SavedLogsAdapter(ctx.dailyReportsList, getActivity(),project_name);
        rv_logs.setAdapter(adapter);
    }

    public void notifyAdapter(String report_id){
        for (int i=0;i< ctx.dailyReportsList.size();i++){
            if (ctx.dailyReportsList.get(i).getId().equals(report_id)){
                ctx.dailyReportsList.remove(i);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
