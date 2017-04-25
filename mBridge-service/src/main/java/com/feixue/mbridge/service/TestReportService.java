package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.domain.report.TestReportVO;

import java.util.List;

/**
 * Created by zxxiao on 16/4/30.
 */
public interface TestReportService {
    /**
     * 保存测试结果
     * @param testReportDO
     * @return
     */
    boolean addTestReport(TestReportDO testReportDO);

    /**
     * 获取客户端分页数据
     * @param protocolId
     * @param pageStart
     * @param length
     * @return
     */
    TablePageVO<List<TestReportVO>> getClientHistoryPage(long protocolId, long pageStart, int length);

    /**
     * 获取指定协议服务端测试分页数据
     * @param protocolId
     * @param pageStart
     * @param length
     * @return
     */
    TablePageVO<List<TestReportVO>> getServerHistoryPage(long protocolId, long pageStart, int length);

    /**
     * 获取指定协议服务端测试最新测试报告
     * @param protocolId
     * @return
     */
    TestReportVO getServerLastReport(long protocolId);
}
