import React, { Component } from 'react';

export default class DiffResultsTableHeader extends Component {

	_handleSort() {
		var sortReverse = this.isSortedColumn() ? !this.props.sortReverse : this.props.sortReverse;
		this.props.onSort(this.props.columnSortType, sortReverse);
	}

	isSortedColumn() {
		return (this.props.tableSortType == this.props.columnSortType);
	}

	render() {
		var sortIcon;	
		if (this.isSortedColumn()) {
			if (this.props.sortReverse) {
				sortIcon = (<span className="fa fa-caret-up"></span>);
			} else {
				sortIcon = (<span className="fa fa-caret-down"></span>);
			}
		}
		return (
			<th className={this.props.className}>
				<a href="javascript:;" onClick={this._handleSort.bind(this)}>
					{this.props.children}
				</a>
				{ sortIcon }
			</th>
		);
	}

}