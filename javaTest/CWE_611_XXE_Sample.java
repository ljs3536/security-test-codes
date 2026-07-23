package javaTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@RestController
public class CWE_611_XXE_Sample {

    // 1. 보안 설정이 누락된 DocumentBuilderFactory를 사용한 취약한 XML 파싱 (CWE-611 / XXE)
    @PostMapping(value = "/api/xml/parse", consumes = "application/xml")
    public String parseXmlUnsafe(@RequestBody String xmlInput) {
        try {
            // [취약점 포인트] 기본 생성된 DocumentBuilderFactory는 외부 DTD 및 Entity 참조를 허용함
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            // 보안 가이드에 명시된 아래와 같은 차단 설정이 누락됨
            // dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            // dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            
            // 공격자가 악성 XML 페이로드에 외부 엔티티를 선언하여 전송하면 
            // (예: <!ENTITY xxe SYSTEM "file:///etc/passwd">) 서버가 이를 파싱하면서 시스템 내부 파일을 읽어 유출함
            Document doc = db.parse(new InputSource(new StringReader(xmlInput)));
            doc.getDocumentElement().normalize();

            return "XML Root Element: " + doc.getDocumentElement().getNodeName();
            
        } catch (Exception e) {
            return "Parsing error: " + e.getMessage();
        }
    }
}