(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('UsersignupDetailController', UsersignupDetailController);

    UsersignupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Usersignup'];

    function UsersignupDetailController($scope, $rootScope, $stateParams, previousState, entity, Usersignup) {
        var vm = this;

        vm.usersignup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:usersignupUpdate', function(event, result) {
            vm.usersignup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
