package com.example.myapplication.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;


public class MainActivity extends Activity {
    private Button addImags;
    private GridView selectedImgs;
    public MainActivity(){
        super();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        listeners();
    }

    private void listeners() {
        addImags.setOnClickListener(new addImgsListener());
    }

    class addImgsListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,ShowAllDirectory.class);
            MainActivity.this.startActivity(intent);

        }
    }
    private void findViews(){
        addImags= (Button) findViewById(R.id.addImgs);
        selectedImgs=(GridView)findViewById(R.id.showSelectedImgs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
