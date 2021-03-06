package org.nuclearfog.twidda.backend;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import org.nuclearfog.twidda.backend.engine.EngineException;
import org.nuclearfog.twidda.backend.engine.TwitterEngine;
import org.nuclearfog.twidda.backend.items.TwitterTrend;
import org.nuclearfog.twidda.database.AppDatabase;
import org.nuclearfog.twidda.fragment.TrendFragment;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Background task to load a list of location specific trends
 *
 * @see TrendFragment
 */
public class TrendListLoader extends AsyncTask<Integer, Void, List<TwitterTrend>> {

    @Nullable
    private EngineException twException;
    private WeakReference<TrendFragment> callback;
    private TwitterEngine mTwitter;
    private AppDatabase db;
    private boolean isEmpty;


    public TrendListLoader(TrendFragment callback) {
        this.callback = new WeakReference<>(callback);
        db = new AppDatabase(callback.getContext());
        mTwitter = TwitterEngine.getInstance(callback.getContext());
        isEmpty = callback.isEmpty();
    }


    @Override
    protected void onPreExecute() {
        if (callback.get() != null)
            callback.get().setRefresh(true);
    }


    @Override
    protected List<TwitterTrend> doInBackground(Integer[] param) {
        List<TwitterTrend> trends;
        int woeId = param[0];
        try {
            if (isEmpty) {
                trends = db.getTrends(woeId);
                if (trends.isEmpty()) {
                    trends = mTwitter.getTrends(woeId);
                    db.storeTrends(trends, woeId);
                }
            } else {
                trends = mTwitter.getTrends(woeId);
                db.storeTrends(trends, woeId);
            }
            return trends;
        } catch (EngineException twException) {
            this.twException = twException;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(List<TwitterTrend> trends) {
        if (callback.get() != null) {
            callback.get().setRefresh(false);
            if (trends != null) {
                callback.get().setData(trends);
            }
            if (twException != null) {
                callback.get().onError(twException);
            }
        }
    }
}