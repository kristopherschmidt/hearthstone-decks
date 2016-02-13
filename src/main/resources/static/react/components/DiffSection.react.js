

import React, { Component } from 'react';
import DiffStore from '../stores/DiffStore';
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
			<DiffResultsTable data={this.state.data} />
		);
	}

	_onChange() {
		this.updateFromStores();
	}

}

export default DiffSection;