package com.example.yumatsuzawa.googlehistorian;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private String googleHeader = "https://www.google.co.jp/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goButton = (Button)this.findViewById(R.id.button_go);
        final EditText queryInput = (EditText)this.findViewById(R.id.editText_query);
        final ListView history = (ListView)this.findViewById(R.id.listView_history);

        // init history list with handler
        final ArrayAdapter<String> historyAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        history.setAdapter(historyAdapter);
        historyAdapter.add("Example");



        // inside click handler, executes:
        //      acquiring query from EditText form, then clearing the form,
        //      storing the query into history,
        //      run google search with that query keyword, starting Act.
//        goButton.setOnClickListener(
//                new onClickSearch(queryInput.getText().toString(), queryInput));
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String query = queryInput.getText().toString();
                historyAdapter.add(query);
                queryInput.setText("");

                Uri queryUri = Uri.parse(googleHeader + query);
                Intent searchIntent = new Intent(Intent.ACTION_VIEW, queryUri);
                startActivity(searchIntent);
            }
        });
    }

    public class onClickSearch implements View.OnClickListener {

        private String query = "";
        private TextView relatedText = null;
        private String googleHeader = "https://www.google.co.jp/search?q=";

        public onClickSearch (String query) {
            this.query = query;
            this.relatedText = null;
        }

        public onClickSearch (String query, TextView relatedText) {
            this.query = query;
            this.relatedText = relatedText;
        }

        @Override
        public void onClick(View v) {
            if (!this.query.isEmpty()) {
                this.relatedText.setText("");
                Uri queryUri = Uri.parse(this.googleHeader + this.query);
                Intent searchIntent = new Intent(Intent.ACTION_VIEW, queryUri);
                startActivity(searchIntent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
