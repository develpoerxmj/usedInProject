package com.example.android.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainHActivity extends AppCompatActivity {

    private static final String TAG = "<><><><>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_h);
        /**
         * 功能操作符
         * 1、delay()----延时一段时间发送事件
         * 2、doOnEach()---没发送一个事件都会回调该方法
         * 3、doOnNext()----Observable每发送onNext()时先调用
         * 4、doAfterNext()----Observable每发送onNext()后调用
         * 5、doOnComplete()----onComplete()之前调用
         * 6、doOnError()----onError()之前调用
         * 7、doOnSubscribe()----发送onSubscribe()前调用(订阅前)
         * 8、doOnDispose()----调用Disposable的dispose()方法后回调
         * 9、doOnLifecycle()----两个参数，1：onSubscribe之前回调，可取消订阅，2：onNext中取消订阅回调
         * 10、doOnTerminate()/doAfterTerminate()----发送事件完毕(onComplete)或异常(onError)终止(之前/之后)回调
         * 11、doFinally()----所有事件发送完毕后回调
         * 12、onErrorReturn()----接受onError时回调，返回值回调onNext，并正常结束事件序列
         * 13、onErrorResumeNext()----接收onError时回调，返回一个新的Observable，结束事件序列
         * 14、onExceptionResumeNext()----与onErrorResumeNext()一样，但只能捕捉异常
         * 15、retry()----出现错误时，重新发送所有事件
         * 16、retryUntil()----出现错误，判断是否继续发送事件，TRUE 不发，FALSE 发
         * 17、retryWhen()----发生错误时回调，将错误传给新的Observable，新的Observable发送error事件，则以前的Observable不会再发送事件，新的Observable发送next事件，则以前的Observable重新发送事件
         * 18、repeat()----重复发送Observable的事件
         * 19、repeatWhen()----返回新的Observable，如果返回onComplete或onError，以前的Observable不会发送事件，否则会发送事件
         * 20、subscribeOn()----指定Observable的线程，多次调用第一次有效
         * 21、observeOn()----指定观察者线程，指定一次生效一次
         *      RxJava中的调度器
         *          Schedulers.computation()----用于使用计算任务，如事件循环和回调
         *          Schedulers.immediate()----当前线程
         *          Schedulers.io()----用于IO密集任务，如io操作
         *          Schedulers.newThread()----开启新线程
         *          AndroidSchedulers.mainThread()----UI线程
         */
//        delay();
//        doOnEach();
//        doOnNext();
//        doAfterNext();
//        doOnComplete();
//        doOnDispose();
//        doOnLifecycle();
//        doOnTerminate();
//        doFinally();
//        onErrorReturn();
//        onErrorResumeNext();
//        onExceptionResumeNext();
//        retry();
//        retryUntil();
//        retryWhen();
//        repeat();
//        repeatWhen();
//        subscribeOn();
        observeOn();
    }

    private void observeOn() {
        Observable.just(1, 2, 3).observeOn(Schedulers.newThread())
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.i(TAG, "apply: " + Thread.currentThread().getName());
                        return Observable.just("apply" + integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "accept: " + Thread.currentThread().getName());
                Log.i(TAG, "accept: " + s);
            }
        });
    }

    private void subscribeOn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "subscribe: " + Thread.currentThread().getName());
                emitter.onNext(1);
            }
        })
//                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void repeatWhen() {
        Observable.just(1, 2, 3).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return Observable.just(4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable);
            }
        });
    }

    private void repeat() {
        Observable.just(1, 2, 3).repeat(3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void retryWhen() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new NullPointerException("NullPointerException"));
                emitter.onNext(2);
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable.getMessage().equals("NullPointerException")){
                            return Observable.error(new NullPointerException());
                        }
                        return Observable.just(1);
                    }
                });
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable);
            }
        });
    }

    private void retryUntil() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new NullPointerException());
            }
        }).retryUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Exception {
                return true;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "throwable: ");
            }
        });
    }

    private void retry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new NullPointerException("Exception"));
            }
        }).retry(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "Throwable: " + throwable);
            }
        });
    }

    private void onExceptionResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
//                emitter.onError(new Throwable("Throwable"));
//                emitter.onError(new Error("Error"));
                emitter.onError(new Exception("Exception"));
            }
        })
                .onExceptionResumeNext(new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                Log.i(TAG, "onExceptionResumeNext: ");
                observer.onNext(2);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable);
            }
        });
    }

    private void onErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new Exception());
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                return Observable.just(2, 3, 4);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable);
            }
        });
    }

    private void onErrorReturn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new NullPointerException());
            }
        }).onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                return 2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "accept: " + throwable);
            }
        });
    }

    private void doFinally() {
    }

    private void doOnTerminate() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onError(new NullPointerException());
//                emitter.onComplete();
            }
        }).doOnTerminate(new Action() {
            @Override
            public void run() throws Exception {
                Log.i("<><><><>", "run: ");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void doOnLifecycle() {
        Observable.just(1, 2, 3).doOnLifecycle(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i("<><><><>", "accept: ");
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.i("<><><><>", "run: ");
            }
        }).subscribe(new Observer<Integer>() {
            Disposable mDisposable;
            @Override
            public void onSubscribe(Disposable d) {
                Log.i("<><><><>", "onSubscribe: ");
                mDisposable = d;
            }
            @Override
            public void onNext(Integer integer) {
                Log.i("<><><><>", "onNext: " + integer);
                mDisposable.dispose();
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void doOnDispose() {
        Observable.just(1, 2, 3).doOnDispose(new Action() {
            @Override
            public void run() throws Exception {
                Log.i("<><><><>", "run: ");
            }
        }).subscribe(new Observer<Integer>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
            @Override
            public void onNext(Integer integer) {
                Log.i("<><><><>", "onNext: " + integer);
                disposable.dispose();
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void doOnComplete() {
        Observable.just(1, 2, 3).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.i("<><><><>", "run: ");
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Log.i("<><><><>", "run: complete");
            }
        });
    }

    private void doAfterNext() {
        Observable.just(1, 2, 3).doAfterNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "after: " + integer);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        });
    }

    private void doOnNext() {
        Observable.just(1, 2, 3).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        });
    }

    private void doOnEach() {
        Observable.just(1, 2, 3)
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.i("<><><><>", "integerNotification: " + integerNotification.getValue());
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        });
    }

    private void delay() {
        Observable.just(1, 2, 3).delay(10, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("<><><><>", "accept: " + integer);
                    }
                });
    }
}
