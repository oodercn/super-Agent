package net.ooder.editor.menu;

import net.ooder.annotation.*;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/rad/menu/editor/"})
@MenuBarMenu(menuType = CustomMenuType.MENUBAR, caption = "编辑(E)", index = 2)
@Aggregation(type = AggregationType.MENU, rootClass = EditMenuAction.class, userSpace = UserSpace.SYS)
public class EditMenuAction {

    @MethodChinaName(cname = "撤销")
    @RequestMapping(value = {"undo"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 0, caption = "撤销", imageClass = "ri-arrow-go-back-line", tips = "撤销上一步操作")
    @ResponseBody
    public ResultModel<Boolean> undo() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "重做")
    @RequestMapping(value = {"redo"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1, caption = "重做", imageClass = "ri-arrow-go-forward-line", tips = "重做上一步操作")
    @ResponseBody
    public ResultModel<Boolean> redo() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @RequestMapping(value = {"split1"})
    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split1() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "剪切")
    @RequestMapping(value = {"cut"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 3, caption = "剪切", imageClass = "ri-scissors-line", tips = "剪切选中内容")
    @ResponseBody
    public ResultModel<Boolean> cut() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "复制")
    @RequestMapping(value = {"copy"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 4, caption = "复制", imageClass = "ri-file-copy-line", tips = "复制选中内容")
    @ResponseBody
    public ResultModel<Boolean> copy() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    @MethodChinaName(cname = "粘贴")
    @RequestMapping(value = {"paste"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 5, caption = "粘贴", imageClass = "ri-clipboard-line", tips = "粘贴内容")
    @ResponseBody
    public ResultModel<Boolean> paste() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }
}