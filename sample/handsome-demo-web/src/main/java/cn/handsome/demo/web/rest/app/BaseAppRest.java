package cn.handsome.demo.web.rest.app;

import cn.handsome.core.Constants;
import cn.handsome.web.annotation.EnableAuth;
import cn.handsome.web.base.BaseController;

/**
 * @author shoy
 * @date 2021/6/23
 */
@EnableAuth(group = Constants.GROUP_APP)
public class BaseAppRest extends BaseController {
}
