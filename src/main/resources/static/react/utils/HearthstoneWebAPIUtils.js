import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

var diff = function() {
	HearthstoneDispatcher.dispatch({
		type: "DIFF_RESULTS",
		diff : [ 'e', 'f' ]
	});
}

export { diff };