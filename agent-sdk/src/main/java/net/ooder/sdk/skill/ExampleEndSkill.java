package net.ooder.sdk.skill;

import net.ooder.sdk.agent.EndAgent;
import java.util.Map;

public class ExampleEndSkill extends AbstractSkill {
    
    @Override
    public boolean start() {
        EndAgent endAgent = (EndAgent) getAgent();
        return endAgent.start().join();
    }
    
    @Override
    public boolean stop() {
        EndAgent endAgent = (EndAgent) getAgent();
        return endAgent.stop().join();
    }
    
    @Override
    public Object execute(Object task) {
        // 执行任务逻辑
        EndAgent endAgent = (EndAgent) getAgent();
        
        // 这里可以使用 endAgent 执行具体任务
        System.out.println("Executing task: " + task);
        System.out.println("Using EndAgent: " + endAgent.getAgentId());
        
        return "Task executed successfully by " + endAgent.getAgentName();
    }
}
