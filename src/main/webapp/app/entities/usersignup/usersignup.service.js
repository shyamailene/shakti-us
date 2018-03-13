(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Usersignup', Usersignup);

    Usersignup.$inject = ['$resource'];

    function Usersignup ($resource) {
        var resourceUrl =  'api/usersignups/:id';

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
