import React from 'react';
import ReactDOM from'react-dom';
import { Router, Route, Link, hashHistory } from 'react-router';
import Sidebar from './components/Sidebar.react';
import DiffSection from './components/DiffSection.react';
import LoadDecksSection from './components/LoadDecksSection.react';
import HearthstoneDispatcher from './dispatcher/HearthstoneDispatcher';

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
		<Sidebar homeLink="/index_react.html" menuData={menuData} />,
		document.getElementById('sidebar-wrapper')
);

ReactDOM.render(
	(
		<Router history={hashHistory}>
			<Route path="/" component={DiffSection}></Route>
			<Route path="/admin/load" component={LoadDecksSection}></Route>
		</Router>
	), document.getElementById('content')
);
