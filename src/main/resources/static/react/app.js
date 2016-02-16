import React from 'react';
import ReactDOM from'react-dom';
import { IndexRoute, Router, Route, Link, hashHistory, browserHistory } from 'react-router';
import App from './components/App.react';
import DiffSection from './components/DiffSection.react';
import LoadDecksSection from './components/LoadDecksSection.react';


const adminMenuItemData = [ { link : "#/admin/load", text : "Load Decks" } ];
const collectionMenuItemData = [ 
	{ text : "HearthPwn", link : "http://hearthpwn.com" },
	{ text : "Hearthstone Top Deck", link : "http://hearthstonetopdeck.com" },
	{ text : "IcyVeins", link : "http://www.icy-veins.com/hearthstone" },
	{ text : "Tempo Storm", link : "https://tempostorm.com/hearthstone/decks"}
];
const adminMenuData = { icon : 'icon_tools', text : "Admin", menuItemData : adminMenuItemData }
const collectionMenuData = { icon : 'icon_archive_alt', text : "Collections", menuItemData : collectionMenuItemData }
const menuData = [ adminMenuData, collectionMenuData ];

ReactDOM.render(
	(
		<Router history={browserHistory}>
			<Route path="/index_react.html" component={App}>
				<IndexRoute component={DiffSection}/>
				<Route path="/admin/load" component={LoadDecksSection}></Route>
			</Route>
		</Router>
	), document.getElementById('container')
);
