package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.adapter.OrgWebAdapter;
import net.ooder.mvp.skill.scene.dto.OrgUserDTO;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.todo.TodoDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.TodoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/my/todos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TodoController {

    private static final Logger log = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @Autowired(required = false)
    private OrgWebAdapter orgWebAdapter;

    private String getCurrentUserId(String userIdHeader) {
        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            return userIdHeader;
        }
        
        if (orgWebAdapter != null) {
            try {
                OrgUserDTO currentUser = orgWebAdapter.getCurrentUser();
                if (currentUser != null && currentUser.getUserId() != null) {
                    return currentUser.getUserId();
                }
            } catch (Exception e) {
                log.warn("[getCurrentUserId] Failed to get current user from org: {}", e.getMessage());
            }
        }
        
        return "current-user";
    }

    @GetMapping
    public ResultModel<PageResult<TodoDTO>> listMyTodos(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        PageResult<TodoDTO> result = todoService.listMyTodos(currentUserId, status, type, pageNum, pageSize);
        return ResultModel.success(result);
    }

    @GetMapping("/pending")
    public ResultModel<List<TodoDTO>> listPendingTodos(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        List<TodoDTO> result = todoService.listPendingTodos(currentUserId);
        return ResultModel.success(result);
    }

    @GetMapping("/count")
    public ResultModel<Map<String, Integer>> countByType(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        Map<String, Integer> result = todoService.countByType(currentUserId);
        return ResultModel.success(result);
    }

    @GetMapping("/{todoId}")
    public ResultModel<TodoDTO> getTodo(@PathVariable String todoId) {
        TodoDTO todo = todoService.getTodo(todoId);
        if (todo == null) {
            return ResultModel.notFound("待办不存在: " + todoId);
        }
        return ResultModel.success(todo);
    }

    @PostMapping("/{todoId}/accept")
    public ResultModel<Boolean> acceptTodo(
            @PathVariable String todoId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        boolean result = todoService.acceptTodo(currentUserId, todoId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "接受失败");
    }

    @PostMapping("/{todoId}/reject")
    public ResultModel<Boolean> rejectTodo(
            @PathVariable String todoId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        boolean result = todoService.rejectTodo(currentUserId, todoId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "拒绝失败");
    }

    @PostMapping("/{todoId}/complete")
    public ResultModel<Boolean> completeTodo(
            @PathVariable String todoId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        boolean result = todoService.completeTodo(currentUserId, todoId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "完成失败");
    }

    @PostMapping("/{todoId}/approve")
    public ResultModel<Boolean> approveTodo(
            @PathVariable String todoId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        String currentUserId = getCurrentUserId(userIdHeader);
        boolean result = todoService.approveTodo(currentUserId, todoId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "审批失败");
    }

    @DeleteMapping("/{todoId}")
    public ResultModel<Boolean> deleteTodo(@PathVariable String todoId) {
        boolean result = todoService.deleteTodo(todoId);
        if (result) {
            return ResultModel.success(true);
        }
        return ResultModel.error(400, "删除失败");
    }
}
