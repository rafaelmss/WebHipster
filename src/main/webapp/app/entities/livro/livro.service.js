(function() {
    'use strict';
    angular
        .module('webHipsterApp')
        .factory('Livro', Livro);

    Livro.$inject = ['$resource'];

    function Livro ($resource) {
        var resourceUrl =  'api/livros/:id';

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
