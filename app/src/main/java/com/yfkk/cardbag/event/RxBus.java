package com.yfkk.cardbag.event;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RxBus {
    private static volatile RxBus sInstance;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxBus getInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 发送事件(post event)
     *
     * @param event : event object(事件的内容)
     */
    public void post(Object event) {
        mEventBus.onNext(event);
    }

    /**
     * @param cls :保证接受到制定的类型
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> cls) {
        // ofType起到过滤的作用,确定接受的类型
        return mEventBus.onBackpressureLatest().ofType(cls);//此处采用Rxjava背压防止事件发生过快
    }

    class Message {
        int code;
        Object event;

        public Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }
}