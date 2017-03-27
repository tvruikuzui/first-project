package com.aldevapp.com.sharktips;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    File file = null;
    ListView lstStuff;
    Button btnAdd;
    EditText edtItem;
    List<String> itemList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(getFilesDir(),"liran");

        //readFromFileToArrey(file, itemList);


        lstStuff = (ListView) findViewById(R.id.lstStuff);
        btnAdd = (Button) findViewById(R.id.btnSave);
        edtItem = (EditText) findViewById(R.id.edtTextToSave);

        adapter = new ArrayAdapter<>(this,R.layout.items,itemList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = edtItem.getText().toString();
                itemList.add(item);
                adapter.notifyDataSetChanged();
            }
        });
        lstStuff.setAdapter(adapter);
    }

//    private void addToList() {
//        String item = edtItem.getText().toString();
//        itemList.add(item);
//        adapter.notifyDataSetChanged();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        new AsyncTask<File, Void, Void>() {
            @Override
            protected Void doInBackground(File... params) {
                FileOutputStream fileOutputStream = null;
                JSONArray jsonArray = new JSONArray(itemList);
                try {
                    fileOutputStream = new FileOutputStream(params[0]);
                    fileOutputStream.write(jsonArray.toString().getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (fileOutputStream != null)
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                return null;
            }
        }.execute(file);
    }

    private void readFromFileToArrey(File file, List<String> itemList) {
        new AsyncTask<File, Void, List>() {
            @Override
            protected List doInBackground(File... params) {
                FileInputStream fileInputStream = null;
                JSONArray jsonArray = null;
                StringBuilder builder = new StringBuilder();
                List<String> readed = new ArrayList<String>();
                try {
                    fileInputStream = new FileInputStream(params[0]);
                    int actually;
                    byte[] buffer = new byte[128];
                    while ((actually = fileInputStream.read(buffer)) != -1)
                        builder.append(new String(buffer,0,actually));
                    jsonArray = new JSONArray(builder.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if (fileInputStream != null)
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                if (jsonArray != null){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            readed.add(jsonArray.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return readed;
            }

            @Override
            protected void onPostExecute(List list) {
                super.onPostExecute(list);

            }
        }.execute(file);

    }
}
