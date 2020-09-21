package hk.com.rubyicl.gpms.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/4 上午 09:47
 *     description:
 *  <pre>
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayout() != 0) {
            view = inflater.inflate(getLayout(), container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    protected abstract int getLayout();

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
