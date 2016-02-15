import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { Link } from 'react-router'

class SidebarLink extends Component {
	render() {
		return (
				<li className="active">
					<a className="" href={this.props.link}>
						<i className={this.props.icon}></i> 
						<span>{this.props.children}</span>
					</a>
				</li>
		);
	}
}

class SubMenuList extends Component {

	render() {
		var menuItems = this.props.menuItemData.map(function(item) {
			var link;
			if (item.link.startsWith("http")) {
				link = (<a href={item.link}>{item.text}</a>);
			} else {
				link = (<Link to={item.link}>{item.text}</Link>);
			}
			return (
				<li key={item.text}>{link}</li>
			);
		});
		return (
			<ul className="sub">
				{ menuItems }
			</ul>
		);
	}

	slideUp() {
		$(ReactDOM.findDOMNode(this)).slideUp();
	}

	slideDown() {
		$(ReactDOM.findDOMNode(this)).slideDown();
	}

	componentDidUpdate() {
        if (this.props.visible) {
        	this.slideDown();
        } else {
        	this.slideUp();
        }
    }
}

class SubMenu extends Component {
	constructor() {
		super();
		this.state = {
			open : false
		}
	}

	handleClick(event) {
		this.setState({ open : !this.state.open });
	}

	render() {
		return (
			<li className="sub-menu">
				<a href="javascript:;" className="" onClick={this.handleClick.bind(this)}> 
					<i className={this.props.icon}></i>
					<span>{this.props.children}</span>
					<span className={ "menu-arrow " + (this.state.open ? "arrow_carrot-down" : "arrow_carrot-right") }></span>
				</a>
				<SubMenuList visible={this.state.open} menuItemData={this.props.menuItemData}></SubMenuList>
			</li>
		);
	}
}

class Sidebar extends Component {
	render() {
	
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
}

export default Sidebar;