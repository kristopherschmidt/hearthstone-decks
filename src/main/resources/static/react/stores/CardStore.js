import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import * as HearthstoneWebAPIUtils from '../utils/HearthstoneWebAPIUtils';

class CardStore extends FluxStore {

	constructor(dispatcher) {
		super(dispatcher);
		this.cards;
	}

	getCards() {
		if (this.cards) {
			return this.cards;
		} else {
			HearthstoneWebAPIUtils.getCards();
			return [];
		}
	}

	__onDispatch(payload) {
		switch(payload.type) {
			case "CARD_LOAD":
				this.cards = payload.cards;
				this.__emitChange();
				break;

			default:
				//no-op

		}
	}

}

const instance = new CardStore(HearthstoneDispatcher);
export default instance;