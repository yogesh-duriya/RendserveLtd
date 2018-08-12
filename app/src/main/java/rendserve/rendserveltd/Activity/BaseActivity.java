package rendserve.rendserveltd.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import rendserve.rendserveltd.R;
import utils.RendserveConstant;

/**
 * Created by Mahesh on 20-11-2017.
 */

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    void startIntent(Context applicationContext, Class<LoginActivity> homeActivityClass)
    {
        startActivity(new Intent(applicationContext,homeActivityClass));
    }

    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    public ArrayList<String> findRejectedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm) && !shouldWeAsk(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (canMakeSmores()) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public boolean shouldWeAsk(String permission) {
        return (sharedPreferences.getBoolean(permission, true));
    }

    public boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void markAsAsked(String permission, SharedPreferences sharedPreferences) {
        this.sharedPreferences.edit().putBoolean(permission, false).apply();
    }

    public void clearMarkAsAsked(String permission) {
        sharedPreferences.edit().putBoolean(permission, true).apply();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void saveIntoPrefs(String key, String value,String email,String username) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.putString("email", email);
        edit.putString("username", username);
        edit.commit();
    }

    public void saveIntIntoPrefs(String key, int value) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void saveDoubleIntoPrefs(String key, double value) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(key, Double.doubleToRawLongBits(value));
        edit.commit();
    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key, RendserveConstant.DEFAULT_VALUE);
    }

    public int getIntFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public double getDoubleFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(0)));
    }


    public void saveBooleanIntoPrefs(String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean getBooleanFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public void clearPrefs() {
        SharedPreferences prefs = getSharedPreferences(RendserveConstant.PREF_NAME, MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    public SimpleDateFormat getDateFormat() {
        String myFormat = "yyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf;
    }

    public void setImageInLayout(Context ctx, int width, int height, String url, ImageView image) {
        if(url != null) {
            Picasso picasso = Picasso.with(ctx);
            picasso.load(url).resize(width, height).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
        }
    }
}

