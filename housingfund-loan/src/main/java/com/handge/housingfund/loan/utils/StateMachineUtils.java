package com.handge.housingfund.loan.utils;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.HashMap;

public class StateMachineUtils {

    @SuppressWarnings({"NonAsciiCharacters"})
    public enum Operation {
        //        房开申请受理
        房开申请_初始状态_保存("初始状态->(保存)", "LoanFKSQSL", "初始状态", "保存"),
        房开申请_初始状态_提交("初始状态->(提交)", "LoanFKSQSL", "初始状态", "提交"),
        房开申请_新建_提交("新建->(提交)", "LoanFKSQSL", "新建", "提交"),
        房开申请_新建_保存("新建->(保存)", "LoanFKSQSL", "新建", "保存"),
        房开申请_待审核_撤回("待审核->(撤回)", "LoanFKSQSL", "待审核", "撤回"),
        房开申请_待审核_通过("待审核->(通过)", "LoanFKSQSL", "待审核", "通过"),
        房开申请_待审核_不通过("待审核->(不通过)", "LoanFKSQSL", "待审核", "不通过"),
        //        房开申请_待审核_打开("待审核->(打开)","LoanFKSQSL","待审核","打开"),
//        房开申请_审核中_审核不通过("审核中->(审核不通过)","LoanFKSQSL","待审核","审核不通过"),
        房开申请_审核不通过_提交("审核不通过->(提交)", "LoanFKSQSL", "审核不通过", "提交"),
        //        房开申请_审核中_通过("审核中->(通过)","LoanFKSQSL","审核中","通过"),
//        房开申请_审核中_关闭("审核中->(关闭)","LoanFKSQSL","审核中","关闭"),
        房开申请_审核不通过_保存("审核不通过->(保存)", "LoanFKSQSL", "审核不通过", "保存"),
        房开申请_办结_结束("办结->(结束)", "LoanFKSQSL", "办结", "结束"),
        //        房开变更
        房开变更_初始状态_保存("初始状态->(保存)", "LoanFKBGSL", "初始状态", "保存"),
        房开变更_初始状态_提交("初始状态->(提交)", "LoanFKBGSL", "初始状态", "提交"),
        房开变更_新建_提交("新建->(提交)", "LoanFKBGSL", "新建", "提交"),
        房开变更_新建_保存("新建->(保存)", "LoanFKBGSL", "新建", "保存"),
        房开变更_待审核_撤回("待审核->(撤回)", "LoanFKBGSL", "待审核", "撤回"),
        房开变更_待审核_通过("待审核->(通过)", "LoanFKBGSL", "待审核", "通过"),
        房开变更_待审核_不通过("待审核->(不通过)", "LoanFKBGSL", "待审核", "不通过"),
        //        房开变更_审核中_不通过("待审核->(不通过)","LoanFKBGSL","待审核","不通过"),
        房开变更_审核不通过_提交("审核不通过->(提交)", "LoanFKBGSL", "审核不通过", "提交"),
        房开变更_审核不通过_保存("审核不通过->(保存)", "LoanFKBGSL", "审核不通过", "保存"),
        房开变更_办结_结束("办结->(结束)", "LoanFKBGSL", "办结", "结束"),
        //        楼盘申请
        楼盘申请_初始状态_保存("初始状态->(保存)", "LoanLPSQSL", "初始状态", "保存"),
        楼盘申请_初始状态_提交("初始状态->(提交)", "LoanLPSQSL", "初始状态", "提交"),
        楼盘申请_新建_提交("新建->(提交)", "LoanLPSQSL", "新建", "提交"),
        楼盘申请_新建_保存("新建->(保存)", "LoanLPSQSL", "新建", "保存"),
        楼盘申请_待审核_撤回("待审核->(撤回)", "LoanLPSQSL", "待审核", "撤回"),
        楼盘申请_待审核_不通过("待审核->(不通过)", "LoanLPSQSL", "待审核", "不通过"),
        楼盘申请_待审核_通过("待审核->(通过)", "LoanLPSQSL", "待审核", "通过"),
        楼盘申请_审核不通过_修改后提交("审核不通过->(提交)", "LoanLPSQSL", "审核不通过", "提交"),
        楼盘申请_已入账分摊_结束("办结->(结束)", "LoanLPSQSL", "办结", "结束"),
        //         楼盘变更
        楼盘变更_初始状态_保存("初始状态->(保存)", "LoanLPBGSL", "初始状态", "保存"),
        楼盘变更_初始状态_提交("初始状态->(提交)", "LoanLPBGSL", "初始状态", "提交"),
        楼盘变更_新建_提交("新建->(提交)", "LoanLPBGSL", "新建", "提交"),
        楼盘变更_新建_保存("新建->(保存)", "LoanLPBGSL", "新建", "保存"),
        楼盘变更_待审核_撤回("待审核->(撤回)", "LoanLPBGSL", "待审核", "撤回"),
        楼盘变更_待审核_不通过("待审核->(不通过)", "LoanLPBGSL", "待审核", "不通过"),
        楼盘变更_待审核_通过("待审核->(通过)", "LoanLPBGSL", "待审核", "通过"),
        楼盘变更_审核不通过_修改后提交("审核不通过->(提交)", "LoanLPBGSL", "审核不通过", "提交"),
        楼盘变更_已入账分摊_结束("办结->(结束)", "LoanLPBGSL", "办结", "结束"),
        //        个人贷款申请
        个人贷款申请_初始状态_保存("初始状态->(保存)", "LoanGRDKSQ", "初始状态", "保存"),
        个人贷款申请_初始状态_提交("初始状态->(提交)", "LoanGRDKSQ", "初始状态", "提交"),
        个人贷款申请_新建_保存("新建->(保存)", "LoanGRDKSQ", "新建", "保存"),
        个人贷款申请_新建_提交("新建->(提交)", "LoanGRDKSQ", "新建", "提交"),
        个人贷款申请_待审核_撤回("待审核->(撤回)", "LoanGRDKSQ", "待审核", "撤回"),
        个人贷款申请_待审核_通过("待审核->(通过)", "LoanGRDKSQ", "待审核", "通过"),
        个人贷款申请_待签合同_作废("待签合同->(作废)", "LoanGRDKSQ", "待签合同", "作废"),
        个人贷款申请_待签合同_签订("待签合同->(签订)", "LoanGRDKSQ", "待签合同", "签订"),
        个人贷款申请_待放款_放款("待放款->(放款)", "LoanGRDKSQ", "待放款", "放款"),
        个人贷款申请_已发放_确认("已发放->(确认)", "LoanGRDKSQ", "已发放", "确认"),
        个人贷款申请_已发放_失败("已发放->(失败)", "LoanGRDKSQ", "已发放", "失败"),
        个人贷款申请_待审核_不通过("待审核->(不通过)", "LoanGRDKSQ", "待审核", "不通过"),
        个人贷款申请_审核不通过_保存("审核不通过->(保存)", "LoanGRDKSQ", "审核不通过", "保存"),
        个人贷款申请_审核不通过_提交("审核不通过->(提交)", "LoanGRDKSQ", "审核不通过", "提交"),
        个人贷款申请_已到账_结束("已到账->(结束)", "LoanGRDKSQ", "已到账", "结束"),
        //         个人还款申请
        个人还款申请_初始状态_保存("初始状态->(保存)", "LoanGRHKSQ", "初始状态", "保存"),
        个人还款申请_初始状态_提交("初始状态->(提交)", "LoanGRHKSQ", "初始状态", "提交"),
        个人还款申请_新建_提交("新建->(提交)", "LoanGRHKSQ", "新建", "提交"),
        个人还款申请_待审核_撤回("待审核->(撤回)", "LoanGRHKSQ", "待审核", "撤回"),
        个人还款申请_待审核_通过("待审核->(通过)", "LoanGRHKSQ", "待审核", "通过"),
        个人还款申请_待入账_通过("待入账->(通过)", "LoanGRHKSQ", "待入账", "通过"),
        个人还款申请_待入账_失败("待入账->(失败)", "LoanGRHKSQ", "待入账", "失败"),
        个人还款申请_待审核_不通过("待审核->(不通过)", "LoanGRHKSQ", "待审核", "不通过"),
        个人还款申请_审核不通过_保存("审核不通过->(保存)", "LoanGRHKSQ", "审核不通过", "保存"),
        个人还款申请_审核不通过_提交("审核不通过->(提交)", "LoanGRHKSQ", "审核不通过", "提交"),
        个人还款申请_已入账_结束("已入账->(结束)", "LoanGRHKSQ", "已入账", "结束");


