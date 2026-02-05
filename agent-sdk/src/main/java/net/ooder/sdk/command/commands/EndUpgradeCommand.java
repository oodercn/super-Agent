package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

/**
 * END_UPGRADE
 * End Agentİ汾
 */
public class EndUpgradeCommand extends Command {
    /**
     * 汾
     */
    @JSONField(name = "targetVersion")
    private String targetVersion;

    /**
     * 
     */
    @JSONField(name = "force")
    private boolean force;

    /**
     * URL
     */
    @JSONField(name = "upgradePackageUrl")
    private String upgradePackageUrl;

    /**
     * 
     */
    @JSONField(name = "restartAfterUpgrade")
    private boolean restartAfterUpgrade;

    /**
     * 
     */
    public EndUpgradeCommand() {
        super(CommandType.END_UPGRADE);
    }

    /**
     * 
     * @param targetVersion 汾
     * @param force 
     * @param upgradePackageUrl URL
     * @param restartAfterUpgrade 
     */
    public EndUpgradeCommand(String targetVersion, boolean force, String upgradePackageUrl, boolean restartAfterUpgrade) {
        super(CommandType.END_UPGRADE);
        this.targetVersion = targetVersion;
        this.force = force;
        this.upgradePackageUrl = upgradePackageUrl;
        this.restartAfterUpgrade = restartAfterUpgrade;
    }

    // GetterSetter
    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getUpgradePackageUrl() {
        return upgradePackageUrl;
    }

    public void setUpgradePackageUrl(String upgradePackageUrl) {
        this.upgradePackageUrl = upgradePackageUrl;
    }

    public boolean isRestartAfterUpgrade() {
        return restartAfterUpgrade;
    }

    public void setRestartAfterUpgrade(boolean restartAfterUpgrade) {
        this.restartAfterUpgrade = restartAfterUpgrade;
    }

    @Override
    public String toString() {
        return "EndUpgradeCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", force=" + force +
                ", upgradePackageUrl='" + upgradePackageUrl + '\'' +
                ", restartAfterUpgrade=" + restartAfterUpgrade +
                '}';
    }
}
















