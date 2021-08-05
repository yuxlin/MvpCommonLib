package cn.kaer.common.bases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.kaer.common.mvp.BasePresenter;
import cn.kaer.common.mvp.IView;
import cn.kaer.common.rx.RxManage;
import cn.kaer.common.utils.TUtil;
import cn.kaer.common.utils.WindowsUtils;

/**
 * User: yxl
 * Date: 2020/5/4
 */
@SuppressLint("Registered")
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView {
    public String TAG = getClass().getSimpleName();
    private Toolbar mToolbar;
    protected RxManage mRxManage;
    protected Fragment currentFragment;
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null
    private Unbinder mBind;
    private Menu mMenu;

    private QMUITipDialog mQmuiTipLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowsUtils.setTranslucentStatus(this, isDeepStatusBar(), isHideNavigation());
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.setView(this);
        }

        if (getContentView() != 0) {
            setContentView(getContentView());
        }
        mBind = ButterKnife.bind(this);

        initBase();

        initView();

        initData();

        initEvent();

        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    public interface OnTabActivityResultListener {
        public void onTabActivityResult(int requestCode, int resultCode, Intent data);
    }


    protected void initBase() {
        mRxManage = new RxManage();
        mToolbar = findViewById(getToolBarId());


        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (!isHideBackButton()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            //   getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isHideNavigation()) { //切换到别的有导航按钮的页面再切换来会显示导航按钮所以在Onresume中再切换一遍
            WindowsUtils.setTranslucentStatus(this, isDeepStatusBar(), isHideNavigation());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        this.mPresenter = null;
        if (mBind != null) {
            mBind.unbind();
        }
        mRxManage.clear();
    }


    public abstract int getContentView();

    public abstract int getToolBarId();

    public abstract int getMenuId();

    public abstract void initView();

    public abstract void initData();

    public abstract void initEvent();

    public boolean isDeepStatusBar() { //是否是深色状态栏
        return true;
    }

    public boolean isHideNavigation() {
        return false;
    }

    public boolean isHideBackButton() {
        return false;
    }


    protected RxManage getRxManage() {
        return mRxManage;
    }

    protected BaseActivity getParentActivity() {
        return this;
    }


    public void replaceFragment(int container, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                /*     .setCustomAnimations(R.animator.fragment_enter,R.animator.fragment_enter)*/

                .replace(container, fragment, fragment.getClass().getSimpleName())

                .commitAllowingStateLoss();

        currentFragment = fragment;
    }

    public void addFragment(int container, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(container, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
        currentFragment = fragment;
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                remove(fragment)

                .commitAllowingStateLoss();

    }

    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        currentFragment = fragment;
    }

    public void startActivityC(Class cls) {
        super.startActivity(new Intent(this, cls));
    }

    public void startActivityC(Intent intent) {
        super.startActivity(intent);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = getMenuId();
        if (menuId != 0) {
            getMenuInflater().inflate(menuId, menu);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Context getContext() {
        return this;
    }
    /*
    *    Observable.create((ObservableOnSubscribe<Boolean>) e -> {

                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(dataList -> {

                }, Throwable::printStackTrace);
    * */

    public void showTipResultDialog(boolean isSuccess, String s) {
        new QMUITipDialog.Builder(this)
                .setIconType(isSuccess ? QMUITipDialog.Builder.ICON_TYPE_SUCCESS : QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(s)
                .create()
                .show();
    }

    public void showTipProgress(CharSequence msg) {
        if (mQmuiTipLoadDialog == null) {
            mQmuiTipLoadDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg)
                    .create();
        }

        mQmuiTipLoadDialog.show();
    }

    public void dismissTipProgress() {
        if (mQmuiTipLoadDialog != null && mQmuiTipLoadDialog.isShowing()) {
            mQmuiTipLoadDialog.dismiss();
        }
    }
}