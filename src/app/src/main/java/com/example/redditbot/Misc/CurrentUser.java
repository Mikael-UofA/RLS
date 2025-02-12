package com.example.redditbot.Misc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.redditbot.Containers.SubredditList;
import com.example.redditbot.DataHolders.AgentInfo;
import com.example.redditbot.DataHolders.Client;
import com.example.redditbot.DataHolders.Subreddit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import masecla.reddit4j.objects.Sorting;

/**
 * This class is a Simpleton that contains the agent information and the list of subreddit of the user.
 * It is across the codebase to transfer and provide the information necessary.
 */
public class CurrentUser implements Serializable {
    private static final CurrentUser instance = new CurrentUser();
    private AgentInfo agent;
    private SubredditList subreddits;
    private final Client client;
    private final ArrayList<String> sortingList = new ArrayList<>();

    private CurrentUser() {
        this.subreddits = new SubredditList();
        this.client = new Client();
        Collections.addAll(this.sortingList, Sorting.HOT.getValue(), Sorting.NEW.getValue(),
                Sorting.TOP.getValue(), Sorting.CONTROVERSIAL.getValue(), Sorting.RISING.getValue());
    }

    public static CurrentUser getInstance() {
        return instance;
    }
    public AgentInfo getAgent() {
        return agent;
    }
    public Client getClient() {
        return client;
    }
    public ArrayList<String> getSortingList() {
        return sortingList;
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
    public void addSubreddit(Subreddit subreddit) {
        this.subreddits.add(subreddit);
    }
    public void editSubreddit(Subreddit subreddit, Integer position) {
        this.subreddits.replace(subreddit, position);
    }

    /**
     * Use this method to authenticate the agent
     * @param context The context required to make a Toast
     */
    public void setClientInfo(Context context) {
        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(() -> {
            try {
                client.setInfo(agent);
                handler.post(() -> Toast.makeText(context, "Authentication Successful", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                handler.post(() -> Toast.makeText(context, "Authentication Unsuccessful", Toast.LENGTH_SHORT).show());
            }
        });

        thread.start();
    }
    /**
     * Use this method to save the agent information into the Internal Storage
     *
     * @param context The context needed to access the storage
     */
    public void saveAgentInfo(Context context) {
        try (FileOutputStream fos = context.openFileOutput("agent-info.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.agent);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }

    /**
     * Use this method to save the list of subreddit into the Internal Storage
     *
     * @param context The context needed to access the storage
     */
    public void saveSubreddits(Context context) {
        try (FileOutputStream fos = context.openFileOutput("subreddits.ser", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this.subreddits);
        } catch (IOException e) {
            Log.w("FileSaving", "Error: " + e);
        }
    }
}
