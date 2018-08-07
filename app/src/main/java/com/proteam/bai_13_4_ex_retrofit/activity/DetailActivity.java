package com.proteam.bai_13_4_ex_retrofit.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.proteam.bai_13_4_ex_retrofit.R;
import com.proteam.bai_13_4_ex_retrofit.model.Movie;
import com.proteam.bai_13_4_ex_retrofit.rest.ApiClient;
import com.proteam.bai_13_4_ex_retrofit.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();


    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";

    private int id;
    private TextView textView;
    private String content = "";

    // Progress dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView = (TextView) findViewById(R.id.textContent);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
        }

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        showDialog();
        Call<Movie> call = apiService.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                hideDialog();
                int statusCode = response.code();
                Movie movie = response.body();
                content = "-Title: " + movie.getTitle() + "\n"
                        + "-Overview: " + movie.getOverview() + "\n"
                        + "-Title: " + movie.getTitle() + "\n"
                        + "-BackdropPath: " + movie.getBackdropPath() + "\n"
                        + "-ID: " + movie.getId() + "\n"
                        + "-PosterPath: " + movie.getPosterPath() + "\n"
                        + "-Video: " + movie.getVideo() + "\n"
                        + "-VoteAverage: " + movie.getVoteAverage() + "\n";
                textView.setText(content);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                hideDialog();
            }
        });
    }

    // Show loading request
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    // Hide loading request
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
