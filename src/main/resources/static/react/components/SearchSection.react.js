var React = require('react');
var SearchResultsTable = require('./SearchResultsTable.react')

var SearchSection = React.createClass({

	render: function() {

		return (
			<SearchResultsTable data={this.props.data} />
		);
	}

});

module.exports = SearchSection;