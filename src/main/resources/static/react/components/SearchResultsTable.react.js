var React = require('react');

var SearchResultsTable = React.createClass({

	render: function() {

		var rows = this.props.data.map(function(diffRow) {
			return (
				<tr key={diffRow}>
					<td>{diffRow}</td>
				</tr>
			);
		});

		return (
			<table className="table table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th>ColName</th>
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		);
	}

});

module.exports = SearchResultsTable;