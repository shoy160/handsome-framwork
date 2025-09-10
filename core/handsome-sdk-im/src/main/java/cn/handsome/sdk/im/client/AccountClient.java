package cn.handsome.sdk.im.client;

import cn.handsome.core.http.annotation.Host;
import cn.handsome.core.http.annotation.Json;
import cn.handsome.core.http.annotation.Post;
import cn.handsome.sdk.im.ImConstants;
import cn.handsome.sdk.im.model.BaseIdentifier;
import cn.handsome.sdk.im.model.request.account.*;
import cn.handsome.sdk.im.model.response.RestResp;
import cn.handsome.sdk.im.model.response.account.AccountCheckResp;
import cn.handsome.sdk.im.model.response.account.AccountDeleteResp;
import cn.handsome.sdk.im.model.response.account.AccountQueryResp;
import cn.handsome.sdk.im.model.response.account.FailAccountsResp;
import cn.handsome.sdk.im.model.request.account.*;

/**
 * 账号管理
 *
 * @author shoy
 * @date 2021/6/17
 */
@Host(ImConstants.BASE_URL + "im_open_login_svc/")
public interface AccountClient {
    /**
     * 导入单个账号
     *
     * @param req req
     * @return resp
     */
    @Post("account_import")
    RestResp accountImport(@Json AccountReq req);

    /**
     * 批量导入
     *
     * @param req req
     * @return resp
     */
    @Post("multiaccount_import")
    FailAccountsResp multiImport(@Json AccountsReq req);

    /**
     * 删除账号
     *
     * @param req req
     * @return resp
     */
    @Post("account_delete")
    AccountDeleteResp accountDelete(@Json AccountsDeleteReq req);

    /**
     * 查询账号
     *
     * @param req request
     * @return resp
     */
    @Post("account_check")
    AccountCheckResp accountCheck(@Json AccountCheckReq req);

    /**
     * 失效登录状态
     *
     * @param req req
     * @return resp
     */
    @Post("kick")
    RestResp kick(@Json BaseIdentifier req);

    /**
     * 查询状态
     *
     * @param req req
     * @return resp
     */
    @Post("/v4/openim/querystate")
    AccountQueryResp queryState(@Json AccountQueryReq req);
}
