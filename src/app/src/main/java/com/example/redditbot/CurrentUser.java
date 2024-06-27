package com.example.redditbot;

import android.content.Context;
import android.util.Log;

import com.example.redditbot.Containers.SubredditList;
import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.DataHolders.Client;
import com.example.redditbot.DataHolders.Subreddit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CurrentUser implements Serializable {
    private static final CurrentUser instance = new CurrentUser();
    private AgentInfo agent;
    private SubredditList subreddits;
    private final Client client;
    private Boolean connected;

    private CurrentUser() {
        this.connected = false;
        this.subreddits = new SubredditList();
        this.client = new Client();
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public AgentInfo getAgent() {
        return agent;
    }

    public void setAgent(AgentInfo agent) {
        this.agent = agent;
    }

    public SubredditList getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(SubredditList subreddits) {
        this.subreddits = subreddits;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void addSubreddit(Subreddit subreddit) {
        this.subreddits.add(subreddit);
    }
    public void editSubreddit(Subreddit subreddit, int position) {
        this.subreddits.replace(subreddit, position);
    }
    public void authClient2() {
        client.startConnection2();
    }
    public void setClientInfo() {
        client.setInfo(agent);
    }

    public void saveAgentInfo(Context context) {
        try (FileOutputStream fos = context.openFileOutput("agent-info.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.agent);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
    public void saveSubreddits(Context context) {
        try (FileOutputStream fos = context.openFileOutput("subreddits.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.subreddits);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
}
