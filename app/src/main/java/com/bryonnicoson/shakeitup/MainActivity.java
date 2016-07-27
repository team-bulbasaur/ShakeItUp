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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    // TODO: move yelpTask, searchResponse processing to separate class? service? - ask James for feedback

    // yelp object & YelpTask parameters map & arrayList of our Restaurant objects
    public SearchResponse searchResponse;
    public Map paramsMap = new HashMap<String, String>();
    public ArrayList<Restaurant> restaurants = new ArrayList<>();

    private class YelpTask extends AsyncTask<Map, Void, com.yelp.clientlib.entities.SearchResponse> {

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

        // test - fork change
        protected void onPostExecute(SearchResponse searchResponse) {

            for (int i = 0; i < searchResponse.businesses().size(); i++) {
                // get what we need from the yelp response object
                String id = searchResponse.businesses().get(i).id().toString();
                String name = searchResponse.businesses().get(i).name().toString();
                String image = searchResponse.businesses().get(i).imageUrl().toString();
                String website = searchResponse.businesses().get(i).mobileUrl().toString();
                Double latitude = searchResponse.businesses().get(i).location().coordinate().latitude();
                Double longitude = searchResponse.businesses().get(i).location().coordinate().longitude();
                String snippetText = searchResponse.businesses().get(i).snippetText().toString();
                Integer reviewCount = searchResponse.businesses().get(i).reviewCount();
                String snippetImg = searchResponse.businesses().get(i).snippetImageUrl();
                String ratingImg = searchResponse.businesses().get(i).ratingImgUrlLarge().toString();
                String phone = searchResponse.businesses().get(i).displayPhone().toString();

                // unpack and format categories and displayAddress lists
                StringBuilder sb = new StringBuilder();
                Integer categoriesListSize = searchResponse.businesses().get(i).categories().size();
                for (int j = 0; j < categoriesListSize; j++) {
                    sb.append(searchResponse.businesses().get(i).categories().get(j).name());
                    if (j+1 < categoriesListSize)
                        sb.append(" \u00B7 "); // put a middot between category names
                }
                String categories = sb.toString();
                sb.setLength(0);
                Integer addressListSize = searchResponse.businesses().get(i).location().displayAddress().size();
                for (int k = 0; k < addressListSize; k++) {
                    sb.append(searchResponse.businesses().get(i).location().displayAddress().get(k).toString());
                    if (k+1 < addressListSize)
                        sb.append("\n");  // if there's another element in addressList, put it on a new line
                }
                String address = sb.toString();

                // construct new Restaurant object
                Restaurant restaurant = new Restaurant(id, name, image, categories, address, phone,
                        website, ratingImg, reviewCount, snippetImg, snippetText, latitude, longitude);

                // add to arraylist
                restaurants.add(restaurant);

            }
            // TODO: return restaurants to ui adapter - for now, logging to validate...
            // let's pretend i added something to the ui - bb
            for (int inc = 0; inc < restaurants.size(); inc++) {
                Log.i("RESTAURANTS", restaurants.get(inc).getName());
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
        paramsMap.put("limit", "20");  // fyi: 20 is max

        YelpTask yelpTask = new YelpTask();
        yelpTask.execute(paramsMap);
    }
}