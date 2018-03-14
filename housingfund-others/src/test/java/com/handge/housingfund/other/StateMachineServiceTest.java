package com.handge.housingfund.other;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

/**
 * Created by xuefei_wang on 17-8-8.
 */
public class StateMachineServiceTest {

    public static void main(String[] args) throws IOException {
        // StateMachineServiceImpl stateMachineService = new StateMachineServiceImpl();
        // List<String> types = stateMachineService.listTypes();
        // for(String s : types){
        //     System.out.println(s);
        // }

        try {
            PdfReader reader = new PdfReader("/home/ubuntu/IdeaProjects/housingfund/housingfund-others/src/main/resources/templates/WithdrawlReceiptTemp.pdf");
            System.out.println(reader.getInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
