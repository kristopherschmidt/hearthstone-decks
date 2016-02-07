var React = require('react');
var ReactDOM = require('react-dom');
var Sidebar = require('./components/Sidebar.react');
var SearchSection = require('./components/SearchSection.react');

var adminMenuItemData = [ { link : "#/admin/load", text : "Load Decks" } ];
var collectionMenuItemData = [ 
	{ text : "HearthPwn", link : "http://hearthpwn.com" },
	{ text : "Hearthstone Top Deck", link : "http://hearthstonetopdeck.com" },
	{ text : "IcyVeins", link : "http://www.icy-veins.com/hearthstone" },
	{ text : "Tempo Storm", link : "https://tempostorm.com/hearthstone/decks"}
];
var adminMenuData = { icon : 'icon_tools', text : "Admin", menuItemData : adminMenuItemData }
var collectionMenuData = { icon : 'icon_archive_alt', text : "Collections", menuItemData : collectionMenuItemData }
var menuData = [ adminMenuData, collectionMenuData ];

ReactDOM.render(
		<Sidebar homeLink="/index_react.html" menuData={menuData} />,
		document.getElementById('sidebar-wrapper')
);

ReactDOM.render(
	<SearchSection/>, document.getElementById('content')
);
