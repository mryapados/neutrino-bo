module.exports = function (grunt) {
    /**
     * Déclaration, configuration des tâches,
     * l'ordre de déclaration n'a aucune importance
     */
    grunt.initConfig({
 
        /**
         * Configuration du plugin less
         * 2 sous tâches sont déclarées. 'less:dist' qui va compiler les fichiers less dans src/less/
         * dans le fichier 'dist/css/styles.css'. 'test:dist' qui va juste compiler le fichier file1.less
         */
        less: {
            dist: {
                src: ['src/less/*'],
                dest: 'dist/css/styles.css'
            },
 
            test: {
                src: ['src/less/file1.less'],
                dest: 'dist/css/file1.css'
            }
        },
 
        
        /**
         * 
         * 
         * 
         */
        ngtemplates: {
            boApp: {
            	// cwd: 'src/bo',
            	src: ['src/bo/js/templates/**.html'],
                dest: 'src/bo/js/templates/tpl.js',
                options: {
                	htmlmin: {
                		  collapseBooleanAttributes:      true,
                		  collapseWhitespace:             true,
                		  removeAttributeQuotes:          true,
                		  removeComments:                 true, // Only if you don't use comment directives! 
                		  removeEmptyAttributes:          true,
                		  removeRedundantAttributes:      true,
                		  removeScriptTypeAttributes:     true,
                		  removeStyleLinkTypeAttributes:  true
                	}
                }
            },
        }, 
        
        /**
         * 
         * 
         * 
         */
        copy: {
        	main: {
        		files: [
        	    	// includes files within path and its sub-directories
        	      {expand: true, cwd: 'src/bo/js/', src: ['templates/resources/**'], dest: 'dist/bo/'},
        	      
        	    ],
        	},
        },
        
        
        
        
        /**
         * Configuration du plugin concat
         * une seule tâche qui va concatener tous les fichiers situés dans 'src/js/'
         * dans un seul fichier 'dist/js/built.js'
         */
        concat: {            
            back: {
                src: ['src/back/**/*.js'],
                dest: 'dist/back/back.js'
            },
            
            bo: {
                src: ['src/bo/**/*.js'],
                dest: 'dist/bo/bo.js'
            },
            
            front: {
                src: ['src/front/**/*.js'],
                dest: 'dist/front/front.js'
            },
        }, 
        /**
         * Configuration du plugin uglify
         * une seule tâche qui va minimifier le fichier built.js
         * généré par la tâche précédente
         */
        uglify: {            
            back: {
                src: ['dist/back/back.js'],
                dest: 'dist/back/back.min.js'
            }, 
            
            bo: {
                src: ['dist/bo/bo.js'],
                dest: 'dist/bo/bo.min.js'
            }, 
            
            front: {
                src: ['dist/front/front.js'],
                dest: 'dist/front/front.min.js'
            }
        },
 
        /**
         * Configuration du plugin watch
         * une seule tâche, qui va scruter le répertoire src
         * et si un fichier est modifié, va lancer la tâche 'default'
         */
        watch: {
            files: ['src/**'],
            tasks: ['default'],
        }
    });
 
    /**
     * Chargement des plugins Grunt
     */
 
    // plugin du préprocesseur less
    grunt.loadNpmTasks('grunt-contrib-less');
 
    // plugin ...
    grunt.loadNpmTasks('grunt-angular-templates');
    
    // plugin pour copier des fichiers ressources
    grunt.loadNpmTasks('grunt-contrib-copy');
    
    // plugin pour concatener des fichiers
    grunt.loadNpmTasks('grunt-contrib-concat');
 
    // plugin pour minimifier les fichiers JS
    grunt.loadNpmTasks('grunt-contrib-uglify');
 
    // plugin pour scruter les changements dans vos fichiers et lancer des tâches
    grunt.loadNpmTasks('grunt-contrib-watch');
 
    /**
     * Déclaration des tâches "groupées"
     * la tâche par default (en tapant juste 'grunt') lancera les tâches 'less:dist' 'concat' et 'uglify'
     * 'grunt watch-src' lancera les tâches 'default' puis 'watch' (l'ordre de déclaration est ici important)
     * 
     * le lancement de la tâche 'watch-src' ne vous rendra pas la main, mais grunt restera actif
     * pour scruter vos fichiers
     */
    grunt.registerTask('default', ['less:dist', 'ngtemplates', 'copy', 'concat', 'uglify']);
    grunt.registerTask('watch-src', ['default', 'watch']);
 
};