package io.luzh.cordova.plugin;

import android.app.Activity;
import android.util.Log;
import android.text.TextUtils;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.android.gms.games.AnnotatedData;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.GamesSignInClient;

import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;

import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;

import android.content.Intent;

import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GooglePlayGamesPlugin extends CordovaPlugin {

    private static final String TAG = "GOOGLE_PLAY_GAMES";

    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final int RC_SAVED_GAMES = 9009;

    private static final String EVENT_LOAD_SAVED_GAME_REQUEST = "loadSavedGameRequest";
    private static final String EVENT_SAVE_GAME_REQUEST = "saveGameRequest";
    private static final String EVENT_SAVE_GAME_CONFLICT = "saveGameConflict";

    private RelativeLayout bannerContainerLayout;
    private CordovaWebView cordovaWebView;
    private ViewGroup parentLayout;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (action.equals("init")) {
            this.initAction(args, callbackContext);
            return true;
        }

        else if (action.equals("login")) {
            this.loginAction(args, callbackContext);
            return true;
        }

        else if (action.equals("unlockAchievement")) {
            this.unlockAchievementAction(args.getString(0), callbackContext);
            return true;
        }

        else if (action.equals("incrementAchievement")) {
            this.incrementAchievementAction(args.getString(0), args.getInt(1), callbackContext);
            return true;
        }

        else if (action.equals("showAchievements")) {
            this.showAchievementsAction(callbackContext);
            return true;
        }

        else if (action.equals("updatePlayerScore")) {
            this.updatePlayerScoreAction(args.getString(0), args.getInt(1), callbackContext);
            return true;
        }

        else if (action.equals("loadPlayerScore")) {
            this.loadPlayerScoreAction(args.getString(0), callbackContext);
            return true;
        }

        else if (action.equals("showLeaderboard")) {
            this.showLeaderboardAction(args.getString(0), callbackContext);
            return true;
        }

        else if (action.equals("showSavedGames")) {
            this.showSavedGamesAction(args.getString(0), args.getBoolean(1), args.getBoolean(2), args.getInt(3), callbackContext);
            return true;
        }

        else if (action.equals("saveGame")) {
            this.saveGameAction(args.getString(0), args.getString(1), args.getJSONObject(2), callbackContext);
            return true;
        }

        else if (action.equals("loadGameSave")) {
            this.loadGameSaveAction(args.getString(0), callbackContext);
            return true;
        }

        return false;
    }

    /** --------------------------------------------------------------- */

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        cordovaWebView = webView;
        super.initialize(cordova, webView);
        PlayGamesSdk.initialize(cordova.getActivity());
    }

    /** ----------------------- UTILS --------------------------- */

    private void emitWindowEvent(final String event) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s');", event));
            }
        });
    }

    private void emitWindowEvent(final String event, final JSONObject data) {
        final CordovaWebView view = this.webView;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl(String.format("javascript:cordova.fireWindowEvent('%s', %s);", event, data.toString()));
            }
        });
    }

    /** ----------------------- INITIALIZATION --------------------------- */

    /**
     * Intilization action Initializes Yandex Ads
     */
    private void initAction(JSONArray args, final CallbackContext callbackContext) throws JSONException {

    }

    /**
     * Initializes Rewarded ad
     */
    private void loginAction(JSONArray args, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(cordova.getActivity());

                gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
                    boolean isAuthenticated =
                            (isAuthenticatedTask.isSuccessful() &&
                                    isAuthenticatedTask.getResult().isAuthenticated());

                    if (isAuthenticated) {
                        PlayGames.getPlayersClient(cordova.getActivity()).getCurrentPlayer().addOnCompleteListener(mTask -> {
                                try {
                                    JSONObject result = new JSONObject();
                                    result.put("id", mTask.getResult().getPlayerId());
                                    callbackContext.success(result);
                                } catch (JSONException err) {
                                    callbackContext.error(err.getMessage());
                                }
                            }
                        );
                    } else {
                        callbackContext.error("Login failed");
                    }
                });
            }
        });
    }

    /**
     * Unlock achievement
     */
    private void unlockAchievementAction(String achievementId, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getAchievementsClient(cordova.getActivity()).unlock(achievementId);
                callbackContext.success();
            }
        });
    }

    /**
     * Increment achievement
     */
    private void incrementAchievementAction(String achievementId, Integer count, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getAchievementsClient(cordova.getActivity()).increment(achievementId, count);
                callbackContext.success();
            }
        });
    }

    /**
     * Show achievements
     */
    private void showAchievementsAction(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getAchievementsClient(cordova.getActivity())
                        .getAchievementsIntent()
                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                cordova.getActivity().startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                            }
                        });
                callbackContext.success();
            }
        });
    }

    /**
     * Update player score
     */
    private void updatePlayerScoreAction(String leaderboardId, Integer score, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getLeaderboardsClient(cordova.getActivity())
                        .submitScore(leaderboardId, score);
                callbackContext.success();
            }
        });
    }

    /**
     * Load player score
     */
    private void loadPlayerScoreAction(String leaderboardId, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getLeaderboardsClient(cordova.getActivity())
                    .loadCurrentPlayerLeaderboardScore(leaderboardId, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
                    .addOnSuccessListener(new OnSuccessListener<AnnotatedData<LeaderboardScore>>() {
                        @Override
                        public void onSuccess(AnnotatedData<LeaderboardScore> score) {
                            try {
                                @Nullable LeaderboardScore scoreObject = score.get();
                                JSONObject result = new JSONObject();
                                result.put("score", scoreObject != null ? scoreObject.getRawScore() : 0);
                                callbackContext.success(result);
                            } catch (JSONException err) {
                                callbackContext.error(err.getMessage());
                            }
                        }
                    });
            }
        });
    }

    /**
     * Show leaderboard
     */
    private void showLeaderboardAction(String leaderboardId, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                PlayGames.getLeaderboardsClient(cordova.getActivity())
                        .getLeaderboardIntent(leaderboardId)
                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                cordova.getActivity().startActivityForResult(intent, RC_LEADERBOARD_UI);
                            }
                        });
            }
        });
    }

    /**
     * Show saved games
     */
    private void showSavedGamesAction(String title, Boolean allowAddButton, Boolean allowDelete, Integer numberOfSavedGames, final CallbackContext callbackContext) {
        GooglePlayGamesPlugin self = this;
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                SnapshotsClient snapshotsClient =
                        PlayGames.getSnapshotsClient(cordova.getActivity());
                int maxNumberOfSavedGamesToShow = numberOfSavedGames;

                snapshotsClient
                    .getSelectSnapshotIntent(title, allowAddButton, allowDelete, maxNumberOfSavedGamesToShow)
                    .addOnSuccessListener(
                        new OnSuccessListener<Intent>(){
                            @Override
                            public void onSuccess(Intent intent) {
                                cordova.setActivityResultCallback(self);
                                cordova.getActivity().startActivityForResult(intent, RC_SAVED_GAMES);
                                callbackContext.success();
                            }
                        }
                    );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        GooglePlayGamesPlugin self = this;
        Log.d(TAG, "onActivityResult");
        if (intent != null) {
            if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA)) {
                // Load a snapshot.
                SnapshotMetadata snapshotMetadata =
                        intent.getParcelableExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA);
                String mCurrentSaveName = snapshotMetadata.getUniqueName();
                try {
                    JSONObject result = new JSONObject();
                    result.put("id", mCurrentSaveName);
                    self.emitWindowEvent(EVENT_LOAD_SAVED_GAME_REQUEST, result);
                } catch (JSONException err) {
                    Log.d(TAG, "onActivityResult error", err);
                }
            } else if (intent.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_NEW)) {
                // Create a new snapshot named with a unique string
                self.emitWindowEvent(EVENT_SAVE_GAME_REQUEST);
            }
        }
    }

    /**
     * Save game
     */
    private void saveGameAction(String snapshotName, String snapshotDescription, JSONObject snapshotContents, final CallbackContext callbackContext) {
        GooglePlayGamesPlugin self = this;
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;
                SnapshotsClient snapshotsClient =
                        PlayGames.getSnapshotsClient(cordova.getActivity());
                snapshotsClient
                    .open(snapshotName, true, conflictResolutionPolicy)
                    .addOnSuccessListener(new OnSuccessListener<SnapshotsClient.DataOrConflict<Snapshot>>(){
                        @Override
                        public void onSuccess(SnapshotsClient.DataOrConflict<Snapshot> dataOrConflict) {
                            if (dataOrConflict.isConflict()) {
                                // Send conflict id
                                try {
                                    JSONObject result = new JSONObject();
                                    result.put("conflictId", dataOrConflict.getConflict().getConflictId());
                                    self.emitWindowEvent(EVENT_SAVE_GAME_CONFLICT, result);
                                } catch (JSONException err) {
                                    callbackContext.error(err.getMessage());
                                }
                                return;
                            }
                            // Set the data payload for the snapshot
                            dataOrConflict.getData().getSnapshotContents().writeBytes(snapshotContents.toString().getBytes(StandardCharsets.UTF_8));

                            // Create the change operation
                            SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                                    .setDescription(snapshotDescription)
                                    .build();

                            // Commit the operation
                            snapshotsClient.commitAndClose(dataOrConflict.getData(), metadataChange);
                            callbackContext.success();
                        }
                    });
            }
        });
    }

    /**
     * Save game
     */
    private void loadGameSaveAction(String snapshotName, final CallbackContext callbackContext) {
        GooglePlayGamesPlugin self = this;
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.e(TAG, "READ START");
                SnapshotsClient snapshotsClient =
                        PlayGames.getSnapshotsClient(cordova.getActivity());
                // In the case of a conflict, the most recently modified version of this snapshot will be used.
                int conflictResolutionPolicy = SnapshotsClient.RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED;

                // Open the saved game using its name.
                snapshotsClient
                    .open(snapshotName, true, conflictResolutionPolicy)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error while opening Snapshot.", e);
                            callbackContext.error(e.getMessage());
                        }
                    })
                    .continueWith(new Continuation<SnapshotsClient.DataOrConflict<Snapshot>, byte[]>() {
                        @Override
                        public byte[] then(@NonNull Task<SnapshotsClient.DataOrConflict<Snapshot>> task) throws Exception {
                            Snapshot snapshot = task.getResult().getData();
                            // Opening the snapshot was a success and any conflicts have been resolved.
                            try {
                                // Extract the raw data from the snapshot.
                                return snapshot.getSnapshotContents().readFully();
                            } catch (IOException e) {
                                Log.e(TAG, "Error while reading Snapshot.", e);
                                callbackContext.error(e.getMessage());
                            }
                            return null;
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                        @Override
                        public void onComplete(@NonNull Task<byte[]> task) {
                            task.addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    try {
                                        Log.e(TAG, "READ SUCCESS");
                                        callbackContext.success(new JSONObject(new String(bytes, StandardCharsets.UTF_8)));
                                    } catch (JSONException e) {
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
            }
        });
    }
}
