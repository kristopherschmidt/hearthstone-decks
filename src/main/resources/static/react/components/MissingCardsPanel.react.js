import React, { Component } from 'react';

export default class MissingCardsPanel extends Component {

	handleClick(cardName) {
		this.props.onClick(cardName);
	}

	render() {
		var cards = [];
		this.props.missingCards.sort(function(a, b) {
			return b.numCards - a.numCards;
		});
		for (var i = 0; i < this.props.missingCards.length; ++i) {
			if (i >= this.props.limit) {
				break;
			}
			var deckCard = this.props.missingCards[i];
			cards.push(
				<a href="javascript:;" onClick={this.handleClick.bind(this, deckCard.cardName)} className="list-group-item" key={deckCard.cardName}>{ deckCard.cardName } ({ deckCard.numCards })</a>
			);
		}

		return (
			<div>
				<div className="panel-content">Top missing cards (click to filter):</div>
				<ol className="list-group">
					{ cards }
				</ol>
			</div>
		);
	}
}