(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('CommitteeDetailController', CommitteeDetailController);

    CommitteeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Committee'];

    function CommitteeDetailController($scope, $rootScope, $stateParams, previousState, entity, Committee) {
        var vm = this;

        vm.committee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:committeeUpdate', function(event, result) {
            vm.committee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
