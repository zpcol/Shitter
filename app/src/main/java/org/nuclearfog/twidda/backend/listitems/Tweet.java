package org.nuclearfog.twidda.backend.listitems;

public class Tweet {
    public final TwitterUser user;
    public final Tweet embedded;
    public final long tweetID;
    public final String tweet, replyName,source;
    public final long time, replyID;
    public final int retweet, favorit;
    public final String[] media;
    public final boolean retweeted, favorized, profileflag;

    public Tweet(long tweetID, int retweet, int favorit, TwitterUser user, String tweet, long time,
                 String replyName, String[] media, String source, long replyID, Tweet embedded,
                 boolean retweeted, boolean favorized, boolean profileflag) {
        this.tweetID = tweetID;
        this.user = user;
        this.retweet = retweet;
        this.favorit = favorit;
        this.tweet = tweet;
        this.time = time;
        this.replyID = replyID;
        this.embedded = embedded;
        this.replyName = replyName;
        this.media = media;
        this.source = source;
        this.retweeted = retweeted;
        this.favorized = favorized;
        this.profileflag = profileflag;
    }
}