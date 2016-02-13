import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

var	receiveDiffAnalyzerResults = function(diffAnalyzerResults) {
		HearthstoneDispatcher.dispatch({
			type: "DIFFANALYZER_RESULTS",
			diffAnalyzer : diffAnalyzerResults
		});
}

export { receiveDiffAnalyzerResults };