package net.ooder.skill.common.spi;

import net.ooder.skill.common.spi.todo.TodoInfo;
import net.ooder.skill.common.spi.todo.TodoStatus;

import java.util.List;

public interface TodoSyncService {
    
    TodoInfo createTodo(TodoInfo todo);
    
    TodoInfo getTodo(String todoId);
    
    List<TodoInfo> listTodos(String userId);
    
    List<TodoInfo> listTodosByStatus(String userId, TodoStatus status);
    
    TodoInfo updateTodo(TodoInfo todo);
    
    TodoInfo completeTodo(String todoId);
    
    void deleteTodo(String todoId);
    
    void syncFromPlatform(String platform);
    
    List<String> getAvailablePlatforms();
}
