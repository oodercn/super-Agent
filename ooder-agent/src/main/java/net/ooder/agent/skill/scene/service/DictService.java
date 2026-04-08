package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.dict.Dict;
import net.ooder.mvp.skill.scene.dto.dict.DictDTO;
import net.ooder.mvp.skill.scene.dto.dict.DictItem;
import net.ooder.mvp.skill.scene.dto.dict.DictItemDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DictService {

    private static final Logger logger = LoggerFactory.getLogger(DictService.class);

    private final Map<String, DictDTO> dictCache = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends Enum<?>>> dictEnumRegistry = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public DictService() {
        init();
    }

    private synchronized void init() {
        if (initialized) {
            return;
        }
        
        registerDictEnum(net.ooder.mvp.skill.scene.capability.model.CapabilityType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.capability.model.DriverType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.ParticipantType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.ParticipantRole.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.ParticipantStatus.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.SceneGroupStatus.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.SceneType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.ConnectorType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.CapabilityProviderType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.CapabilityBindingStatus.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.TemplateStatus.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.TemplateCategory.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.KeyType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.scene.KeyStatus.class);
        
        registerDictEnum(net.ooder.mvp.skill.scene.dto.audit.AuditEventType.class);
        registerDictEnum(net.ooder.mvp.skill.scene.dto.audit.AuditResultType.class);
        
        initialized = true;
        logger.info("DictService initialized with {} dictionaries", dictCache.size());
    }

    public void registerDictEnum(Class<? extends Enum<?>> enumClass) {
        Dict dictAnnotation = enumClass.getAnnotation(Dict.class);
        if (dictAnnotation == null) {
            logger.warn("Enum class {} does not have @Dict annotation", enumClass.getName());
            return;
        }

        String code = dictAnnotation.code();
        String name = dictAnnotation.name().isEmpty() ? enumClass.getSimpleName() : dictAnnotation.name();
        String description = dictAnnotation.description();

        DictDTO dictDTO = new DictDTO(code, name, description);
        List<DictItemDTO> items = new ArrayList<>();

        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        for (Enum<?> enumConstant : enumConstants) {
            if (enumConstant instanceof DictItem) {
                DictItem dictItem = (DictItem) enumConstant;
                DictItemDTO itemDTO = new DictItemDTO(
                    dictItem.getCode(),
                    dictItem.getName(),
                    dictItem.getDescription(),
                    dictItem.getIcon(),
                    dictItem.getSort()
                );
                items.add(itemDTO);
            }
        }

        items.sort(Comparator.comparingInt(DictItemDTO::getSort));
        dictDTO.setItems(items);

        if (dictAnnotation.cacheable()) {
            dictCache.put(code, dictDTO);
        }
        dictEnumRegistry.put(code, enumClass);

        logger.debug("Registered dict: {} -> {}", code, enumClass.getName());
    }

    public List<DictDTO> getAllDicts() {
        return new ArrayList<>(dictCache.values());
    }

    public DictDTO getDict(String code) {
        return dictCache.get(code);
    }

    public List<DictItemDTO> getDictItems(String code) {
        DictDTO dict = dictCache.get(code);
        if (dict != null) {
            return dict.getItems();
        }
        return new ArrayList<>();
    }

    public DictItemDTO getDictItem(String code, String itemCode) {
        List<DictItemDTO> items = getDictItems(code);
        for (DictItemDTO item : items) {
            if (item.getCode().equals(itemCode)) {
                return item;
            }
        }
        return null;
    }

    public String getDictItemName(String code, String itemCode) {
        DictItemDTO item = getDictItem(code, itemCode);
        return item != null ? item.getName() : itemCode;
    }

    public void refreshCache() {
        dictCache.clear();
        initialized = false;
        init();
    }
}
