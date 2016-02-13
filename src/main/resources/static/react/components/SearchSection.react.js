var React = require('react');
var DiffStore = require('../stores/DiffStore');
var SearchResultsTable = require('./SearchResultsTable.react')

var SearchSection = React.createClass({

	componentDidMount: function() {
		DiffStore.addListener(this._onChange);
	},

	render: function() {

		return (
			<SearchResultsTable data={this.props.data} />
		);
	},

	_onChange: function() {
		alert("SearchSection: DiffStore.onChange");
	}

});

module.exports = SearchSection;