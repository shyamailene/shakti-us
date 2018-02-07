(function() {
    'use strict';

    angular
        .module('shaktiusApp')
        .factory('CommitteeSearch', CommitteeSearch);

    CommitteeSearch.$inject = ['$resource'];

    function CommitteeSearch($resource) {
        var resourceUrl =  'api/_search/committees/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
