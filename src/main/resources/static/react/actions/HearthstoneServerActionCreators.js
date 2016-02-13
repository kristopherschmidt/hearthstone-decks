import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

var	receiveDiffResults = function(diffResults) {
		HearthstoneDispatcher.dispatch({
			type: "DIFF_RESULTS",
			diff : diffResults
		});
}

export { receiveDiffResults };