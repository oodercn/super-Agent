package net.ooder.tools;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.engine.ConnectInfo;
import net.ooder.esd.custom.component.grid.SimpleGridComponet;
import net.ooder.esd.tool.component.TreeGridComponent;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.JDSClientService;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@MethodChinaName(cname = "本地功能调用")
@RequestMapping(value = {"/LOCALESD/"})

public class LocalESDService {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "executeCMD")
    @MethodChinaName(cname = "执行命令")
    public @ResponseBody
    ResultModel<Object> executeCMD(String expression) {

        ResultModel<Object> resultModel = new ResultModel();
        JDSClientService clientService = EsbUtil.parExpression(JDSClientService.class);
        try {
            if (clientService.getConnectInfo() == null) {
                try {
                    Person person = OrgManagerFactory.getOrgManager().getPersonByID(JDSServer.getInstance().getAdminUser().getId());
                    ConnectInfo connectInfo = new ConnectInfo(person.getID(), person.getAccount(), person.getPassword());
                    clientService.connect(connectInfo);
                } catch (PersonNotFoundException e) {
                    e.printStackTrace();
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }

            Object obj = EsbUtil.parExpression(expression);
            resultModel.setData(obj);
        } catch (Throwable e) {
            resultModel = new ErrorResultModel<>();
            ((ErrorResultModel<Object>) resultModel).setErrdes("表达式错误！" + e.getMessage());
        }


        return resultModel;
    }

    ;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "executeGridCMD")
    @MethodChinaName(cname = "执行命令")
    public @ResponseBody
    ResultModel<TreeGridComponent> executeGridCMD(String expression) {
        Object obj = null;
        ResultModel<TreeGridComponent> resultModel = new ResultModel();
        JDSClientService clientService = EsbUtil.parExpression(JDSClientService.class);
        if (clientService.getConnectInfo() == null) {
            try {
                Person person = OrgManagerFactory.getOrgManager().getPersonByID(JDSServer.getInstance().getAdminUser().getId());
                ConnectInfo connectInfo = new ConnectInfo(person.getID(), person.getAccount(), person.getPassword());
                clientService.connect(connectInfo);
            } catch (PersonNotFoundException e) {
                e.printStackTrace();
            } catch (JDSException e) {
                e.printStackTrace();
            }

        }

        try {


            obj = JDSActionContext.getActionContext().Par(expression);
            //obj = EsbUtil.parExpression(expression);
            TreeGridComponent gridComponent = new SimpleGridComponet(obj);
            resultModel.setData(gridComponent);
        } catch (Throwable e) {
            e.printStackTrace();
            resultModel = new ErrorResultModel<>();
            ((ErrorResultModel<TreeGridComponent>) resultModel).setErrdes("表达式错误！" + e.getMessage());
        }


        return resultModel;
    }

}
