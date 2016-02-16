import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import * as HearthstoneWebAPIUtils from '../utils/HearthstoneWebAPIUtils';

class DiffStore extends FluxStore {

	constructor(dispatcher) {
		super(dispatcher);
		this.diffAnalyzer = { allMissingCards: { cards: [] }, filteredDiffs: [] };
		this.diffCriteria = { collection: "", playerClasses: [], cards: [] };
		this.diffToken = null;
		this.runDiffAnalyzer();
	}

	getDiffAnalyzer() {
		return this.diffAnalyzer;
	}

	getDiffCriteria() {
		return this.diffCriteria;
	}

	runDiffAnalyzer() {
		if (this.diffToken) {
			var criteria = this.diffCriteria;
			this.diffToken = this.diffToken.always(function() { HearthstoneWebAPIUtils.runDiffAnalyzer(criteria) });
		} else {
			this.diffToken = HearthstoneWebAPIUtils.runDiffAnalyzer(this.diffCriteria);
		}
	}

	__onDispatch(payload) {
		var i = "breakpoint";
		switch(payload.type) {
			case "DIFFANALYZER_RESULTS":
				this.diffAnalyzer = payload.diffAnalyzer;
				this.diffToken = null;
				this.__emitChange();
				break;
			case "CHANGE_PLAYER_CLASSES":
				this.diffCriteria.playerClasses = payload.playerClasses;
				this.runDiffAnalyzer();
				this.__emitChange();
				break;
			case "CHANGE_COLLECTION":
				if (this.diffCriteria.collection != payload.collection) {
					this.diffCriteria.collection = payload.collection;
					this.runDiffAnalyzer();
					this.__emitChange();
				}
				break;
			case "ADD_FILTER_CARD":
				if (!this.diffCriteria.cards.includes(payload.cardName)) {
					this.diffCriteria.cards.push(payload.cardName);
					this.runDiffAnalyzer();
					this.__emitChange();
				}
				break;
			case "REMOVE_FILTER_CARD":	
				var filterCardIndex = this.diffCriteria.cards.indexOf(payload.cardName);
				if (filterCardIndex != -1) {
					this.diffCriteria.cards.splice(filterCardIndex, 1);
				}
				this.runDiffAnalyzer();
				this.__emitChange();
				break;

			default:
				//no-op

		}
	}

}

const instance = new DiffStore(HearthstoneDispatcher);
export default instance;