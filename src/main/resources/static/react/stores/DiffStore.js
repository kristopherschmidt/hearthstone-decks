import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import * as HearthstoneWebAPIUtils from '../utils/HearthstoneWebAPIUtils';

class DiffStore extends FluxStore {

	constructor(dispatcher) {
		super(dispatcher);
		this.diffAnalyzer = null;
		this.diffCriteria = { collection: "", playerClasses: [], cards: [] };
	}

	getDiffAnalyzer() {
		if (this.diffAnalyzer) {
			return this.diffAnalyzer;
		} else {
			this.runDiffAnalyzer();
			return [];
		}
	}

	getDiffCriteria() {
		return this.diffCriteria;
	}

	runDiffAnalyzer() {
		HearthstoneWebAPIUtils.runDiffAnalyzer(this.diffCriteria);
	}

	__onDispatch(payload) {
		switch(payload.type) {
			case "DIFFANALYZER_RESULTS":
				this.diffAnalyzer = payload.diffAnalyzer;
				this.__emitChange();
				break;
			case "CHANGE_CARDS":
				this.diffCriteria.cards = payload.cards;
				this.__emitChange();
				this.runDiffAnalyzer();
				break;
			case "CHANGE_COLLECTION":
				if (this.diffCriteria.collection != payload.collection) {
					this.diffCriteria.collection = payload.collection;
					this.__emitChange();
					this.runDiffAnalyzer();
				}
				break;
			case "CHANGE_PLAYER_CLASSES":
				this.diffCriteria.playerClasses = payload.playerClasses;
				this.__emitChange();
				this.runDiffAnalyzer();
				break;
			case "ADD_FILTER_CARD":
				if (!this.diffCriteria.cards.includes(payload.cardName)) {
					this.diffCriteria.cards.push(payload.cardName);
					this.__emitChange();
					this.runDiffAnalyzer();
				}
				break;

			default:
				//no-op

		}
	}

}

const instance = new DiffStore(HearthstoneDispatcher);
export default instance;