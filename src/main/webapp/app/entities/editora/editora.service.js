(function() {
    'use strict';
    angular
        .module('webHipsterApp')
        .factory('Editora', Editora);

    Editora.$inject = ['$resource'];

    function Editora ($resource) {
        var resourceUrl =  'api/editoras/:id';

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
