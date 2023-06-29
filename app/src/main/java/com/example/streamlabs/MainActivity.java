package com.example.streamlabs;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private WebView web;
    private String url;

    private String Symbol;
    org.jsoup.nodes.Document document = null;
    TextView txturl;
    Button btn;
    private boolean isGenerating = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.wv);
        btn = findViewById(R.id.aleatoire);
        txturl = findViewById(R.id.txturl);


        WebSettings webstg = web.getSettings();
        webstg.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGenerating){
                    isGenerating = true;
                    generateAndLoadUrl();
                }

            }

        });

    }

    private void generateAndLoadUrl(){

        btn.setEnabled(false);

        StringBuilder sb = new StringBuilder();

        if(!isGenerating){
            btn.setEnabled(true);
            return;
        }

        for (int i = 0; i < 6; i++) {
            Random ran = new Random();

            int randomType = ran.nextInt(2);

            if (randomType == 0) {

                int randomNumber = ran.nextInt(10);
                sb.append(randomNumber);

            } else {

                char randomChar = (char) (ran.nextInt(26) + 'a');
                sb.append(randomChar);
            }
        }

        String randomstring = sb.toString();
        url = "https://streamable.com/" + randomstring;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentadd c = new contentadd();
                c.execute();
            }
        });

        sb.setLength(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateAndLoadUrl();
            }
        }, 450); //450ms seems the best to avoid the error page and get one vid as fast as possible. At 150ms and lower the program start to get bug 10 sec after its launch

    }

    public void onBackPressed() {
        finish();
    }

    private class contentadd extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                document = Jsoup.connect(url).get();
                org.jsoup.select.Elements data = document.getElementsByClass("metadata");
                Symbol = data.text();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(Symbol != null) {
                isGenerating = false;
                web.loadUrl(url);
                txturl.setText(url);
                Symbol = null;
            }else{
                txturl.setText(url);
            }

        }
    }


}