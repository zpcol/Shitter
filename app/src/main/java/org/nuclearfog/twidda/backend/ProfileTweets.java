package org.nuclearfog.twidda.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import org.nuclearfog.twidda.database.TweetDatabase;
import org.nuclearfog.twidda.R;
import org.nuclearfog.twidda.viewadapter.TimelineAdapter;
import org.nuclearfog.twidda.window.UserProfile;

import twitter4j.Paging;
import twitter4j.Twitter;

public class ProfileTweets extends AsyncTask<Long, Void, Void> {

    private Context context;
    private SwipeRefreshLayout tweetsReload, favoritsReload;
    private ListView profileTweets, profileFavorits;
    private TwitterResource twitterResource;
    private TimelineAdapter homeTl, homeFav;
    private SharedPreferences settings;
    private int load;

    public ProfileTweets(Context context){
        this.context=context;
        twitterResource = TwitterResource.getInstance(context);
        twitterResource.init();
        settings = context.getSharedPreferences("settings", 0);
        load = settings.getInt("preload", 10);
    }

    /**
     * UserProfile Backend
     * @see UserProfile accessing this class
     */
    @Override
    protected void onPreExecute() {
        tweetsReload    = (SwipeRefreshLayout)((UserProfile)context).findViewById(R.id.hometweets);
        favoritsReload  = (SwipeRefreshLayout)((UserProfile)context).findViewById(R.id.homefavorits);
        profileTweets   = (ListView)((UserProfile)context).findViewById(R.id.ht_list);
        profileFavorits = (ListView)((UserProfile)context).findViewById(R.id.hf_list);
    }

    /**
     * @param id UserID[0]  Mode[1]
     */
    @Override
    protected Void doInBackground(Long... id) {
        try {
            long userId = id[0];
            Paging p = new Paging();
            p.setCount(load);
            Twitter twitter = twitterResource.getTwitter();
            if(id[1] == 0) {
                TweetDatabase hTweets = new TweetDatabase(twitter.getUserTimeline(userId,p), context,TweetDatabase.USER_TL,userId);
                homeTl = new TimelineAdapter(context,hTweets);
            } else if(id[1] == 1) {
               TweetDatabase fTweets = new TweetDatabase(twitter.getFavorites(userId,p), context,TweetDatabase.FAV_TL,userId);
                homeFav = new TimelineAdapter(context,fTweets);
            }
        } catch(Exception err){err.printStackTrace();}
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if(homeTl != null) {
            profileTweets.setAdapter(homeTl);
            tweetsReload.setRefreshing(false);
        } else if(homeFav != null) {
            profileFavorits.setAdapter(homeFav);
            favoritsReload.setRefreshing(false);
        }
    }
}