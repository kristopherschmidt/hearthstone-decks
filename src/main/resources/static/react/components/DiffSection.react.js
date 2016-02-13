var React = require('react');
var DiffStore = require('../stores/DiffStore');
var DiffResultsTable = require('./DiffResultsTable.react')

var DiffSection = React.createClass({

	componentDidMount: function() {
		DiffStore.addListener(this._onChange);
		DiffStore.getDiff();
	},

	getInitialState: function() {
		return { data: [] };
	},

	updateFromStores: function() {
		this.setState({ data: DiffStore.getDiff() });
	},

	render: function() {

		return (
			<DiffResultsTable data={this.state.data} />
		);
	},

	_onChange: function() {
		this.updateFromStores();
	}

});

module.exports = DiffSection;