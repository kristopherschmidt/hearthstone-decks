

import React, { Component } from 'react';
import DiffStore from '../stores/DiffStore';
import DiffCriteriaPanel from './DiffCriteriaPanel.react';
import MissingCardsPanel from './MissingCardsPanel.react';
import DiffResultsTable from './DiffResultsTable.react';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';

class DiffSection extends Component {

	constructor() {
		super();
		this.state = this._getStateFromStores();
	}

	componentDidMount() {
		this.removeListenerToken = DiffStore.addListener(this._onChange.bind(this));
	}

	componentWillUnmount() {
   		this.removeListenerToken.remove();
  	}

	_handleMissingCardsClick(cardName) {
		HearthstoneActionCreators.addFilterCard(cardName);
	}

	_getStateFromStores() {
		return { diffAnalyzer: DiffStore.getDiffAnalyzer(), diffCriteria: DiffStore.getDiffCriteria() };
	}

	_onChange() {
		this.setState(this._getStateFromStores());
	}

	render() {

		return (

			<div>

				<div className="row">
					<div className="col-sm-12">
						<h3 className="page-header">Deck Search & Compare</h3>
					</div>
				</div>

				<DiffCriteriaPanel diffCriteria={this.state.diffCriteria}></DiffCriteriaPanel>

				<hr></hr>
				
				<section className="panel">
					<div className="panel-heading">Found { this.state.diffAnalyzer.filteredDiffs.length } results.</div>
					<div className="panel-body">
						<MissingCardsPanel missingCards={this.state.diffAnalyzer.allMissingCards.cards} limit="15" onClick={this._handleMissingCardsClick.bind(this)} />
						<div className="panel-content">All Results:</div>
						<DiffResultsTable deckDiffs={this.state.diffAnalyzer.filteredDiffs} />
					</div>
				</section>

			</div>

		);
	}

}

export default DiffSection;