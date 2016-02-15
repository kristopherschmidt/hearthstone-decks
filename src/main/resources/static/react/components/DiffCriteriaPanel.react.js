import React, { Component } from 'react';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';
import CardInput from './CardInput.react';
import CheckboxGroup from 'react-checkbox-group';

export default class DiffCriteriaPanel extends Component {

	constructor() {
		super();
	}

	handleCardAddition(cardName) {
		HearthstoneActionCreators.addFilterCard(cardName);
	}

	handleCardDelete(cardName) {
		HearthstoneActionCreators.removeFilterCard(cardName);
	}

	handleCollectionChange(event) {
		if (event) {
			HearthstoneActionCreators.changeCollection(event.target.value);
		}
	}

	handleClassChange(event) {
		HearthstoneActionCreators.changePlayerClasses(this.refs.playerClasses.getCheckedValues());
	}

	render() {

		var playerClassesData = [ "Druid", "Hunter", "Mage", "Paladin", "Priest", "Rogue", "Shaman", "Warlock", "Warrior" ];
		var playerClassCheckboxes = playerClassesData.map(function(playerClass) {
			return (
				<label className="checkbox-inline" key={playerClass}> 
					<input type="checkbox" value={playerClass}></input>{playerClass}
				</label>
			);
		});

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

							   <CheckboxGroup name="fruits" ref="playerClasses" onChange={this.handleClassChange.bind(this)} value={this.props.diffCriteria.playerClasses}>
									{ playerClassCheckboxes }
							   	</CheckboxGroup>
							
							</div>
						</div>

						<div className="form-group">
							<label className="col-sm-2 control-label">Cards</label>
							<div className="col-sm-10">
									<CardInput handleAddition={this.handleCardAddition.bind(this)} handleDelete={this.handleCardDelete.bind(this)} cardNames={this.props.diffCriteria.cards}></CardInput>
							</div>
						</div>

					</form>
				</div>				

			</section>
		);
	}
}