package com.handge.housingfund.test;

import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.trigger.IPersitListener;
import com.handge.housingfund.statemachine.trigger.IStateMachineListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.state.State;

/**
 * Created by xuefei_wang on 17-7-11.
 */
public class StateMachineRespositoryTest {
//    @Autowired
//    IStateMachineService stateMachineService;
//
//    public IStateMachineService getStateMachineService() {
//        return stateMachineService;
//    }

    public static void main(String[] args) throws Exception {





//        ApplicationContext ac =new ClassPathXmlApplicationContext("application-context.xml");
//        //添加业务类型
//        Scanner sc = new Scanner(System.in);
//        IStateMachineService stateMachineService =  (IStateMachineService)ac.getBean("stateMachineServiceV2");
//
//        Arrays.asList(ac.getBeanDefinitionNames()).stream().forEach(s -> System.out.println("names: " + s));
//
//        /**
//         * 状态机的监听器 和 持久化 可根据自己需求设置
//         *
//         */
//        //根据自己的需求，自定义的监听器，在调用之前，添加到服务中
//        stateMachineService.registerStateMachineListener(new TestListener());
//        //根据自己的需求，自定义的持久化操作，在调用之前，添加到服务中
//        stateMachineService.registerPersistHandler(new TestPersist());
////        stateMachineService.setUp(BusinessType.Collection,"GJ1");
//        TaskEntity taskEntity = new TaskEntity();
//        while (true){
////            stateMachineService.setUp(BusinessType.Collection, subtype);
//            System.out.println("业务Id:");
//            String td = sc.nextLine();
//            System.out.println("输入当前状态:");
//            String state = sc.nextLine();
//            System.out.println("输入事件:");
//            String event = sc.nextLine();
//            taskEntity.setTaskId(td == null || td .equals("") ? taskEntity.getTaskId() : td);
//            taskEntity.setNote("通过");
//            taskEntity.setStatus(state);
//            taskEntity.setSubtype("TEST");
//            taskEntity.setType(BusinessType.WithDrawl);
//            System.out.println("　输入操作类型：　1 AUDIT     2 ALL    3 ORDINARY");
//            int s = sc.nextInt();
//            if(s == 1) taskEntity.setRole("审核员A");
//            else if(s == 2) taskEntity.setRole("Admin");
//            taskEntity.setOperator("JimTom");
//            taskEntity.setWorkstation("成都市公积金中心");
//            boolean r2 = false;
//            try{
//                r2= stateMachineService.handle(event, taskEntity);
//            }catch (Exception e){
//                e.printStackTrace();
//                System.out.println(">>>>> 获取到异常信息: " + e.getMessage());
//            }
//            System.out.println(r2);
//            System.out.println("测试： 持久层是否执行成功： " + TestPersist.flag );
//        }
    }
}
class TestListener extends IStateMachineListener {

    /**
     * 状态改变实施监听
     *
     * @param form 当前状态
     * @param to   　目标状态
     */
    @Override
    public void afterStateChanged(String form, String to) {
        System.out.println(">>>>>>>>  afterStateChanged : start......");
    }

    /**
     * 状态进入时，执行
     *
     * @param state 目标状态
     */
    @Override
    public void stateEntered(String state) {

        System.out.println(">>>>>>>>  stateEntered : start......");
    }

    /**
     * 状态退出时，执行
     *
     * @param state 原状态
     */
    @Override
    public void stateExited(String state) {

        System.out.println(">>>>>>>>  stateExited : start......");
    }

    /**
     * 触发的时间不被接受时，状态不能改变成功，执行
     *
     * @param event
     * @param headers
     */
    @Override
    protected void eventNotAccepted(String event, MessageHeaders headers) {

        System.out.println(">>>>>>>>  eventNotAccepted : start......" + headers);
    }
}
class TestPersist extends IPersitListener{
    @Override
    public void persist(State<String, String> state, String event, TaskEntity taskEntity) {

        /**
         *  you can update database
         */
        System.out.println("*************************** 自定义 IPersitListener ***********************************");
        System.out.println("  改变后的状态：state: " + state.getId() +"    触发的事件： event:  " + event + "    taskEntity:  " + taskEntity);
        System.out.println("**** taskEntity:  " + taskEntity);
        String s = null;
        try{
            s.equals("s");
        }catch (Exception e){
            System.out.println(e);
            flag = false;
        }
        if(flag){
            System.out.println("更新成功。。");
        }else{
            System.out.println("更新失败");
        }

        System.out.println("**************************************************************************************");
    }

    /**
     * 更新状态
     */
    public static volatile boolean flag = false;
}

