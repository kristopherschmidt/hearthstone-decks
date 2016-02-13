var FluxStore = require('flux/utils').Store;
var HearthstoneDispatcher = require('../dispatcher/HearthstoneDispatcher')

class DiffStore extends FluxStore {

	getDiff() {
		if (this.diff) {
			return this.diff;
		} else {
			HearthstoneDispatcher.dispatch({
				type: "DIFF_RESULTS",
				diff : [ 'c', 'd' ]
			});
			return [];
		}
	}

	__onDispatch(payload) {
		switch(payload.type) {
			case 'DIFF_RESULTS':
				this.diff = payload.diff;
				this.__emitChange();
				break;

			default:
				//no-op

		}
	}

}

module.exports = new DiffStore(HearthstoneDispatcher);
