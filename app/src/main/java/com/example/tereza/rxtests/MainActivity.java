package com.example.tereza.rxtests;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

// asynctask - MIE                   - RxJava alebo aj blby Thread
// loader - URCITE NIE               - RxJava alebo aj blby Thread
// fragment - FUUUUUUUUJ             - custom viewgroup
// content provider - MASOCHIZMUS    - ak netreba zdielat data tak blbe sharedpreferences
// zadavanie hodnot cez dialogy       - vsetko vo view
// downloader - radsej sa vyhnut      - plac
// relativelayout - rozjebe sa, vzdy  - akykolvek normalny layout
// urlconnection                      - okhttp/retrofit
// org.json.JsonDaco                  - gson

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MAIN ACTIVITY";

    private final Generator gen1 = new Generator();
    private final Generator gen2 = new Generator();
    private final Generator gen3 = new Generator();
    private final Context context = this;
    protected @InjectView(R.id.but1)ToggleButton but1;
    protected @InjectView(R.id.but2)ToggleButton but2;
    protected @InjectView(R.id.but3)ToggleButton but3;
    protected @InjectView(R.id.one)TextView tv1;
    protected @InjectView(R.id.two)TextView tv2;
    protected @InjectView(R.id.three)TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.inject(this);
        initButtons();
        setSubscribers();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setSubscribers() {
        subscribeToObservable(getObserverOfGen(gen1, but1, 100), tv1);
        subscribeToObservable(getObserverOfGen(gen2, but2, 1000), tv2);
        subscribeToObservable(getObserverOfGen(gen3, but3, 10000), tv3);
    }

    private void subscribeToObservable(Observable o, final TextView tv) {
        o.subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String f) {
                        tv.setText(f);
                    }
                });
    }

    private Observable getObserverOfGen(final Generator g, final ToggleButton tb, final int sleepTime) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    while(tb.isChecked()) {
                        Thread.sleep(sleepTime);
                        subscriber.onNext(String.valueOf(g.getIntFromInterval(1, 10)));
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e); // In case there are network errors
                }
            }
        });
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.but1 :
                Log.d(TAG, "but1 clicked");
                break;

            case R.id.but2 :
                Log.d(TAG, "but2 clicked");
                break;

            case R.id.but3 :
                Log.d(TAG, "but3 clicked");
                break;
        }
    }

    private void initButtons() {
        this.but1.setChecked(true);
        this.but2.setChecked(true);
        this.but3.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void fetchForm(final int idForm) {
        Observable<String> fetchFromServer = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
//                    form = controller.getForm(context, idForm);
                    subscriber.onNext(String.valueOf(gen1.getInt())); // Emit the contents of the URL
                    subscriber.onCompleted(); // Nothing more to emit
                } catch (Exception e) {
                    subscriber.onError(e); // In case there are network errors
                }
            }
        });

        fetchFromServer.subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String f) {
//                        view.setText(view.getText() + "\n" + s); // Change a View
//                        finishFormLoading();
                        System.out.println(f);
                        Toast toast = Toast.makeText(context, f + " downloaded", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
