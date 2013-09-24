/*
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import us.mn.state.health.lims.menu.valueholder.Menu;
import us.mn.state.health.lims.test.valueholder.TestSection;

public class MenuItem {
	private List<MenuItem> childMenus = new ArrayList<MenuItem>();
    private String elementId;
    private String localizedTooltip;
    private boolean openInNewWindow;
    private String actionURL;
    private String clickAction;
    private String localizedTitle;
    private int presentationOrder;

    public static MenuItem create(Menu menu) {
        MenuItem menuItem = new MenuItem(menu.getElementId(), menu.getLocalizedTooltip(), menu.isOpenInNewWindow(), menu.getActionURL(), menu.getClickAction(), menu.getLocalizedTitle(), menu.getPresentationOrder());
        return menuItem;
    }

    public static MenuItem emptyMenu() {
        return new MenuItem();
    }

    public static MenuItem create(TestSection testSection) {
        String testSectionName = testSection.getTestSectionName();
        String urlTest = getEncoded(testSectionName);
        return new MenuItem("test_section_" + testSection.getId(), testSectionName, false, "/LogbookResults.do?type=" + urlTest, "", testSectionName, testSection.getSortOrderInt());
    }


    private static String getEncoded(String actionURL) {
        String encode = "";

        try {
            encode = URLEncoder.encode(actionURL, Charset.defaultCharset().displayName());
        } catch (UnsupportedEncodingException e) {
            encode = "UTF-8";
        }

        return encode;
    }

    private MenuItem(String elementId, String localizedTooltip, boolean openInNewWindow, String actionURL, String clickAction, String localizedTitle, int presentationOrder) {
        this.elementId = elementId;
        this.localizedTooltip = localizedTooltip;
        this.openInNewWindow = openInNewWindow;
        this.actionURL = actionURL;
        this.clickAction = clickAction;
        this.localizedTitle = localizedTitle;
        this.presentationOrder = presentationOrder;
    }

    private MenuItem() {

    }

	public List<MenuItem> getChildMenus() {
		return Collections.unmodifiableList(childMenus);
	}

	public void sortChildren(){
		Collections.sort(childMenus, new Comparator<MenuItem>(){

			@Override
			public int compare(MenuItem o1, MenuItem o2) {
				return o1.presentationOrder - o2.presentationOrder;
			}});
	}

    public String getElementId() {
        return elementId;
    }

    public String getLocalizedTooltip() {
        return localizedTooltip;
    }

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }

    public String getActionURL() {
        return actionURL;
    }

    public String getClickAction() {
        return clickAction;
    }

    public String getLocalizedTitle() {
        return localizedTitle;
    }

    public void addChild(MenuItem menuItem){
        childMenus.add(menuItem);
    }

    public void addAllChildren(Collection<MenuItem> menuItems){
        childMenus.addAll(menuItems);
    }

    public boolean containsChild(MenuItem menuItem){
        return childMenus.contains(menuItem);
    }
}
