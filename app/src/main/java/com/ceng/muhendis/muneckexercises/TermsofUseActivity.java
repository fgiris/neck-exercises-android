package com.ceng.muhendis.muneckexercises;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;

public class TermsofUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsof_use);
        Toolbar toolbar = findViewById(R.id.toolbarTermsofUse);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            ((TextView)findViewById(R.id.title_terms)).setText("Terms of Use");
            ((TextView)findViewById(R.id.termsOfUseContent)).setText(getResources().getString(R.string.termsOfUseContentEng));
            ((Button)findViewById(R.id.btn_terms_continue)).setText("Continue");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void continueTerms(View btn)
    {
        Intent intent = new Intent(TermsofUseActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}
