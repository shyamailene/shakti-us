(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Seminars', Seminars);

    Seminars.$inject = ['$resource'];

    function Seminars ($resource) {
        var resourceUrl =  'api/seminars/:id';

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
