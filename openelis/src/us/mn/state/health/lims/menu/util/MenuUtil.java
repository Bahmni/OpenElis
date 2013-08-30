/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
*
*/
package us.mn.state.health.lims.menu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import us.mn.state.health.lims.menu.daoimpl.MenuDAOImpl;
import us.mn.state.health.lims.menu.valueholder.Menu;
import us.mn.state.health.lims.test.dao.TestSectionDAO;
import us.mn.state.health.lims.test.daoimpl.TestSectionDAOImpl;
import us.mn.state.health.lims.test.valueholder.TestSection;

public class MenuUtil {

    public static final String MENU_RESULTS = "menu_results";
    private static List<MenuItem> root;
	
	public static List<MenuItem> getMenuTree(){
        createTree();
		return root;
	}

	private static void createTree() {
		List<Menu> menuList = new MenuDAOImpl().getAllMenus();
        List<TestSection> allTestSections = new TestSectionDAOImpl().getAllActiveTestSections();

        Map<Menu, MenuItem> menuToMenuItemMap = new HashMap<>();
		
		for( Menu menu : menuList){
            MenuItem menuItem = MenuItem.create(menu);
			menuToMenuItemMap.put(menu, menuItem);
		}
		
		MenuItem rootWrapper = MenuItem.emptyMenu();
        MenuItem enterMenu = createMenuBasedOnTestSections(allTestSections);

        for( Menu menu : menuList){
            if( menu.getParent() == null){
                rootWrapper.addChild(menuToMenuItemMap.get(menu));
            }else{
                MenuItem parentMenuItem = menuToMenuItemMap.get(menu.getParent());

                parentMenuItem.addChild(menuToMenuItemMap.get(menu));

                if (parentMenuItem.getElementId().equals(MENU_RESULTS) && !parentMenuItem.containsChild(enterMenu)){
                    parentMenuItem.addChild(enterMenu);
                }
            }
        }

        sortChildren(rootWrapper);

		root = rootWrapper.getChildMenus();
	}

    private static MenuItem createMenuBasedOnTestSections(List<TestSection> allTestSections) {
        List<MenuItem> testSectionMenuItems = new ArrayList<>();

        for (TestSection testSection : allTestSections) {
            testSectionMenuItems.add(MenuItem.create(testSection));
        }

        MenuItem enterMenu = MenuItem.create(createEnterMenu());
        enterMenu.addAllChildren(testSectionMenuItems);

        return enterMenu;
    }

    private static Menu createEnterMenu() {
        Menu menu = new Menu();
        menu.setElementId("menu_enter_testsection");
        menu.setToolTipKey("banner.menu.results.logbook");
        menu.setDisplayKey("banner.menu.results.logbook");
        menu.setOpenInNewWindow(false);
        menu.setActionURL("");
        menu.setClickAction("");
        menu.setPresentationOrder(1);
        return menu;
    }

    public static String getMenuAsHTML( String contextPath){
		StringBuffer html = new StringBuffer();
		html.append("<ul class=\"nav-menu\" id=\"main-nav\" >\n");
		addChildMenuItems(html, getMenuTree(), contextPath, true);
		html.append("</ul>");
		return html.toString();
	}
	
	private static void addChildMenuItems(StringBuffer html, List<MenuItem> menuTree, String contextPath, boolean topLevel) {
		
		int topLevelCount = 0;
		for(MenuItem menuItem : menuTree){

			if( topLevel ){
				if( topLevelCount == 0){
					html.append("\t<li id=\"nav-first\" >\n");	
				}else if(topLevelCount == menuTree.size() - 1){
					html.append("\t<li id=\"nav-last\" >\n");	
				}else{
					html.append("\t<li>\n");
				}
				
				topLevelCount++;
			}else{
				html.append("\t<li>\n");
			}
		
			
			html.append("\t\t<a ");
			html.append("id=\"");
			html.append(menuItem.getElementId());
			html.append("\" ");
			
			if( !GenericValidator.isBlankOrNull(menuItem.getLocalizedTooltip())){
				html.append(" title=\"");
				html.append(menuItem.getLocalizedTooltip());
				html.append("\" ");
			}
			
			if( menuItem.isOpenInNewWindow()){
				html.append(" target=\"_blank\" ");
			}
			
			if( GenericValidator.isBlankOrNull(menuItem.getActionURL()) && GenericValidator.isBlankOrNull(menuItem.getClickAction())){
				html.append(" class=\"no-link\" >");
			}else{
				html.append(" href=\"");
				html.append(contextPath);
				html.append(menuItem.getActionURL());
				html.append("\" >"); 
			}
						
			html.append(menuItem.getLocalizedTitle());
			html.append("</a>\n"); 
			
			if( !menuItem.getChildMenus().isEmpty()){
				html.append("<ul>\n");
				addChildMenuItems(html,menuItem.getChildMenus(), contextPath, false );
				html.append("</ul>\n");
			}
			
			html.append("\t</li>\n");
		}
		
	}
		
	private static void sortChildren(MenuItem menuItem) {
		menuItem.sortChildren();
		
		for( MenuItem child : menuItem.getChildMenus()){
			sortChildren( child);
		}
		
	}
}
