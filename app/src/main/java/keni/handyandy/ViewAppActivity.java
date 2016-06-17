package keni.handyandy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewAppActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView textViewCategory;
    private TextView textViewDescription;
    private TextView textViewClientName;
    private TextView textViewClientAddress;
    private TextView textViewClientPhone;
    private TextView textViewDate;

    private String id;

    private Button buttonUpdateSelectApp;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_app);
        initToolBar();

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.APP_ID);

        textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewClientName = (TextView) findViewById(R.id.textViewClientName);
        textViewClientAddress = (TextView) findViewById(R.id.textViewClientAddress);
        textViewClientPhone = (TextView) findViewById(R.id.textViewClientPhone);
        textViewDate = (TextView) findViewById(R.id.textViewDate);

        buttonUpdateSelectApp = (Button) findViewById(R.id.buttonUpdateSelectApp);
        buttonUpdateSelectApp.setOnClickListener(this);

        getApp();
    }

    private void getApp()
    {
        class GetApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppActivity.this, "Загрузка...", "Ждите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                showApp(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_APP, id);
                return s;
            }
        }
        GetApp ga = new GetApp();
        ga.execute();
    }

    private void showApp(String json)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            String description = c.getString(Config.TAG_APP_DESCRIPTION);
            String category = c.getString(Config.TAG_APP_CATEGORY);
            String client_name = c.getString(Config.TAG_APP_CLIENTNAME);
            String client_address = c.getString(Config.TAG_APP_CLIENTADDRESS);
            String client_phone = c.getString(Config.TAG_APP_CLIENTPHONE);
            String date = c.getString(Config.TAG_APP_DATE);

            textViewDescription.setText(description);
            textViewCategory.setText(category);
            textViewClientName.setText(client_name);
            textViewClientAddress.setText(client_address);
            textViewClientPhone.setText(client_phone);
            textViewDate.setText(date);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void updateSelectApp()
    {
        class UpdateSelectApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAppActivity.this, "Обновление...", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewAppActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_APP_ID, id);
                hashMap.put(Config.KEY_ENGINEER, Config.ENGINEER);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_SELECT_APP, hashMap);

                return s;
            }
        }

        UpdateSelectApp usa = new UpdateSelectApp();
        usa.execute();


    }

    @Override
    public void onClick(View view)
    {
        updateSelectApp();
        Intent back = new Intent(ViewAppActivity.this, AllAppsActivity.class);
        startActivity(back);
    }

    public void initToolBar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

    }

}
