(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('FilmeDeleteController',FilmeDeleteController);

    FilmeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Filme'];

    function FilmeDeleteController($uibModalInstance, entity, Filme) {
        var vm = this;

        vm.filme = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Filme.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
