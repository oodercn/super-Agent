package net.ooder.nexus.service.collaboration;

import net.ooder.nexus.domain.collaboration.model.CollaborationScene;
import net.ooder.nexus.domain.collaboration.model.SceneMember;

import java.util.List;
import java.util.Map;

/**
 * 协作场景服务接口
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
public interface CollaborationService {

    /**
     * 创建场景
     *
     * @param name        场景名称
     * @param description 场景描述
     * @param ownerId     所有者ID
     * @param ownerName   所有者名称
     * @param skillIds    技能ID列表
     * @return 创建的场景
     */
    CollaborationScene createScene(String name, String description, String ownerId, String ownerName, List<String> skillIds);

    /**
     * 获取场景列表
     *
     * @param userId   用户ID
     * @param status   状态过滤（可选）
     * @param page     页码
     * @param pageSize 每页大小
     * @return 场景列表
     */
    List<CollaborationScene> getSceneList(String userId, String status, int page, int pageSize);

    /**
     * 获取场景详情
     *
     * @param sceneId 场景ID
     * @return 场景详情
     */
    CollaborationScene getSceneDetail(String sceneId);

    /**
     * 更新场景
     *
     * @param sceneId     场景ID
     * @param name        名称
     * @param description 描述
     * @param skillIds    技能ID列表
     * @return 更新后的场景
     */
    CollaborationScene updateScene(String sceneId, String name, String description, List<String> skillIds);

    /**
     * 删除场景
     *
     * @param sceneId  场景ID
     * @param operatorId 操作者ID
     * @return 是否成功
     */
    boolean deleteScene(String sceneId, String operatorId);

    /**
     * 添加成员
     *
     * @param sceneId    场景ID
     * @param memberId   成员ID
     * @param memberName 成员名称
     * @param role       角色
     * @param operatorId 操作者ID
     * @return 是否成功
     */
    boolean addMember(String sceneId, String memberId, String memberName, String role, String operatorId);

    /**
     * 移除成员
     *
     * @param sceneId    场景ID
     * @param memberId   成员ID
     * @param operatorId 操作者ID
     * @return 是否成功
     */
    boolean removeMember(String sceneId, String memberId, String operatorId);

    /**
     * 获取成员列表
     *
     * @param sceneId 场景ID
     * @return 成员列表
     */
    List<SceneMember> getMembers(String sceneId);

    /**
     * 生成场景密钥
     *
     * @param sceneId    场景ID
     * @param operatorId 操作者ID
     * @return 新密钥
     */
    String generateSceneKey(String sceneId, String operatorId);

    /**
     * 更新场景状态
     *
     * @param sceneId    场景ID
     * @param status     新状态
     * @param operatorId 操作者ID
     * @return 是否成功
     */
    boolean updateStatus(String sceneId, String status, String operatorId);

    /**
     * 获取场景总数
     *
     * @param userId 用户ID
     * @return 总数
     */
    int getSceneCount(String userId);
}
