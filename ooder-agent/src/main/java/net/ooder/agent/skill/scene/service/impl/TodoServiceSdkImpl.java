package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.todo.TodoDTO;
import net.ooder.mvp.skill.scene.service.TodoService;
import net.ooder.skill.common.storage.JsonStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Primary
public class TodoServiceSdkImpl implements TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoServiceSdkImpl.class);
    
    private static final String COLLECTION_NAME = "todos";
    
    private final Map<String, TodoDTO> allTodos = new ConcurrentHashMap<>();
    private final Map<String, List<TodoDTO>> userTodos = new ConcurrentHashMap<>();
    
    private JsonStorageService jsonStorageService;

    public TodoServiceSdkImpl() {
    }

    @Autowired
    public void setJsonStorageService(JsonStorageService jsonStorageService) {
        this.jsonStorageService = jsonStorageService;
    }

    @PostConstruct
    public void init() {
        reload();
        log.info("TodoServiceSdkImpl initialized with {} todos", allTodos.size());
    }

    public void reload() {
        try {
            List<TodoDTO> todos = jsonStorageService.loadList(COLLECTION_NAME, TodoDTO.class);
            allTodos.clear();
            userTodos.clear();
            
            for (TodoDTO todo : todos) {
                if (todo.getId() != null) {
                    allTodos.put(todo.getId(), todo);
                    
                    String toUser = todo.getToUser();
                    if (toUser != null && !toUser.isEmpty()) {
                        userTodos.computeIfAbsent(toUser, k -> new ArrayList<>()).add(todo);
                    }
                }
            }
            log.info("[reload] Loaded {} todos from storage", allTodos.size());
        } catch (Exception e) {
            log.warn("[reload] Failed to load todos, starting with empty cache: {}", e.getMessage());
        }
    }

    @Override
    public PageResult<TodoDTO> listMyTodos(String userId, String status, int pageNum, int pageSize) {
        return listMyTodos(userId, status, null, pageNum, pageSize);
    }

    @Override
    public PageResult<TodoDTO> listMyTodos(String userId, String status, String type, int pageNum, int pageSize) {
        List<TodoDTO> todos = userTodos.getOrDefault(userId, new ArrayList<>());
        
        if (status != null && !status.isEmpty()) {
            todos = todos.stream()
                .filter(t -> status.equals(t.getStatus()))
                .collect(Collectors.toList());
        }
        
        if (type != null && !type.isEmpty()) {
            todos = todos.stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList());
        }
        
        todos.sort((a, b) -> Long.compare(b.getCreateTime(), a.getCreateTime()));
        
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, todos.size());
        
        List<TodoDTO> pagedTodos = start < todos.size() 
            ? new ArrayList<>(todos.subList(start, end))
            : new ArrayList<>();
        
        PageResult<TodoDTO> result = new PageResult<>();
        result.setList(pagedTodos);
        result.setTotal(todos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public List<TodoDTO> listPendingTodos(String userId) {
        List<TodoDTO> todos = userTodos.getOrDefault(userId, new ArrayList<>());
        return todos.stream()
            .filter(t -> "pending".equals(t.getStatus()))
            .sorted((a, b) -> Long.compare(b.getCreateTime(), a.getCreateTime()))
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> countByType(String userId) {
        List<TodoDTO> todos = userTodos.getOrDefault(userId, new ArrayList<>());
        Map<String, Integer> counts = new HashMap<>();
        
        for (TodoDTO todo : todos) {
            if ("pending".equals(todo.getStatus())) {
                String type = todo.getType();
                counts.put(type, counts.getOrDefault(type, 0) + 1);
            }
        }
        
        return counts;
    }

    @Override
    public TodoDTO getTodo(String todoId) {
        return allTodos.get(todoId);
    }

    @Override
    public boolean acceptTodo(String userId, String todoId) {
        TodoDTO todo = allTodos.get(todoId);
        if (todo == null) {
            return false;
        }
        todo.setStatus("completed");
        todo.setCompletedTime(System.currentTimeMillis());
        persistAllTodos();
        log.info("[acceptTodo] User {} accepted todo {}", userId, todoId);
        return true;
    }

    @Override
    public boolean rejectTodo(String userId, String todoId) {
        TodoDTO todo = allTodos.get(todoId);
        if (todo == null) {
            return false;
        }
        todo.setStatus("completed");
        todo.setCompletedTime(System.currentTimeMillis());
        persistAllTodos();
        log.info("[rejectTodo] User {} rejected todo {}", userId, todoId);
        return true;
    }

    @Override
    public boolean completeTodo(String userId, String todoId) {
        TodoDTO todo = allTodos.get(todoId);
        if (todo == null) {
            return false;
        }
        todo.setStatus("completed");
        todo.setCompletedTime(System.currentTimeMillis());
        persistAllTodos();
        log.info("[completeTodo] User {} completed todo {}", userId, todoId);
        return true;
    }

    @Override
    public boolean approveTodo(String userId, String todoId) {
        TodoDTO todo = allTodos.get(todoId);
        if (todo == null) {
            return false;
        }
        todo.setStatus("completed");
        todo.setCompletedTime(System.currentTimeMillis());
        persistAllTodos();
        log.info("[approveTodo] User {} approved todo {}", userId, todoId);
        return true;
    }

    @Override
    public boolean createInvitationTodo(String sceneGroupId, String fromUserId, String toUserId, String role) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("invitation");
        todo.setTitle(fromUserId + " 邀请您加入场景");
        todo.setSceneGroupId(sceneGroupId);
        todo.setFromUser(fromUserId);
        todo.setToUser(toUserId);
        todo.setRole(role);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("normal");
        
        saveTodo(todo);
        log.info("[createInvitationTodo] Created invitation todo for user {} from {}", toUserId, fromUserId);
        return true;
    }

    @Override
    public boolean createDelegationTodo(String sceneGroupId, String fromUserId, String toUserId, String title, Long deadline) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("delegation");
        todo.setTitle(title);
        todo.setSceneGroupId(sceneGroupId);
        todo.setFromUser(fromUserId);
        todo.setToUser(toUserId);
        todo.setDeadline(deadline);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("high");
        
        saveTodo(todo);
        log.info("[createDelegationTodo] Created delegation todo for user {}", toUserId);
        return true;
    }

    @Override
    public boolean createReminderTodo(String sceneGroupId, String userId, String title, Long deadline) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("reminder");
        todo.setTitle(title);
        todo.setSceneGroupId(sceneGroupId);
        todo.setToUser(userId);
        todo.setDeadline(deadline);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("normal");
        
        saveTodo(todo);
        log.info("[createReminderTodo] Created reminder todo for user {}", userId);
        return true;
    }

    @Override
    public boolean createActivationTodo(String userId, String installId, String capabilityId, String capabilityName) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("activation");
        todo.setTitle("能力待激活: " + capabilityName);
        todo.setDescription("您安装的能力 " + capabilityName + " 需要激活后才能使用");
        todo.setToUser(userId);
        todo.setInstallId(installId);
        todo.setCapabilityId(capabilityId);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("high");
        todo.setActionType("activate");
        
        saveTodo(todo);
        log.info("[createActivationTodo] Created activation todo for user {}", userId);
        return true;
    }

    @Override
    public boolean createApprovalTodo(String sceneGroupId, String fromUserId, String toUserId, String title, String description) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("approval");
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setSceneGroupId(sceneGroupId);
        todo.setFromUser(fromUserId);
        todo.setToUser(toUserId);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("high");
        todo.setActionType("approve");
        
        saveTodo(todo);
        log.info("[createApprovalTodo] Created approval todo for user {}", toUserId);
        return true;
    }

    @Override
    public boolean createSceneNotificationTodo(String sceneGroupId, String userId, String title, String description) {
        TodoDTO todo = new TodoDTO();
        todo.setId("todo-" + UUID.randomUUID().toString().substring(0, 8));
        todo.setType("scene-notification");
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setSceneGroupId(sceneGroupId);
        todo.setToUser(userId);
        todo.setCreateTime(System.currentTimeMillis());
        todo.setStatus("pending");
        todo.setPriority("normal");
        todo.setActionType("view");
        
        saveTodo(todo);
        log.info("[createSceneNotificationTodo] Created notification todo for user {}", userId);
        return true;
    }

    @Override
    public boolean deleteTodo(String todoId) {
        TodoDTO todo = allTodos.remove(todoId);
        if (todo == null) {
            return false;
        }
        
        String toUser = todo.getToUser();
        if (toUser != null) {
            List<TodoDTO> userTodoList = userTodos.get(toUser);
            if (userTodoList != null) {
                userTodoList.removeIf(t -> t.getId().equals(todoId));
            }
        }
        
        persistAllTodos();
        log.info("[deleteTodo] Deleted todo {}", todoId);
        return true;
    }
    
    private void saveTodo(TodoDTO todo) {
        if (todo.getId() != null) {
            allTodos.put(todo.getId(), todo);
        }
        
        String toUser = todo.getToUser();
        if (toUser != null && !toUser.isEmpty()) {
            List<TodoDTO> list = userTodos.computeIfAbsent(toUser, k -> new ArrayList<>());
            boolean exists = list.stream().anyMatch(t -> t.getId().equals(todo.getId()));
            if (!exists) {
                list.add(todo);
            }
        }
        
        persistAllTodos();
    }
    
    private void persistAllTodos() {
        try {
            List<TodoDTO> todos = new ArrayList<>(allTodos.values());
            jsonStorageService.saveList(COLLECTION_NAME, todos);
            log.debug("[persistAllTodos] Saved {} todos", todos.size());
        } catch (Exception e) {
            log.error("[persistAllTodos] Failed to persist todos: {}", e.getMessage());
        }
    }
}
