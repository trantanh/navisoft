package com.trantanh.eet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.trantanh.eet.impl.EndpointType;
import openeet.lite.EetRegisterRequest;

/**
 *
 * @author Tran Tuan Anh, tran.t.anh@email.cz
 */
public class TestEet {

//    public static void main(String[] args) throws IOException{
//
//        String dic="CZ1212121218";
//        String cer="C:\\Users\\tuan\\Dropbox\\NetBeansProjects\\Pokladna\\EET_CA1_Playground-CZ1212121218.p12";
//        String path="C:\\Users\\tuan\\Dropbox\\Project\\Pokladna\\NaviPOS1.1\\src\\com\\trantanh\\eet\\CZ00000019.p12";
//        String dic2="CZ8908234324";
//        String cer2="C:\\Users\\tuan\\Dropbox\\2103838939.p12";
//        InputStream is = new FileInputStream(path);
//
//        try {
//            EetRegisterRequest request = EetRegisterRequest.builder()
//                    .dic_popl(dic2)
//                    .id_provoz("1")
//                    .id_pokl("POKLADNA01")
//                    .porad_cis("1")
//                    .dat_trzby(EetRegisterRequest.formatDate(new Date()))
//                    .celk_trzba(100.0)
//                    .rezim(0)
//                    .pkcs12(EetRegisterRequest.loadStream(is))
//                    .pkcs12password("eet")
//                    .build();
//            //try send
//            String requestBody = request.generateSoapRequest();
//
//            System.out.println("BKP: " + request.formatBkp());
//            System.out.println("PKP: " + request.formatPkp());
//                   System.out.printf("===== BEGIN EET REQUEST =====\n%s\n===== END EET REQUEST =====\n", requestBody);
//
//            String response = request.sendRequest(requestBody, new URL(EndpointType.PRODUCTION.url()));
//
//            if(response.contains("Potvrzeni fik=")){
//                System.out.println("je to TAM!!");
//            }else{
//                System.out.println("neni tam!!");
//            }
//            System.out.printf("===== BEGIN EET RESPONSE =====\n%s\n===== END EET RESPONSE =====\n", response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
