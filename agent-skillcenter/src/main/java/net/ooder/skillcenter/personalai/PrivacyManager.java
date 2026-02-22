/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.personalai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Privacy Control Manager - Manages privacy settings for Personal AI Center
 */
public class PrivacyManager {
    private static final Logger logger = LoggerFactory.getLogger(PrivacyManager.class);

    // Current privacy level
    private PrivacyLevel privacyLevel;

    /**
     * Constructor
     */
    public PrivacyManager() {
        this.privacyLevel = PrivacyLevel.MEDIUM;
    }

    /**
     * Start privacy manager
     */
    public void start() {
        logger.info("Privacy Manager started with level: {}", privacyLevel);
    }

    /**
     * Stop privacy manager
     */
    public void stop() {
        logger.info("Privacy Manager stopped");
    }

    /**
     * Get current privacy level
     * @return Current privacy level
     */
    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    /**
     * Set privacy level
     * @param level Privacy level
     */
    public void setPrivacyLevel(PrivacyLevel level) {
        this.privacyLevel = level;
        logger.info("Privacy level set to: {}", level);
    }

    /**
     * Check if data sharing is allowed
     * @param dataType Data type
     * @return Whether data sharing is allowed
     */
    public boolean isDataSharingAllowed(String dataType) {
        switch (privacyLevel) {
            case LOW:
                return true; // Low privacy level, allow all data sharing
            case MEDIUM:
                // Medium privacy level, allow non-sensitive data sharing
                return !isSensitiveDataType(dataType);
            case HIGH:
                return false; // High privacy level, no data sharing allowed
            default:
                return false;
        }
    }

    /**
     * Check if data type is sensitive
     * @param dataType Data type
     * @return Whether it's a sensitive data type
     */
    private boolean isSensitiveDataType(String dataType) {
        String[] sensitiveTypes = {"personal", "financial", "health", "location"};
        for (String type : sensitiveTypes) {
            if (type.equalsIgnoreCase(dataType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if skill sharing is allowed
     * @param skillId Skill ID
     * @return Whether skill sharing is allowed
     */
    public boolean isSkillSharingAllowed(String skillId) {
        switch (privacyLevel) {
            case LOW:
                return true; // Low privacy level, allow all skill sharing
            case MEDIUM:
                return true; // Medium privacy level, allow skill sharing
            case HIGH:
                return false; // High privacy level, no skill sharing allowed
            default:
                return false;
        }
    }

    /**
     * Check if device access is allowed
     * @param deviceId Device ID
     * @return Whether device access is allowed
     */
    public boolean isDeviceAccessAllowed(String deviceId) {
        switch (privacyLevel) {
            case LOW:
                return true; // Low privacy level, allow all device access
            case MEDIUM:
                return true; // Medium privacy level, allow device access
            case HIGH:
                return false; // High privacy level, no device access allowed
            default:
                return false;
        }
    }

    /**
     * Privacy Level Enum
     */
    public enum PrivacyLevel {
        LOW("Low", "Allow most data and skill sharing"),
        MEDIUM("Medium", "Allow non-sensitive data and skill sharing"),
        HIGH("High", "No data or skill sharing allowed");

        private String name;
        private String description;

        PrivacyLevel(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
