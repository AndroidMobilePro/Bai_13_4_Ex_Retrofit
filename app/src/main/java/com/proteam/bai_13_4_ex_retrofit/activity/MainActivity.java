package com.proteam.bai_13_4_ex_retrofit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.proteam.bai_13_4_ex_retrofit.R;
import com.proteam.bai_13_4_ex_retrofit.adapter.MoviesAdapter;
import com.proteam.bai_13_4_ex_retrofit.model.Movie;
import com.proteam.bai_13_4_ex_retrofit.model.MoviesResponse;
import com.proteam.bai_13_4_ex_retrofit.rest.ApiClient;
import com.proteam.bai_13_4_ex_retrofit.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.OnCardClickListner {

    private static final String TAG = MainActivity.class.getSimpleName();

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";

    private MoviesAdapter moviesAdapter;
    private List<Movie> movies;
    // Progress dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext(), this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(moviesAdapter);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        showDialog();
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                hideDialog();
                int statusCode = response.code();
                movies.addAll(response.body().getResults());
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                hideDialog();
            }
        });
    }

    @Override
    public void OnCardClicked(View view, int position, int id) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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
