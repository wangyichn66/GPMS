package hk.com.rubyicl.gpms.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import hk.com.rubyicl.gpms.R;
import hk.com.rubyicl.gpms.fragment.OneFragment;
import hk.com.rubyicl.gpms.fragment.ThreeFragment;
import hk.com.rubyicl.gpms.fragment.TwoFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    private List<Fragment> fragments;
    private Fragment mCurrentFragment = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, getColor(R.color.search_layout_bg), 0);
        StatusBarUtil.setLightMode(this);
        initFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
    }

    private void initFragment() {
        if (fragments != null) {
            fragments.clear();
        } else {
            fragments = new ArrayList<>();
            fragments.add(new OneFragment());
            fragments.add(new TwoFragment());
            fragments.add(new ThreeFragment());
//            fragments.add(new FourFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_one:
                showFragment(0);
                return true;
            case R.id.navigation_two:
                showFragment(1);
                return true;
            case R.id.navigation_three:
                showFragment(2);
                return true;
//            case R.id.navigation_four:
//                showFragment(3);
//                return true;
        }
        return false;
    }

    private void showFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            Fragment fragment = fragments.get(position);
            if (null != fragment && mCurrentFragment != fragment) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment);
                }
                mCurrentFragment = fragment;
                if (!fragment.isAdded()) {
                    transaction.add(R.id.page_fragment_container, fragment);
                } else {
                    transaction.show(fragment);
                }
                transaction.commit();
            }
        }
    }

    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}