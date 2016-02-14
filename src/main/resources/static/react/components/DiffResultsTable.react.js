import React, { Component } from 'react';
import numeral from 'numeral';
import DiffResultsTableHeader from './DiffResultsTableHeader.react'

export default class DiffResultsTable extends Component {

	constructor() {
		super();
		this.sortType = "fullDustValue";
		this.state = { sortedDiffs : [] };
	}


	_sortedDiffs(sortType) {
		//shallow copy the diff props array for sorting, so as not to disturb the actual props
		var sortedDiffs = this.props.deckDiffs.slice();
		sortedDiffs.sort(function(a, b) {
			if (a[sortType] < b[sortType]) {
				return 1;
			} else if (a[sortType] > b[sortType]) {
				return -1;
			} else {
				return 0;
			}
		});
		return sortedDiffs;
	}

	render() {

		var rows = this._sortedDiffs(this.sortType).map(function(deckDiff) {

			var missingCards = deckDiff.missingCards.cards.map(function(card) {
				return (
					<li key={card.cardName}>{card.numCards} x { card.cardName }</li>
				);
			});

			return (
				<tr key={deckDiff.desiredDeck.url}>
					<td>
						<a href={ deckDiff.desiredDeck.url } target="_blank">{ deckDiff.desiredDeck.name }</a>
					</td>
					<td>{ deckDiff.requiredDust }</td>
					<td>{ deckDiff.fullDustValue }</td>

					<td>{ numeral(deckDiff.percentComplete / 100).format('0%') }</td>
					<td>{ deckDiff.rating }</td>
					<td>{ numeral(deckDiff.rankingMetric).format(0) }</td>
					<td>
						<ul>{ missingCards }</ul>
					</td>
				</tr>
			);
		});

		return (
			<table className="table table-condensed table-bordered table-striped">
				<thead>
					<tr>
						<DiffResultsTableHeader className="col-sm-4" columnName="Deck Name"></DiffResultsTableHeader>
						<DiffResultsTableHeader className="col-sm-1" columnName="Required Dust"></DiffResultsTableHeader>
						<th className="col-sm-1">Full Dust Value</th>
						<th className="col-sm-1">Percent Complete</th>
						<th className="col-sm-1">Rating</th>
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