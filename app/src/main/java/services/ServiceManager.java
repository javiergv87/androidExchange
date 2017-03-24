package services;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import app.android.javierg.exchandroid.ItemData;
import app.android.javierg.exchandroid.R;
import cz.msebera.android.httpclient.Header;


/**
 * Created by javierg on 21/03/2017.
 */

public class ServiceManager{

    public static List<ItemData> getCurrencyList() {
        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("AUD Australian Dollar", R.drawable.australia,"AUD"));
        list.add(new ItemData("BGN Bulgarian Lev",R.drawable.bulgaria,"BGN"));
        list.add(new ItemData("BRL Brazilian real",R.drawable.brazil,"BRL"));
        list.add(new ItemData("CAD Canadian Dollar",R.drawable.canada,"CAD"));
        list.add(new ItemData("CHZ Swiss franc",R.drawable.switzerland,"CHZ"));
        list.add(new ItemData("CZK Czched crown",R.drawable.czech,"CZK"));
        list.add(new ItemData("DKK Danish crown",R.drawable.australia,"DKK"));
        list.add(new ItemData("EUR Euro",R.drawable.euro,"EUR"));
        list.add(new ItemData("GBP British pound",R.drawable.uk,"GBP"));
        list.add(new ItemData("USD United States dollar",R.drawable.usa,"USD"));
        return list;
    }


}


