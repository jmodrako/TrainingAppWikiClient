package com.example.razi.wikipediaclient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
@EActivity(R.layout.activity_wikipedia)
public class SuperActivity extends Activity {

	@ViewById(R.id.homeTitle) TextView title;

	@AfterViews void afterViews() {

	}

	@Click(R.id.homeDownloadBtn) void onDownload() {
		downloadAsync();
	}

	@Click(R.id.homeDownloadBtnSync) void onDownloadSync() {
		download();
	}

	@Background void download() {
		final WikiApi wikiApi = Connection.createApi();
		onDownloadSuccess(wikiApi.wikiSearch("Joseph_Fourier"));
	}

	@UiThread void onDownloadSuccess(MainResponseModel model) {
		title.setText(model.getQuery().getPages().get(0).getTitle());
	}

	private void downloadAsync() {
		final WikiApi wikiApi = Connection.createApi();
		wikiApi.wikiSearch("Joseph_Fourier", new Callback<MainResponseModel>() {
			@Override public void success(MainResponseModel s, Response response) {
				Toast.makeText(SuperActivity.this, s.toString(),
						Toast.LENGTH_SHORT).show();
				title.setText(s.getQuery().getPages().get(0).getTitle());
			}

			@Override public void failure(RetrofitError error) {
				Toast.makeText(SuperActivity.this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
