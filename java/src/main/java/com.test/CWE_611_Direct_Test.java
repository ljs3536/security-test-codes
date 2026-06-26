import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@RestController
public class CWE_611_Direct_Test {

    /**
     * Taint 추적이 끊기지 않도록 중간 객체(InputSource 등)를 생략한 버전
     */
    @GetMapping("/api/parseXmlDirect")
    public String parseXmlDirect(@RequestParam("xmlUri") String xmlUri) throws Exception {
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        // 보안 설정(Feature) 누락됨
        
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        
        // [핵심 테스트 포인트] 외부 입력(xmlUri)이 어떤 래핑도 없이 그대로 parse()의 인자로 들어감
        // 분석기가 이것도 못 잡는다면 builder.parse() 자체가 Sink로 등록 안 되어 있는 것입니다.
        Document doc = builder.parse(xmlUri); 
        
        return "파싱 완료: " + doc.getDocumentURI();
    }
}