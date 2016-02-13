var FluxStore = require('flux/utils').Store;


var HearthstoneDispatcher = require('../dispatcher/HearthstoneDispatcher')

class DiffStore extends FluxStore {


}

module.exports = new DiffStore(HearthstoneDispatcher);
