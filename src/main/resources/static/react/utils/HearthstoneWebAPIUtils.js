import { receiveDiffAnalyzerResults } from '../actions/HearthstoneServerActionCreators';

var runDiffAnalyzer = function() {
	  $.ajax({
	      url: '/api/diffanalyzer',
	      dataType: 'json',
	      cache: false,
	      success: function(data) {
	        receiveDiffAnalyzerResults(data);
	      }
    	});
}

export { runDiffAnalyzer };