(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('CommitteeDeleteController',CommitteeDeleteController);

    CommitteeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Committee'];

    function CommitteeDeleteController($uibModalInstance, entity, Committee) {
        var vm = this;

        vm.committee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Committee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
