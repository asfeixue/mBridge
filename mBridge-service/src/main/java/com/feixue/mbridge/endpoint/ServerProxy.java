package com.feixue.mbridge.endpoint;

import com.feixue.mbridge.dao.BodyDao;
import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.server.ServerDO;
import com.feixue.mbridge.endpoint.handler.BridgeHandler;
import com.feixue.mbridge.endpoint.handler.MockHandler;
import com.feixue.mbridge.service.BridgeService;
import com.feixue.mbridge.service.TestReportService;
import com.feixue.mbridge.util.job.BaseJob;
import com.feixue.mbridge.util.job.SchedulerManager;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 15/11/8.
 */
@Service
public class ServerProxy implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ServerProxy.class);

    @Resource
    private ProtocolDao protocolDao;

    @Resource
    private BodyDao bodyDao;

    @Resource
    private TestReportService testReportService;

    private static final String CONNECTOR = "connector-";

    private Server server = new Server();

    HandlerCollection collection = new HandlerCollection(true);

    /**
     * 启动指定的server
     * @param serverDO
     * @return
     */
    public BusinessWrapper<Boolean> startServer(ServerDO serverDO) {
        logger.warn("start server for:" + serverDO.toString());

        try {
            while (!server.isStarted()) {
                Thread.sleep(1000);
            }

            MockHandler mockHandler = new MockHandler(serverDO, protocolDao, bodyDao, testReportService);
            mockHandler.setServer(server);
            mockHandler.start();
            collection.addHandler(mockHandler);
            Connector connector = getConnector(serverDO.getServerPort());
            server.addConnector(connector);
            connector.start();

            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serviceFailure.getCode(), e.getMessage());
        }
    }

    /**
     * 关闭指定的server
     * @param serverDO
     * @return
     */
    public BusinessWrapper<Boolean> stopServer(ServerDO serverDO) {
        logger.warn("stop server for:" + serverDO.toString());

        try {
            while (!server.isStarted()) {
                Thread.sleep(1000);
            }

            //停止接受端口请求
            for(Connector connector : server.getConnectors()) {
                if (connector.getName().equals(CONNECTOR + serverDO.getServerPort())) {
                    connector.stop();
                    break;
                }
            }

            //关闭处理此端口数据的handler
            for(Handler handler : collection.getHandlers()) {
                MockHandler mockHandler = (MockHandler) handler;
                if (mockHandler.checkEqual(serverDO)) {
                    handler.stop();
                    break;
                }
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serviceFailure.getCode(), e.getMessage());
        }
    }

    private ServerConnector getConnector(int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        connector.setName(CONNECTOR + port);

        return connector;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.setHandler(collection);
                    server.start();
                    server.join();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }).start();
    }

    @Override
    public void destroy() throws Exception {
        for (Connector connector : server.getConnectors()) {
            connector.stop();
        }

        server.getHandler().stop();
        server.stop();
    }
}
