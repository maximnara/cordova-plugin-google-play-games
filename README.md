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
- [ ] <img src="https://img.shields.io/badge/-In%20Development-yellow.svg?label=Friends%20Support&style=flat-square">
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

### Feel free to make your PRs for code structure or new functions
