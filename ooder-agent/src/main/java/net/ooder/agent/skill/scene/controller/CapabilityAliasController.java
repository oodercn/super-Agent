package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.dto.PageResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController("capabilityAliasController")
@RequestMapping("/api/v1/capabilities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CapabilityAliasController {

    private static final Logger log = LoggerFactory.getLogger(CapabilityAliasController.class);

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private CapabilityBindingService bindingService;

    @GetMapping
    public ResultModel<List<Capability>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "100") Integer pageSize) {
        
        log.info("[list] keyword={}, category={}, status={}, pageNum={}, pageSize={}", 
                keyword, category, status, pageNum, pageSize);
        
        List<Capability> capabilities = capabilityService.findAll();
        
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            List<Capability> filtered = new ArrayList<Capability>();
            for (Capability cap : capabilities) {
                if (cap.getName() != null && cap.getName().toLowerCase().contains(lowerKeyword)) {
                    filtered.add(cap);
                } else if (cap.getCapabilityId() != null && cap.getCapabilityId().toLowerCase().contains(lowerKeyword)) {
                    filtered.add(cap);
                }
            }
            capabilities = filtered;
        }
        
        if (category != null && !category.isEmpty()) {
            List<Capability> filtered = new ArrayList<Capability>();
            for (Capability cap : capabilities) {
                if (category.equals(cap.getCategory())) {
                    filtered.add(cap);
                }
            }
            capabilities = filtered;
        }
        
        log.info("[list] Returning {} capabilities", capabilities.size());
        return ResultModel.success(capabilities);
    }

    @GetMapping("/{capabilityId}")
    public ResultModel<Capability> getById(@PathVariable String capabilityId) {
        log.info("[getById] capabilityId={}", capabilityId);
        Capability capability = capabilityService.findById(capabilityId);
        if (capability == null) {
            return ResultModel.error(404, "能力不存在: " + capabilityId);
        }
        return ResultModel.success(capability);
    }

    @GetMapping("/bindings")
    public ResultModel<List<CapabilityBinding>> listAllBindings() {
        log.info("[listAllBindings] Getting all bindings");
        List<CapabilityBinding> bindings = bindingService.listAll();
        return ResultModel.success(bindings);
    }

    @GetMapping("/{capabilityId}/bindings")
    public ResultModel<List<CapabilityBinding>> listBindings(@PathVariable String capabilityId) {
        log.info("[listBindings] capabilityId={}", capabilityId);
        List<CapabilityBinding> bindings = bindingService.listByCapability(capabilityId);
        return ResultModel.success(bindings);
    }

    @PostMapping
    public ResultModel<Capability> create(@RequestBody Capability capability) {
        log.info("[create] capabilityId={}", capability.getCapabilityId());
        Capability created = capabilityService.register(capability);
        return ResultModel.success(created);
    }

    @PutMapping("/{capabilityId}")
    public ResultModel<Capability> update(@PathVariable String capabilityId, @RequestBody Capability capability) {
        log.info("[update] capabilityId={}", capabilityId);
        capability.setCapabilityId(capabilityId);
        Capability updated = capabilityService.update(capability);
        return ResultModel.success(updated);
    }

    @DeleteMapping("/{capabilityId}")
    public ResultModel<Void> delete(@PathVariable String capabilityId) {
        log.info("[delete] capabilityId={}", capabilityId);
        capabilityService.unregister(capabilityId);
        return ResultModel.success(null);
    }
}
