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
*
* Contributor(s): CIRG, University of Washington, Seattle WA.
*/
package us.mn.state.health.lims.note.daoimpl;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import us.mn.state.health.lims.audittrail.dao.AuditTrailDAO;
import us.mn.state.health.lims.audittrail.daoimpl.AuditTrailDAOImpl;
import us.mn.state.health.lims.common.action.IActionConstants;
import us.mn.state.health.lims.common.daoimpl.BaseDAOImpl;
import us.mn.state.health.lims.common.exception.LIMSRuntimeException;
import us.mn.state.health.lims.common.log.LogEvent;
import us.mn.state.health.lims.common.util.StringUtil;
import us.mn.state.health.lims.common.util.SystemConfiguration;
import us.mn.state.health.lims.hibernate.HibernateUtil;
import us.mn.state.health.lims.note.dao.NoteDAO;
import us.mn.state.health.lims.note.valueholder.Note;

import java.util.List;

/**
* @author diane benz
*/
public class NoteDAOImpl extends BaseDAOImpl implements NoteDAO {

	public void deleteData(List notes) throws LIMSRuntimeException {
		//add to audit trail
		try {
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			for (int i = 0; i < notes.size(); i++) {
				Note data = (Note)notes.get(i);

				Note oldData = readNote(data.getId());
				Note newData = new Note();

				String sysUserId = data.getSysUserId();
				String event = IActionConstants.AUDIT_TRAIL_DELETE;
				String tableName = "NOTE";
				auditDAO.saveHistory(newData,oldData,sysUserId,event,tableName);
			}
		}  catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","AuditTrail deleteData()",e.toString());
			throw new LIMSRuntimeException("Error in Note AuditTrail deleteData()", e);
		}

		try {
			for (int i = 0; i < notes.size(); i++) {
				Note data = (Note) notes.get(i);
				//bugzilla 2206
				data = readNote(data.getId());
				HibernateUtil.getSession().delete(data);
				HibernateUtil.getSession().flush();
				HibernateUtil.getSession().clear();
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","deleteData()",e.toString());
			throw new LIMSRuntimeException("Error in Note deleteData()",e);
		}
	}

	public boolean insertData(Note note) throws LIMSRuntimeException {
		try {
            note.setText(StringUtil.encode(note.getText()));
			String id = (String) HibernateUtil.getSession().save(note);
			note.setId(id);

			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = note.getSysUserId();
			String tableName = "NOTE";
			auditDAO.saveNewHistory(note,sysUserId,tableName);

			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","insertData()",e.toString());
			throw new LIMSRuntimeException("Error in Note insertData()", e);
		}

		return true;
	}

	public void updateData(Note note) throws LIMSRuntimeException {
		Note oldData = readNote(note.getId());
		Note newData = note;

		//add to audit trail
		try {
            note.setText(StringUtil.encode(note.getText()));
			AuditTrailDAO auditDAO = new AuditTrailDAOImpl();
			String sysUserId = note.getSysUserId();
			String event = IActionConstants.AUDIT_TRAIL_UPDATE;
			String tableName = "NOTE";
			auditDAO.saveHistory(newData,oldData,sysUserId,event,tableName);
		}  catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","AuditTrail updateData()",e.toString());
			throw new LIMSRuntimeException("Error in Note AuditTrail updateData()", e);
		}

