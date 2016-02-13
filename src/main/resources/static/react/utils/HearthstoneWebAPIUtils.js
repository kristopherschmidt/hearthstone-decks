import { receiveDiffResults } from '../actions/HearthstoneServerActionCreators';

var diff = function() {
	  $.ajax({
	      url: '/api/diffanalyzer',
	      dataType: 'json',
	      cache: false,
	      success: function(data) {
	        receiveDiffResults(data.allMissingCards.cards);
	      }
    	});
}

export { diff };