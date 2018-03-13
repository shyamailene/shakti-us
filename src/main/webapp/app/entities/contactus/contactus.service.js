(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Contactus', Contactus);

    Contactus.$inject = ['$resource'];

    function Contactus ($resource) {
        var resourceUrl =  'api/contactuses/:id';

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
