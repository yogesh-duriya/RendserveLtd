package rendserve.rendserveltd.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import rendserve.rendserveltd.Activity.CreateProject;
import rendserve.rendserveltd.Activity.MainDeshboardActivity;
import rendserve.rendserveltd.Activity.NewDailyReport;
import rendserve.rendserveltd.R;


public class ProjectDetailFragment extends Fragment implements View.OnClickListener {
    //spartacus
    //topcorn
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String projectId;
    private String projectName;
    private TextView tv_edit_project, tv_change_project, tv_project_name, tv_new_daily_report;
    private TabLayout tl_tablayout;
    private View view;
    private SavedLogsFragment savedLogsFragment;
    PdfFilesFragment pdfFilesFragment;
    PhotosFragment photosFragment;
    DocumentFragment documentFragment;
    private MainDeshboardActivity ctx;
    private String tag;
    // private OnFragmentInteractionListener mListener;

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    public static ProjectDetailFragment newInstance(String param1, String param2) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
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
            projectId = getArguments().getString(ARG_PARAM1);
            projectName = getArguments().getString(ARG_PARAM2);
            Log.d("project id is :",projectName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_project_detail, container, false);
        initializer();
        setListener();
        return view;
    }

    private void initializer() {
        ctx = ((MainDeshboardActivity)getActivity());

        tv_edit_project = (TextView)view.findViewById(R.id.tv_edit_project);
        tv_change_project = (TextView)view.findViewById(R.id.tv_change_project);
        tv_project_name = (TextView)view.findViewById(R.id.tv_project_name);
        tv_new_daily_report = (TextView)view.findViewById(R.id.tv_new_daily_report);
        tl_tablayout = (TabLayout) view.findViewById(R.id.tl_tablayout);

        tv_project_name.setText(projectName);
        setupTabLayout();
    }

    private void setupTabLayout() {
        savedLogsFragment = SavedLogsFragment.newInstance(projectId, projectName);
        tl_tablayout.addTab(tl_tablayout.newTab().setText("Saved logs"));

        pdfFilesFragment= PdfFilesFragment.newInstance(projectId, projectName);
        tl_tablayout.addTab(tl_tablayout.newTab().setText("PDF Files"));


        photosFragment = new PhotosFragment();
        tl_tablayout.addTab(tl_tablayout.newTab().setText("Photos"));

        documentFragment = new DocumentFragment();
        tl_tablayout.addTab(tl_tablayout.newTab().setText("Docs"));

        setCurrentTabFragment(0);
    }

    private void setListener() {

        tl_tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tv_change_project.setOnClickListener(this);
        tv_new_daily_report.setOnClickListener(this);
        tv_edit_project.setOnClickListener(this);
    }
    private void setCurrentTabFragment(int tabPosition)
    {
        tag ="";
        switch (tabPosition)
        {
            case 0 :
                tag="saved";
                replaceFragment(savedLogsFragment);
                break;
            case 1 :
                tag="pdf";
                replaceFragment(pdfFilesFragment);
                break;
            case 2 :
                tag="photo";
                replaceFragment(photosFragment);
                break;
            case 3 :
                tag="photo";
                replaceFragment(photosFragment);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment,tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }



    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.tv_change_project :
               ((MainDeshboardActivity)getActivity()).currentProject();
                break;
            case R.id.tv_new_daily_report:
                startActivity(new Intent(getActivity(), NewDailyReport.class)
                        .putExtra("project_id", projectId));
                break;
            case R.id.tv_edit_project:
                startActivity(new Intent(ctx, CreateProject.class)
                        .putExtra("project_id",projectId));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //tl_tablayout.getTabAt(0)
        SavedLogsFragment customFragment = (SavedLogsFragment)getChildFragmentManager().findFragmentByTag("saved");
        if (customFragment!=null)
            customFragment.getLogs();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
