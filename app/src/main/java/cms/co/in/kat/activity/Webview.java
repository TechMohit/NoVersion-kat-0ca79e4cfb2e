package cms.co.in.kat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cms.kat.cws.R;

import cms.co.in.kat.utils.Constant;

public class Webview extends AppCompatActivity {

    private WebView webview;
    private ProgressBar progressBar;
    private LinearLayout footerCaseSearch,footerHome,footerJudgment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initLayout();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String url = bundle.getString("URL");
            if(url!=null && url.length()>0) {
                setValue(url);
            }
        }
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
        footerListner();
    }

    private void initLayout() {
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        footerCaseSearch=(LinearLayout)findViewById(R.id.footerCaseSearch);
        footerHome=(LinearLayout)findViewById(R.id.footerHome);
        footerJudgment=(LinearLayout)findViewById(R.id.footerJudgement);


    }
    private void footerListner() {

        footerCaseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),CaseTrack.class);
                startActivity(i);
            }
        });

        footerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(Constant.MY_PREF_PROFILE, MODE_PRIVATE);
                String name = prefs.getString(Constant.MY_PREF_USERNAME, null);
                Intent i;
                if (name != null) {
                    i = new Intent(getApplicationContext(), LoginHome.class);
                }else {
                    i = new Intent(getApplicationContext(), GuestHome.class);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);

            }
        });

        footerJudgment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Judgement.class);
                startActivity(i);
            }
        });
    }


    private void setValue(String URL) {

        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + URL);
        //webview.loadUrl(URL);

    }
}
