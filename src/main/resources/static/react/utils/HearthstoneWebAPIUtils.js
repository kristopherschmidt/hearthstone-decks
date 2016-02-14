import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';

var runDiffAnalyzer = function(criteria) {
	$.ajaxSettings.traditional = true;
	  $.ajax({
	      url: '/api/diffanalyzer',
	      data: criteria,
	      dataType: 'json',
	      cache: false,
	      success: function(data) {
	        HearthstoneActionCreators.receiveDiffAnalyzerResults(data);
	      }
    	});
}

var getCards = function() {
	  $.ajax({
	      url: '/api/cards',
	      dataType: 'json',
	      cache: false,
	      success: function(data) {
	        HearthstoneActionCreators.receiveCards(data);
	      }
    	});
}

export { runDiffAnalyzer, getCards };