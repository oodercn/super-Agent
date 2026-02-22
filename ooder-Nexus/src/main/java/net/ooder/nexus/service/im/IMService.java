package net.ooder.nexus.service.im;

import net.ooder.nexus.domain.im.model.Conversation;
import net.ooder.nexus.domain.im.model.IMMessage;
import net.ooder.nexus.domain.im.model.Contact;

import java.util.List;
import java.util.Map;

/**
 * 即时通讯服务接口
 * 
 * <p>提供即时通讯核心功能，支持：</p>
 * <ul>
 *   <li>个人对个人消息</li>
 *   <li>个人对部门消息</li>
 *   <li>群组聊天</li>
 * </ul>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface IMService {

    /**
     * 获取会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<Conversation> getConversations(String userId);

    /**
     * 创建会话
     *
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param name 会话名称
     * @return 创建的会话
     */
    Conversation createConversation(String userId, String targetId, String targetType, String name);

    /**
     * 获取会话详情
     *
     * @param conversationId 会话ID
     * @return 会话详情
     */
    Conversation getConversation(String conversationId);

    /**
     * 发送消息
     *
     * @param conversationId 会话ID
     * @param senderId 发送者ID
     * @param senderName 发送者名称
     * @param msgType 消息类型
     * @param content 消息内容
     * @return 发送的消息
     */
    IMMessage sendMessage(String conversationId, String senderId, String senderName, String msgType, String content);

    /**
     * 发送文件消息
     *
     * @param conversationId 会话ID
     * @param senderId 发送者ID
     * @param senderName 发送者名称
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param fileSize 文件大小
     * @return 发送的消息
     */
    IMMessage sendFileMessage(String conversationId, String senderId, String senderName, 
                              String fileName, String filePath, long fileSize);

    /**
     * 获取消息列表
     *
     * @param conversationId 会话ID
     * @param lastMessageId 最后消息ID（用于分页）
     * @param limit 数量限制
     * @return 消息列表
     */
    List<IMMessage> getMessages(String conversationId, String lastMessageId, int limit);

    /**
     * 标记消息已读
     *
     * @param conversationId 会话ID
     * @param userId 用户ID
     * @param lastReadMessageId 最后已读消息ID
     * @return 操作结果
     */
    boolean markAsRead(String conversationId, String userId, String lastReadMessageId);

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读数量统计
     */
    Map<String, Integer> getUnreadCounts(String userId);

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean recallMessage(String messageId, String operatorId);

    /**
     * 获取联系人列表
     *
     * @param userId 用户ID
     * @param contactType 联系人类型
     * @return 联系人列表
     */
    List<Contact> getContacts(String userId, String contactType);

    /**
     * 搜索联系人
     *
     * @param userId 用户ID
     * @param keyword 关键词
     * @return 联系人列表
     */
    List<Contact> searchContacts(String userId, String keyword);

    /**
     * 创建群组
     *
     * @param name 群组名称
     * @param ownerId 群主ID
     * @param ownerName 群主名称
     * @param memberIds 成员ID列表
     * @return 创建的会话
     */
    Conversation createGroup(String name, String ownerId, String ownerName, List<String> memberIds);

    /**
     * 添加群组成员
     *
     * @param conversationId 会话ID
     * @param memberId 成员ID
     * @param memberName 成员名称
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean addGroupMember(String conversationId, String memberId, String memberName, String operatorId);

    /**
     * 移除群组成员
     *
     * @param conversationId 会话ID
     * @param memberId 成员ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    boolean removeGroupMember(String conversationId, String memberId, String operatorId);

    /**
     * 获取群组成员
     *
     * @param conversationId 会话ID
     * @return 成员列表
     */
    List<Contact> getGroupMembers(String conversationId);

    /**
     * 添加消息监听器
     *
     * @param userId 用户ID
     * @param listener 监听器
     */
    void addMessageListener(String userId, MessageListener listener);

    /**
     * 移除消息监听器
     *
     * @param userId 用户ID
     * @param listener 监听器
     */
    void removeMessageListener(String userId, MessageListener listener);

    /**
     * 消息监听器接口
     */
    interface MessageListener {
        void onMessage(IMMessage message);
        void onRead(String conversationId, String userId, String lastReadMessageId);
        void onRecall(String messageId, String operatorId);
    }
}
