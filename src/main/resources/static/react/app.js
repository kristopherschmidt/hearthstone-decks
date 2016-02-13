import React from 'react';
import ReactDOM from'react-dom';
import Sidebar from './components/Sidebar.react';
import DiffSection from './components/DiffSection.react'
import HearthstoneDispatcher from './dispatcher/HearthstoneDispatcher'

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
	<DiffSection />, document.getElementById('content')
);
