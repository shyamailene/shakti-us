(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('fundraiser', fundraiser);

    fundraiser.$inject = ['$resource'];

    function fundraiser ($resource) {
        var resourceUrl =  'api/fundraisers/:id';

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
