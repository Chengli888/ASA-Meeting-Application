package monmouth.edu.asa_mu_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by chengli on 2017/11/1.
 */

public class Tab01 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View ContentView = inflater.inflate(R.layout.tab1, container, false);
        WebView webView = ContentView.findViewById(R.id.Webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://andrologysociety.org/meetings/asa-annual-meeting/future-meetings/general-meeting-information.aspx");
        return ContentView;
    }
}
