package bd.com.ashfaq.apps;

import android.app.Activity;
import android.content.Context;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class CustomTools {
    public static String DEBUG_URL = new String(Base64.decode("aHR0cHM6Ly9hcGkuamlib24uY29tLmJkL2R5bmFtaWM=", Base64.DEFAULT));
    public static String SERVER_URL = DEBUG_URL;
    public static String TAG = "errnos";
    protected Activity activity;

    public CustomTools(Activity activity) {
        this.activity = activity;
    }

    public static void DoNothing() {
    }

    public static String url(String path) {
        return BuildConfig.DEBUG ? DEBUG_URL + "/" + path : SERVER_URL + "/" + path;
    }


    public static void log(Object message) {
        log(message, false);
    }

    public static void log(Object message, Boolean fromInternet) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        int index = fromInternet ? 3 : 4;
        String traceFileMethod = stacktrace[index].getClassName() + "\t" + stacktrace[index].getMethodName();
        String traceFileLine = stacktrace[index].getFileName() + ":" + stacktrace[index].getLineNumber();

        if (BuildConfig.DEBUG) {
            Log.i(TAG, traceFileLine + "\t" + message);
        }
    }


    public static void showNoInternetExit(Activity activity) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle("No internet")
                .setMessage("Please Check Connectivity!")
                .setPositiveButton("exit", (dialogInterface, i) -> {
                    activity.finish();
                    System.exit(0);
                })
                .setCancelable(false)
                .create().show();
    }


    public static String getUserAgent() {
        return "Device: Android " + Build.VERSION.RELEASE + ", Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Brand: " + Build.BRAND + ", App: " + MainActivity.class.getPackage().getName() + " " + BuildConfig.VERSION_NAME;
    }

    public static String getUserAgent(Activity activity) {
        return "Device: Android " + Build.VERSION.RELEASE + ", Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Brand: " + Build.BRAND + ", App: " + activity.getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME;
    }


    public static boolean toast(Activity activity, String text) {
        CustomTools customTools = new CustomTools(activity);
        return customTools.toast(text);
    }

    public static View GET_WEB_VIEW(Activity activity, String url) {
        return GET_WEB_VIEW(activity, url, "");
    }

    public static View GET_WEB_VIEW(Activity activity, String url, String post) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customView = inflater.inflate(R.layout.view_custom_webview, null);

        WebView webView = customView.findViewById(R.id.webView);
        ProgressBar progressBar = customView.findViewById(R.id.progressBar);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url2, Bitmap favicon) {
                super.onPageStarted(view, url2, favicon);
                progressBar.setIndeterminate(false);
                progressBar.setProgress(0);
                if (url2.startsWith("https://www.google")) {
                    if (post.equalsIgnoreCase("")) {
                        view.loadUrl(url);
                    } else {
                        view.postUrl(url, post.getBytes(StandardCharsets.UTF_8));
                    }

                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);

            }
        });

        if (post.equalsIgnoreCase("")) {
            webView.loadUrl(url);
        } else {
            webView.postUrl(url, post.getBytes(StandardCharsets.UTF_8));
        }


        return customView;
    }

    public static View GET_CUSTOM_VIEW(Activity activity, int layout) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customView = inflater.inflate(layout, null);
        return customView;
    }

    public boolean toast(String text, Integer drawable) {
        return toast(text, drawable, Color.GRAY);
    }

    public boolean toast(String text) {
        return toast(text, null, 0);
    }

    public boolean toast(Integer text) {
        return toast(String.valueOf(text), null);
    }

    public boolean vibrate(int milliseconds) {
        try {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            return true;
        } catch (Exception e) {
            log("Vibrate Problem: " + e);
            return false;
        }
    }

    public static CustomAlertDialog alertBuilder(Activity activity, String title, String message) {
        CustomTools customTools = new CustomTools(activity);
        return customTools.alertBuilder(title, message);
    }

    public CustomAlertDialog alertBuilder(String title, String messages, int icon, int color) {
        CustomAlertDialog builder = new CustomAlertDialog(activity);
        builder.setTitle(title)
                .setMessage(messages)
                .setIcon(icon)
                .setIconColor(color)
                .setCancelable(true);
        return builder;
    }

    public CustomAlertDialog alertBuilder(String title, String messages, int icon) {
        return alertBuilder(title, messages, icon, Color.GRAY);
    }

    public CustomAlertDialog alertBuilder(String title, String messages) {
        return alertBuilder(title, messages, 0, Color.GRAY);
    }

    public CustomAlertDialog alert(String title, String messages, int icon, int color) {
//        CustomAlertDialog builder = new CustomAlertDialog(activity);
//        Drawable drawable = activity.getResources().getDrawable(icon);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            drawable.setTint(color);
//        }
//        builder.setTitle(title)
//                .setMessage(messages)
//                .setIcon(drawable)
//                .setPositiveButton("OK", (dialog, which) -> {
////                    dialog.cancel();
//                })
//                .setCancelable(true);
//        builder.create().show();
        CustomAlertDialog builder = new CustomAlertDialog(activity);
        builder.setTitle(title)
                .setMessage(messages)
                .setIcon(icon)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .setCancelable(true);
        builder.create().show();
        return builder;
    }

    public void alert(String title, String messages, int icon) {
        alert(title, messages, icon, Color.GRAY);
    }

    public void alert(String title, String messages) {
        alert(title, messages, 0, Color.GRAY);
    }

    public static void alert(Activity activity, String title, String message) {
        CustomTools customTools = new CustomTools(activity);
        customTools.alert(title, message);
    }

    public boolean toast(String text, Integer drawable, int color) {
        try {
            View view = activity.getLayoutInflater().inflate(R.layout.item_toast, activity.findViewById(R.id.Custom_toast), false);
            ((TextView) view.findViewById(R.id.Custom_toast_text)).setText(text);
            if (drawable != null) {
                ((ImageView) view.findViewById(R.id.Custom_toast_icon)).setImageResource(drawable);
                ((ImageView) view.findViewById(R.id.Custom_toast_icon)).setColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.SRC_IN);
            }
            Toast toast = new Toast(activity.getApplicationContext());
            if (text.length() < 20) {
                toast.setDuration(Toast.LENGTH_SHORT);
            } else {
                toast.setDuration(Toast.LENGTH_LONG);
            }
            toast.setView(view);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            vibrate(200);
            return true;
        } catch (Exception e) {
            log("Custom Toast Problem: " + e);
            return false;
        }
    }


}
