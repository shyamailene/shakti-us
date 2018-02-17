(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('SeminarsDeleteController',SeminarsDeleteController);

    SeminarsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Seminars'];

    function SeminarsDeleteController($uibModalInstance, entity, Seminars) {
        var vm = this;

        vm.seminars = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Seminars.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
