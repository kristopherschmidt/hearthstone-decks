var hearthstoneApp = angular.module('hearthstoneApp', [ 'angucomplete' ])

hearthstoneApp.controller('IndexController', [ '$scope', '$http',
		function($scope, $http) {

			$http.get('/api/cards').success(function(data) {
				$scope.cards = data;
			})

			$scope.diff = function(collection) {
				$http.get('/api/diffs', {
					params : {
						collection : collection
					}
				}).success(function(data) {
					$scope.diffs = data;
				})
			}

			$scope.search = function() {
				$http.get('/api/diffs2', {
					params : {
						card : $scope.card.title
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