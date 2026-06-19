import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;

@RestController
public class CWE_611_XXE {

    // 분석기 테스트 포인트:
    // Source: @RequestBody String xmlString
    // Sink: builder.parse() 실행 전에 외부 엔티티 비활성화 설정이 없음을 감지해야 함
    @PostMapping("/parse-xml")
    public String parseXml(@RequestBody String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            // 취약점: 아래와 같은 보안 설정(Feature)들이 주석 처리되거나 누락되어 있음 (VULNERABLE)
            // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            // factory.setFeature("http://xml.org/sax/features/external-general-entities", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // 외부 엔티티가 포함된 악의적 XML 문자열이 그대로 파싱됨
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            
            return "XML Parsed successfully. Root Element: " + doc.getDocumentElement().getNodeName();
        } catch (Exception e) {
            return "Error parsing XML";
        }
    }
}