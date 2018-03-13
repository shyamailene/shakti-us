(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Signup', Signup);

    Signup.$inject = ['$resource'];

    function Signup ($resource) {
        var resourceUrl =  'api/signups/:id';

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
