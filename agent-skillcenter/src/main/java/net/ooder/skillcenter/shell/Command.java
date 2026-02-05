package net.ooder.skillcenter.shell;

/**
 * 命令接口，定义命令的执行方法
 */
public interface Command {
    /**
     * 获取命令名称
     * @return 命令名称
     */
    String getName();
    
    /**
     * 获取命令描述
     * @return 命令描述
     */
    String getDescription();
    
    /**
     * 执行命令
     * @param args 命令参数
     * @throws Exception 执行异常
     */
    void execute(String[] args) throws Exception;
}
