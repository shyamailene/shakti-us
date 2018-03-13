(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Locations', Locations);

    Locations.$inject = ['$resource'];

    function Locations ($resource) {
        var resourceUrl =  'api/locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
