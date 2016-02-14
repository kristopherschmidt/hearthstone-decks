import React, { Component } from 'react';

export default class DiffResultsTableHeader extends Component {

	constructor() {
		super();
		this.state = { sorted: false, sortReverse: false };
	}

	_handleSort(columnName) {
		this.setState({ sorted: true, sortReverse: !this.state.sortReverse });
		this.props.onSort(columnName);
	}

	render() {
		var sortIcon;	
		if (this.state.sorted) {
			if (this.sortReverse) {
				sortIcon = (<span className="fa fa-caret-down"></span>);
			} else {
				sortIcon = (<span className="fa fa-caret-up"></span>);
			}
		}
		return (
			<th>
				<a href="javascript:;" onClick={this._handleSort.bind(this, this.props.columnName)}>
					{this.props.columnName}
				</a>
				{ sortIcon }
			</th>
		);
	}

}