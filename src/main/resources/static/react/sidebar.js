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
				<li><a className="" href={item.link}>{item.text}</a></li>
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
		var menuItemData = [ { link : "#/admin/load", text : "Load Decks" } ];
		return (
			<li className="sub-menu">
				<a href="javascript:;" className="" onClick={this.handleClick}> 
					<i className={this.props.icon}></i>
					<span>{this.props.children}</span>
					<span className={ "menu-arrow " + (this.state.open ? "arrow_carrot-down" : "arrow_carrot-right") }></span>
				</a>
				<SubMenuList visible={this.state.open} menuItemData={menuItemData}></SubMenuList>
			</li>
		);
	}
});

var Sidebar = React.createClass({
	render: function() {
		return (

		<aside>
			<div id="sidebar" className="nav-collapse" tabIndex="5000"
				style={ { overflow: 'hidden', outline: 'none' } }>

				<ul className="sidebar-menu">
					
					<SidebarLink icon="icon_house_alt" link="/index_react.html">Home</SidebarLink>

					<SubMenu icon="icon_tools">Admin</SubMenu>

					
					<li className="sub-menu"><a href="javascript:;" className=""> <i
							className="icon_archive_alt"></i> <span>Collections</span> <span
							className="menu-arrow arrow_carrot-right"></span>
					</a>
						<ul className="sub">
							<li><a className="" href="http://hearthpwn.com"
								target="_blank">Hearthpwn</a></li>
							<li><a className="" href="http://hearthstonetopdeck.com"
								target="_blank">Hearthstone Top Deck</a></li>
							<li><a className="" href="http://www.icy-veins.com/hearthstone"
								target="_blank">IcyVeins</a></li>
							<li><a className=""
								href="https://tempostorm.com/hearthstone/decks" target="_blank">Tempo
									Storm</a></li>
						</ul></li>
				</ul>

			</div>
		</aside>

		    	);
  	}
});

ReactDOM.render(
		<Sidebar />,
		document.getElementById('sidebar-wrapper')
);