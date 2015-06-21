angular.module('hearthstoneApp', []).controller('IndexController',
		[ '$scope', '$http', function($scope, $http) {

			$scope.diffOneList = function() {
				$http.get('/api/difflist').success(function(data) {
					$scope.diffs = data;
				})
			}

			$scope.diffAll = function() {
				$http.get('/api/diffalldb').success(function(data) {
					$scope.diffs = data;
				})
			}
			
			$scope.diffAll();

		} ]);