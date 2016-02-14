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

var changeCards = function(cards) {
	HearthstoneDispatcher.dispatch({
		type: "CHANGE_CARDS",
		cards: cards
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

export { receiveDiffAnalyzerResults, changeCollection, changePlayerClasses, receiveCards, changeCards, addFilterCard };