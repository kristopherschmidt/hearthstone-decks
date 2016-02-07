var SidebarLink = React.createClass({
	render: function() {
		return (
				<li className="active">
					<a className="" href={this.props.link}>
						<i className={this.props.icon}></i> 
						<span>{this.props.children}</span>
					</a>
				</li>
		);
	}
});

var SubMenuList = React.createClass({
	render : function() {
		var menuItems = this.props.menuItemData.map(function(item) {
			return (
				<li key={item.text}><a className="" href={item.link}>{item.text}</a></li>
			);
		});
		return (
			<ul className="sub">
				{ menuItems }
			</ul>
		);
	},
	slideUp : function() {
		$(React.findDOMNode(this)).slideUp();
	},
	slideDown : function() {
		$(React.findDOMNode(this)).slideDown();
	},
	componentDidUpdate: function() {
        if (this.props.visible) {
        	this.slideDown();
        } else {
        	this.slideUp();
        }
    }
});

var SubMenu = React.createClass({
	getInitialState : function() {
		return {
			open : false
		}
	},
	handleClick : function(event) {
		this.setState({ open : !this.state.open });
	},
	render : function() {
		return (
			<li className="sub-menu">
				<a href="javascript:;" className="" onClick={this.handleClick}> 
					<i className={this.props.icon}></i>
					<span>{this.props.children}</span>
					<span className={ "menu-arrow " + (this.state.open ? "arrow_carrot-down" : "arrow_carrot-right") }></span>
				</a>
				<SubMenuList visible={this.state.open} menuItemData={this.props.menuItemData}></SubMenuList>
			</li>
		);
	}
});

var Sidebar = React.createClass({
	render: function() {
	
		var menus = this.props.menuData.map(function(menuData) {
			return (
				<SubMenu key={menuData.text} icon={menuData.icon} menuItemData={menuData.menuItemData}>{menuData.text}</SubMenu>
			);
		});

		return (

		<aside>
			<div id="sidebar" className="nav-collapse" tabIndex="5000"
				style={ { overflow: 'hidden', outline: 'none' } }>

				<ul className="sidebar-menu">
					
					<SidebarLink icon="icon_house_alt" link={this.props.homeLink}>Home</SidebarLink>

					{ menus }

				</ul>

			</div>
		</aside>

		    	);
  	}
});

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