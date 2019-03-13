(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('FundraiserDeleteController',FundraiserDeleteController);

    FundraiserDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fundraiser'];

    function FundraiserDeleteController($uibModalInstance, entity, Fundraiser) {
        var vm = this;

        vm.fundraiser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Fundraiser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
