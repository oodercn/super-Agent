package net.ooder.skillcenter.shell;

/**
 * 退出命令，用于退出控制台
 */
public class ExitCommand extends AbstractCommand {
    
    private ShellConsole console;
    
    /**
     * 构造方法
     * @param console 控制台实例
     */
    public ExitCommand(ShellConsole console) {
        this.console = console;
    }
    
    @Override
    public String getName() {
        return "exit";
    }
    
    @Override
    public String getDescription() {
        return "退出控制台";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        output.println("正在退出控制台...");
        console.stop();
    }
}
