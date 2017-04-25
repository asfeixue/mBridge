package com.feixue.mbridge.util.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SchedulerJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /**
         * 1.获取任务，并执行
         * 2.注册消息通知者，将处理消息通知到notify，用于后续通知等
         */
        BaseJob job = (BaseJob) context.getJobDetail().getJobDataMap().get("job");
        job.execute();
    }

}
