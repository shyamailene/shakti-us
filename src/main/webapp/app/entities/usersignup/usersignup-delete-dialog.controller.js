(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('UsersignupDeleteController',UsersignupDeleteController);

    UsersignupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Usersignup'];

    function UsersignupDeleteController($uibModalInstance, entity, Usersignup) {
        var vm = this;

        vm.usersignup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Usersignup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
