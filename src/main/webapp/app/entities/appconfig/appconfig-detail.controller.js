(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('AppconfigDetailController', AppconfigDetailController);

    AppconfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Appconfig'];

    function AppconfigDetailController($scope, $rootScope, $stateParams, previousState, entity, Appconfig) {
        var vm = this;

        vm.appconfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:appconfigUpdate', function(event, result) {
            vm.appconfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
