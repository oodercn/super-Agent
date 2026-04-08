package net.ooder.mvp.skill.scene.capability.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFirstConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CheckStep> selfCheck;
    private List<StartStep> selfStart;
    private DriveConfig selfDrive;
    private List<CollaborationStep> startCollaboration;

    public MainFirstConfig() {
        this.selfCheck = new ArrayList<CheckStep>();
        this.selfStart = new ArrayList<StartStep>();
        this.startCollaboration = new ArrayList<CollaborationStep>();
    }

    public List<CheckStep> getSelfCheck() {
        return selfCheck;
    }

    public void setSelfCheck(List<CheckStep> selfCheck) {
        this.selfCheck = selfCheck;
    }

    public List<StartStep> getSelfStart() {
        return selfStart;
    }

    public void setSelfStart(List<StartStep> selfStart) {
        this.selfStart = selfStart;
    }

    public DriveConfig getSelfDrive() {
        return selfDrive;
    }

    public void setSelfDrive(DriveConfig selfDrive) {
        this.selfDrive = selfDrive;
    }

    public List<CollaborationStep> getStartCollaboration() {
        return startCollaboration;
    }

    public void setStartCollaboration(List<CollaborationStep> startCollaboration) {
        this.startCollaboration = startCollaboration;
    }
    
    public boolean isEmpty() {
        boolean hasSelfCheck = selfCheck != null && !selfCheck.isEmpty();
        boolean hasSelfStart = selfStart != null && !selfStart.isEmpty();
        boolean hasSelfDrive = selfDrive != null 
            && ((selfDrive.getScheduleRules() != null && !selfDrive.getScheduleRules().isEmpty())
                || (selfDrive.getEventRules() != null && !selfDrive.getEventRules().isEmpty())
                || (selfDrive.getCapabilityChains() != null && !selfDrive.getCapabilityChains().isEmpty()));
        boolean hasCollaboration = startCollaboration != null && !startCollaboration.isEmpty();
        
        return !hasSelfCheck && !hasSelfStart && !hasSelfDrive && !hasCollaboration;
    }

    public static class CheckStep implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private List<String> checkCapabilities;
        private List<String> checkDriverCapabilities;
        private List<String> checkCollaborative;

        public CheckStep() {
            this.checkCapabilities = new ArrayList<String>();
            this.checkDriverCapabilities = new ArrayList<String>();
            this.checkCollaborative = new ArrayList<String>();
        }

        public List<String> getCheckCapabilities() {
            return checkCapabilities;
        }

        public void setCheckCapabilities(List<String> checkCapabilities) {
            this.checkCapabilities = checkCapabilities;
        }

        public List<String> getCheckDriverCapabilities() {
            return checkDriverCapabilities;
        }

        public void setCheckDriverCapabilities(List<String> checkDriverCapabilities) {
            this.checkDriverCapabilities = checkDriverCapabilities;
        }

        public List<String> getCheckCollaborative() {
            return checkCollaborative;
        }

        public void setCheckCollaborative(List<String> checkCollaborative) {
            this.checkCollaborative = checkCollaborative;
        }
    }

    public static class StartStep implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private List<String> initDriverCapabilities;
        private List<String> initCapabilities;
        private String bindAddresses;

        public StartStep() {
            this.initDriverCapabilities = new ArrayList<String>();
            this.initCapabilities = new ArrayList<String>();
        }

        public List<String> getInitDriverCapabilities() {
            return initDriverCapabilities;
        }

        public void setInitDriverCapabilities(List<String> initDriverCapabilities) {
            this.initDriverCapabilities = initDriverCapabilities;
        }

        public List<String> getInitCapabilities() {
            return initCapabilities;
        }

        public void setInitCapabilities(List<String> initCapabilities) {
            this.initCapabilities = initCapabilities;
        }

        public String getBindAddresses() {
            return bindAddresses;
        }

        public void setBindAddresses(String bindAddresses) {
            this.bindAddresses = bindAddresses;
        }
    }

    public static class DriveConfig implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private List<ScheduleRule> scheduleRules;
        private List<EventRule> eventRules;
        private Map<String, CapabilityChain> capabilityChains;

        public DriveConfig() {
            this.scheduleRules = new ArrayList<ScheduleRule>();
            this.eventRules = new ArrayList<EventRule>();
            this.capabilityChains = new HashMap<String, CapabilityChain>();
        }

        public List<ScheduleRule> getScheduleRules() {
            return scheduleRules;
        }

        public void setScheduleRules(List<ScheduleRule> scheduleRules) {
            this.scheduleRules = scheduleRules;
        }

        public List<EventRule> getEventRules() {
            return eventRules;
        }

        public void setEventRules(List<EventRule> eventRules) {
            this.eventRules = eventRules;
        }

        public Map<String, CapabilityChain> getCapabilityChains() {
            return capabilityChains;
        }

        public void setCapabilityChains(Map<String, CapabilityChain> capabilityChains) {
            this.capabilityChains = capabilityChains;
        }
    }

    public static class CollaborationStep implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String startScene;
        private String bindInterface;

        public String getStartScene() {
            return startScene;
        }

        public void setStartScene(String startScene) {
            this.startScene = startScene;
        }

        public String getBindInterface() {
            return bindInterface;
        }

        public void setBindInterface(String bindInterface) {
            this.bindInterface = bindInterface;
        }
    }

    public static class ScheduleRule implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String trigger;
        private String action;

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static class EventRule implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String event;
        private String condition;
        private String action;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public static class CapabilityChain implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private List<ChainStep> steps;

        public CapabilityChain() {
            this.steps = new ArrayList<ChainStep>();
        }

        public List<ChainStep> getSteps() {
            return steps;
        }

        public void setSteps(List<ChainStep> steps) {
            this.steps = steps;
        }
    }

    public static class ChainStep implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String capability;
        private Map<String, Object> input;
        private String condition;
        private String onError;

        public ChainStep() {
            this.input = new HashMap<String, Object>();
        }

        public String getCapability() {
            return capability;
        }

        public void setCapability(String capability) {
            this.capability = capability;
        }

        public Map<String, Object> getInput() {
            return input;
        }

        public void setInput(Map<String, Object> input) {
            this.input = input;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getOnError() {
            return onError;
        }

        public void setOnError(String onError) {
            this.onError = onError;
        }
    }
}
