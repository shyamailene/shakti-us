(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('EventsDetailController', EventsDetailController);

    EventsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Events'];

    function EventsDetailController($scope, $rootScope, $stateParams, previousState, entity, Events) {
        var vm = this;

        vm.events = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:eventsUpdate', function(event, result) {
            vm.events = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
