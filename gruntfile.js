module.exports = function(grunt) {
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-browserify');

  grunt.registerTask('default', ['browserify', 'watch']);

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    browserify: {
      main: {
        src: 'src/main/resources/static/react/hearthstonereactapp.js',
        dest: 'src/main/resources/static/react/bundle.js'
      }
    },
    watch: {
      files: 'js/*',
      tasks: ['default']
    }
  });
}