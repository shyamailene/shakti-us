(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('fundraiserDetailController', fundraiserDetailController);

    fundraiserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'fundraiser'];

    function fundraiserDetailController($scope, $rootScope, $stateParams, previousState, entity, fundraiser) {
        var vm = this;

        vm.fundraiser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:fundraiserUpdate', function(event, result) {
            vm.fundraiser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
