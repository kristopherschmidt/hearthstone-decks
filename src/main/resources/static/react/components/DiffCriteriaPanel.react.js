import React, { Component } from 'react';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';
var CheckboxGroup = require('react-checkbox-group');

export default class DiffCriteriaPanel extends Component {

	constructor() {
		super();
		this.state = {};
	}

	handleCollectionChange(event) {
		if (event) {
			HearthstoneActionCreators.changeCollection(event.target.value);
		}
	}

	handleClassChange(event) {
		this.state.playerClasses = this.refs.playerClasses.getCheckedValues();
	}

	render() {
		return (
			<section className="panel">
				
				<div className="panel-body">
					<form className="form-horizontal">

						<div className="form-group">
							<label className="col-sm-2 control-label">Collection</label>
							<div className="col-sm-10">
								<select className="form-control" onChange={this.handleCollectionChange.bind(this)} value={this.props.diffCriteria.collection}>
									<option value="">All</option>
									<option value="hearthpwnRepository">Hearthpwn</option>
									<option value="hearthstoneTopDeckRepository">Hearthstone
										TopDecks</option>
									<option value="icyVeinsDeckRepository">IcyVeins</option>
									<option value="tempoStormDeckRepository">Tempo Storm</option>
								</select>
							</div>
						</div>

						<div className="form-group">
							<label className="col-sm-2 control-label">Classes</label>
							<div className="col-sm-10">

							   <CheckboxGroup name="fruits" ref="playerClasses" onChange={this.handleClassChange.bind(this)}>
									<label className="checkbox-inline"> 
										<input type="checkbox" value="Druid"></input>
										Druid
									</label>
									<label className="checkbox-inline"> 
										<input type="checkbox" value="Hunter"></input>
										Hunter
									</label> 
							   	</CheckboxGroup>
							
							</div>
						</div>

					</form>
				</div>				

			</section>
		);
	}
}