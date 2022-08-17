/* eslint-disable */

exports.errorCodes = {
    ERROR_CODE_HAS_RESOLUTION: 1,
    ERROR_CODE_NO_RESOLUTION: 2,
};

/**
 * Login
 */
exports.login = function login()
{
    return new Promise((resolve, reject) => {
        callPlugin('login', [], resolve, reject);
    });
};

/**
 * Unlock achievement
 * @param {String} params.id - id of achievement
 */
exports.unlockAchievement = function unlockAchievement(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false)
    {
        throw new Error('You should specify id of achievement');
    }
    return new Promise((resolve, reject) => {
        callPlugin('unlockAchievement', [params.id], resolve, reject);
    });
};

/**
 * Increment achievement
 * @param {String} params.id - id of achievement
 * @param {Number} params.count - count of how much increment achievement
 */
exports.incrementAchievement = function incrementAchievement(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false || params.hasOwnProperty('count') === false)
    {
        throw new Error('You should specify id of achievement and count how much to increase');
    }
    return new Promise((resolve, reject) => {
        callPlugin('incrementAchievement', [params.id, params.count], resolve, reject);
    });
};

/**
 * Show achievements
 */
exports.showAchievements = function showAchievements()
{
    return new Promise((resolve, reject) => {
        callPlugin('showAchievements', [], resolve, reject);
    });
};

/**
 * Reveal achievement
 * @param {String} params.id - id of achievement
 */
exports.revealAchievement = function revealAchievement(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false)
    {
        throw new Error('You should specify id of achievement');
    }
    return new Promise((resolve, reject) => {
        callPlugin('revealAchievement', [params.id], resolve, reject);
    });
};

/**
 * Set count of steps in achievement
 * @param {String} params.id - id of achievement
 * @param {Number} params.count - count of steps, should be greater than 0
 */
exports.setStepsInAchievement = function setStepsInAchievement(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false || params.hasOwnProperty('count') === false)
    {
        throw new Error('You should specify id of achievement and count of steps');
    }
    return new Promise((resolve, reject) => {
        callPlugin('setStepsInAchievement', [params.id, params.count], resolve, reject);
    });
};

/**
 * Update player score
 * @param {String} params.id - id of leaderboard
 * @param {Number} params.score - player score to set
 */
exports.updatePlayerScore = function updatePlayerScore(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false || params.hasOwnProperty('score') === false)
    {
        throw new Error('You should specify id of leaderboard and player score you want to set');
    }
    return new Promise((resolve, reject) => {
        callPlugin('updatePlayerScore', [params.id, params.score], resolve, reject);
    });
};

/**
 * Load player score
 * @param {String} params.id - id of leaderboard
 */
exports.loadPlayerScore = function loadPlayerScore(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false)
    {
        throw new Error('You should specify leaderboard id');
    }
    return new Promise((resolve, reject) => {
        callPlugin('loadPlayerScore', [params.id], resolve, reject);
    });
};

/**
 * Show leaderboard
 * @param {String} params.id - id of leaderboard
 */
exports.showLeaderboard = function showLeaderboard(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false)
    {
        throw new Error('You should specify leaderboard id');
    }
    return new Promise((resolve, reject) => {
        callPlugin('showLeaderboard', [params.id], resolve, reject);
    });
};

/**
 * Show all leaderboards
 */
exports.showAllLeaderboards = function showAllLeaderboards()
{
    return new Promise((resolve, reject) => {
        callPlugin('showAllLeaderboards', [], resolve, reject);
    });
};

/**
 * Show saved games
 * @param {String} params.title - The title to display in the action bar
 * @param {Boolean} params.allowAddButton - Whether or not to display a "create new snapshot" option
 * @param {Boolean} params.allowDelete - Whether or not to provide a delete overflow menu option for each snapshot
 * @param {Number} params.maxSnapshots - The maximum number of snapshots to display
 */
exports.showSavedGames = function showSavedGames(params)
{
    params = defaults(params, {});
    if (
      params.hasOwnProperty('title') === false ||
      params.hasOwnProperty('allowAddButton') === false ||
      params.hasOwnProperty('allowDelete') === false ||
      params.hasOwnProperty('maxSnapshots') === false
    ) {
        throw new Error('You should specify title, allowAddButton, allowDelete and maxSnapshots parameters');
    }
    return new Promise((resolve, reject) => {
        callPlugin('showSavedGames', [params.title, params.allowAddButton, params.allowDelete, params.maxSnapshots], resolve, reject);
    });
};

/**
 * Save game
 * @param {String} params.snapshotName - The inner used name for snapshot, should be unique for each game save
 * @param {String} params.snapshotDescription - The snapshot description, will be shown in saves list UI
 * @param {Object} params.snapshotContents - Object you want to save
 */
exports.saveGame = function saveGame(params)
{
    params = defaults(params, {});
    if (
      params.hasOwnProperty('snapshotName') === false ||
      params.hasOwnProperty('snapshotDescription') === false ||
      params.hasOwnProperty('snapshotContents') === false
    ) {
        throw new Error('You should specify snapshotName, snapshotDescription and snapshotContents parameters');
    }
    return new Promise((resolve, reject) => {
        callPlugin('saveGame', [params.snapshotName, params.snapshotDescription, params.snapshotContents], resolve, reject);
    });
};

/**
 * Save game
 * @param {String} params.snapshotName - The inner used name for snapshot, should be unique for each game save
 */
