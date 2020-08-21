package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("KtH6i1dxT6qiQysFeu96BiJvpY4BOMbM9qbSNoSX")
                // if defined
                .clientKey("AiyPi8uo3vsyjfNBIjgtO0KnRb3Liwnia09m95KC")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
