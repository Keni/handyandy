package keni.handyandy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import keni.handyandy.Config.Config;
import keni.handyandy.Config.RequestHandler;

public class ViewMyAppActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView textViewCategoryMyApp;
    private TextView textViewDescriptionMyApp;
    private TextView textViewClientNameMyApp;
    private TextView textViewClientAddressMyApp;
    private TextView textViewClientPhoneMyApp;
    private TextView textViewDateMyApp;

    private EditText editTextComment;

    private String id;

    private Button buttonUpdateFinishApp;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_app);
        initToolBar();

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.APP_ID);

        textViewCategoryMyApp = (TextView) findViewById(R.id.textViewCategoryMyApp);
        textViewDescriptionMyApp = (TextView) findViewById(R.id.textViewDescriptionMyApp);
        textViewClientNameMyApp = (TextView) findViewById(R.id.textViewClientNameMyApp);
        textViewClientAddressMyApp = (TextView) findViewById(R.id.textViewClientAddressMyApp);
        textViewClientPhoneMyApp = (TextView) findViewById(R.id.textViewClientPhoneMyApp);
        textViewDateMyApp = (TextView) findViewById(R.id.textViewDateMyApp);

        buttonUpdateFinishApp = (Button) findViewById(R.id.buttonUpdateFinishApp);
        buttonUpdateFinishApp.setOnClickListener(this);

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
                loading = ProgressDialog.show(ViewMyAppActivity.this, "Загрузка...", "Ждите...", false, false);
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

            textViewDescriptionMyApp.setText(description);
            textViewCategoryMyApp.setText(category);
            textViewClientNameMyApp.setText(client_name);
            textViewClientAddressMyApp.setText(client_address);
            textViewClientPhoneMyApp.setText(client_phone);
            textViewDateMyApp.setText(date);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void updateFinishApp()
    {
        final String comment = editTextComment.getText().toString().trim();

        class UpdateSelectApp extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewMyAppActivity.this, "Обновление...", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ViewMyAppActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_APP_ID, id);
                hashMap.put(Config.KEY_APP_COMMENT, comment);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_FINISH_APP, hashMap);

                return s;
            }
        }

        UpdateSelectApp usa = new UpdateSelectApp();
        usa.execute();


    }

    @Override
    public void onClick(View view)
    {
        updateFinishApp();
        Intent back = new Intent(ViewMyAppActivity.this, MyAppsFragment.class);
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
