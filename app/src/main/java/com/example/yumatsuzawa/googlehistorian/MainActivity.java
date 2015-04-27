package com.example.yumatsuzawa.googlehistorian;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class MainActivity extends ActionBarActivity {

    private String googleHeader = "https://www.google.co.jp/search?q=";
    private String historyFileName = "search_history.json";
    private String jsonQueryKey = "query";
    private String jsonSearchedAtKey = "searchedAt";
//    private final JSONArray HistoryArray = new JSONArray();
//    private ArrayAdapter<String> historyAdapter;
    private ArrayAdapter<History> historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Elements addressing
        Button goButton = (Button)this.findViewById(R.id.button_go);
        final EditText queryInput = (EditText)this.findViewById(R.id.editText_query);
        final ListView history = (ListView)this.findViewById(R.id.listView_history);

        // init history list with handler
        historyAdapter =
                new ArrayAdapter<History>(this, android.R.layout.simple_list_item_1);
        history.setAdapter(historyAdapter);

        // read history file, putting them into ArrayAdapter
        String jsonHistoryStr = readHistoryFile();
        try {
            JSONArray jsonHistory = new JSONArray(jsonHistoryStr);
            for (int obj_i = 0; obj_i < jsonHistory.length(); obj_i++) {
                History historyObj = new History(
                        jsonHistory.getJSONObject(obj_i).getString(jsonQueryKey),
                        jsonHistory.getJSONObject(obj_i).getString(jsonSearchedAtKey));
                historyAdapter.add(historyObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // adding example if history is empty
        if (historyAdapter.getCount() < 1) {
            historyAdapter.add(new History("Example",urlBuilder("Example")));
        }

        // inside button click handler, executes:
        //      acquiring query from EditText form, then clearing the form,
        //      storing the query into history,
        //      run google search with that query keyword, starting Act.
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String query = queryInput.getText().toString();
                queryInput.setText("");

                historyAdapter.add(new History(query, urlBuilder(query)));
                dumpHistory();

                Uri queryUri = Uri.parse(urlBuilder(query));
                Intent searchIntent = new Intent(Intent.ACTION_VIEW, queryUri);
                startActivity(searchIntent);
            }
        });
    }

    private String urlBuilder(String query) {
        return googleHeader + query;
    }

    //Read JSON history file and return it as String
    private String readHistoryFile() {
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            fis = this.openFileInput(this.historyFileName);
            br = new BufferedReader(new InputStreamReader(fis));
            String jsonRet = br.readLine();
            br.close();

            return jsonRet;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                br.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return String.valueOf("");
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

    @Override
    protected void onStop() {
        super.onStop();
        dumpHistory();
    }

    private void dumpHistory() {
        OutputStream os = null;
        JSONArray finalHistoryArray = new JSONArray();
        try {
            for (int obj_i = 0; obj_i < historyAdapter.getCount(); obj_i++) {
                History tmpHistory = historyAdapter.getItem(obj_i);
                JSONObject historyObj = new JSONObject();
                historyObj.put(jsonQueryKey, tmpHistory.toString());
                historyObj.put(jsonSearchedAtKey, tmpHistory.getSearchedAt());
                finalHistoryArray.put(historyObj);
            }

            os = this.openFileOutput(historyFileName, Context.MODE_PRIVATE);
            os.write(finalHistoryArray.toString().getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
