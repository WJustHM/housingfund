package com.handge.housingfund.server;

import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.others.model.Action;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import java.lang.reflect.Method;
import java.util.*;


@Component
@Service
public class NettyServer extends Application {

	@Autowired
	ApplicationContext ac;

    @Autowired
    IActionService actionService;

    String rootResourcePath = "/housingfund";

    int port = 80;

    private final int ioWorkerCount = Runtime.getRuntime().availableProcessors();
    private final int executorThreadCount = Runtime.getRuntime().availableProcessors();
    private SSLContext sslContext = null;
    private final int maxRequestSize = 1024 * 1024 * 250;
    NettyJaxrsServer netty;

    public void start() {
//        updateAction();
        ResteasyDeployment dp = new ResteasyDeployment();
        Collection<Object> controllers = ac.getBeansWithAnnotation(Controller.class).values();
        dp.getResources().addAll(controllers);
        Collection<Object> filters = ac.getBeansWithAnnotation(Service.class).values();
        dp.getProviders().addAll(filters);
        dp.setApplication(new NettyServer());
        netty = new NettyJaxrsServer();
        netty.setDeployment(dp);
        netty.setPort(port);
        netty.setRootResourcePath(rootResourcePath);
        netty.setIoWorkerCount(ioWorkerCount);
        netty.setExecutorThreadCount(executorThreadCount);
        netty.setMaxRequestSize(maxRequestSize);
        netty.setSSLContext(sslContext);
        netty.setKeepAlive(false);
        netty.setSecurityDomain(null);
        netty.start();
    }

//	@Override
//	public Set<Object> getSingletons() {
//		HashSet<Object> objects = new HashSet<Object>();
////		objects.add(new AuthenticationToken());
//		objects.add(new TokenContextProcessFilter());
//		objects.add(new Cors());
//		return objects;
//	}


    private void updateAction() {
        List<Action> actionList = new ArrayList<Action>();
        Map<String, Object> map = ac.getBeansWithAnnotation(Controller.class);
        Set<String> keys = map.keySet();
        for (String string : keys) {
            Path path = ac.findAnnotationOnBean(string, Path.class);
            String strPath = path.value();
            Method[] methods = map.get(string).getClass().getMethods();
            for (Method method : methods) {
                Path subPath = method.getAnnotation(Path.class);
                POST post = method.getAnnotation(POST.class);
                GET get = method.getAnnotation(GET.class);
                DELETE delete = method.getAnnotation(DELETE.class);
                HEAD head = method.getAnnotation(HEAD.class);
                PUT put = method.getAnnotation(PUT.class);
                String httpMethod = null;
                String strSubPath = null;
                if (subPath != null) {
                    strSubPath = subPath.value();
                }
                if (post != null) {
                    httpMethod = "POST";
                } else if (get != null) {
                    httpMethod = "GET";
                } else if (delete != null) {
                    httpMethod = "DELETE";
                } else if (head != null) {
                    httpMethod = "HEAD";
                } else if (put != null) {
                    httpMethod = "PUT";
                }
                if (httpMethod == null || strSubPath == null) {
                    continue;
                }
                Action action = new Action();
                action.setAction_method(httpMethod);
                action.setAction_url(strPath + strSubPath);
                actionList.add(action);
            }
        }
        actionService.update(actionList);
        actionService.updateActionCode();
    }

}
