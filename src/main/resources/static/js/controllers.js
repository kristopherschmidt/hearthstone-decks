var hearthstoneApp = angular.module('hearthstoneApp', []);

hearthstoneApp.controller('IndexController', [ '$scope', function ($scope) {
  $scope.phones = [
    {'name': 'abc S',
     'snippet': 'Fast just got faster with Nexus S.'}
  ];
}]);