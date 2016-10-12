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

package us.mn.state.health.lims.ws;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bahmni.feed.openelis.ObjectMapperRepository;
import us.mn.state.health.lims.scheduler.daoimpl.CronSchedulerDAOImpl;
import us.mn.state.health.lims.scheduler.valueholder.CronScheduler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksMonitoringAction extends WebServiceAction {

    private final String APPLICATION_JSON = "application/json";

    public TasksMonitoringAction() {
    }

    @Override
    public ActionForward performAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CronScheduler> schedulers = new CronSchedulerDAOImpl().getAllCronSchedules();

        List<TasksMonitoringResponse> tasks = new ArrayList<>();
        for (CronScheduler scheduledTask : schedulers) {
            tasks.add(new TasksMonitoringResponse(scheduledTask.getActive(), scheduledTask.getJobName(), scheduledTask.getLastRun(), null));
        }

        response.setContentType(APPLICATION_JSON);
        ObjectMapperRepository.objectMapper.writeValue(response.getWriter(), tasks);
        return null;
    }
}

class TasksMonitoringResponse {
    private Boolean started;
    private String taskClass;
    private Date lastExecutionTime;
    private Date nextExecutionTime;

    TasksMonitoringResponse() {
    }

    public TasksMonitoringResponse(Boolean started, String taskClass, Date lastExecutionTime, Date nextExecutionTime) {
        this.started = started;
        this.taskClass = taskClass;
        this.lastExecutionTime = lastExecutionTime;
        this.nextExecutionTime = nextExecutionTime;
    }

    public Boolean getStarted() {
        return started;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public Date getLastExecutionTime() {
        return lastExecutionTime;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public void setLastExecutionTime(Date lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }
}

