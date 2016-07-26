package com.bryonnicoson.shakeitup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    // yelp object & YelpTask parameters  map
    public SearchResponse searchResponse;
    public Map paramsMap = new HashMap<String, String>();

    private class YelpTask extends AsyncTask<Map, Void, SearchResponse> {

        @Override
        protected SearchResponse doInBackground(Map... parameters) {
            // first element of parameters is our map
            Map<String, String> params = parameters[0];

            // get the lat and long out of & remove them from params
            Double latitude = Double.parseDouble(params.get("latitude").toString());
            Double longitude = Double.parseDouble(params.get("longitude").toString());
            params.remove("latitude");
            params.remove("longitude");

            // build our coordinatOptions object
            CoordinateOptions coordinates = CoordinateOptions.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

            // yelpAPI oauth w/ keys from gradle.properties file, which is .gitignored
            YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                    BuildConfig.CONSUMER_KEY,
                    BuildConfig.CONSUMER_SECRET,
                    BuildConfig.TOKEN,
                    BuildConfig.TOKEN_SECRET);
            YelpAPI yelpAPI = yelpAPIFactory.createAPI();

            // make the call
            Call<SearchResponse> call = yelpAPI.search(coordinates, params);
            try {
                searchResponse = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResponse;
        }

        protected void onPostExecute(SearchResponse searchResponse) {

            // TODO: Process searchResponse object here - for now, a test...
            for (int i = 0; i < searchResponse.businesses().size(); i++) {
                Log.i("TEST", searchResponse.businesses().get(i).name().toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // TODO: get lat and long from GPS - these are GA Chicago according to Google Maps
        paramsMap.put("latitude", "41.8906631");
        paramsMap.put("longitude", "-87.6290736");

        // TODO: get search parameters from ui / db
        paramsMap.put("term", "food");
        paramsMap.put("limit", "10");

        YelpTask yelpTask = new YelpTask();
        yelpTask.execute(paramsMap);

    }
}
