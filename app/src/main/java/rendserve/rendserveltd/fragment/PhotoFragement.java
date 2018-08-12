package rendserve.rendserveltd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import rendserve.rendserveltd.Activity.NewDailyReport;
import rendserve.rendserveltd.Pojo.ImagePojo;
import rendserve.rendserveltd.R;


public class PhotoFragement extends Fragment {

    private View view;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12;
    private NewDailyReport ctx;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String pagerPosition;
    private ImagePojo imagePojo;

    public PhotoFragement() {
        // Required empty public constructor
    }

    public static PhotoFragement newInstance(String param1) {
        PhotoFragement fragment = new PhotoFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putSerializable(ARG_PARAM2, ip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pagerPosition = getArguments().getString(ARG_PARAM1);
            //imagePojo = (ImagePojo)getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this rendserve.rendserveltd.fragment
        view = inflater.inflate(R.layout.fragment_photo_fragement, container, false);

        initializer();
        setListener();

        if (ctx.img_doc_arr.size()!=0) {
            for (int i=0 ; i < ctx.img_doc_arr.size();i++) {
                if (ctx.img_doc_arr.get(i) != null) {
                    if (ctx.img_doc_arr.get(i).getId().equals(pagerPosition)) {
                        imagePojo = ctx.img_doc_arr.get(i);

                        if (imagePojo.getImageBitmap1() != null) {
                        /*setProfileImageInLayout(this, (int) getResources().getDimension(R.dimen.profile_image), (int) getResources().getDimension(R.dimen.profile_image),
                        CredtiSocietyConstant.BASE_IMAGE_URL + getFromPrefs(CredtiSocietyConstant.PROFILE_IMAGE), iv_profile_image);*/
                            img1.setImageBitmap(imagePojo.getImageBitmap1());
                            //Picasso.with(ctx).load(url).placeholder(R.mipmap.no_image).error(R.mipmap.no_image).into(image);
                        }
                        if (imagePojo.getImageBitmap2() != null)
                            img2.setImageBitmap(imagePojo.getImageBitmap2());

                        if (imagePojo.getImageBitmap3() != null)
                            img3.setImageBitmap(imagePojo.getImageBitmap3());

                        if (imagePojo.getImageBitmap4() != null)
                            img4.setImageBitmap(imagePojo.getImageBitmap4());

                        if (imagePojo.getImageBitmap5() != null)
                            img5.setImageBitmap(imagePojo.getImageBitmap5());

                        if (imagePojo.getImageBitmap6() != null)
                            img6.setImageBitmap(imagePojo.getImageBitmap6());

                        if (imagePojo.getImageBitmap7() != null)
                            img7.setImageBitmap(imagePojo.getImageBitmap7());

                        if (imagePojo.getImageBitmap8() != null)
                            img8.setImageBitmap(imagePojo.getImageBitmap8());

                        if (imagePojo.getImageBitmap9() != null)
                            img9.setImageBitmap(imagePojo.getImageBitmap9());

                        if (imagePojo.getImageBitmap10() != null)
                            img10.setImageBitmap(imagePojo.getImageBitmap10());

                        if (imagePojo.getImageBitmap11() != null)
                            img11.setImageBitmap(imagePojo.getImageBitmap11());

                        if (imagePojo.getImageBitmap12() != null)
                            img12.setImageBitmap(imagePojo.getImageBitmap12());

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

        img4.setOnClickListener(ctx);
        img4.setTag(R.string.fourth_camera,"camera_four");
        img4.setTag(R.string.id,pagerPosition);

        img5.setOnClickListener(ctx);
        img5.setTag(R.string.fivth_camera,"camera_five");
        img5.setTag(R.string.id,pagerPosition);

        img6.setOnClickListener(ctx);
        img6.setTag(R.string.sixth_camera,"camera_six");
        img6.setTag(R.string.id,pagerPosition);

        img7.setOnClickListener(ctx);
        img7.setTag(R.string.seventh_camera,"camera_seven");
        img7.setTag(R.string.id,pagerPosition);

        img8.setOnClickListener(ctx);
        img8.setTag(R.string.eighth_camera,"camera_eight");
        img8.setTag(R.string.id,pagerPosition);

        img9.setOnClickListener(ctx);
        img9.setTag(R.string.ninth_camera,"camera_nine");
        img9.setTag(R.string.id,pagerPosition);

        img10.setOnClickListener(ctx);
        img10.setTag(R.string.tenth_camera,"camera_ten");
        img10.setTag(R.string.id,pagerPosition);

        img11.setOnClickListener(ctx);
        img11.setTag(R.string.eleventh_camera,"camera_eleven");
        img11.setTag(R.string.id,pagerPosition);

        img12.setOnClickListener(ctx);
        img12.setTag(R.string.twelfth_camera,"camera_twelve");
        img12.setTag(R.string.id,pagerPosition);

    }

    private void initializer() {
        img1= (ImageView) view.findViewById(R.id.img1 );
        img2= (ImageView) view.findViewById(R.id.img2 );
        img3= (ImageView) view.findViewById(R.id.img3 );
        img4 = (ImageView)view.findViewById(R.id.img4);
        img5 = (ImageView)view.findViewById(R.id.img5);
        img6 = (ImageView)view.findViewById(R.id.img6);
        img7 = (ImageView)view.findViewById(R.id.img7);
        img8 = (ImageView)view.findViewById(R.id.img8);
        img9 = (ImageView)view.findViewById(R.id.img9);
        img10 = (ImageView)view.findViewById(R.id.img10);
        img11 = (ImageView)view.findViewById(R.id.img11);
        img12 = (ImageView)view.findViewById(R.id.img12);

    }


}
