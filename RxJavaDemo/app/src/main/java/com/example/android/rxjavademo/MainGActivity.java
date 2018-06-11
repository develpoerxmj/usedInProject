package com.example.android.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainGActivity extends AppCompatActivity {

    private static final String TAG = "<><><><>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_g);
        /**
         * 过滤操作符
         * 1、filter()----通过一定逻辑过滤Observable发送的事件，返回TRUE发送事件，返回FALSE不发送
         * 2、ofType()----过滤不符合指定类型的事件,
         * 3、skip()/skipLast()----跳过某些事件(skip----前面事件。skipLast----后面事件)
         * 4、distinct()----过滤事件中的重复事件
         * 5、distinctUntilChanged()----过滤掉连续重复的事件
         * 6、take()/takeLast()----控制观察者接受事件的数量(take----前面事件。takeLast----后面事件)
         * 7、debounce()/throttleWithTimeout()----两个事件发送的事件间隔小于设定的时间，则前一个事件不会发送给观察者
         * 8、firstElement()/lastElement()----取事件序列的第一个/最后一个元素
         * 9、elementAt()/elementAtOrError()----指定接受某事件，但是越界时不会有结果，但elementAtOrError()会抛出异常信息
         */
//        filter();
//        ofType();
//        skip();
//        distinct();
//        distinctUntilChanged();
//        take();
//        debounce();
//        throttleWithTimeout();
//        element();
//        elementAt();

        /**
         * 条件操作符
         * 1、all()----判断每个事件是否满足设置的条件。满足TRUE，不满足FALSE
         * 2、takeWhile()----Observable满足条件时发送数据，不满足时终止发送
         * 3、skipWhile()----Observable满足条件时跳过数据，不满足时开始发送数据
         * 4、takeUntil()----满足条件时，下一次事件不再发送
         * 5、skipUntil()----skipUntil中的Observable开始发送数据，原来的Observable才开始发送数据
         * 6、sequenceEqual()----判断两个Observable发送的事件列是否相同
         * 7、contains()----判断Observable发送的事件序列是否含有指定数据
         * 8、isEmpty()----判断事件序列是否为空
         * 9、amb()----传入Observable集合，但只发送先发送事件的Observable的事件
         * 10、defaultIfEmpty()----只发送onComplete()事件时，发送一个默认值
         */
//        all();
//        takeWhile();
//        skipWhile();
//        takeUntil();
//        skipUntil();
//        sequenceEqual();
//        contains();
//        isEmpty();
//        amb();
        defaultIfEmpty();
    }

    private void defaultIfEmpty() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();
            }
        }).defaultIfEmpty(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void amb() {
        List<ObservableSource<Integer>> list = new ArrayList<>();
        list.add(Observable.just(1, 2, 3).delay(3, TimeUnit.SECONDS));
        list.add(Observable.just(4, 5, 6).delay(2, TimeUnit.SECONDS));
        Observable.amb(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void isEmpty() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();
            }
        }).isEmpty().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.i(TAG, "accept: " + aBoolean);
            }
        });
    }

    private void contains() {
        Observable.just(1, 2, 3, 4).contains(1).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.i(TAG, "accept: " + aBoolean);
            }
        });
    }

    private void sequenceEqual() {
        Observable.sequenceEqual(Observable.just("1", "2", "3"), Observable.just("1", "2", "3"))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.i(TAG, "accept: " + aBoolean);
                    }
                });
    }

    private void skipUntil() {
        //skipUntil(Observable):Observable参数不会发送事件给观察者
        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                .skipUntil(Observable.interval(3, TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "accept: " + aLong);
                    }
                });
    }

    private void takeUntil() {
        Observable.just(1, 2, 3, 2, 4, 5, 3).takeUntil(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer > 4;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                .takeUntil(Observable.interval(3, 5,TimeUnit.SECONDS))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "accept: " + aLong);
                    }
                });
    }

    private void skipWhile() {
        Observable.just(1, 2, 3, 4, 5, 2, 1, 6)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void takeWhile() {
        Observable.just(4, 5, 2, 3, 4, 5)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void all() {
        Observable.just(1, 2, 3, 4)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;
                    }
                }).map(new Function<Boolean, String>() {
            @Override
            public String apply(Boolean aBoolean) throws Exception {
                if (aBoolean){
                    return "满足条件";
                }
                return "不满足条件";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "accept: " + s);
            }
        });
    }

    private void elementAt() {
        Observable.just(1, 2, 3, 4).elementAt(3).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
        Observable.just(1, 2, 3, 4).elementAtOrError(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }

    private void element() {
        Observable.just(1, 2, 3, 4).firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
        Observable.just(1, 2, 3, 4).lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void throttleWithTimeout() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
//                Thread.sleep(1100);
                Thread.sleep(900);
                emitter.onNext(2);
            }
        }).throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
//                Thread.sleep(1100);
                Thread.sleep(900);
                emitter.onNext(2);
            }
        }).debounce(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void take() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void distinctUntilChanged() {
        Observable.just(1, 2, 2, 4, 6, 4)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void distinct() {
        Observable.just(1, 2, 2, 4, 6, 4)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void skip() {
        Observable.just(1, 2, 3, 4, 5, 6)
                .skip(2)
                .skipLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept: " + integer);
                    }
                });
    }

    private void ofType() {
        Observable.just(1, 2, "3", 4, "5", 6)
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "accept: " + s);
                    }
                });
    }

    private void filter() {
        Observable.just(1, 2, 3 ,4 ,5, 6)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        if (integer < 5){
                            return true;
                        }
                        return false;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept: " + integer);
            }
        });
    }
}
