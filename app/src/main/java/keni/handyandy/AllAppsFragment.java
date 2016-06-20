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
import android.widget.TextView;

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
public class AllAppsFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener
{
    private ListView list_apps;

    private String JSON_STRING;

    private TextView textViewEngineerBalance, textViewEngineerFullName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_all_apps, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        list_apps = (ListView) getActivity().findViewById(R.id.list_apps);
        list_apps.setOnItemClickListener(this);

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
                String client_address;
                String date = jo.getString(Config.TAG_APP_DATE);

                if (Integer.parseInt(Config.KEY_ENGINEER_BALANCE) <= 0)
                    client_address = "<Скрыто>";
                else
                    client_address = jo.getString(Config.TAG_APP_CLIENTADDRESS);


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
                getActivity(), list, R.layout.list_all_apps,
                new String[]{Config.TAG_APP_CATEGORY, Config.TAG_APP_CLIENTADDRESS, Config.TAG_APP_DATE},
                new int[]{R.id.textViewCategory, R.id.textViewClientAddress, R.id.textViewDate});

        list_apps.setAdapter(adapter);

    }

    private void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String>
        {
            //Поставил swipeRefreshLayout отключил ProgressDialog, чтобы не дублировалась загрузка
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
        Intent app = new Intent(getActivity(), ViewAppActivity.class);
        HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
        String addId = map.get(Config.TAG_APP_ID).toString();

        app.putExtra(Config.APP_ID, addId);
        onPause();
        startActivity(app);
    }

}
