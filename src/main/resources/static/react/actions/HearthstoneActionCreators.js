import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

var	receiveDiffAnalyzerResults = function(diffAnalyzerResults) {
		HearthstoneDispatcher.dispatch({
			type: "DIFFANALYZER_RESULTS",
			diffAnalyzer : diffAnalyzerResults
		});
}

var receiveCards = function(data) {
	HearthstoneDispatcher.dispatch({
		type: "CARD_LOAD",
		cards: data
	});
}

var addFilterCard = function(data) {
	HearthstoneDispatcher.dispatch({
		type: "ADD_FILTER_CARD",
		cardName: data
	});
}

var removeFilterCard = function(data) {
	HearthstoneDispatcher.dispatch({
		type: "REMOVE_FILTER_CARD",
		cardName: data
	});
}

var changeCollection = function(collection) {
	HearthstoneDispatcher.dispatch({
		type: "CHANGE_COLLECTION",
		collection: collection
	});
}

var changePlayerClasses = function(playerClasses) {
	HearthstoneDispatcher.dispatch({
		type: "CHANGE_PLAYER_CLASSES",
		playerClasses: playerClasses
	});
}

var loadDecks = function() {
	HearthstoneDispatcher.dispatch({
		type: "LOAD_DECKS"
	});
}

var deckLoadSuccess = function() {
	HearthstoneDispatcher.dispatch({
		type: "DECK_LOAD_SUCCESS"
	});
}

var deckLoadFailure = function() {
	HearthstoneDispatcher.dispatch({
		type: "DECK_LOAD_FAILURE"
	});
}

export { receiveDiffAnalyzerResults, changeCollection, changePlayerClasses, receiveCards, addFilterCard, removeFilterCard, loadDecks, deckLoadSuccess, deckLoadFailure };