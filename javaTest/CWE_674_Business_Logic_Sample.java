package javaTest;

import java.util.List;

public class CWE_674_Business_Logic_Sample {

    // 카테고리 노드 구조체
    public static class Category {
        private String name;
        private List<Category> subCategories;

        public List<Category> getSubCategories() {
            return subCategories;
        }
    }

    // 실무형 무한 재귀 취약 메서드 (안전한 탈출 조건이나 방문 노드 체크 부재)
    public void printCategoryTree(Category category, int level) {
        if (category == null) {
            return;
        }

        // 들여쓰기 출력
        for (int i = 0; i < level; i++) {
            System.out.print("--");
        }
        System.out.println(category.name);

        // [취약점 포인트] 만약 데이터베이스나 객체 간 관계에서 순환 참조(A가 B를 가리키고, B가 다시 A를 가리킴)가 발생하거나,
        // 종료 조건/깊이 제한(Max Depth) 검증이 누락된 경우 무한 재귀로 StackOverflowError 유발
        if (category.getSubCategories() != null) {
            for (Category sub : category.getSubCategories()) {
                // 재귀 호출 시 깊이(level)만 증가하고 순환 방어 로직이 없음
                printCategoryTree(sub, level + 1);
            }
        }
    }
}