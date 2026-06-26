import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@RestController
public class CWE_611_Xxe_TestController {
    /**
     * [CWE-611 취약점] 안전하지 않은 XML 파서 설정
     * Taint Source: @RequestBody xmlData
     * Taint Sink: builder.parse()
     */
    @PostMapping("/api/parseXml")
    public String parseXml(@RequestBody String xmlData) {
        try {
            // 파서 팩토리 생성
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            
            // [취약점 핵심] 아래와 같은 XXE 방어 룰(Feature 설정)이 누락됨
            // dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            
            // 외부 입력(xmlData)이 방어 설정이 없는 파서의 Sink로 직접 유입됨
            Document doc = builder.parse(new InputSource(new StringReader(xmlData)));
            
            return "XML 파싱 성공: 최상위 노드 = " + doc.getDocumentElement().getNodeName();
            
        } catch (Exception e) {
            return "파싱 에러: " + e.getMessage();
        }
    }
}
