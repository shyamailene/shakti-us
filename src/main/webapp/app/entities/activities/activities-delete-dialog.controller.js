(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('ActivitiesDeleteController',ActivitiesDeleteController);

    ActivitiesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Activities'];

    function ActivitiesDeleteController($uibModalInstance, entity, Activities) {
        var vm = this;

        vm.activities = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Activities.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
