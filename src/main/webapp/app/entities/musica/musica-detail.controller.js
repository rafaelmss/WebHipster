(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('MusicaDetailController', MusicaDetailController);

    MusicaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Musica'];

    function MusicaDetailController($scope, $rootScope, $stateParams, previousState, entity, Musica) {
        var vm = this;

        vm.musica = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('webHipsterApp:musicaUpdate', function(event, result) {
            vm.musica = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
