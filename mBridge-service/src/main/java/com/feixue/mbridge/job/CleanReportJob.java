package com.feixue.mbridge.job;

import com.feixue.mbridge.dao.TestReportDao;
import com.feixue.mbridge.domain.request.MockRequestDO;
import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.service.BodyService;
import com.feixue.mbridge.util.job.BaseJob;
import com.feixue.mbridge.util.job.SchedulerManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 15/11/15.
 */
@Service
public class CleanReportJob implements BaseJob, InitializingBean {

    @Autowired
    private SchedulerManager schedulerManager;

    @Resource
    private BodyService bodyService;

    @Resource
    private TestReportDao testReportDao;

    private static final String cron = "0 0/1 * * * ?";

    @Override
    public void execute() {
        // 定时清除多余的测试报告
        testReportDao.delNoProtcolTestReport();

        for(MockRequestDO mockRequestDO : bodyService.getAllRequestMock()) {
            TestReportDO testReportDO = testReportDao.getTestReportMinId(mockRequestDO);
            if (testReportDO != null) {
                testReportDao.cleanTestReport(testReportDO);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this, cron, CleanReportJob.class.getSimpleName());
    }
}
