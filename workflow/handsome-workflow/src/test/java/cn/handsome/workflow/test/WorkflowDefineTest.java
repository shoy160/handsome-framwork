package cn.handsome.workflow.test;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.workflow.domain.params.InputParameters;
import cn.handsome.workflow.domain.WorkflowDefine;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 *
 * @author luoyong
 * @date 2025/7/18
 */
@Slf4j
public class WorkflowDefineTest {
    @Test
    public void defineTest() {
        String defineJson = "{\"ownerApp\":null,\"createTime\":null,\"updateTime\":1752830613229,\"createdBy\":null,\"updatedBy\":null,\"name\":\"63da2046a7024cc39ad624b9_2025022617241012937_xwck8t\",\"description\":null,\"version\":1,\"tasks\":[{\"name\":\"trigger_config\",\"taskReferenceName\":\"trigger_config\",\"description\":\"选择触发器\",\"inputParameters\":{\"$view\":{\"isConfig\":true,\"position\":{\"x\":72.203125,\"y\":260.35546875},\"size\":{\"w\":147,\"h\":160}},\"type\":\"trigger_config\",\"configuration\":{},\"canDelete\":false},\"type\":\"SIMPLE\",\"decisionCases\":{},\"defaultCase\":[],\"forkTasks\":[],\"startDelay\":0,\"joinOn\":[],\"optional\":false,\"defaultExclusiveJoinTask\":[],\"asyncComplete\":false,\"loopOver\":[],\"triggerId\":6764,\"workflowId\":9785},{\"name\":\"luoyong_official\",\"taskReferenceName\":\"official_TKrgvp\",\"description\":\"自定义代码（7）\",\"inputParameters\":{\"$view\":{\"icon\":\"authing-code-box-fill\",\"appType\":\"function\",\"sortLabel\":\"允许执行自定义代码处理数据。\",\"actionInfo\":{\"nodeName\":\"code\"},\"position\":{\"x\":313.4698402972211,\"y\":179.31620300286082},\"size\":{\"w\":194,\"h\":128},\"isNewCanvas\":true,\"isConfig\":true},\"type\":\"code\",\"version\":1,\"configuration\":{\"jsCode\":{\"jsCode\":\"console.log(new Date())\\nfunction sleep(ms){\\nconst start = Date.now();\\nwhile (Date.now() - start < ms){\\n// 消耗时间\\n}\\n}\\nawait sleep(1500);\\nconsole.log(new Date())\\nreturn await utils.toBase64('kddd:ddedf', 'utf8')\"},\"outputKey\":\"data\",\"returnRawData\":false,\"functionId\":\"9785_official_TKrgvp\"},\"credentials\":null,\"workflowInput\":{\"requestTime\":\"${workflow.input.requestTime}\",\"eventData\":\"${workflow.input.eventData}\",\"appId\":\"${workflow.input.appId}\",\"workflowLanguage\":\"${workflow.input.workflowLanguage}\",\"triggerType\":\"${workflow.input.triggerType}\",\"eventType\":\"${workflow.input.eventType}\",\"userPoolId\":\"63da2046a7024cc39ad624b9\",\"nodeId\":\"${workflow.input.nodeId}\",\"workflowId\":9785,\"providerType\":\"${workflow.input.providerType}\",\"identityProviderId\":9188}},\"type\":\"SIMPLE\",\"dynamicTaskNameParam\":null,\"caseValueParam\":null,\"caseExpression\":null,\"scriptExpression\":null,\"decisionCases\":{},\"dynamicForkJoinTasksParam\":null,\"dynamicForkTasksParam\":null,\"dynamicForkTasksInputParamName\":null,\"defaultCase\":[],\"forkTasks\":[],\"startDelay\":0,\"subWorkflowParam\":null,\"joinOn\":[],\"sink\":null,\"optional\":false,\"taskDefinition\":null,\"rateLimited\":null,\"defaultExclusiveJoinTask\":[],\"asyncComplete\":false,\"loopCondition\":null,\"loopOver\":[],\"retryCount\":0,\"evaluatorType\":null,\"expression\":null,\"workflowId\":9785}],\"inputParameters\":[],\"outputParameters\":{},\"failureWorkflow\":null,\"schemaVersion\":2,\"restartable\":true,\"workflowStatusListenerEnabled\":false,\"ownerEmail\":\"dev@authing.cn\",\"timeoutPolicy\":\"ALERT_ONLY\",\"timeoutSeconds\":0,\"variables\":{},\"inputTemplate\":{\"identityProviderId\":9188,\"workflowLanguage\":\"zh-CN\"}}";
        WorkflowDefine define = JsonUtils.json(defineJson, WorkflowDefine.class);
        Assert.notNull(define);
        define.eachTask(task -> {
            InputParameters parameters = task.parameters();
            log.info("task: {}, parameters: {}", task.getTaskReferenceName(), JsonUtils.toPrettyJson(parameters));
        });
        log.info("define: {}", JsonUtils.toPrettyJson(define));
    }
}
