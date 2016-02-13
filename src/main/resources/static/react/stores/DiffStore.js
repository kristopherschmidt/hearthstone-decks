import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'

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

const instance = new DiffStore(HearthstoneDispatcher);
export default instance;