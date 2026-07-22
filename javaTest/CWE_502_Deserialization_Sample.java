package javaTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class CWE_502_Deserialization_Sample {

    // 1. 신뢰할 수 없는 외부 입력(Base64 문자열 등)을 필터링 없이 그대로 ObjectInputStream으로 역직렬화하는 경우 (CWE-502)
    public Object deserializeDataUnsafe(String base64Payload) {
        try {
            // Base64로 인코딩된 외부 데이터를 바이트 배열로 디코딩
            byte[] decodedBytes = Base64.getDecoder().decode(base64Payload);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
            
            // [취약점 포인트] 외부에서 유입된 바이트 스트림을 검증(Look-ahead 필터링 등) 없이 
            // 곧바로 ObjectInputStream 객체로 생성하여 역직렬화 수행
            // 공격자가 가공된 악성 Gadget Chain 객체를 주입할 경우 시스템이 장악당할 수 있음
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            
            ois.close();
            return obj;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization error: " + e.getMessage());
            return null;
        }
    }
}