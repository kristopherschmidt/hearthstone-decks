import React, { Component } from 'react';
import numeral from 'numeral';
import DiffResultsTableHeader from './DiffResultsTableHeader.react'

export default class DiffResultsTable extends Component {

	constructor() {
		super();
		this.state = { sortType: "requiredDust", sortReverse: true, sortedDiffs : [] };
		this.onSort = this.onSort.bind(this);
	}

	componentWillReceiveProps(newProps) {
		this._sortDiffs(this.state.sortType, this.state.sortReverse, newProps.deckDiffs);
	}

	_sortDiffs(sortType, sortReverse, deckDiffs) {
		//shallow copy the diff props array for sorting, so as not to disturb the actual props
		var sortedDiffs = deckDiffs.slice().sort(function(a, b) {
			if (a[sortType] < b[sortType]) {
				return sortReverse ? -1 : 1;
			} else if (a[sortType] > b[sortType]) {
				return sortReverse ? 1 :-1;
			} else {
				return 0;
			}
		});
		this.setState({ sortType: sortType, sortReverse: sortReverse, sortedDiffs: sortedDiffs });
	}

	onSort(sortType, sortReverse) {
		this._sortDiffs(sortType, sortReverse, this.props.deckDiffs)
	}

	render() {

		var rows = this.state.sortedDiffs.map(function(deckDiff) {

			var missingCards = deckDiff.missingCards.cards.map(function(card) {
				return (
					<li key={card.cardName}>{card.numCards}x { card.cardName }</li>
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

		var tableHeaderData = [
			{ className: "col-sm-4", columnSortType: "", columnName: "Deck Name" },
			{ className: "col-sm-1", columnSortType: "requiredDust", columnName: "Required Dust" },
			{ className: "col-sm-1", columnSortType: "fullDustValue", columnName: "Full Dust Value" },
			{ className: "col-sm-1", columnSortType: "percentComplete", columnName: "Percent Complete" },
			{ className: "col-sm-1", columnSortType: "rating", columnName: "Rating" },
			{ className: "col-sm-1", columnSortType: "rankingMetric", columnName: "Ranking Metric" },
			{ className: "col-sm-3", columnSortType: "", columnName: "Missing Cards" },
		];

		var tableHeaders = tableHeaderData.map(function(headerData) {
			return (
				<DiffResultsTableHeader key={headerData.columnName} className={headerData.className} columnSortType={headerData.columnSortType} onSort={this.onSort} tableSortType={this.state.sortType} sortReverse={this.state.sortReverse}>
					{headerData.columnName}
				</DiffResultsTableHeader>
			);
		}.bind(this));

		return (
			<table className="table table-condensed table-bordered table-striped">
				<thead>
					<tr>
						{ tableHeaders }
					</tr>
				</thead>
				<tbody>
					{rows}
				</tbody>
			</table>
		);
	}

}