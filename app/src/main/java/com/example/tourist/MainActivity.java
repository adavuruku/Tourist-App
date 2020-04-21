package com.example.tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView kl = findViewById(R.id.text);
        String cont = "<h4>International Institute of Tropical Agriculture  (IITA)</h4>\n" +
                "<p>International Institute of Tropical Agriculture is located at Located at KM 40 Along The Old Oyo Road, PMB 5320, Oyo.</p>\n" +
                "\n" +
                "<p>Interested in Agricultural research? Or fun seeking? The international institute of Tropical Agriculture provides an avenue for those seeking to have fun or for those interested in Agricultural research.</p>\n" +
                "\n" +
                "<h4>Safety/Security</h4>\n" +
                "<p>This is a very safe location.</p>\n" +
                "\n" +
                "<h4>What To Bring</h4>\n" +
                "<p>Before heading to International Institute of Tropical Agriculture, here are some helpful things to take along with you:</p>\n" +
                "<ol>\n" +
                "    <li>Camera</li>\n" +
                "    <li>Swimming gear</li>\n" +
                "</ol>\n" +
                "<h4>Things to do while at International Institute of Tropical Agriculture</h4>\n" +
                "These are the following things you can do while you're here:\n" +
                "<ol>\n" +
                "    <li>Bird watching</li>\n" +
                "    <li>Walking with the wild.</li>\n" +
                "</ol>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            kl.setText(Html.fromHtml(cont, Html.FROM_HTML_MODE_COMPACT));
        } else {
            kl.setText(Html.fromHtml(cont));
        }
    }
}
