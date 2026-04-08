package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.dict.DictDTO;
import net.ooder.mvp.skill.scene.dto.dict.DictItemDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.DictService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dicts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping
    public ResultModel<List<DictDTO>> getAllDicts() {
        List<DictDTO> dicts = dictService.getAllDicts();
        return ResultModel.success(dicts);
    }

    @GetMapping("/{code}")
    public ResultModel<DictDTO> getDict(@PathVariable String code) {
        DictDTO dict = dictService.getDict(code);
        if (dict == null) {
            return ResultModel.notFound("字典不存在: " + code);
        }
        return ResultModel.success(dict);
    }

    @GetMapping("/{code}/items")
    public ResultModel<List<DictItemDTO>> getDictItems(@PathVariable String code) {
        List<DictItemDTO> items = dictService.getDictItems(code);
        return ResultModel.success(items);
    }

    @GetMapping("/{code}/items/{itemCode}")
    public ResultModel<DictItemDTO> getDictItem(
            @PathVariable String code,
            @PathVariable String itemCode) {
        DictItemDTO item = dictService.getDictItem(code, itemCode);
        if (item == null) {
            return ResultModel.notFound("字典项不存在: " + code + "/" + itemCode);
        }
        return ResultModel.success(item);
    }

    @GetMapping("/{code}/items/{itemCode}/name")
    public ResultModel<String> getDictItemName(
            @PathVariable String code,
            @PathVariable String itemCode) {
        String name = dictService.getDictItemName(code, itemCode);
        return ResultModel.success(name);
    }

    @PostMapping("/refresh")
    public ResultModel<String> refreshCache() {
        dictService.refreshCache();
        return ResultModel.success("字典缓存已刷新");
    }
}
