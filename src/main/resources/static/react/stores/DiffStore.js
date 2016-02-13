import { Store as FluxStore} from 'flux/utils';
import HearthstoneDispatcher from '../dispatcher/HearthstoneDispatcher'
import { runDiffAnalyzer } from '../utils/HearthstoneWebAPIUtils';

class DiffStore extends FluxStore {

	getDiffAnalyzer() {
		if (this.diffAnalyzer) {
			return this.diffAnalyzer;
		} else {
			runDiffAnalyzer();
			return [];
		}
	}

	__onDispatch(payload) {
		switch(payload.type) {
			case 'DIFFANALYZER_RESULTS':
				this.diffAnalyzer = payload.diffAnalyzer;
				this.__emitChange();
				break;

			default:
				//no-op

		}
	}

}

const instance = new DiffStore(HearthstoneDispatcher);
export default instance;