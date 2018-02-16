package org.nuclearfog.twidda.window;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.nuclearfog.twidda.backend.UserLists;
import org.nuclearfog.twidda.database.UserDatabase;
import org.nuclearfog.twidda.R;
import org.nuclearfog.twidda.viewadapter.UserAdapter;

/**
 * Get Follow Connections from an User
 * @see UserLists
 */
public class UserDetail extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private long userID, tweetID;
    private long mode;
    private ListView userListview;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.user);
        Intent i = getIntent();
        userID = i.getExtras().getLong("userID");
        mode = i.getExtras().getLong("mode");
        if(i.hasExtra("tweetID")){
            tweetID = i.getExtras().getLong("tweetID");
        }
        userListview = (ListView) findViewById(R.id.followList);
        userListview.setOnItemClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.follow_toolbar);
        setSupportActionBar(toolbar);
        getUsers();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu m ) {
        toolbar.inflateMenu(R.menu.setting);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserAdapter uAdp = (UserAdapter) userListview.getAdapter();
        UserDatabase uDB = uAdp.getData();
        long userID = uDB.getUserID(position);
        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        Bundle bundle = new Bundle();
        bundle.putLong("userID",userID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getUsers() {
        UserLists uList = new UserLists(UserDetail.this);
        if(mode == 0L){
            getSupportActionBar().setTitle(R.string.following);
            uList.execute(userID, UserLists.FOLLOWING, -1L);
        } else if(mode == 1L){
            getSupportActionBar().setTitle(R.string.follower);
            uList.execute(userID, UserLists.FOLLOWERS, -1L);
        } else if(mode == 2L){
            getSupportActionBar().setTitle(R.string.retweet);
            uList.execute(userID, UserLists.RETWEETER, -1L);
        } else if(mode == 3L){
            getSupportActionBar().setTitle(R.string.favorite);
            uList.execute(userID, UserLists.FAVORISER, -1L);
        }
    }
}