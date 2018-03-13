(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SignupDetailController', SignupDetailController);

    SignupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Signup'];

    function SignupDetailController($scope, $rootScope, $stateParams, previousState, entity, Signup) {
        var vm = this;

        vm.signup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('shaktiusApp:signupUpdate', function(event, result) {
            vm.signup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
