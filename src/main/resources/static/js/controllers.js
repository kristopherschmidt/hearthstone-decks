angular.module('hearthstoneApp', []).controller('IndexController',
		[ '$scope', '$http', function($scope, $http) {

			$scope.diff = function(collection) {
				$http.get('/api/diffs', {
					params: {
						collection: collection
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
			
			$scope.updateCollection = function() {
				console.log("updateCollection: " + $scope.collection);
				$scope.diff($scope.collection);
			}
			
			$scope.collection = "";
			$scope.diff();

		} ]);