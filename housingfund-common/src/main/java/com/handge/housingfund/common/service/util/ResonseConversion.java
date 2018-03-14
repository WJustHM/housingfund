//package com.handge.housingfund.common.service.util;
//
//import javax.ws.rs.core.Response;
//
//import com.handge.housingfund.common.service.util.ResponseEntity;
//
//public class ResonseConversion {
//
//    public static Response wrapEntityIfNeeded(ResponseEntity responseEntity) {
//
//        if(responseEntity==null||(responseEntity.getEntity()==null)){
//
//            return Response.status(200).build();
//        }
//
//        if(responseEntity.getEntity()!=null){
//
//            return Response.status(200).entity(new ResponseWrapper<Object>(responseEntity.getEntity())).build();
//        }
//
//        return Response.status(200).build();
//
//    }
//}
