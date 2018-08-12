package utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

import rendserve.rendserveltd.Pojo.ProjectDataPojo;

/**
 * Created by abhi on 24-01-2018.
 */

public class RendserveConstant {

    public final static String PREF_NAME = "rendserve.rendserveltd.prefs";
    public final static String BASE_URL = "http://52.14.98.152:3008";
    public final static String BASE_IMAGE_URL = "http://52.14.98.152:3008/uploads/";
    public final static String PDF_URL = "http://52.14.98.152:3008/uploads/";

    public final static String DEFAULT_VALUE = "";
    public final static String USER_ID = "user_id";
    public final static String NAME = "name";
    public static final String EMAIL_REGEX = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    public static final String PASSWORD_REGEX = "(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
    public static ArrayList<ProjectDataPojo> projectList;
    public static Bitmap map_bitmap;
}
