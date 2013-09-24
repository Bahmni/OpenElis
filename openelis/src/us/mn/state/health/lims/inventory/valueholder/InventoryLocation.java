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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/

package us.mn.state.health.lims.inventory.valueholder;

import java.sql.Timestamp;

import us.mn.state.health.lims.common.valueholder.BaseObject;
import us.mn.state.health.lims.common.valueholder.ValueHolder;
import us.mn.state.health.lims.common.valueholder.ValueHolderInterface;

public class InventoryLocation extends BaseObject{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String lotNumber;
	private Timestamp expirationDate;
	private ValueHolderInterface inventoryItem = new ValueHolder();
	private String inventoryItemId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getInventoryItemId() {
		return inventoryItemId;
	}
	public void setInventoryItemId(String inventoryItemId) {
		this.inventoryItemId = inventoryItemId;
	}
	
	public void setInventoryItem(ValueHolderInterface inventoryItem) {
		this.inventoryItem = inventoryItem;
	}
	
	public void setInventoryItemHolder(ValueHolderInterface inventoryItem) {
		this.inventoryItem = inventoryItem;
	}
	
	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem.setValue(inventoryItem);
	}
	
	public ValueHolderInterface getInventoryItemHolder() {
		return inventoryItem;
	}
	
	public InventoryItem getInventoryItem() {
		return (InventoryItem)inventoryItem.getValue();
	}
}
