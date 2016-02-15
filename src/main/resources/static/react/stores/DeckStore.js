import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import * as HearthstoneWebAPIUtils from '../utils/HearthstoneWebAPIUtils';

class DeckStore extends FluxStore {

	constructor(dispatcher) {
		super(dispatcher);
		this.isLoading = false;
	}

	getIsLoading() {
		return this.isLoading;
	}


	__onDispatch(payload) {
		switch(payload.type) {
			case "LOAD_DECKS":
				this.isLoading = true;
				this.__emitChange();
				HearthstoneWebAPIUtils.loadDecks();
				break;
			case "DECK_LOAD_SUCCESS":
				this.isLoading = false;
				this.__emitChange();
				break;
			case "DECK_LOAD_FAILURE":
				this.isLoading = false;
				this.__emitChange();
				break;

			default:
				//no-op
		}
	}

}

const instance = new DeckStore(HearthstoneDispatcher);
export default instance;