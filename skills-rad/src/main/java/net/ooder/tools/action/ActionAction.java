package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.config.TreeListResultModel;
import net.ooder.dsm.admin.temp.JavaTempNavTree;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = {"/action/action/"})
@MethodChinaName(cname = "模板")
@MenuBarMenu(menuType = CustomMenuType.TOP, caption = "模板", index = 4, imageClass = "ri-tools-line")
@Aggregation(type = AggregationType.MENU,userSpace = UserSpace.SYS)
public class ActionAction {

    @MethodChinaName(cname = "代码模板")
    @RequestMapping(method = RequestMethod.POST, value = "CodeTemps")  //web 规范
    @APIEventAnnotation(autoRun = true)//入口数据装载 配置事件为自动装载数据
    @NavTreeViewAnnotation// 特定视图强制规范
    @CustomAnnotation(imageClass = "ri-tools-line", index = 2) //服务通用注解规范
    @DialogAnnotation(caption = "代码模板", width = "900", height = "680")// 视图窗口配置
    @ModuleAnnotation(dynLoad = true, caption = "代码模板")//在具有特定强制视图规范的注解后然然使用，dynLoad = true 标识该视图需要动态运行期渲染
    @ResponseBody// JSON 入口页面强制配置
    public TreeListResultModel<List<JavaTempNavTree>> getTempManager(String id) {//强制视图类型为TreeDatas时使用对应json封装
         TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(DSMType.values()), JavaTempNavTree.class);
        return resultModel;

    }

    @RequestMapping(method = RequestMethod.POST, value = "ViewTemps")
    @APIEventAnnotation(autoRun = true)
    @NavTreeViewAnnotation
    @CustomAnnotation(imageClass = "ri-settings-3-line", index = 3)

    @DialogAnnotation(caption = "代码模板", width = "900", height = "680")
    @ModuleAnnotation(dynLoad = true, caption = "视图配置")
    @ResponseBody
    public TreeListResultModel<List<JavaTempNavTree>> getViewTemps(String id) {
        TreeListResultModel<List<JavaTempNavTree>> resultModel = new TreeListResultModel<List<JavaTempNavTree>>();
        resultModel = TreePageUtil.getTreeList(Arrays.asList(DSMType.AGGREGATION), JavaTempNavTree.class);
        return resultModel;

    }

}
