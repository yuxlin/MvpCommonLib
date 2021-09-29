package cn.kaer.common.rx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;


public class RxManage {
    public RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables = new HashMap();
    private CompositeDisposable mCompositeSubscription = new CompositeDisposable();
    public RxManage() {
    }

    public void on(String eventName, Consumer<Object> action1) {
        Observable mObservable = this.mRxBus.register(eventName);
        this.mObservables.put(eventName, mObservable);
        this.mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }));
    }

    public void add(Disposable m) {
        this.mCompositeSubscription.add(m);
    }

    public void remove(Disposable m) {
        this.mCompositeSubscription.remove(m);
    }

    public void clear() {
        this.mCompositeSubscription.clear();
        Iterator var1 = this.mObservables.entrySet().iterator();

        while(var1.hasNext()) {
            Map.Entry entry = (Map.Entry)var1.next();
            this.mRxBus.unregister(entry.getKey(), (Observable)entry.getValue());
        }

    }

    public void post(Object tag, Object content) {
        this.mRxBus.post(tag, content);
    }
}
