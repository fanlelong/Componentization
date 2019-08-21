package com.plugin.text;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ancely.fyw.aroute.base.BaseActivity;

public class PluginTextActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_text);

        Button button = findViewById(R.id.startPluginService);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(mActivity, TextService.class));
            }
        });
    }
}
