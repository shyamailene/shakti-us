(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('AppconfigDeleteController',AppconfigDeleteController);

    AppconfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'Appconfig'];

    function AppconfigDeleteController($uibModalInstance, entity, Appconfig) {
        var vm = this;

        vm.appconfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Appconfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
