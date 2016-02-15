import React, { Component } from 'react';
import { WithContext as ReactTags } from 'react-tag-input';
import CardStore from '../stores/CardStore';
import * as HearthstoneActionCreators from '../actions/HearthstoneActionCreators';

export default class CardInput extends Component {

	constructor() {
		super();
		this.state = { tags: [], suggestions: this._getSuggestionsFromStore() };
	}

	componentDidMount() {
		this.removeListenerToken = CardStore.addListener(this._onChange.bind(this));
	}

	componentWillUnmount() {
   		this.removeListenerToken.remove();
  	}

	componentWillReceiveProps(props) {
		this.setState({ tags: this._getTagsFromProps() });
	}

	handleDelete(index) {
		/*
		var tags=this.state.tags;
		tags.splice(index,1);
		this.setState({tags:tags});
		this.props.onChange(this.state.tags.map(function(tag) { return tag.text }));
		*/
	}

	handleAddition(tag) {
		/*
		var tags=this.state.tags;
		tags.push({id:tags.length+ 1,text:tag});
		this.setState({tags:tags});
		this.props.onChange(this.state.tags.map(function(tag) { return tag.text }));
		*/
	}

	handleDrag(tag,currPos,newPos) {
		/*
		var tags=this.state.tags;
		tags.splice(currPos,1);
		tags.splice(newPos,0,tag);
		this.setState({tags:tags});
		*/
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
		return(
			 <ReactTags
                    tags={this.state.tags}
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
