package fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jupiter.on.tetsuo.colofulmusic.ColorPickFragment;
import com.jupiter.on.tetsuo.colofulmusic.HistoryFragment;
import com.jupiter.on.tetsuo.colofulmusic.HistorySingleFragment;
import com.jupiter.on.tetsuo.colofulmusic.HomeFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Home", "History", "Color", "Genre history"};

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new HistoryFragment();
            case 2:
                return new ColorPickFragment();
            case 3:
                return new HistorySingleFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TITLES.length;
    }
}
