(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('LivroDeleteController',LivroDeleteController);

    LivroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Livro'];

    function LivroDeleteController($uibModalInstance, entity, Livro) {
        var vm = this;

        vm.livro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Livro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
