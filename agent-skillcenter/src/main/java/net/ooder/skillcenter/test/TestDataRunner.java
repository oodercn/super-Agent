package net.ooder.skillcenter.test;

/**
 * 测试数据运行器，用于执行测试数据生成
 */
public class TestDataRunner {
    
    public static void main(String[] args) {
        System.out.println("开始生成测试技能数据...");
        
        // 生成50个测试技能
        TestDataGenerator.generateTestSkills(50);
        
        System.out.println("测试数据生成完成！");
    }
}
