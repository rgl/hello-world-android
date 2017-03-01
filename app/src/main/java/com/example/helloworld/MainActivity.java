package com.example.helloworld;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button helloButton = (Button) findViewById(R.id.helloButton);
        helloButton.setOnClickListener(view -> {
            GsonRequest<IpResponse> request = new GsonRequest<>(
                    "http://httpbin.org/ip",
                    IpResponse.class,
                    null,
                    response -> {
                        AlertDialog.Builder d = new AlertDialog.Builder(this);
                        d.setTitle("Hello?");
                        d.setMessage(String.format("Hello World!\n\nyour ip is %s", response.origin));
                        d.setPositiveButton("Dismiss", (dialog, which) -> {});
                        d.show();
                    },
                    error -> {
                        String body = null;
                        if (error.networkResponse.data != null) {
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                body = String.format("Failed to read response body %s", e);
                            }
                        }
                        String errorMessage = String.format(
                                "Failed to get ip address.\n\n%s (%d)\n%s",
                                error,
                                error.networkResponse.statusCode,
                                body);

                        AlertDialog.Builder d = new AlertDialog.Builder(this);
                        d.setTitle("Error!");
                        d.setMessage(errorMessage);
                        d.setPositiveButton("Dismiss", (dialog, which) -> {});
                        d.show();
                    }
            );
            request.setTag(TAG);

            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(this);
            }

            mRequestQueue.add(request);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    static private class IpResponse
    {
        public String origin;
    }
}
