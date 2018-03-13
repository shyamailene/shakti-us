(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('LocationsDetailController', LocationsDetailController);

    LocationsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Locations'];

    function LocationsDetailController($scope, $rootScope, $stateParams, previousState, entity, Locations) {
        var vm = this;

        vm.locations = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:locationsUpdate', function(event, result) {
            vm.locations = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
