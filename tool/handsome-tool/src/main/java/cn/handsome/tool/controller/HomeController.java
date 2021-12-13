package cn.handsome.tool.controller;

import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.tool.ipregion.IpRegion;
import cn.handsome.tool.config.IpRegionProperties;
import cn.handsome.tool.ipregion.Searcher;
import cn.handsome.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shoy
 * @date 2021/12/7
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(value = "Home", tags = "工具类服务")
public class HomeController extends BaseController {
    private final Searcher searcher;

    @GetMapping("ip")
    @ApiOperation(value = "IP 地址解析", notes = "IP 地址解析")
    public ResultDTO<?> ipRegion(String ip) {
        try {
            IpRegion region = searcher.bTreeSearch(ip);
            return null == region ? fail("IP 解析失败") : success(region);
        } catch (Exception ex) {
            ex.printStackTrace();
            return fail("IP 解析失败");
        }
    }
}
