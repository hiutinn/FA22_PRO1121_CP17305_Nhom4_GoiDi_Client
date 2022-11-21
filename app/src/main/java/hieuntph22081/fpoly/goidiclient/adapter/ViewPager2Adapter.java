package hieuntph22081.fpoly.goidiclient.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hieuntph22081.fpoly.goidiclient.fragment.FeedbackFragment;
import hieuntph22081.fpoly.goidiclient.fragment.HomeFragment;
import hieuntph22081.fpoly.goidiclient.fragment.MyOrdersFragment;
import hieuntph22081.fpoly.goidiclient.fragment.ProfileFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MyOrdersFragment();
            case 2:
                return new FeedbackFragment();
            case 3:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
