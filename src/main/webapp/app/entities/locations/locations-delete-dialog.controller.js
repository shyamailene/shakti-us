(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('LocationsDeleteController',LocationsDeleteController);

    LocationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Locations'];

    function LocationsDeleteController($uibModalInstance, entity, Locations) {
        var vm = this;

        vm.locations = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Locations.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
