import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import * as HearthstoneWebAPIUtils from '../utils/HearthstoneWebAPIUtils';

class DiffStore extends FluxStore {

	constructor(dispatcher) {
		super(dispatcher);
		this.diffAnalyzer = null;
		this.diffCriteria = { collection: "hearthpwnRepository" };
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
			case 'DIFFANALYZER_RESULTS':
				this.diffAnalyzer = payload.diffAnalyzer;
				this.__emitChange();
				break;
			case 'CHANGE_COLLECTION':
				if (this.diffCriteria.collection != payload.collection) {
					this.diffCriteria.collection = payload.collection;
					this.__emitChange();
					this.runDiffAnalyzer();
				}


			default:
				//no-op

		}
	}

}

const instance = new DiffStore(HearthstoneDispatcher);
export default instance;