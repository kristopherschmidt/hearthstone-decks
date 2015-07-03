angular.module('hearthstoneApp', []).controller('IndexController',
		[ '$scope', '$http', function($scope, $http) {

			$scope.diffAll = function() {
				$http.get('/api/diffs2').success(function(data) {
					$scope.diffs = data;
				})
			}
			
			$scope.diffAll();

		} ]);