        private String type;

        private String from;

        //private String to;

        private String operation;

        private String description;

        Operation(String description, String type, String from, String operation) {
            this.type = type;
            this.from = from;
            // this.to = to;
            this.operation = operation;
            this.description = description;

        }

        public String getType() {
            return type;
        }



        public String getFrom() {
            return from;
        }


        public String getOperation() {
            return operation;
        }

        public String getDescription() {
            return description;
        }




    }

    public interface StateChangeHandler {

        void onStateChange(boolean succeed, String next, Exception e);
    }

    public static void updateState(IStateMachineService stateMachineService, String event, TaskEntity taskEntity, StateChangeHandler handler){

        try {

            HashMap map = new HashMap<String,Object>();

            stateMachineService.registerPersistHandler(new PersistStateMachineHandler.PersistStateChangeListener() {

                @Override
                public void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
                    map.put("state",state);
                    map.put("message",message);
                    map.put("transition",transition);
                    map.put("stateMachine",stateMachine);
                }
            });

            if(!stateMachineService.handle(event,taskEntity)){

                handler.onStateChange(false,null,new ErrorException(ReturnEnumeration.Authentication_Failed,"状态转换失败"));

            }else {



                State<String, String> state = (State<String, String>) map.get("state");

                Message<String> message = (Message<String>) map.get("message");

                if (message!=null&&message.getPayload().equals(event)) {

                    if (state == null || state.getId() == null) {
                        handler.onStateChange(false,null,new ErrorException(ReturnEnumeration.Data_MISS,"业务状态"));
                        return;
                    }
                    handler.onStateChange(true, state.getId(), null);

                    if(taskEntity.getSubtype().equals("DKGRSQ")){

                        System.err.println("ywlsh"+ taskEntity.getTaskId() + ", status:" + taskEntity.getStatus() + ", event" +event + ", next:"+state.getId());
                    }
                }else {
                    handler.onStateChange(false, null, new ErrorException(ReturnEnumeration.StateMachineConfig_Retryable_Error,"状态转换失败"));
                }


            }
        } catch (Exception e) {

            handler.onStateChange(false, taskEntity.getStatus(), e);

            e.printStackTrace();
        }
    }
}
