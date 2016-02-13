

import React, { Component } from 'react';
import DiffStore from '../stores/DiffStore';
import MissingCardsPanel from './MissingCardsPanel.react'
import DiffResultsTable from './DiffResultsTable.react'

class DiffSection extends Component {

	constructor() {
		super();
		this.state = { data: [] };
	}

	componentDidMount() {
		DiffStore.addListener(this._onChange.bind(this));
		DiffStore.getDiff();
	}

	updateFromStores() {
		this.setState({ data: DiffStore.getDiff() });
	}

	render() {

		return (
			<MissingCardsPanel missingCards={this.state.data} limit="15" />
		);
	}

	_onChange() {
		this.updateFromStores();
	}

}

export default DiffSection;