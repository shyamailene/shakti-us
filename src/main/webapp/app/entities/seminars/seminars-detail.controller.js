(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SeminarsDetailController', SeminarsDetailController);

    SeminarsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Seminars'];

    function SeminarsDetailController($scope, $rootScope, $stateParams, previousState, entity, Seminars) {
        var vm = this;

        vm.seminars = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:seminarsUpdate', function(event, result) {
            vm.seminars = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
