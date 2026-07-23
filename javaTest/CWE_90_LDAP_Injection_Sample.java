package javaTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

@RestController
public class CWE_90_LDAP_Injection_Sample {

    // 1. 외부 입력값을 LDAP 필터 문자열에 직접 결합하여 검색/인증을 수행하는 취약한 패턴 (CWE-90)
    @GetMapping("/api/ldap/search")
    public String searchLdapUserUnsafe(@RequestParam("uid") String userId) {
        StringBuilder results = new StringBuilder();
        
        try {
            // LDAP 서버 연결 설정 (JNDI Context)
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://localhost:389/o=JNDIExample");

            DirContext ctx = new InitialDirContext(env);

            // [취약점 포인트] 사용자가 입력한 uid가 LDAP 검색 필터 문자열에 검증 없이 직접 결합됨
            // 공격자가 "admin)(uid=*)" 또는 "*" 같은 LDAP 메타문자 및 와일드카드를 입력하면
            // 필터 로직이 우회되어 전체 사용자의 정보가 노출되거나 인증이 무력화됨
            String searchFilter = "(&(objectClass=person)(uid=" + userId + "))";

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // LDAP 쿼리 실행 싱크
            NamingEnumeration<SearchResult> answer = ctx.search("ou=users", searchFilter, constraints);

            while (answer.hasMore()) {
                SearchResult sr = answer.next();
                results.append("Found User: ").append(sr.getNameInNamespace()).append("\n");
            }

            ctx.close();
        } catch (Exception e) {
            return "LDAP Error: " + e.getMessage();
        }

        return results.toString();
    }
}