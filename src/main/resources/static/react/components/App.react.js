import React, { Component } from 'react';
import Sidebar from './Sidebar.react';

const adminMenuItemData = [ { link : "/admin/load", text : "Load Decks" } ];
const collectionMenuItemData = [ 
	{ text : "HearthPwn", link : "http://hearthpwn.com" },
	{ text : "Hearthstone Top Deck", link : "http://hearthstonetopdeck.com" },
	{ text : "IcyVeins", link : "http://www.icy-veins.com/hearthstone" },
	{ text : "Tempo Storm", link : "https://tempostorm.com/hearthstone/decks"}
];
const adminMenuData = { icon : 'icon_tools', text : "Admin", menuItemData : adminMenuItemData }
const collectionMenuData = { icon : 'icon_archive_alt', text : "Collections", menuItemData : collectionMenuItemData }
const menuData = [ adminMenuData, collectionMenuData ];

export default class App extends Component {
	render() {
		return (

			<div>

				<header id="topheader" className="header dark-bg">
					<a href="/index_react.html" className="logo">Hearthstone Deck Helper (React.js / Flux)</a>
				</header>

				<Sidebar homeLink="/index_react.html" menuData={menuData}></Sidebar>

				<section id="main-content">
					{this.props.children}
				</section>

			</div>

		);
	}
}