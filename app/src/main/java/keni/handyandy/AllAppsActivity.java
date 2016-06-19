package keni.handyandy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllAppsActivity extends AppCompatActivity implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView list_apps;

    private String JSON_STRING;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.allapps);
        setSupportActionBar(toolbar);

        System.out.println(Config.ENGINEER_BALANCE);

        list_apps = (ListView) findViewById(R.id.list_apps);
        list_apps.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
                getJSON();
            }
        });
    }

    @Override
    public void onRefresh()
    {
        getJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_myapps)
        {
            Intent myapps = new Intent(this, MyAppsActivity.class);
            startActivity(myapps);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showApps()
    {
        JSONObject jsonObject;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_APP_ID);
                String category = jo.getString(Config.TAG_APP_CATEGORY);
                String client_address = jo.getString(Config.TAG_APP_CLIENTADDRESS);;
                String date = jo.getString(Config.TAG_APP_DATE);

/**                System.out.println(Config.ENGINEER_BALANCE);
                if (Integer.parseInt(Config.ENGINEER_BALANCE) <= 0)
                    client_address = "<Скрыто>";
                else
                    client_address = jo.getString(Config.TAG_APP_CLIENTADDRESS);
*/

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_APP_ID, id);
                apps.put(Config.TAG_APP_CATEGORY, category);
                apps.put(Config.TAG_APP_CLIENTADDRESS, client_address);
                apps.put(Config.TAG_APP_DATE, date);
                list.add(apps);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                AllAppsActivity.this, list, R.layout.list_all_apps,
                new String[]{Config.TAG_APP_CATEGORY, Config.TAG_APP_CLIENTADDRESS, Config.TAG_APP_DATE},
                new int[]{R.id.textViewCategory, R.id.textViewClientAddress, R.id.textViewDate});

        list_apps.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void getJSON()
    {
        swipeRefreshLayout.setRefreshing(true);

        class GetJSON extends AsyncTask<Void, Void, String>
        {
            //Поставил swipeRefreshLayout отключил ProgressDialog, чтобы не дублировалась загрузка
            //ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                //loading = ProgressDialog.show(AllAppsActivity.this, "Загрузка данных", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                //loading.dismiss();
                JSON_STRING = s;
                showApps();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL_APPS);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent app = new Intent(this, ViewAppActivity.class);
        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
        String addId = map.get(Config.TAG_APP_ID).toString();

        app.putExtra(Config.APP_ID, addId);
        onPause();
        startActivity(app);
    }

}
