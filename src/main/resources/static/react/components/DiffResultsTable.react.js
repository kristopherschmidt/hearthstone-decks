import React, { Component } from 'react'

export default class DiffResultsTable extends Component {

	render() {

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

}