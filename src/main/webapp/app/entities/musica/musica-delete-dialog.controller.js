(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('MusicaDeleteController',MusicaDeleteController);

    MusicaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Musica'];

    function MusicaDeleteController($uibModalInstance, entity, Musica) {
        var vm = this;

        vm.musica = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Musica.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
