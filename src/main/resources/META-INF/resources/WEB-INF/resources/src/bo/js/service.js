(function() {
	var bModule=angular.module("frontServices",['FileManagerApp']);

	angular.module('FileManagerApp').config(['fileManagerConfigProvider', function (config) {
      var defaults = config.$get();
      config.set({
        appName: 'Neutrino',
        
        listUrl: '/neutrino/bo/file/list/',
        uploadUrl: '/neutrino/bo/file/add/',
        renameUrl: '/neutrino/bo/file/rename/',
        copyUrl: 'bridges/php/handler.php',
        moveUrl: '/neutrino/bo/file/move/',
        removeUrl: '/neutrino/bo/file/remove/',
        editUrl: 'bridges/php/handler.php',
        getContentUrl: 'bridges/php/handler.php',
        createFolderUrl: 'bridges/php/handler.php',
        downloadFileUrl: '/neutrino/bo/file/download/',
        downloadMultipleUrl: '/neutrino/bo/file/downloadMultiple/',
        compressUrl: '/neutrino/bo/file/compress/',
        extractUrl: 'bridges/php/handler.php',
        permissionsUrl: 'bridges/php/handler.php',
        
        pickCallback: function(item) {
          var msg = 'Picked %s "%s" for external use'
            .replace('%s', item.type)
            .replace('%s', item.fullPath());
          window.alert(msg);
        },

        searchForm: false,
        sidebar: true,
        breadcrumb: true,
        navbar: true,
        multiSelect:true,
        allowedActions: {
            upload: true,
            rename: true,
            move: true,
            copy: true,
            edit: true,
            changePermissions: true,
            compress: true,
            compressChooseName: true,
            extract: true,
            download: true,
            downloadMultiple: true,
            preview: true,
            remove: true,
            createFolder: true,
            pickFiles: false,
            pickFolders: false
        },
        
        
        multipleDownloadFileName: 'angular-filemanager.zip',
        showExtensionIcons: true,
        showSizeForDirectories: false,
        useBinarySizePrefixes: false,
        downloadFilesByAjax: true,
        previewImagesInModal: true,
        enablePermissionsRecursive: true,
        compressAsync: false,
        extractAsync: false,
        pickCallback: null,

        
      });
      
    }]);
	
	
}());