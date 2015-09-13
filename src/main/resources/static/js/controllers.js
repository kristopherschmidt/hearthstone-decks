var hearthstoneApp = angular.module('hearthstoneApp', [ 'ngTagsInput',
		'ngRoute' ])

hearthstoneApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/admin/load', {
		templateUrl : 'partials/admin/load.html',
		controller : 'DeckLoadController'
	}).when('/', {
		templateUrl : 'partials/search.html',
		controller : 'IndexController'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

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
				var playerClassParam = [];
				for (className in $scope.playerClass) {
					if ($scope.playerClass[className]) {
						playerClassParam.push(className);
					}
				}

				$http.get('/api/diffanalyzer', {
					params : {
						collection : $scope.collection,
						cards : searchCardsParam,
						playerClasses : playerClassParam,
						maxRequiredDust : $scope.maxRequiredDust,
						minRequiredDust : $scope.minRequiredDust,
						cardSet : $scope.cardSet
					}
				}).success(function(data) {
					$scope.diffanalyzer = data;
					$scope.allMissingCards = data.allMissingCards.cards;
					$scope.diffs = data.filteredDiffs;
				})
			}

			$scope.getCardsMatchingName = function(query) {
				return $scope.cards.filter(function(card) {
					return card.name.toLowerCase().startsWith(
							query.toLowerCase());
				});
			}

			$scope.filterByCard = function(card) {
				var alreadyContainsSearchCard = $scope.searchCards
						.some(function(c) {
							return c.id == card.id
						});
				if (!alreadyContainsSearchCard) {
					$scope.searchCards.push(card);
					$scope.diff();
				}

			}

			$scope.collection = "";
			$scope.searchCards = [];
			$scope.diffFilter = "";
			$scope.playerClass = {};
			$scope.diff();

		} ]);

hearthstoneApp.controller('DeckLoadController', [ '$scope', '$http',
		function($scope, $http) {
			$scope.reloadDecks = function() {
				$scope.loading = true;
				console.log("reloadDecks");
				$scope.loading = true;
				$http.get('/api/decks/refresh', {
					params: {
						collection: $scope.collection
					}
				}).success(function() {
					$scope.loading = false;
					$("#deckLoadSuccessModal").modal();
				}).error(function() {
					$scope.loading = false;
					$("#deckLoadErrorModal").modal();
				})
			}
			$scope.loading = false;

		} ]);

hearthstoneApp.controller('TableSortController', [ '$scope', function($scope) {
	$scope.sortType = 'requiredDust';
	$scope.sortReverse = false;

} ]);