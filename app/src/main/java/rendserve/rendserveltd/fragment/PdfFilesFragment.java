package rendserve.rendserveltd.fragment;

import android.content.Context;
import android.net.Uri;
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

import rendserve.rendserveltd.Activity.MainDeshboardActivity;
import rendserve.rendserveltd.R;
import rendserve.rendserveltd.adapter.PdfAdapter;
import rendserve.rendserveltd.adapter.SavedLogsAdapter;

public class PdfFilesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String project_id;
    private String project_name;
    private View view;
    private MainDeshboardActivity ctx;
    private RecyclerView rv_pdf;
    private PdfAdapter adapter;

    public PdfFilesFragment() {
        // Required empty public constructor
    }

     public static PdfFilesFragment newInstance(String param1, String param2) {
        PdfFilesFragment fragment = new PdfFilesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_pdf_files, container, false);
        initialize();
        //setListener();

        return view;
    }
    private void initialize(){
        ctx = ((MainDeshboardActivity)getActivity());
        //btn_edit = (Button)view.findViewById(R.id.btn_edit);
        rv_pdf = (RecyclerView) view.findViewById(R.id.rv_pdf);

       // ctx.dailyReportsList = new ArrayList<>();

        setAdapter();
    }

    private void setAdapter() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_pdf.setLayoutManager(mLayoutManager);
        rv_pdf.setItemAnimator(new DefaultItemAnimator());
        adapter = new PdfAdapter(ctx.dailyReportsList, getActivity(),project_name);
        rv_pdf.setAdapter(adapter);
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
    public void onResume() {
        super.onResume();
        setAdapter();
    }
}
