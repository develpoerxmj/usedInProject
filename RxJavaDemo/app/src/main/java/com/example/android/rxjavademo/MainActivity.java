package com.example.android.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 创建操作符
         * 1、create()----创建一个被观察者
         * 2、just()----创建一个被观察者并发送事件，发送的事件不能超过十个
         * 3、fromArray()----如just，但可以传入多于10个变量，和数组
         * 4、fromCallable()----Callable与Runnable用法一样，但会有返回值，返回值会发送给观察者
         * 5、fromFuture()----增加cancel()等方法操作Callable，通过get()获取Callable的返回值
         * 6、fromIterable()----发送List集合给观察者
         * 7、defer()----只有观察者被订阅后才会创建新的被观察者
         * 8、timer()----指定时间后，发送数值0L给观察者
         * 9、interval()----每隔一段指定时间，就会发送事件（事件为：从0开始，不断增加的整数）
         * 10、intervalRange()----与interval类似，可以指定开始值和数量
         * 11、range()----同时发送一定范围的事件序列，与intervalRange()相比，无延时
         * 12、rangeLong()----与range()一样，数据类型为long
         * 13、empty()----发送onComplete()事件，never()----不发送事件，error()----发送onError()事件
         */
//        create();
//        just();
//        fromArray();
//        fromCallable();
//        fromFuture();
//        fromIterable();
//        defer();
//        timer();
//        interval();
//        intervalRange();
//        range();

        /**
         * 转换操作符
         * 1、map()----将被观察者发送的数据类型转换为其他类型
         * 2、flatMap()----将事件序列整合加工，返回新的被观察者
         * 3、concatMap()----和flatMap类似，转发出来的事件为有序(与转换前的顺序一致)
         * 4、buffer()----从需要发送的事件中获取一定数量的事件，放入缓存区，一并发出
         * 5、groupBy()----将发送的数据进行分组，每个分组都会返回被观察者
         * 6、scan()----将数据以一定的逻辑聚合起来
         */
//        map();
//        flatMap();
//        concatMap();
//        buffer();
//        groupBy();
        scan();
    }

    private void scan() {
        Observable.just("1", "2", "3", "4", "5").scan(new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Exception {
                Log.i("<><><><><>", "apply: " + s + ":" + s2);
                return s + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "accept: " + s);
            }
        });
    }

    private void groupBy() {
        //groupBy----返回分组的名字，没返回一个值表示穿件一个分组
        Observable.just(1, 2, 3, 4, 6).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                if (integer < 4){
                    return "1";
                }
                return "2";
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i("<><><><><><>", "accept: " + stringIntegerGroupedObservable.getKey() + ":" + integer);
                    }
                });
            }
        });
    }

    private void buffer() {
        /**
         * count----缓冲元素个数
         * skip----填充满后，下一次事件序列跳过的个数
         */
        Observable.just(1, 2, 3, 4, 5).buffer(3, 2).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                for (int i = 0; i < integers.size(); i++){
                    Log.i("<><><><><>", "accept: " + integers.get(i));
                }
                Log.i("<><><><><>", "accept: " + "跳");
            }
        });
    }

    private void concatMap() {
        List<Bean> list = new ArrayList<>();
        flatMapData(list);
        Observable.fromIterable(list).concatMap(new Function<Bean, ObservableSource<Bean.ItemBean>>() {
            @Override
            public ObservableSource<Bean.ItemBean> apply(Bean bean) throws Exception {
                return Observable.fromIterable(bean.getList());
            }
        }).concatMap(new Function<Bean.ItemBean, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Bean.ItemBean itemBean) throws Exception {
                if ("1".equals(itemBean.getName())){
                    return Observable.just(itemBean.getName()).delay(10, TimeUnit.MILLISECONDS);
                }
                return Observable.just(itemBean.getName());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "accept: " + s);
            }
        });
    }

    private void flatMap() {
        List<Bean> list = new ArrayList<>();
        flatMapData(list);
        Observable.fromIterable(list).flatMap(new Function<Bean, ObservableSource<Bean.ItemBean>>() {
            @Override
            public ObservableSource<Bean.ItemBean> apply(Bean bean) throws Exception {
                return Observable.fromIterable(bean.getList());
            }
        }).flatMap(new Function<Bean.ItemBean, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Bean.ItemBean itemBean) throws Exception {
                if ("1".equals(itemBean.getName())){
                    return Observable.just(itemBean.getName()).delay(10, TimeUnit.MILLISECONDS);
                }
                return Observable.just(itemBean.getName());
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "accept: " + s);
            }
        });
    }

    private void flatMapData(List<Bean> list) {
        for (int i = 0; i < 3; i++){
            Bean bean = new Bean();
            List<Bean.ItemBean> itemList = new ArrayList<>();
            Bean.ItemBean itemBean = new Bean.ItemBean();
            itemBean.setName("" + i);
            itemList.add(itemBean);
            bean.setList(itemList);
            list.add(bean);
        }
    }

    private void map() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Observable.fromIterable(list).map(new Function<String, Float>() {
            @Override
            public Float apply(String s) throws Exception {
                return new BigDecimal(s + "0").floatValue();
            }
        }).subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) throws Exception {
                Log.i("<><><><><>", "onNext: " + aFloat);
            }
        });
    }

    private void range() {
        Observable.range(3, 10).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i("<><><><><>", "onNext: " + integer);
            }
        });
    }

    private void intervalRange() {
        /**
         * start----开始数值
         * count----发送个数
         * initialDelay----第一个事件延迟时间
         * period----每个事件的间隔发送时间
         */
        Observable.intervalRange(10, 5, 3, 2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i("<><><><><>", "onNext: " + aLong);
            }
        });
    }

    private void interval() {
        Observable.interval(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i("<><><><><>", "onNext: " + aLong);
            }
        });
    }

    private void timer() {
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i("<><><><><>", "onNext: " + aLong);
            }
        });
    }

    private String mString;
    private void defer() {
        mString = "1";
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just(mString);
            }
        });
//        Observable<String> observable = Observable.just(mString);
        mString = "2";
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "onNext: " + s);
            }
        });
    }

    private void fromIterable() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        Observable.fromIterable(list).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "onNext: " + s);
            }
        });
    }

    private void fromFuture() {
        final FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "futureTask";
            }
        });
        Observable.fromFuture(futureTask).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                futureTask.run();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("<><><><><>", "onNext: " + s);
            }
        });
    }

    private void fromCallable() {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "callable";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {
                Log.i("<><><><><>", "onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void fromArray() {
        String[] strings = {"10", "20", "30"};
        Observable.fromArray(strings).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {
                Log.i("<><><><><>", "onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void just() {
        Observable.just(1, 2, 3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Integer integer) {
                Log.i("<><><><><>", "onNext: " + integer);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    private void create() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("create");
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {
                Log.i("<><><><><>", "onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }
}
