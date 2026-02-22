package net.ooder.nexus.service.group;

import net.ooder.nexus.domain.group.model.GroupMessage;
import net.ooder.nexus.dto.group.MessageListDTO;
import net.ooder.nexus.dto.group.MessageReadDTO;
import net.ooder.nexus.dto.group.MessageSendDTO;

import java.util.List;
import java.util.Map;

/**
 * 群组消息服务接口
 * 
 * <p>提供群组即时消息的发送、接收、历史查询等功能。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface GroupMessageService {

    /**
     * 发送消息
     *
     * @param dto 消息发送 DTO
     * @return 发送的消息
     */
    GroupMessage sendMessage(MessageSendDTO dto);

    /**
     * 获取消息列表
     *
     * @param dto 查询参数
     * @return 消息列表
     */
    List<GroupMessage> getMessageList(MessageListDTO dto);

    /**
     * 获取消息历史
     *
     * @param groupId 群组ID
     * @param beforeTime 时间戳，获取此时间之前的消息
     * @param limit 数量限制
     * @return 消息列表
     */
    List<GroupMessage> getMessageHistory(String groupId, Long beforeTime, Integer limit);

    /**
     * 标记消息已读
     *
     * @param dto 已读标记 DTO
     * @return 操作结果
     */
    boolean markAsRead(MessageReadDTO dto);

    /**
     * 获取未读消息数量
     *
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 未读数量
     */
    int getUnreadCount(String groupId, String userId);

    /**
     * 获取所有群组的未读消息统计
     *
     * @param userId 用户ID
     * @return 群组ID -> 未读数量
     */
    Map<String, Integer> getAllUnreadCounts(String userId);

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean recallMessage(String messageId, String operatorId);

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean deleteMessage(String messageId, String operatorId);

    /**
     * 添加消息监听器
     *
     * @param groupId 群组ID
     * @param listener 监听器
     */
    void addMessageListener(String groupId, MessageListener listener);

    /**
     * 移除消息监听器
     *
     * @param groupId 群组ID
     * @param listener 监听器
     */
    void removeMessageListener(String groupId, MessageListener listener);

    /**
     * 消息监听器接口
     */
    interface MessageListener {
        /**
         * 收到新消息
         *
         * @param message 消息
         */
        void onMessage(GroupMessage message);

        /**
         * 消息状态变更
         *
         * @param messageId 消息ID
         * @param status 新状态
         */
        void onStatusChange(String messageId, String status);

        /**
         * 消息被撤回
         *
         * @param messageId 消息ID
         * @param operatorId 操作者ID
         */
        void onRecall(String messageId, String operatorId);
    }
}
