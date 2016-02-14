import { receiveDiffAnalyzerResults } from '../actions/HearthstoneServerActionCreators';

var runDiffAnalyzer = function(criteria) {
	$.ajaxSettings.traditional = true;
	  $.ajax({
	      url: '/api/diffanalyzer',
	      data: criteria,
	      dataType: 'json',
	      cache: false,
	      success: function(data) {
	        receiveDiffAnalyzerResults(data);
	      }
    	});
}

export { runDiffAnalyzer };