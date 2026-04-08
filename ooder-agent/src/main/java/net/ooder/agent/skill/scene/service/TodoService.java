package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.todo.TodoDTO;

import java.util.List;
import java.util.Map;

public interface TodoService {
    
    PageResult<TodoDTO> listMyTodos(String userId, String status, int pageNum, int pageSize);
    
    PageResult<TodoDTO> listMyTodos(String userId, String status, String type, int pageNum, int pageSize);
    
    List<TodoDTO> listPendingTodos(String userId);
    
    Map<String, Integer> countByType(String userId);
    
    TodoDTO getTodo(String todoId);
    
    boolean acceptTodo(String userId, String todoId);
    
    boolean rejectTodo(String userId, String todoId);
    
    boolean completeTodo(String userId, String todoId);
    
    boolean approveTodo(String userId, String todoId);
    
    boolean createInvitationTodo(String sceneGroupId, String fromUserId, String toUserId, String role);
    
    boolean createDelegationTodo(String sceneGroupId, String fromUserId, String toUserId, String title, Long deadline);
    
    boolean createReminderTodo(String sceneGroupId, String userId, String title, Long deadline);
    
    boolean createActivationTodo(String userId, String installId, String capabilityId, String capabilityName);
    
    boolean createApprovalTodo(String sceneGroupId, String fromUserId, String toUserId, String title, String description);
    
    boolean createSceneNotificationTodo(String sceneGroupId, String userId, String title, String description);
    
    boolean deleteTodo(String todoId);
}
