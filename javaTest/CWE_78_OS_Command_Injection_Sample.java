package javaTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CWE_78_OS_Command_Injection_Sample {

    // 1. Runtime.getRuntime().exec()를 사용한 전형적인 명령어 삽입 (CWE-78)
    public String executePingUnsafe(String ipAddress) {
        StringBuilder output = new StringBuilder();
        try {
            // [취약점 포인트] 사용자가 입력한 ipAddress가 시스템 명령어 문자열에 그대로 결합됨
            // 공격자가 "127.0.0.1; cat /etc/passwd" 또는 "8.8.8.8 & net user" 형태를 입력하면
            // 파이프(|), 세미콜론(;), 앰퍼샌드(&) 등의 쉘 메타문자가 해석되어 추가 악성 명령어가 실행됨
            String command = "ping -c 3 " + ipAddress;
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Command execution error: " + e.getMessage());
        }
        return output.toString();
    }

    // 2. ProcessBuilder를 사용했지만 쉘(sh/cmd)을 통해 인자를 넘기는 취약한 패턴
    public void executeProcessBuilderUnsafe(String userInput) {
        try {
            // [취약점 포인트] ProcessBuilder를 사용하여 인자를 리스트로 분리하더라도,
            // 쉘 프로그램(sh, cmd)의 인자("-c", "/c")로 외부 입력값을 포함한 전체 문자열을 넘기면
            // 여전히 쉘 메타문자가 해석되어 명령어 삽입이 발생함
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("sh", "-c", "ls -l " + userInput));
            pb.start();
        } catch (Exception e) {
            System.err.println("ProcessBuilder error: " + e.getMessage());
        }
    }
}
