(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('ActivitiesDetailController', ActivitiesDetailController);

    ActivitiesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Activities'];

    function ActivitiesDetailController($scope, $rootScope, $stateParams, previousState, entity, Activities) {
        var vm = this;

        vm.activities = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:activitiesUpdate', function(event, result) {
            vm.activities = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
