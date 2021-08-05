/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.kaer.common.mvp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleObserver;
import cn.kaer.common.rx.RxManage;

/**
 * User: yxl
 * Date: 2021/2/18
 */
public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter, LifecycleObserver {
    protected final String TAG = this.getClass().getSimpleName();
    protected M mModel;
    protected V mView;
    protected RxManage mRxManage;
    protected Context mContext;

    public BasePresenter() {
        this.mModel = createModel();
        mRxManage = new RxManage();

    }


    public void setView(V iView) {
        mView = iView;
        mContext = mView.getContext();
    }

    @Override
    public void onDestroy() {
        mRxManage.clear();
    }

    protected abstract M createModel();

    @NonNull
    protected String getString(@StringRes int id) {
        return mView.getContext().getString(id);
    }

}
