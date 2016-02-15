import React, { Component } from 'react';
import DeckStore from '../stores/DeckStore';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';

export default class LoadDecksSection extends Component {

	constructor() {
		super();
		this.state = this._getStateFromStores();
	}

	componentDidMount() {
		this.removeListenerToken = DeckStore.addListener(this._onChange.bind(this));
	}

	componentWillUnmount() {
   		this.removeListenerToken.remove();
  	}

	_getStateFromStores() {
		return { isLoading: DeckStore.getIsLoading() };
	}

	_onChange() {
		this.setState(this._getStateFromStores());
	}

	_onClick() {
		HearthstoneActionCreators.loadDecks();
	}

	render() {

		var button;
		if (this.state.isLoading) {
			button = (<i className="fa fa-spinner fa-spin fa-3x"></i>);
		} else {
			button = (<a className="btn btn-primary form-control" onClick={this._onClick.bind(this)}>Go!</a>);
		}

		return (

	<section className="wrapper">

		<div>

			<div className="row">
				<div className="col-sm-12">
					<h3 className="page-header">Reload Decks</h3>
				</div>
			</div>

			<section className="panel">

				<header className="panel-heading">
					Reload Decks
				</header>

				<div className="panel-body">
					
					<form className="form-horizontal">

						<div className="form-group">
							<label className="col-sm-2 control-label">Collection</label>
							<div className="col-sm-10">
								<select className="form-control">
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
							<div className="col-sm-12">
								{button}
							</div>
						</div>
					
					</form>
			
				</div>
			</section>

		</div>

	</section>

		);
	}
}
