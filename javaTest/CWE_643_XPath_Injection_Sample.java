package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;

@RestController
public class CWE_643_XPath_Injection_Sample {

    // 임의의 XML 데이터 (실무의 사용자 정보 DB XML 파일이나 설정 파일 대용)
    private static final String XML_DATA = 
        "<users><user><username>admin</username><password>Secret123!</password></user>" +
        "<user><username>guest</username><password>guest123</password></user></users>";

    // 1. 외부 입력값을 XPath 쿼리 문자열에 직접 결합하여 인증이나 조회를 수행하는 취약한 패턴 (CWE-643)
    @GetMapping("/api/xpath/login")
    public String loginWithXPathUnsafe(@RequestParam("user") String username, @RequestParam("pass") String password) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(XML_DATA.getBytes()));

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            // [취약점 포인트] 사용자가 입력한 username과 password가 XPath 쿼리 문자열에 검증 없이 직접 결합됨
            // 공격자가 username에 "' or '1'='1" 같은 XPath 인젝션 페이로드를 입력하면 
            // 조건문이 무조건 참이 되어 비밀번호 검증 없이 관리자 계정으로 로그인이 우회될 수 있음
            String expression = "/users/user[username='" + username + "' and password='" + password + "']/username/text()";
            
            XPathExpression expr = xpath.compile(expression);
            NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            if (nl.getLength() > 0) {
                return "Login Success for user: " + nl.item(0).getNodeValue();
            } else {
                return "Login Failed";
            }
            
        } catch (Exception e) {
            return "XPath Error: " + e.getMessage();
        }
    }
}