		try {
			HibernateUtil.getSession().merge(note);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().evict(note);
			HibernateUtil.getSession().refresh(note);
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","updateData()",e.toString());
			throw new LIMSRuntimeException("Error in Note updateData()", e);
		}
	}

	public void getData(Note note) throws LIMSRuntimeException {
		try {
			Note nt = (Note) HibernateUtil.getSession().get(Note.class, note.getId());
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			if (nt != null) {
				PropertyUtils.copyProperties(note, nt);
			} else {
				note.setId(null);
			}
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","getData()",e.toString());
			throw new LIMSRuntimeException("Error in Note getData()", e);
		}
	}

	@Override
	public Note getData(String noteId) throws LIMSRuntimeException {
		try {
			Note note = (Note) HibernateUtil.getSession().get(Note.class,	noteId);
			closeSession();
			return note;
		} catch (Exception e) {
			handleException(e, "getData");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Note> getAllNotes() throws LIMSRuntimeException {
		try {
			String sql = "from Note";
			Query query = HibernateUtil.getSession().createQuery(sql);
            List<Note> list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

            return list;

		} catch (Exception e) {
			LogEvent.logError("NoteDAOImpl","getAllNotes()",e.toString());
			throw new LIMSRuntimeException("Error in Note getAllNotes()", e);
		}
	}

	public List getPageOfNotes(int startingRecNo) throws LIMSRuntimeException {
		try {
			// calculate maxRow to be one more than the page size
			int endingRecNo = startingRecNo + (SystemConfiguration.getInstance().getDefaultPageSize() + 1);
    		//bugzilla 2571 go through ReferenceTablesDAO to get reference tables info
			String sql = "from Note nt order by nt.referenceTableId, nt.referenceId,nt.noteType";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setFirstResult(startingRecNo - 1);
			query.setMaxResults(endingRecNo - 1);

            List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();

            return list;
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","getPageOfNotes()",e.toString());
			throw new LIMSRuntimeException(
					"Error in Note getPageOfNotes()", e);
		}
	}

	public Note readNote(String idString) {
		try {
			Note note = (Note) HibernateUtil.getSession().get(Note.class, idString);
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
            return note;
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","readNote()",e.toString());
			throw new LIMSRuntimeException("Error in Note readNote()", e);
		}

	}

	public List getNextNoteRecord(String id) throws LIMSRuntimeException {
		return null;//getNextRecord(id, "Note", Note.class);

	}

	public List getPreviousNoteRecord(String id) throws LIMSRuntimeException {
		return null;//getPreviousRecord(id, "Note", Note.class);
	}


	public List getAllNotesByRefIdRefTable(Note note) throws LIMSRuntimeException {
		try {
			String sql = "from Note n where n.referenceId = :refId and n.referenceTableId = :refTableId order by n.noteType desc, n.lastupdated desc";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("refId", Integer.parseInt(note.getReferenceId()));
			query.setInteger("refTableId", Integer.parseInt(note.getReferenceTableId()));

			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
            return list;
		} catch (Exception e) {
			LogEvent.logError("NoteDAOImpl","getAllNotesByRefIdRefTable()",e.toString());
			throw new LIMSRuntimeException("Error in Note getAllNotesByRefIdRefTable()", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Note> getNotesByNoteTypeRefIdRefTable(Note note) throws LIMSRuntimeException {
		try {
			String sql = "from Note n where n.referenceId = :refId and n.referenceTableId = :refTableId and n.noteType = :noteType order by n.lastupdated";
			org.hibernate.Query query = HibernateUtil.getSession().createQuery(
					sql);
			query.setInteger("refId", Integer.parseInt(note.getReferenceId()));

			query.setInteger("refTableId", Integer.parseInt(note.getReferenceTableId()));
			query.setParameter("noteType", note.getNoteType());

			List<Note> list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
            return list;

		} catch (Exception e) {
			LogEvent.logError("NoteDAOImpl","getNotesByNoteTypeRefIdRefTable()",e.toString());
			throw new LIMSRuntimeException("Error in Note getNotesByNoteTypeRefIdRefTable()", e);
		}
	}


	public Integer getTotalNoteCount() throws LIMSRuntimeException {
		return getTotalCount("Note", Note.class);
	}

	public List getNextRecord(String id, String table, Class clazz) throws LIMSRuntimeException {
        //bugzilla 1908
		try {
			//bugzilla 1908 cannot use named query for postgres because of oracle ROWNUM
			//instead get the list in this sortorder and determine the index of record with id = currentId
			//bugzilla 2571 go through ReferenceTablesDAO to get reference tables info
			String sql = "select n.id from Note n " +
					" order by n.referenceTableId, n.referenceId, n.noteType";

 			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
			List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
            int rrn = list.indexOf(id);

			list = HibernateUtil.getSession().getNamedQuery(getTablePrefix(table) + "getNext")
                        .setFirstResult(rrn + 1)
                        .setMaxResults(2)
                        .list();
            return list;
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","getNextRecord()",e.toString());
			throw new LIMSRuntimeException("Error in getNextRecord() for " + table, e);
		}
	}


	public List getPreviousRecord(String id, String table, Class clazz) throws LIMSRuntimeException {
		int currentId = (Integer.valueOf(id)).intValue();
		String tablePrefix = getTablePrefix(table);

		//bugzilla 1908
		int rrn = 0;
		try {
			//bugzilla 1908 cannot use named query for postgres because of oracle ROWNUM
			//instead get the list in this sortorder and determine the index of record with id = currentId
			//bugzilla 2571 go through ReferenceTablesDAO to get reference tables info
			String sql = "select n.id from Note n " +
					" order by n.referenceTableId desc, n.referenceId desc, n.noteType desc";

 			org.hibernate.Query query = HibernateUtil.getSession().createQuery(sql);
            List list = query.list();
			HibernateUtil.getSession().flush();
			HibernateUtil.getSession().clear();
			rrn = list.indexOf(String.valueOf(currentId));

			list = HibernateUtil.getSession().getNamedQuery(
					tablePrefix + "getPrevious").setFirstResult(
					rrn + 1).setMaxResults(2).list();

            return list;
		} catch (Exception e) {
			//bugzilla 2154
			LogEvent.logError("NoteDAOImpl","getPreviousRecord()",e.toString());
			throw new LIMSRuntimeException("Error in getPreviousRecord() for " + table, e);
		}
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<Note> getNoteByRefIAndRefTableAndSubject(String refId, String table_id, String subject) throws LIMSRuntimeException {
		String sql = "FROM Note n where n.referenceId = :refId " +
                "and n.referenceTableId = :tableId " +
                "and n.subject = :subject " +
                "order by n.lastupdated desc";

		try{
			Query query = HibernateUtil.getSession().createQuery(sql);
			query.setInteger("refId", Integer.parseInt(refId));
			query.setInteger("tableId", Integer.parseInt(table_id));
			query.setString("subject", subject);

			List<Note> noteList = query.list();
			closeSession();
			return noteList;
		}catch(HibernateException e){
			handleException(e, "getNoteByRefIAndRefTableAndSubject");
		}
		return null;
	}
}
