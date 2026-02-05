package net.ooder.tools.component;

import net.ooder.common.JDSException;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.menu.TreeMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.tools.SPACompoentService;
import net.ooder.web.RequestMethodBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TreeAnnotation(heplBar = true, lazyLoad = true, selMode = SelModeType.singlecheckbox, customService = SPACompoentService.class, bottombarMenu = {TreeMenu.SAVE, TreeMenu.CLOSE})
public class SPAClassTree extends TreeListItem {

    public SPAClassTree(String dsmId) {
        this.caption = "实体信息";
        this.setInitFold(false);
        this.addTagVar("dsmId", dsmId);
        this.setImageClass("ri-database-2-line");
        this.id = "all";
        try {
            DSMFactory.getInstance().reload();
            List<ESDClass> esdClasses = BuildFactory.getInstance().getClassManager().getAllAggView();
            if (esdClasses.size() > 0) {
                for (ESDClass child : esdClasses) {
                    this.addChild(new SPAClassTree(child, dsmId));
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public SPAClassTree(ESDClass esdClass, String dsmId) {
        this.caption = esdClass.getName() + "(" + esdClass.getDesc() + ")";
        this.setEuClassName(esdClass.getClassName());
        this.setImageClass("ri-table-line");
        this.setInitFold(false);
        this.tagVar = new HashMap<>();
        this.tagVar.put("className", esdClass.getClassName());
        this.tagVar.put("dsmId", dsmId);
        this.tagVar.put("serviceName", esdClass.getClassName());
        this.setInitFold(true);
        this.id = esdClass.getClassName();
        this.setSub(new ArrayList());

    }

    public SPAClassTree(EUModule module, RequestMethodBean methodBean, CustomMenuItem item, String dsmId) {
        this.caption = module.getName() + "(" + item.getName() + ")";
        this.setEuClassName(methodBean.getClassName());
        this.setImageClass(item.getImageClass());
        this.setInitFold(false);
        this.tagVar = new HashMap<>();
        this.tagVar.put("className", module.getClassName());
        this.tagVar.put("dsmId", dsmId);
        this.id = module.getClassName();

    }
}
