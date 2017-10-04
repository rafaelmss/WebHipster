(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('FilmeDetailController', FilmeDetailController);

    FilmeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Filme'];

    function FilmeDetailController($scope, $rootScope, $stateParams, previousState, entity, Filme) {
        var vm = this;

        vm.filme = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webHipsterApp:filmeUpdate', function(event, result) {
            vm.filme = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
