import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

var	receiveDiffAnalyzerResults = function(diffAnalyzerResults) {
		HearthstoneDispatcher.dispatch({
			type: "DIFFANALYZER_RESULTS",
			diffAnalyzer : diffAnalyzerResults
		});
}

var changeCollection = function(collection) {
	HearthstoneDispatcher.dispatch({
		type: "CHANGE_COLLECTION",
		collection: collection
	})
}

export { receiveDiffAnalyzerResults, changeCollection };