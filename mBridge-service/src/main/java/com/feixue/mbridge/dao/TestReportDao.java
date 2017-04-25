package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.domain.request.MockRequestDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TestReportDao {

    /**
     * 保存测试结果
     * @param testReportDO
     * @return
     */
    boolean addTestReport(TestReportDO testReportDO);

    /**
     * 获取客户端请求数量
     * @param protocolId
     * @return
     */
    long getClientHistorySize(@Param("protocolId") long protocolId);

    /**
     * 获取客户端请求分页数据
     * @param protocolId
     * @param pageStart
     * @param length
     * @return
     */
    List<TestReportDO> getClientHistoryPageInfo(@Param("protocolId") long protocolId, @Param("pageStart") long pageStart,
                                              @Param("length") int length);

    /**
     * 获取指定协议服务端测试数量
     * @param protocolId
     * @return
     */
    long getServerHistorySize(@Param("protocolId") long protocolId);

    /**
     * 获取指定协议服务端测试分页数据
     * @param protocolId
     * @param pageStart
     * @param length
     * @return
     *
     */
    List<TestReportDO> getServerHistoryPageInfo(@Param("protocolId") long protocolId, @Param("pageStart") long pageStart,
                                              @Param("length") int length);

    /**
     * 删除协议清除后残留的测试报告
     * @return
     */
    int delNoProtcolTestReport();

    /**
     * 获取满足保留条件下的最小测试报告编号
     * @param mockRequestDO
     * @return
     */
    TestReportDO getTestReportMinId(MockRequestDO mockRequestDO);

    /**
     * 清理多余的测试报告
     * @param testReportDO
     * @return
     */
    int cleanTestReport(TestReportDO testReportDO);

    /**
     * 获取指定协议服务端测试最新测试报告
     * @param protocolId
     * @param testType
     * @return
     */
    TestReportDO getTestReportLast(@Param("protocolId") long protocolId, @Param("testType") String testType);
}
