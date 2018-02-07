(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .controller('CommitteeController', CommitteeController);

    CommitteeController.$inject = ['Committee', 'CommitteeSearch'];

    function CommitteeController(Committee, CommitteeSearch) {

        var vm = this;

        vm.committees = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Committee.query(function(result) {
                vm.committees = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CommitteeSearch.query({query: vm.searchQuery}, function(result) {
                vm.committees = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
