module.exports = function(grunt) {
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-browserify');

  grunt.registerTask('default', ['browserify', 'watch']);

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    browserify: {
      dist: {
        files: {
          'src/main/resources/static/bundle.js' : 
            [ 'src/main/resources/static/react/**/*.js' ]
        },
        options: {
          transform: ['babelify'],
          watch: true,
          browserifyOptions: {
        	debug: true
      	  }
        },
      }
    },
    watch: {
      build: {
        files: 'src/main/resources/static/bundle.js',
        options: {
          livereload: {
            host: 'localhost',
            port: 35729,
          }
        }
      }
    }
  });
}