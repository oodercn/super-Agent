package net.ooder.skill.auth.service;

import net.ooder.skill.auth.dto.QrcodeDTO;
import net.ooder.skill.auth.dto.QrcodeStatusDTO;

/**
 * 扫码登录服务接口
 */
public interface QrcodeLoginService {
    
    /**
     * 获取二维码
     * @param platform 平台类型（dingding/weixin/feishu）
     * @return 二维码信息
     */
    QrcodeDTO getQrcode(String platform);
    
    /**
     * 检查扫码状态
     * @param qrcodeId 二维码ID
     * @return 扫码状态
     */
    QrcodeStatusDTO checkQrcodeStatus(String qrcodeId);
    
    /**
     * 处理回调
     * @param platform 平台类型
     * @param code 授权码
     * @return 用户信息
     */
    QrcodeStatusDTO handleCallback(String platform, String code);
}
