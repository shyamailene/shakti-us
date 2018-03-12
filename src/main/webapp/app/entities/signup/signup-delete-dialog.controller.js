(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SignupDeleteController',SignupDeleteController);

    SignupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Signup'];

    function SignupDeleteController($uibModalInstance, entity, Signup) {
        var vm = this;

        vm.signup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Signup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
