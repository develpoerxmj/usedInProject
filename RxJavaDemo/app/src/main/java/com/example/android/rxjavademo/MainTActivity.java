package com.example.android.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

public class MainTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);
        /**
         * 组合操作符
         * 1、concat()----多个被观察者组合在一起，按照之前发送顺序发送事件，最多4个
         * 2、concatArray()----与concat()一样，但数量可大于4
         * 3、merge()----与concat()一样，但并行发送事件，数量最多4
         * 4、mergeArray()----与merge()一样，但数量不限
         *              concat()与merge()的区别：
         *                  concat()：事件发送是串行的
         *                  merge()：事件发送是并行的
         *                  如两个被观察者ObservableA(A1, A2, A3)，ObservableB(B1, B2, B3)----按照时间线
         *                  concat()的结果为----A1, A2, A3, B1, B2, B3
         *                  merge()的结果为----A1, B1, A2, B2, A3, B3
         * 5、concatDelayError()/mergeDelayError()----使用concat()/merge()时，如果其中一个被观察者发送了onError()事件，则所有被观察者会停止发送事件，
         *                                            concatDelayError()/mergeDelayError()可以使所有被观察者发送完事件后再执行onError()事件
         * 6、concatDelayArrayError()/mergeDelayArrayError()----被观察者数量不限
         * 7、zip()----合并多个被观察者，按照各个被观察者发送事件的顺序一个个结合，最终发送事件数量与被观察者中事件最少的一致
         * 8、combineLatest()----所有的Observable都发送事件之后，其中一个Observable发送事件，会与其他Observable最新发送的事件结合
         * 9、combineLatestDelayError()----与combineLatest()一样，但可延时onError()事件
         *          zip()----按个数和并
         *          combineLatest()----按时间合并
         * 10、reduce()----把被观察者需要发送的事件聚合成1个事件发送
         * 11、collect()----Observable发送的数据收集到一个数据结构里
         * 12、startWith()/startWithArray()----发送事件前追加一个/多个事件
         * 13、count()----返回Observable发送事件的数量
         */
//        concat();
//        concatArray();
//        merge();
//        delayError();
//        zip();
//        combineLatest();
//        reduce();
//        collect();
//        startWithOrStartWithArray();
        count();
    }

    private void count() {
        Observable.just("2", "4", "6").count().subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i("<><><><>", "accept: " + aLong);
            }
        });
    }

    private void startWithOrStartWithArray() {
        //多个startWith/StartWithArray时，后调用先追加
        Observable.just(1, 2, 3, 4)
                .startWith(Observable.just(5, 6, 7))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("<><><><>", "accept: " + integer);
                    }
                });
    }

    private void collect() {
        Observable.just(1, 2, 3, 4).collect(new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return new ArrayList<>();
            }
        }, new BiConsumer<List<Integer>, Integer>() {
            @Override
            public void accept(List<Integer> list, Integer integer) throws Exception {
                list.add(integer);
            }
        }).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> list) throws Exception {
                for (int i = 0; i < list.size(); i++){
                    Log.i("<><><><>", "accept: " + list.get(i));
                }
            }
        });
    }

    private void reduce() {
        //前2个事件聚合，其结果在与后一个数据聚合直到没有事件
        Observable.just("1", "2", "3", "4").reduce(new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Exception {
                Log.i("<><><><>", "apply: " + s + "=" + s2);
                return s + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><>", "accept: " + s);
            }
        });
        /**
         * 结果为
         *       apply: 1=2
         *       apply: 12=3
         *       apply: 123=4
         *       accept: 1234
         */
    }

    private void combineLatest() {
        Observable.combineLatest(
                Observable.intervalRange(1, 3, 1, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "A===A" + aLong);
                        return "A" + aLong;
                    }
                }),
                Observable.intervalRange(1, 4, 2, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "B===B" + aLong);
                        return "B" + aLong;
                    }
                }),
                Observable.intervalRange(1, 5, 3, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "C===C" + aLong);
                        return "C" + aLong;
                    }
                }),
                new Function3<String, String, String, String>() {
                    @Override
                    public String apply(String s, String s2, String s3) throws Exception {
                        return s + ":" + s2 + ":" + s3;
                    }
                }
        ).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><>", "accept: " + s);
            }
        });
    }

    private void zip() {
        Observable.zip(
                Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "A===A" + aLong);
                        return "A" + aLong;
                    }
                }),
                Observable.intervalRange(1, 5, 2, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "B===B" + aLong);
                        return "B" + aLong;
                    }
                }),
                Observable.intervalRange(1, 3, 3, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        Log.i("<><><><>", "apply: " + "C===C" + aLong);
                        return "C" + aLong;
                    }
                }),
                new Function3<String, String, String, String>() {
                    @Override
                    public String apply(String s, String s2, String s3) throws Exception {
                        return s + ":" + s2 + ":" + s3;
                    }
                }
        ).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><>", "accept: " + s);
            }
        });
    }

    private void delayError() {
        Observable.mergeDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onError(new NullPointerException());
                    }
                }),
                Observable.just(3, 4, 5)
        ).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><>", "accept: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i("<><><><>", "accept: " + throwable);
            }
        });
        //merge()时结果为：1, 2, java.lang.NullPointerException
        //mergeDelayError()结果为：1, 2, 3, 4, 5, java.lang.NullPointerException
    }

    private void merge() {
        Observable.merge(
                Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return "A" +aLong;
                    }
                }),
                Observable.intervalRange(1, 5, 2, 1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return "B" + aLong;
                    }
                })
        ).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><>", "accept: " + s);
            }
        });
        //按照时间线发送：结果为A1, A2, B1, A3, B2, A4, B3, A5, B4, B5
    }

    private void concatArray() {
        Observable.concatArray(Observable.just(1, 2), Observable.just(3, 4), Observable.just(5, 6), Observable.just(7, 8), Observable.just(9, 10))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("<><><><>", "accept: " + integer);
                    }
                });
    }

    private void concat() {
        Observable.concat(Observable.just(1, 2), Observable.just(3, 4), Observable.just(5, 6), Observable.just(7, 8))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("<><><><>", "accept: " + integer);
                    }
                });
    }
}
