import React, { Component } from 'react'

export default class DiffResultsTable extends Component {

	render() {

		var rows = this.props.deckDiffs.map(function(deckDiff) {
			return (
				<tr key={deckDiff.desiredDeck.url}>
					<td>
						<a href={ deckDiff.desiredDeck.url } target="_blank">{ deckDiff.desiredDeck.name }</a>
					</td>
					<td>{ deckDiff.requiredDust }</td>
					<td>{ deckDiff.fullDustValue }</td>
					<td>{ deckDiff.percentComplete }%</td>
					<td>{ deckDiff.rating }</td>
					<td>{ deckDiff.rankingMetric }</td>
					<td>TBD Missing Cards</td>
				</tr>
			);
		});

		return (
			<table className="table table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<th className="col-sm-4">Deck Name</th>
						<th className="col-sm-1">Required Dust</th>
						<th className="col-sm-1">Full Dust Value</th>
						<th className="col-sm-1">Percent Complete</th>
						<th className="col-sm-1">Ratinge</th>
						<th className="col-sm-1">Ranking Metric</th>
						<th className="col-sm-3">Missing Cards</th>
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		);
	}

}