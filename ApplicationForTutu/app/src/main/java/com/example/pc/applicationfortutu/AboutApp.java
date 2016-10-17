package com.example.pc.applicationfortutu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  Activity "О Приложении"
 */
public class AboutApp extends Activity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app);

        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.textView);

        imageView.setImageResource(R.drawable.tutuimg);
        textView.setText("Версия приложения 1");
    }
}
