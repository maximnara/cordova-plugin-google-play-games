# Google Play Games services for Ionic and Cordova apps.
You can login, make game saves, have leaderboard and achievements.
## Demos:
- [Leaderboard Demo Video](https://drive.google.com/file/d/1F-UGff5QaxCH72_c7DEqMqIWonb221g-/view?usp=sharing)
- [Achievements Demo Video](https://drive.google.com/file/d/1Ey5ZkDWz-bduMlXoPr5NgKCwxy9VnVaK/view?usp=sharing)
- [Game Saves Demo Video](https://drive.google.com/file/d/1F0YhgkF81IvN3d_N2-ezjhxEJN8xy34X/view?usp=sharing)

--------

## Table of Contents

- [State of Development](#state-of-development)
- [Install](#install)
- [Usage](#usage)


## State of Development
- [x] <img src="https://img.shields.io/badge/-Complete-brightgreen.svg?label=Sign%20In%20Support&style=flat-square">
- [x] <img src="https://img.shields.io/badge/-Complete-brightgreen.svg?label=Achievements%20Support&style=flat-square">
- [x] <img src="https://img.shields.io/badge/-Complete-brightgreen.svg?label=Leaderboards%20Support&style=flat-square">
- [x] <img src="https://img.shields.io/badge/-Complete-brightgreen.svg?label=Game%20Savings%20Support&style=flat-square">
- [x] <img src="https://img.shields.io/badge/-Complete-brightgreen.svg?label=Friends%20Support&style=flat-square">
- [ ] <img src="https://img.shields.io/badge/-In%20Development-yellow.svg?label=Player%20Stats%20Support&style=flat-square">
- [ ] <img src="https://img.shields.io/badge/-In%20Development-yellow.svg?label=Anti-Piracy%20Support&style=flat-square">

-------- 

## Install

```bash
npm i cordova-plugin-google-play-games --save
```

You should add few lines in AndroidManifest.xml (app/src/main/res/AndroidManifest.xml) in application tag
```
<meta-data android:name="com.google.android.gms.games.APP_ID" android:value="@string/app_id" />
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version"/>
```

And in strings.xml (app/src/main/res/values/strings.xml):
```
<string name="app_id">999999999999</string>
```

-------- 
## Usage

- [Sign In](#sign-in)
- [Achievements](#achievements)
  - [Unlock achievement](#unlock-achievement)
  - [Increment achievement](#increment-achievement)
  - [Show achievements](#show-achievements)
- [Leaderboards](#leaderboards)
  - [Update player score](#update-player-score)
  - [Load player score](#load-player-score)
  - [Show leaderboard](#show-leaderboard)
- [Game Savings](#game-savings)
  - [Save game](#save-game)
  - [Load game save](#load-game-save)
  - [Show saved games](#show-saved-games)
  - [Saved Games Events](#saved-games-events)
- [Friends](#friends)
  - [Get friend list](#get-friend-list)
  - [Show another players profile](#show-another-players-profile)
- [Player stats](#player-stats)
  
  
This library is Promise style, you can use .then or await to fetch results

### Sign In

```javascript
import * as GooglePlayGames from 'cordova-plugin-google-play-games';
let { id } = await GooglePlayGames.login(); // userId returned
```
***
### Achievements

#### Unlock achievement

```javascript
await GooglePlayGames.unlockAchievement({ id: 'your-id-from-google-play-console' });
```

#### Increment achievement

```javascript
await GooglePlayGames.incrementAchievement({ id: 'your-id', count: 1 }); // Count is how much increment achievement
```

#### Show achievements
This method is show native Google Games UI 
```javascript
await GooglePlayGames.showAchievements();
```
***

### Leaderboards

#### Update player score
This method updates player score in specified leaderboard
```javascript
await GooglePlayGames.updatePlayerScore({ id: 'your-leaderboard-id-from-google-play-console', score: 30 }); // Score you want to set
```
***
#### Load player score

```javascript
const { score } = await GooglePlayGames.loadPlayerScore({ id: 'your-leaderboard-id' });
```
***
#### Show leaderboard
This method is show native Google Games UI
```javascript
await GooglePlayGames.showLeaderboard({ id: 'your-leaderboard-id' });
```
***

### Game Savings

#### Save game
```javascript
await GooglePlayGames.saveGame({
  snapshotName: 'unique-id-this-for-save',
  snapshotDescription: 'ANY NAME YOU WANT',
  snapshotContents: { any: 'data', format: 'object' }
});
```
***
#### Load game save

```javascript
let data = await GooglePlayGames.loadGameSave({ snapshotName: 'unique-id-this-for-save' });
// Data will contain { any: 'data', format: 'object' }
```
***
#### Show saved games
This method is show native Google Games UI
```javascript
await GooglePlayGames.showSavedGames({
  /**
   * This is title for native UI window
   */
  title: 'My saved games 777',
  /**
   * Whether or not to display a "create new snapshot" option.
   * After clicking on add button event will be fired.
   */
  allowAddButton: true,
  /**
   * Whether or not to provide a delete overflow menu option for each snapshot. 
   * After clicking on add button event will be fired.
   */
  allowDelete: true,
  /**
   * The maximum number of snapshots to display in native UI
   */
  maxSnapshots: 3,
});
```

#### Saved Games Events

All events called after user clicks some button from native Google Games UI.   
Example: he can ask to create new game saving or set game progress to specified save id. 

**Load Game Request**

User requested to set his progress on some specified save id.
```javascript
window.addEventListener("loadSavedGameRequest", async (event) => {
  console.log('User requested to set his progress on save id: ', event.id)
});
```
**New Game Save Request**

User requested game to created new Saving. No data passed here.
```javascript
window.addEventListener("saveGameRequest", async () => {
  
});
```
**Save Game Conflict**

Some conflict happened when trying to save game.  
I think this is not common used event, but think it should exist.
```javascript
window.addEventListener("saveGameConflict", async (event) => {
  console.log('Conflicting id is: ', event.conflictId)
});
```

***

### Friends

#### Get friend list
This method will return array of users objects. Inside objects all available info.
Method can 
```javascript
window.addEventListener("friendsListRequestSuccessful", async () => {
  try {
    let list = await GooglePlayGames.getFriendsList();
  } catch (e) {
    console.log('No resolution')
  }
});

try {
  let list = await GooglePlayGames.getFriendsList(); 
} catch (e) {
  const ERROR_CODE_HAS_RESOLUTION = 1;
  const ERROR_CODE_NO_RESOLUTION = 2;
  if (e.code === ERROR_CODE_HAS_RESOLUTION) {
    // That's all right, user will be asked for Friends permission
    // After he give access event "friendsListRequestSuccessful" will be fired.
  } else if (e.code === ERROR_CODE_NO_RESOLUTION) {
    console.log('No resolution: ' + e.message);
  }
}
```
```json
[
  {
    "id":"a_99999",
    "name":"Maxim L.",
    "title":"Летчик-ас",
    "retrievedTimestamp":1658298688691,
    // If you have idea how to deal with content:// URI you can use it
    "bannerImageLandscapeUri":"content://com.google.android.gms.games.background/images/a19ec21b/1005",
    "bannerImagePortraitUri":"content://com.google.android.gms.games.background/images/a19ec21b/1006",
    "iconImageUri":"content://com.google.android.gms.games.background/images/a19ec21b/1003",
    "hiResImageUri":"content://com.google.android.gms.games.background/images/a19ec21b/1004",
    "levelInfo": {
      "currentLevel":11,
      "maxXp":90000,
      "minXp":70000,
      "hashCode":2300362
    },
    "friendStatus":4, // Have no idea what is that yet
    // Use that to show user pic in <img> tag
    "iconImageBase64":"data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAA..." 
  }
]
```
***

#### Show another players profile
This method will show standard Play Games menu with user info.
```javascript
await GooglePlayGames.showAnotherPlayersProfile({ id: result[0].id });
```
***

### Player stats
This method will resolve 6 available current player stats:  

— Average session length: The average session length of the player in minutes. Session length is determined by the time that a player is signed in to Google Play Games services.   
— Days since last played: The approximate number of days since the player last played.  
— Number of purchases: The approximate number of in-app purchases for the player.  
— Number of sessions: The approximate number of sessions of the player. Sessions are determined by the number of times that a player signs in to Google Play Games services.  
— Session percentile: The approximation of sessions percentile for the player, given as a decimal value between 0 to 1 inclusive. This value indicates how many sessions the current player has played in comparison to the rest of this game's player base. Higher numbers indicate that this player has played more sessions.  
— Spend percentile: The approximate spend percentile of the player, given as a decimal value between 0 to 1 inclusive. This value indicates how much the current player has spent in comparison to the rest of this game's player base. Higher numbers indicate that this player has spent more.  
```javascript
let stats = await GooglePlayGames.getCurrentPlayerStats();

if (stats.daysSinceLastPlayed > 7) {
  console.log("It's been longer than a week");
}
if (stats.numberOfSessions > 1000) {
  console.log("Veteran player");
}
if (stats.numberOfPurchases == 0) {
  console.log("Show user special offer");
}
```

### Feel free to make your PRs for code structure or new functions or message me in Telegram @luzhkov
