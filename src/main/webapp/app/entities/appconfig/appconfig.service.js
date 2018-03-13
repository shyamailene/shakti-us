(function() {
    'use strict';
    angular
        .module('shaktiusApp')
        .factory('Appconfig', Appconfig);

    Appconfig.$inject = ['$resource'];

    function Appconfig ($resource) {
        var resourceUrl =  'api/appconfigs/:id';

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
