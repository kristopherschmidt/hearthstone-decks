var hearthstoneApp = angular.module('hearthstoneApp', [ 'ngTagsInput' ])

hearthstoneApp.controller('IndexController', [
		'$scope',
		'$http',
		function($scope, $http) {

			$http.get('/api/cards').success(function(data) {
				$scope.cards = data;
			})

			$scope.diff = function() {
				var searchCardsParam = [];
				if ($scope.searchCards != null) {
					searchCardsParam = $scope.searchCards.map(function(card) {
						return card.name
					});
				}
				/*
				$http.get('/api/diffs', {
					params : {
						collection : $scope.collection,
						cards : searchCardsParam
					}
				}).success(function(data) {
					$scope.diffs = data;
				})
				*/

				$http.get('/api/diffanalyzer', {
					params : {
						collection : $scope.collection,
						cards : searchCardsParam
					}
				}).success(function(data) {
					$scope.diffanalyzer = data;
					$scope.allMissingCards = data.allMissingCards;
					$scope.diffs = data.filteredDiffs;
				})
			}

			$scope.getCardsMatchingName = function(query) {
				console.log("getCardsMatchingName: " + query);
				return $scope.cards.filter(function(card) {
					return card.name.toLowerCase().startsWith(
							query.toLowerCase());
				});
			}

			$scope.collection = "";
			$scope.searchCards = [];
			$scope.diff();

		} ]);