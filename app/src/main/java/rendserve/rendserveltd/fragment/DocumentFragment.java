package rendserve.rendserveltd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import rendserve.rendserveltd.Activity.NewDailyReport;
import rendserve.rendserveltd.R;

public class DocumentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String pagerPosition;
    private String mParam2;
    private View view;
    private ImageView img1, img2, img3;
    private NewDailyReport ctx;

    public DocumentFragment() {
        // Required empty public constructor
    }

    public static DocumentFragment newInstance(String param1) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pagerPosition = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this rendserve.rendserveltd.fragment
        view = inflater.inflate(R.layout.fragment_photos, container, false);

        initializer();
        setListener();

        if (ctx.doc_arr.size()!=0) {
            for (int i=0 ; i < ctx.doc_arr.size();i++) {
                String k = String.valueOf(i+1);
                if (ctx.doc_arr.get(i) != null) {
                    if (k.equals(pagerPosition)) {
                        if (!ctx.doc_arr.get(i).getDoc1().equals("")) {
                            //img1.setBackgroundColor(getResources().getColor(R.color.white));
                            img1.setImageDrawable(null);
                            img1.setBackground(getResources().getDrawable(R.drawable.ic_pdf));
                        }
                        if (!ctx.doc_arr.get(i).getDoc2().equals("")) {
                            img2.setImageDrawable(null);
                            img2.setBackground(getResources().getDrawable(R.drawable.ic_pdf));
                        }
                        if (!ctx.doc_arr.get(i).getDoc3().equals("")) {
                            img3.setImageDrawable(null);
                            img3.setBackground(getResources().getDrawable(R.drawable.ic_pdf));
                        }
                    }
                }
            }
        }
        return view;
    }

    private void setListener() {

        ctx = ((NewDailyReport) getActivity());

        img1.setOnClickListener(ctx);
        img1.setTag(R.string.first_camera,"camera_one");
        img1.setTag(R.string.id,pagerPosition);

        img2.setOnClickListener(ctx);
        img2.setTag(R.string.second_camera,"camera_two");
        img2.setTag(R.string.id,pagerPosition);

        img3.setOnClickListener(ctx);
        img3.setTag(R.string.third_camera,"camera_three");
        img3.setTag(R.string.id,pagerPosition);

    }

    private void initializer() {
        img1= (ImageView) view.findViewById(R.id.doc_img_1 );
        img2= (ImageView) view.findViewById(R.id.doc_img_2 );
        img3= (ImageView) view.findViewById(R.id.doc_img_3 );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
