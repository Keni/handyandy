package keni.handyandy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import keni.handyandy.Config.Config;
import keni.handyandy.Config.RequestHandler;

/**
 * Created by Keni on 20.06.2016.
 */
public class MyAppsFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener
{
    private ListView list_my_apps;

    private String JSON_STRING;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_my_apps, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        list_my_apps = (ListView) getActivity().findViewById(R.id.list_apps);
        list_my_apps.setOnItemClickListener(this);

        getJSON();
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
                String client_address = jo.getString(Config.TAG_APP_CLIENTADDRESS);
                String date = jo.getString(Config.TAG_APP_DATE);

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
                getActivity(), list, R.layout.list_my_apps,
                new String[]{Config.TAG_APP_CATEGORY, Config.TAG_APP_CLIENTADDRESS, Config.TAG_APP_DATE},
                new int[]{R.id.textViewCategoryMyApp, R.id.textViewClientAddressMyApp, R.id.textViewDateMyApp});

        list_my_apps.setAdapter(adapter);
    }

    private void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String>
        {
            ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Загрузка данных", "Подождите...", false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showApps();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_MY_APPS, Config.ENGINEER);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent myapp = new Intent(getActivity(), ViewMyAppActivity.class);
        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
        String addId = map.get(Config.TAG_APP_ID).toString();

        myapp.putExtra(Config.APP_ID, addId);
        startActivity(myapp);
    }
}
