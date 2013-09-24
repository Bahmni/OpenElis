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

package us.mn.state.health.lims.common.dao;

import java.io.Serializable;
import java.util.List;

import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.valueholder.SimpleBaseEntity;

public interface GenericDAO<Key extends Serializable, Entity extends SimpleBaseEntity<Key>> {

	void delete(List<Entity> entities) throws LIMSRuntimeException;

	List<Entity> getAll() throws LIMSRuntimeException;
	
	List<Entity> getAllOrderBy(String columnName) throws LIMSRuntimeException;

	Entity getById(Entity analyzer) throws LIMSRuntimeException;

	void getData(Entity analyzer) throws LIMSRuntimeException;

	boolean insertData(Entity analyzer) throws LIMSRuntimeException;

	void updateData(Entity analyzer) throws LIMSRuntimeException;

	Entity readEntity(Key id) throws LIMSRuntimeException;

	List<Entity> readByExample(Entity entity) throws LIMSRuntimeException;	
}
