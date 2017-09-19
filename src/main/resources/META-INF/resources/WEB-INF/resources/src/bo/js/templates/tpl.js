angular.module('boApp').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('src/bo/js/templates/ui-file-assignement-modal.html',
    "<form name=templateForm class=form-horizontal role=form><div class=modal-header><button type=button class=close data-dismiss=modal aria-label=Close><span aria-hidden=true>&times;</span></button><h4 class=modal-title>{{'block-management.type' | translate}} : {{template.template.name}}</h4></div><div class=modal-body><div data-ng-show=template.errors class=\"alert alert-danger\" role=alert><div data-ng-repeat=\"err in template.errors.data\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=true></span> <span class=sr-only>Error :</span> {{err.defaultMessage}}</div></div><div data-ng-include=urlMaked></div></div><div class=modal-footer><button data-ng-click=cancel() class=\"btn btn-danger btn-small\">Annuler</button> <button class=\"btn btn-success\" data-ng-disabled=\"!templateForm.$dirty || templateForm.$invalid\" data-ng-click=save(templateForm)>Modifier</button></div></form>"
  );


  $templateCache.put('src/bo/js/templates/ui-file-assignement.html',
    "<div><a ng-click=\"add('lg')\" class=\"btn btn-primary btn-xs\">{{ many ? 'add' : 'assign' | translate }}</a> <a ng-click=clear() class=\"btn btn-danger btn-xs\">{{ 'reset' | translate }}</a></div>"
  );


  $templateCache.put('src/bo/js/templates/ui-file.html',
    "<div class=uifile><a ng-href={{url}} target=_blank><div class=content><p class=title>{{name}}</p><p><img ng-src={{src}} alt={{name}}></p></div></a></div>"
  );


  $templateCache.put('src/bo/js/templates/ui-object-assignement-modal.html',
    "<form name=templateForm class=form-horizontal role=form><div class=modal-header><button type=button class=close data-dismiss=modal aria-label=Close><span aria-hidden=true>&times;</span></button><h4 class=modal-title>{{'block-management.type' | translate}} : {{template.template.name}}</h4></div><div class=modal-body><div data-ng-show=template.errors class=\"alert alert-danger\" role=alert><div data-ng-repeat=\"err in template.errors.data\"><span class=\"glyphicon glyphicon-exclamation-sign\" aria-hidden=true></span> <span class=sr-only>Error :</span> {{err.defaultMessage}}</div></div><div data-ng-include=urlMaked></div></div><div class=modal-footer><button data-ng-click=cancel() class=\"btn btn-danger btn-small\">Annuler</button> <button class=\"btn btn-success\" data-ng-disabled=\"!templateForm.$dirty || templateForm.$invalid\" data-ng-click=save(templateForm)>Modifier</button></div></form>"
  );


  $templateCache.put('src/bo/js/templates/ui-object-assignement.html',
    "<div><a ng-click=\"open('lg')\" class=\"btn btn-primary btn-xs\">{{ 'assign' | translate }}</a></div>"
  );

}]);
