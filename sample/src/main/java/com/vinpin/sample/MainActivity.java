package com.vinpin.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById();
    }

    private void findViewById() {
        findViewById(R.id.txt_common).setOnClickListener(this);
        findViewById(R.id.txt_multi).setOnClickListener(this);
        findViewById(R.id.txt_header_footer).setOnClickListener(this);
        findViewById(R.id.txt_loadmore).setOnClickListener(this);
        findViewById(R.id.txt_empty).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_common:
                startActivity(CommonActivity.newIntent(mContext));
                break;
            case R.id.txt_multi:
                startActivity(MultiItemTypeActivity.newIntent(mContext));
                break;
            case R.id.txt_header_footer:
                startActivity(HeaderFooterActivity.newIntent(mContext));
                break;
            case R.id.txt_loadmore:
                startActivity(LoadmoreActivity.newIntent(mContext));
                break;
            case R.id.txt_empty:
                startActivity(EmptyActivity.newIntent(mContext));
                break;
            default:
                break;
        }
    }
}
