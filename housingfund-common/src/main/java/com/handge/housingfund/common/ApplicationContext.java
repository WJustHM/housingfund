package com.handge.housingfund.common;


/**
 * Created by xuefei_wang on 17-7-15.
 */
public class ApplicationContext {

//    private static volatile ApplicationContext applicationContext = null;
//
//    private static volatile ClassPathXmlApplicationContext context = null;
//
//    private final Log logger = LogFactory.getLog(ApplicationContext.class.getName());
//
//    private ApplicationContext() throws ConfigurationException {
//        Configuration configuration = Configure.getInstance().getConfiguration();
//        String[] configs = configuration.getStringArray("dubbo.configs");
//        context = new ClassPathXmlApplicationContext(configs);
//        context.start();
//    }
//    public static ApplicationContext getApplicationContext() throws ConfigurationException {
//        if (applicationContext == null){
//            synchronized (ApplicationContext.class){
//                if (null == applicationContext){
//                    applicationContext = new ApplicationContext();
//                }
//            }
//        }
//        return applicationContext;
//    }
//
//    /**
//     *
//     * @param serviceId
//     * @return
//     */
//    public Object getService(String serviceId){
//        if (!context.isRunning()){
//            context.start();
//        }
//        return  context.getBean(serviceId);
//    }
//
//    /**
//     *
//     * @param serviceId
//     * @param args
//     * @return
//     */
//    public Object getService(String serviceId,Object[] args){
//        if (!context.isRunning()){
//            context.start();
//        }
//        return context.getBean(serviceId ,args);
//    }

}
