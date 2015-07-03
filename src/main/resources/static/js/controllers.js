angular.module('hearthstoneApp', []).controller('IndexController',
		[ '$scope', '$http', function($scope, $http) {

			$scope.diffAll = function() {
				$http.get('/api/diffs', {
					params: {
						card: $scope.card
					}
				}).success(function(data) {
					$scope.diffs = data;
				})
			}
			
			$scope.search = function() {
				$http.get('/api/diffs2', {
					params: {
						card: $scope.card
					}
				}).success(function(data) {
					$scope.diffs = data;
				})
			}
			
			$scope.diffAll();
			


		} ]);