exports.loadGameSave = function loadGameSave(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('snapshotName') === false) {
        throw new Error('You should specify snapshotName parameter');
    }
    return new Promise((resolve, reject) => {
        callPlugin('loadGameSave', [params.snapshotName], resolve, reject);
    });
};

/**
 * Get friends list
 * There is two cases: when no permission needed from user, and permission will be asked.
 *
 * — First case: this function promise will return Array of friends.
 * — Second case:
 * After call for some cases event "friendsListRequestSuccessful" can be thrown. This method promise will be rejected
 * with error code 1. Error code 1 - user error that shows that after it event will be fired, code 2 mean that nothing
 * will happen after this error.
 * When "friendsListRequestSuccessful" event is fired, you should call this function again by yourself.
 */
exports.getFriendsList = function getFriendsList()
{
    return new Promise((resolve, reject) => {
        callPlugin('getFriendsList', [], resolve, reject);
    });
};

/**
 * Show another player profile
 * @param {String} params.id - Another player profile id
 */
exports.showAnotherPlayersProfile = function showAnotherPlayersProfile(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false) {
        throw new Error('You should specify another player id parameter');
    }
    return new Promise((resolve, reject) => {
        callPlugin('showAnotherPlayersProfile', [params.id], resolve, reject);
    });
};

/**
 * Show player search
 */
exports.showPlayerSearch = function showPlayerSearch()
{
    return new Promise((resolve, reject) => {
        callPlugin('showPlayerSearch', [], resolve, reject);
    });
};

/**
 * Get player
 * @param {String} params.id - Player id
 * @param {Boolean=false} params.forceReload - Do google games need to force reload player data from server. This may cause lack of work speed.
 */
exports.getPlayer = function getPlayer(params)
{
    params = defaults(params, { forceReload: false });
    if (params.hasOwnProperty('id') === false) {
        throw new Error('You should specify player id');
    }
    return new Promise((resolve, reject) => {
        callPlugin('getPlayer', [params.id, params.forceReload], resolve, reject);
    });
};

/**
 * Returns current player stats.
 * — Average session length: The average session length of the player in minutes. Session length is determined by the time that a player is signed in to Google Play Games services.
 * — Days since last played: The approximate number of days since the player last played.
 * — Number of purchases: The approximate number of in-app purchases for the player.
 * — Number of sessions: The approximate number of sessions of the player. Sessions are determined by the number of times that a player signs in to Google Play Games services.
 * — Session percentile: The approximation of sessions percentile for the player, given as a decimal value between 0 to 1 inclusive. This value indicates how many sessions the current player has played in comparison to the rest of this game's player base. Higher numbers indicate that this player has played more sessions.
 * — Spend percentile: The approximate spend percentile of the player, given as a decimal value between 0 to 1 inclusive. This value indicates how much the current player has spent in comparison to the rest of this game's player base. Higher numbers indicate that this player has spent more.
 */
exports.getCurrentPlayerStats = function getCurrentPlayerStats()
{
    return new Promise((resolve, reject) => {
        callPlugin('getCurrentPlayerStats', [], resolve, reject);
    });
};

/**
 * Increment event
 * @param {String} params.id - Event id from play console
 * @param {Integer} params.amount - Amount of how much to increment
 */
exports.incrementEvent = function incrementEvent(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false || params.hasOwnProperty('amount') === false) {
        throw new Error('You should specify event id and amount parameters');
    }
    return new Promise((resolve, reject) => {
        callPlugin('incrementEvent', [params.id, params.amount], resolve, reject);
    });
};

/**
 * Get all events
 */
exports.getAllEvents = function getAllEvents()
{
    return new Promise((resolve, reject) => {
        callPlugin('getAllEvents', [], resolve, reject);
    });
};

/**
 * Get event by id
 * @param {String} params.id - Event id from play console
 */
exports.getEvent = function getEvent(params)
{
    params = defaults(params, {});
    if (params.hasOwnProperty('id') === false) {
        throw new Error('You should specify event id parameters');
    }
    return new Promise((resolve, reject) => {
        callPlugin('getEvent', [params.id], resolve, reject);
    });
};

/**
 * Helper function to call cordova plugin
 * @param {String} name - function name to call
 * @param {Array} params - optional params
 * @param {Function} onSuccess - optional on sucess function
 * @param {Function} onFailure - optional on failure functioin
 */
function callPlugin(name, params, onSuccess, onFailure)
{
    cordova.exec(function callPluginSuccess(result)
    {

        if (isFunction(onSuccess))
        {
            onSuccess(result);
        }
    }, function callPluginFailure(error)
    {
        if (isFunction(onFailure))
        {
            onFailure(error)
        }
    }, 'GooglePlayGamesPlugin', name, params);
}

/**
 * Helper function to check if a function is a function
 * @param {Object} functionToCheck - function to check if is function
 */
function isFunction(functionToCheck)
{
    var getType = {};
    var isFunction = functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
    return isFunction === true;
}

/**
 * Helper function to do a shallow defaults (merge). Does not create a new object, simply extends it
 * @param {Object} o - object to extend
 * @param {Object} defaultObject - defaults to extend o with
 */
function defaults(o, defaultObject)
{
    if (typeof o === 'undefined')
    {
        return defaults({}, defaultObject);
    }

    for (var j in defaultObject)
    {
        if (defaultObject.hasOwnProperty(j) && o.hasOwnProperty(j) === false)
        {
            o[j] = defaultObject[j];
        }
    }

    return o;
}
