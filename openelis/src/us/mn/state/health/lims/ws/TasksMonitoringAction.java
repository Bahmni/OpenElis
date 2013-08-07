package us.mn.state.health.lims.ws;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
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

public class TasksMonitoringAction extends Action {

    private final String APPLICATION_JSON = "application/json";
    private final Logger logger = Logger.getLogger(this.getClass());

    public TasksMonitoringAction() {
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

