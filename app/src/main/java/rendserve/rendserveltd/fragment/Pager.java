package rendserve.rendserveltd.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rendserve.rendserveltd.Pojo.ImagePojo;

public class Pager extends FragmentPagerAdapter {

    String pos;
    ImagePojo ip;

    public Pager(FragmentManager fm, String i) {
        super(fm);
        pos = i;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Textsave();
        }
        else if (position == 1)
        {
            fragment = PhotoFragement.newInstance(pos);
        }
        else if (position == 2)
        {
            fragment = DocumentFragment.newInstance(pos);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Notes";
        }
        else if (position == 1)
        {
            title = "Photos";
        }
        else if (position == 2)
        {
            title = "Documents";
        }
        return title;
    }
}
