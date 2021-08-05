package cn.kaer.common.bases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.kaer.common.mvp.BasePresenter;
import cn.kaer.common.mvp.IView;
import cn.kaer.common.rx.RxManage;
import cn.kaer.common.utils.TUtil;

/**
 * User: yxl
 * Date: 2020/5/4
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView {

    protected String TAG = getClass().getSimpleName();
    private View mInflate;
    protected P mPresenter;
    private Unbinder mBind;
    protected RxManage mRxManage = new RxManage();

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return init(inflater, container);
    }

    private View init(LayoutInflater inflater, ViewGroup container) {

        mInflate = inflater.inflate(initLayout(), null);
        mBind = ButterKnife.bind(this, mInflate);
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.setView(this);
        }

        initView(mInflate);
        initData();
        initEvent();

        if (mPresenter != null) {
            mPresenter.onStart();
        }
        return mInflate;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return mInflate.findViewById(id);
    }

    protected abstract int initLayout();


    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void initEvent();


    public void startActivityC(Class cls) {
        super.startActivity(new Intent(getContext(), cls));
    }

    public void startActivityC(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        this.mPresenter = null;

        if (mBind != null) {
            mBind.unbind();
        }

        mRxManage.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void replaceFragment(int container, Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                /*    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)*/
                .replace(container, fragment, fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    public void addFragment(int container, Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .add(container, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    public void removeFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public void hideFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();

    }
}
