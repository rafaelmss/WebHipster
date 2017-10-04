(function() {
    'use strict';
    angular
        .module('webHipsterApp')
        .factory('Filme', Filme);

    Filme.$inject = ['$resource', 'DateUtils'];

    function Filme ($resource, DateUtils) {
        var resourceUrl =  'api/filmes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lancamento = DateUtils.convertLocalDateFromServer(data.lancamento);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lancamento = DateUtils.convertLocalDateToServer(copy.lancamento);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.lancamento = DateUtils.convertLocalDateToServer(copy.lancamento);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
