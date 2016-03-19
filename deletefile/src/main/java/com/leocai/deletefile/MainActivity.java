package com.leocai.deletefile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity {

    private Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                btn_delete = (Button) stub.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File root = Environment.getExternalStorageDirectory();
                        File[] files = root.listFiles();
                        for(File f:files){
                            if(f.getName().contains(".csv")){
                                f.delete();
                            }
                        }
                    }
                });

            }
        });
    }
}
