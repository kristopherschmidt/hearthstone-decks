angular.module('hearthstoneApp', []).controller('IndexController', [ '$scope', '$http',
		function($scope, $http) {
			$http.get('/api/decklistdiff').success(function(data) {
				$scope.diffs = data;
			})
		} ]);