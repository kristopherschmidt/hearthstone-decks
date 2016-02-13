module.exports = function(grunt) {
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-browserify');

  grunt.registerTask('default', ['browserify', 'watch']);

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    browserify: {
      dist: {
        files: {
          'src/main/resources/static/react/bundle.js' : 
            [ 'src/main/resources/static/react/**/*.js',
              'src/main/resources/static/react/**/*.jsx'
            ]
        },
        options: {
          transform: ['babelify']
        }
      }
    },
    watch: {
      build: {
        files: 'src/main/resources/static/react/bundle.js',
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