(function() {
    'use strict';

    angular
        .module('webHipsterApp')
        .controller('MusicaDialogController', MusicaDialogController);

    MusicaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Musica'];

    function MusicaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Musica) {
        var vm = this;

        vm.musica = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.musica.id !== null) {
                Musica.update(vm.musica, onSaveSuccess, onSaveError);
            } else {
                Musica.save(vm.musica, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webHipsterApp:musicaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lancamento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
