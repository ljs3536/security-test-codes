package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import net.sf.saxon.xqj.SaxonXQDataSource;

@RestController
public class CWE_652_XQuery_Injection_Sample {

    // 1. 외부 입력값을 동적으로 문자열 결합하여 XQuery 문을 생성하는 취약한 패턴 (CWE-652)
    @GetMapping("/api/xquery/search")
    public String searchBookByAuthorUnsafe(@RequestParam("author") String authorName) {
        StringBuilder resultXml = new StringBuilder();
        
        try {
            XQDataSource xqds = new SaxonXQDataSource();
            XQConnection conn = xqds.getConnection();
            
            // [취약점 포인트] 사용자가 입력한 authorName이 XQuery 쿼리 문자열에 검증 없이 직접 결합됨
            // 공격자가 조작된 XQuery 구문(' or 1=1 or '...)을 입력하면 XML 데이터베이스의 전체 정보가 유출될 수 있음
            String xqueryString = "doc('books.xml')/catalog/book[author='" + authorName + "']";
            
            XQExpression xqexp = conn.createExpression();
            XQResultSequence result = xqexp.executeCommand(xqueryString); // 또는 executeQuery
            
            while (result.next()) {
                resultXml.append(result.getItemAsString(null));
            }
            
            conn.close();
        } catch (Exception e) {
            return "XQuery Error: " + e.getMessage();
        }
        
        return resultXml.toString();
    }
}