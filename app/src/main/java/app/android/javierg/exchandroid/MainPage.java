package app.android.javierg.exchandroid;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.loopj.android.http.HttpGet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import model.Forex;

import static com.loopj.android.http.AsyncHttpClient.LOG_TAG;

public class MainPage extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private Spinner from;
    private Spinner to;
    private TextView amountConverted;
    private TextView amount;
    private TextView toCurrency;
    private TextView fromCurrency;
    private String fromSelected;
    private String toSelected;
    private String amountConv;
    private FloatingActionButton swappingButton;
    private ProgressBar loading;
    private static boolean connection = true;
    private BigDecimal rate;
    private AdView mAdView;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;



    private ArrayAdapter<String> currencyList;

    public ArrayAdapter<String> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ArrayAdapter<String> currencyList) {
        this.currencyList = currencyList;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_new_version);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        from = (Spinner) findViewById(R.id.from);
        to = (Spinner) findViewById(R.id.to);
        toCurrency = (TextView) findViewById(R.id.toCurrency);
        fromCurrency  = (TextView) findViewById(R.id.fromCurrency);

        loading = (ProgressBar) findViewById(R.id.loading);


        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("AUD",R.drawable.australia,"AUD"));
        list.add(new ItemData("BRL",R.drawable.brazil,"BRL"));
        list.add(new ItemData("CAD",R.drawable.canada,"CAD"));
        list.add(new ItemData("CZK",R.drawable.czech,"CZK"));
        list.add(new ItemData("DKK",R.drawable.australia,"DKK"));
        list.add(new ItemData("EUR",R.drawable.euro,"EUR"));
        list.add(new ItemData("GBP",R.drawable.uk,"GBP"));
        list.add(new ItemData("USD",R.drawable.usa,"USD"));
        list.add(new ItemData("JPY",R.drawable.japan,"JPY"));
        SpinnerAdapter adapter = new SpinnerAdapter(this,R.layout.spinner_layout,R.id.txt,list);
        SpinnerAdapter adapterTo = new SpinnerAdapter(this,R.layout.spinner_layout_to,R.id.txt,list);
        from.setAdapter(adapter);
        to.setAdapter(adapterTo);
        //builder = new AlertDialog.Builder(MainPage.this);
        //if(!isNetworkAvailable())
        //    showDialog();
        from.setSelection(7);
        to.setSelection(6);
        amount = (EditText) findViewById(R.id.amount);
        swappingButton = (FloatingActionButton) findViewById(R.id.swappingButton);
        //updateSwappingButton();

        amount.setText("1");
        amountConverted = (TextView) findViewById(R.id.amountConverted);
        amountConverted.setKeyListener(null);
        fromCurrency.setKeyListener(null);
        toCurrency.setKeyListener(null);
        mAdView = (AdView) findViewById(R.id.adView);

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        //if(isNetworkAvailable())
         //   mAdView.loadAd(adRequest);

        //updateChange();
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                updateChange();
                return true;
            }


        });

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    updateChange();
            }
        });


        swappingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                swapCurrencies();
            }
        });



    }

    @Override
    public void onResume(){
        super.onResume();
        showDialog();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void showDialog()
    {
        if(alertDialog!=null && isNetworkAvailable()) {
            alertDialog.cancel();
            alertDialog = null;
        }
        if(!isNetworkAvailable()) {
            builder = new AlertDialog.Builder(MainPage.this);

            builder.setTitle("Connection failed");
            builder.setMessage("This application requires network access. Please, enable mobile network or Wi-Fi.")
                    .setCancelable(false)
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            MainPage.this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                            //finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainPage.this.finish();
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }
        else {

            updateChange();
        }
    }

    public void swapCurrencies() {
        int origin = from.getSelectedItemPosition();
        int dest = to.getSelectedItemPosition();
        from.setSelection(dest);
        to.setSelection(origin);
        updateChange();
    }


    public void updateChange() {
        //if(alertDialog==null && !alertDialog.isShowing()) {

            ItemData fr, t;
            fr = (ItemData) from.getSelectedItem();
            t = (ItemData) to.getSelectedItem();
            toCurrency.setText(t.getCode());
            fromCurrency.setText(fr.getCode());
            fromSelected = fr.getCode();
            toSelected = t.getCode();
            if (amount.getText().length() == 0)
                amount.setText("1");
            new HttpRequestTask().execute(String.format("http://chartapi.finance.yahoo.com/instrument/1.0/%s%s=X/chartdata;type=quote;range=1d/json", fromSelected, toSelected));
            //updateSwappingButton();

    }



    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
            connection=true;
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        String res = result.replace("finance_charts_json_callback(","").replace(")","");
        return res;

    }


    private class HttpRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response="";
            if(isNetworkAvailable()) {
                try {
                /*RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                String forex = restTemplate.getForObject(String.format(ForexRestClient.API_URL,fromSelected,toSelected), String.class);
                return forex;*/
                    response = GET(urls[0]);
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Forex forex = gson.fromJson(response, Forex.class);
                amountConv = forex.getSeries().get(forex.getSeries().size() - 1).getClose();

                rate = new BigDecimal(amountConv);
                return response;
            } else {
                return null;
            }
        }


        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String result) {
            // The results of the above method
            // Processing the results here
            if(result!=null) {
                setAmountConverted(amountConv);
                loading.setVisibility(View.INVISIBLE);
            }
            else
                showDialog();
            loading.setVisibility(View.INVISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            if(alertDialog!=null && alertDialog.isShowing()) {
                alertDialog.closeOptionsMenu();
            }
        }


    }

    public void setAmountConverted(final String amount) {
        TextView result = (TextView) findViewById(R.id.amountConverted);
        BigDecimal first;
        if(amount!=null)
            first = new BigDecimal(amount);
        else
            first = new BigDecimal(1);
        if(this.amount.getText().toString().isEmpty())
            this.amount.setText("1");
        BigDecimal second = new BigDecimal(this.amount.getText().toString());
        BigDecimal res = first.multiply(second);
        result.setText(String.valueOf(res.setScale(3, RoundingMode.CEILING)));
    }



}
