package com.voitov.todolist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    public static final String TAG = "MainViewModel";
    private NoteDatabase database;
    private MutableLiveData<Integer> countOfPushes = new MutableLiveData<>();
    private int pushes = 0;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = NoteDatabase.getInstance(application);
    }

    public LiveData<List<Note>> getNotes() {
        return database.getNotesDAO().getNotes();
    }

    public void remove(int id) {
        Disposable disposable = database.getNotesDAO().remove(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        Log.d(TAG, "subscribe");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "remove error occurred");
                    }
                });
        compositeDisposable.add(disposable);
        //1 ЖЦ Thread не зависит от ЖЦ ViewModel, возможна утечка памяти
        //2 Сложно переключать потоки. Тяжелочитаемый код
        //3 Есть возможность по-случайности создать много потоков
    }

    public LiveData<Integer> getCountOfPushes() {
        return countOfPushes;
    }

    public void markAsPushed() {
        pushes++;
        countOfPushes.setValue(pushes);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
