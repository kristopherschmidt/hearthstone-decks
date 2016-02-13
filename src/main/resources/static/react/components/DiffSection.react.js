

import React, { Component } from 'react';
import DiffStore from '../stores/DiffStore';
import MissingCardsPanel from './MissingCardsPanel.react'
import DiffResultsTable from './DiffResultsTable.react'

class DiffSection extends Component {

	constructor() {
		super();
		this.state = { diffAnalyzer: { allMissingCards: { cards: [] } } };
	}

	componentDidMount() {
		DiffStore.addListener(this._onChange.bind(this));
		DiffStore.getDiffAnalyzer();
	}

	updateFromStores() {
		this.setState({ diffAnalyzer: DiffStore.getDiffAnalyzer() });
	}

	render() {

		return (
			<MissingCardsPanel missingCards={this.state.diffAnalyzer.allMissingCards.cards} limit="15" />
		);
	}

	_onChange() {
		this.updateFromStores();
	}

}

export default DiffSection;