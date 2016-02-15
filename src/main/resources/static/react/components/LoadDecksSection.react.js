import React, { Component } from 'react';

export default class LoadDecksSection extends Component {
	render() {

		return (

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
								<a className="btn btn-primary form-control">Go!</a>
								<i className="fa fa-spinner fa-spin fa-3x"></i>
							</div>
						</div>
					
					</form>
			
				</div>
			</section>

		</div>

		);
	}
}
