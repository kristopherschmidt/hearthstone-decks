import React, { Component } from 'react';
import { WithContext as ReactTags } from 'react-tag-input';
import CardStore from '../stores/CardStore';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';

export default class CardInput extends Component {

	constructor() {
		super();
		this.state = { suggestions: this._getSuggestionsFromStore() };
	}

	componentDidMount() {
		this.removeListenerToken = CardStore.addListener(this._onChange.bind(this));
	}

	componentWillUnmount() {
   		this.removeListenerToken.remove();
  	}

	handleDelete(index) {
		var cardToDelete = this.props.cardNames[index];
		this.props.handleDelete(cardToDelete);
	}

	handleAddition(tag) {
		if (this.state.suggestions.indexOf(tag) != -1) {
			this.props.handleAddition(tag);
		}
	}

	handleDrag(tag,currPos,newPos) {
	
	}

	_getTagsFromProps() {
		return this.props.cardNames.map(function(cardName) {
			return { id: cardName, text: cardName };
		});
	}

	_getSuggestionsFromStore() {
		return CardStore.getCards().map(function(card) {
			return card.name;
		});
	}

	_onChange() {
		this.setState({ suggestions: this._getSuggestionsFromStore() });
	}

	render() {
		var tags = this._getTagsFromProps();
		return(
			 <ReactTags
                    tags={tags}
                    suggestions={this.state.suggestions}
                    handleDelete={this.handleDelete.bind(this)}
                    handleAddition={this.handleAddition.bind(this)}
                    handleDrag={this.handleDrag.bind(this)}
                    minQueryLength={2}
                    autocomplete={true}
                    placeholder="Add card to filter"
              >
              </ReactTags>
		);
	}

}
