package com.kbeanie.multipicker.search;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kbeanie.multipicker.search.api.BingSearchApi;
import com.kbeanie.multipicker.search.api.RemoteImage;

import java.util.List;

/**
 * Created by kbibek on 3/1/16.
 */
public abstract class BingImageSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void search(String query, String authorizationHeader) {
        showProgress();
        SearchAsyncTask searchTask = new SearchAsyncTask();
        searchTask.setQuery(query, authorizationHeader);
        searchTask.execute((Void) null);
    }

    private ProgressDialog pDialog;

    private void showProgress() {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Searching images");
        pDialog.setMessage("Please wait...");
        pDialog.show();
    }

    private void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public abstract void showResults(List<RemoteImage> images);

    private class SearchAsyncTask extends AsyncTask<Void, Void, List<RemoteImage>> {
        private String query;
        private String authorizationHeader;

        public void setQuery(String query, String authorizationHeader) {
            this.query = query;
            this.authorizationHeader = authorizationHeader;
        }

        @Override
        protected List<RemoteImage> doInBackground(Void... params) {
            BingSearchApi api = new BingSearchApi();
            try {
                return api.getImagesForQuery(query, authorizationHeader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RemoteImage> images) {
            super.onPostExecute(images);
            hideProgress();
            showResults(images);
        }
    }
}
