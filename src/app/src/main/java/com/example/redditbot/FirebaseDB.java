package com.example.redditbot;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseDB {

    private static final FirebaseDB instance = new FirebaseDB();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference agentCollection = db.collection("Agents");
    private final CollectionReference userCollection = db.collection("Users");
    private final CollectionReference subredditCollection = db.collection("Subreddits");
    final String agentsTAG = "Agents";
    final String usersTAG = "Users";
    final String subredditsTAG = "Subreddits";

    private FirebaseDB() {
    }
    public static FirebaseDB getInstance() { return instance; }

    public interface GetBooleanCallBack { void onResult(Boolean bool); }

    public void userExists(GetBooleanCallBack callBack) {
        CurrentUser user = CurrentUser.getInstance();
        userCollection
                .document(user.getDeviceId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callBack.onResult(documentSnapshot.exists());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Could not fetch user document: " + e);
                    }
                });
    }

    public void usernameExists(String username, GetBooleanCallBack callBack) {
        CurrentUser user = CurrentUser.getInstance();
        userCollection
                .whereEqualTo("username", username.toLowerCase())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        callBack.onResult(!queryDocumentSnapshots.isEmpty());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Could not fetch user document: " + e);
                    }
                });
    }

    public void agentExists(String id, GetBooleanCallBack callBack) {
        agentCollection
                .document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callBack.onResult(documentSnapshot.exists());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Could not fetch user document: " + e);
                    }
                });
    }
    public void createUser() {
        CurrentUser user = CurrentUser.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("deviceId", user.getDeviceId());
        data.put("agentId", null);

        userCollection
                .document(user.getDeviceId())
                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while adding user document: " + e);
                    }
                });
    }
    public void createAgent(UserAgent agent) {
        agentCollection
                .add(agent)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while adding agent document: ", e);
                    }
                });
    }
    public void getAgent() {
        CurrentUser user = CurrentUser.getInstance();
        agentCollection
                .whereEqualTo("agentAuthorName", user.getUsername())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            UserAgent agent = documentSnapshot.toObject(UserAgent.class);
                            user.setAgent(agent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while retrieving agent document: ", e);
                    }
                });
    }
    public void loginUser() {
        CurrentUser user = CurrentUser.getInstance();
        userCollection
                .document(user.getDeviceId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            createUser();
                        } else {
                            user.setUsername(documentSnapshot.getString("username"));
                            user.setAgentId(documentSnapshot.getString("agentId"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while logging in: ", e);
                    }
                });
    }
    public void deleteAgent(String agentId) {
        CurrentUser user = CurrentUser.getInstance();
        agentCollection
                .document(agentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userCollection.document(user.getUsername()).update("agentId", null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        user.setAgentId(null);
                                        user.setAgent(null);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(usersTAG, "Error while updating user: ", e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(agentsTAG, "Error while deleting agent document: ", e);
                    }
                });
    }
    public void updatePass(String newPass) {
        CurrentUser user = CurrentUser.getInstance();
        userCollection.document(user.getUsername()).update("pass", newPass)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(usersTAG, "Error while updating password: ", e);
                    }
                });
    }
    public void addSubreddit(Subreddit subreddit) {
        subredditCollection
                .add(subreddit)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(subredditsTAG, "Error while adding subreddit: ", e);
                    }
                });
    }
    public void updateSubreddit(Subreddit subreddit) {
        subredditCollection
                .whereEqualTo("owner", subreddit.getOwner())
                .whereEqualTo("name", subreddit.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subredditCollection
                                        .document(document.getId())
                                        .update("maxPosts", subreddit.getMaxPosts(),
                                                "terms", subreddit.getTerms())
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(subredditsTAG, "Error updating document: ", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(subredditsTAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
