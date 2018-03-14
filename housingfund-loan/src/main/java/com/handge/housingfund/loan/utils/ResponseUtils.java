package com.handge.housingfund.loan.utils;

import com.google.common.base.Throwables;
import com.handge.housingfund.common.service.enumeration.ErrorEnumeration;
import com.handge.housingfund.common.service.loan.model.Error;
import com.handge.housingfund.common.service.loan.model.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;

/*
 * Created by lian on 2017/7/12.
 */
@SuppressWarnings({"WeakerAccess", "SingleStatementInBlock", "ConstantConditions"})
public abstract class ResponseUtils {

    /*
    public static Response wrapEntityIfNeeded(ResponseEntity responseEntity) {

        if(responseEntity==null||(responseEntity.getEntity()==null&&responseEntity.getError()==null)){

            return Response.status(200).build();
        }

        if(responseEntity.getError()!=null){

            return Response.status(200).entity(responseEntity.getError()).build();
        }

        if(responseEntity.getEntity()!=null){

            return Response.status(200).entity(new ResponseWrapper<Object>(responseEntity.getEntity())).build();
        }

        return Response.status(200).build();

//        if (!response.hasEntity()) { return response; }
//
//        ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(response.getEntity());
//
//        try {
//
//            Class currentclass = response.getEntity().getClass();
//
//            System.out.println("======================>" + response.getEntity());
//
//            while (true) {
//
//                boolean shouldBreak = false;
//
//                for (Field field : currentclass.getDeclaredFields()) {
//
//
//                    if (field.getName().contains("$")) {
//
//                        currentclass = currentclass.getSuperclass();
//
//                        break;
//
//                    } else {
//
//
//                        for (Field realField : currentclass.getDeclaredFields()) {
//
//                            if (realField.getName().equals("Res") && realField.getType() == ArrayList.class) {
//
//                                realField.setAccessible(true);
//
//                                Object o = realField.get(response.getEntity());
//
//                                wrapper = new ResponseWrapper<Object>(o);
//                            }
//                        }
//                    }
//
//                    shouldBreak = true;
//                }
//
//                if(currentclass.getDeclaredFields().length == 0){
//
//                    shouldBreak = true;
//                }
//
//                if (shouldBreak) {
//                    break;
//                }
//            }
//
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//        Field responseField = null;
//
//        try {
//
//            Field[] fields = Class.forName(response.getClass().getName()).getDeclaredFields();
//
//            for (Field field : fields) {
//
//                if (field.getName().equals("entity")) {
//
//                    responseField = field;
//                }
//
//            }
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//        if (responseField == null) {
//
//            return response;
//        }
//
//
//        try {
//
//            responseField.setAccessible(true);
//
//            responseField.set(response, wrapper);
//
//        } catch (IllegalAccessException e) {
//
//            return response;
//        }
//
//        return response;


    }

//    public static<T> T wrapEntityIfNeeded(ResponseEntity<T> responseEntity) {
//
//        return responseEntity.getEntity();
//    }
   */
    public static ResponseEntity<Error> buildParametersErrorResponse() {

        return buildCommonErrorResponse(ErrorEnumeration.Parameter_NOT_MATCH, "请检查参数");
    }

    public static ResponseEntity<Error> buildParametersFormatErrorResponse() {

        return buildCommonErrorResponse(ErrorEnumeration.Parameter_NOT_MATCH, "请检查参数格式");

    }

    public static ResponseEntity<Error> buildExceptionErrorResponse(Exception exception) {

        if (exception != null) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        Throwable lastException = Throwables.getRootCause(exception);

        return ResponseUtils.buildCommonErrorResponse(ErrorEnumeration.Program_UNKNOW_ERROR, lastException.getMessage());
//        return Response.status(200).entity(new Error(){{
//
//
//
//            this.setCode(lastException.hashCode() + "");
//
//            this.setMsg(lastException.getMessage());
//
//        }}).build();
    }

    public static ResponseEntity<Error> buildExceptionErrorResponse(ArrayList<Exception> exceptions) {

        return exceptions.size() == 0 ? new ResponseEntity<Error>(null) : ResponseUtils.buildExceptionErrorResponse(exceptions.get(0));
    }

    public static ResponseEntity<Error> buildCommonErrorResponse(ErrorEnumeration errorEnumeration, String content, boolean rollback) {

        if (rollback) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new ResponseEntity<Error>(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + content);

        }});

    }

    public static ResponseEntity<Error> buildCommonErrorResponse(ErrorEnumeration errorEnumeration, boolean rollback) {

        if (rollback) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new ResponseEntity<Error>(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + "");

        }});

    }

    public static ResponseEntity<Error> buildCommonErrorResponse(ErrorEnumeration errorEnumeration, String content) {


        return new ResponseEntity<Error>(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + content);

        }});

    }

    public static ResponseEntity<Error> buildCommonErrorResponse(ErrorEnumeration errorEnumeration) {

        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        //TransactionAspectSupport.currentTransactionStatus().isNewTransaction()
        return new ResponseEntity<Error>(new Error() {{

            this.setCode(errorEnumeration.getCode() + "");

            this.setMsg(errorEnumeration.getMessage() + "");

        }});

    }

    public static <T> ResponseEntity<T> buildCommonEntityResponse(T entity) {
        return new ResponseEntity<>(entity);
    }

    public static ResponseEntity buildCommonResponse(Exception exception, Object entity) {

        if (exception != null) {

            return ResponseUtils.buildExceptionErrorResponse(exception);
        }

        return new ResponseEntity<>(entity);
    }

    public static ResponseEntity buildCommonResponse(ArrayList<Exception> exceptions, Object entity) {

        if (exceptions.size() != 0) {

            return ResponseUtils.buildExceptionErrorResponse(exceptions.get(0));
        }

        return new ResponseEntity<>(entity);
    }

}
