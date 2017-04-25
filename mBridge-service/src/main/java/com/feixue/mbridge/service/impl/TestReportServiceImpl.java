package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.TestReportDao;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.domain.report.TestReportVO;
import com.feixue.mbridge.service.SystemService;
import com.feixue.mbridge.service.TestReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/4/30.
 */
@Service("testReportService")
public class TestReportServiceImpl implements TestReportService {

    private static final Logger logger = LoggerFactory.getLogger(TestReportServiceImpl.class);

    @Resource
    private TestReportDao testReportDao;

    @Resource
    private SystemService systemService;

    @Override
    public boolean addTestReport(final TestReportDO testReportDO) {
        return testReportDao.addTestReport(testReportDO);
    }

    @Override
    public TablePageVO<List<TestReportVO>> getClientHistoryPage(long protocolId, long pageStart, int length) {
        long size = testReportDao.getClientHistorySize(protocolId);
        List<TestReportDO> reportDOList = testReportDao.getClientHistoryPageInfo(protocolId, pageStart, length);

        List<TestReportVO> reportVOList = new ArrayList<>();
        for(TestReportDO reportDO : reportDOList) {
            reportVOList.add(new TestReportVO(reportDO, systemService.querySystem(reportDO.getSystemCode())));
        }
        return new TablePageVO<>(reportVOList, size);
    }

    @Override
    public TablePageVO<List<TestReportVO>> getServerHistoryPage(long protocolId, long pageStart, int length) {
        long size = testReportDao.getServerHistorySize(protocolId);
        List<TestReportDO> data =  testReportDao.getServerHistoryPageInfo(protocolId, pageStart, length);

        List<TestReportVO> reportVOList = new ArrayList<>();
        for(TestReportDO reportDO : data) {
            TestReportVO testReportVO = new TestReportVO(reportDO, systemService.querySystem(reportDO.getSystemCode()));
            reportVOList.add(testReportVO);
        }

        TablePageVO<List<TestReportVO>> tablePageVO = new TablePageVO<>(reportVOList, size);

        return tablePageVO;
    }

    @Override
    public TestReportVO getServerLastReport(long protocolId) {
        TestReportDO testReportDO =  testReportDao.getTestReportLast(protocolId, TestReportDO.TestType.server.getType());
        if (testReportDO == null) {
            return null;
        }

        return new TestReportVO(testReportDO, systemService.querySystem(testReportDO.getSystemCode()));
    }
}
