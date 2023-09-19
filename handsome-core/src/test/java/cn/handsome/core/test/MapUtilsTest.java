package cn.handsome.core.test;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyong
 * @date 2023/4/13
 */
@Slf4j
public class MapUtilsTest {
    private final Map<String, Object> currentMap;
    private static final String JSON_VALUE = "{\n" +
            "      \"name\": \"openapi\",\n" +
            "      \"taskReferenceName\": \"tdbd9da0ed3c142128adfa991eb48fe9e\",\n" +
            "      \"description\": \"List - Add\",\n" +
            "      \"inputParameters\": {\n" +
            "        \"$view\": {\n" +
            "          \"icon\": \"https://authing.co/favicon.ico\",\n" +
            "          \"position\": {\n" +
            "            \"x\": 408.992,\n" +
            "            \"y\": 359.819\n" +
            "          },\n" +
            "          \"operation\": \"custom_shay160#59019200454332416\",\n" +
            "          \"appType\": \"action\",\n" +
            "          \"sortLabel\": \"我的应用\",\n" +
            "          \"actionInfo\": {\n" +
            "            \"category\": \"List\",\n" +
            "            \"value\": \"custom_shay160#59019200454332416\",\n" +
            "            \"nodeName\": \"custom\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"type\": \"custom_shay160#59019200454332416\",\n" +
            "        \"configuration\": {\n" +
            "          \"resource\": \"List\",\n" +
            "          \"operation\": \"custom_shay160#59019200454332416\",\n" +
            "          \"CUSTOM#type\": \"CODE\",\n" +
            "          \"CUSTOM#customCode\": \"return _.concat(data.list, data.item)\",\n" +
            "          \"list\": [\n" +
            "            \"a\",\n" +
            "            \"b\"\n" +
            "          ],\n" +
            "          \"item\": [\n" +
            "            \"c\"\n" +
            "          ]\n" +
            "        },\n" +
            "        \"workflowInput\": \"${workflow.input}\"\n" +
            "      },\n" +
            "      \"type\": \"SIMPLE\",\n" +
            "      \"decisionCases\": {},\n" +
            "      \"defaultCase\": [],\n" +
            "      \"forkTasks\": [1,2],\n" +
            "      \"startDelay\": 0,\n" +
            "      \"joinOn\": [],\n" +
            "      \"optional\": false,\n" +
            "      \"defaultExclusiveJoinTask\": [],\n" +
            "      \"asyncComplete\": false,\n" +
            "      \"loopOver\": [],\n" +
            "      \"retryCount\": 0,\n" +
            "      \"workflowId\": 678\n" +
            "    }";

    public MapUtilsTest() {
        this.currentMap = JsonUtils.jsonMap(JSON_VALUE);
    }

    @Test
    public void getValueTest() {
//        String paths = "inputParameters.$view.position.y";
        String paths = "inputParameters.configuration.list[1]";
//        String paths = "forkTasks[1]";
        String value =
                MapUtils.getValueByPath(this.currentMap, String.class, paths);
        log.info("value: {}", value);
    }

    @Test
    public void setValueTest() {
        String paths = "inputParameters.$view.position.y";
//        String paths = "inputParameters.configuration.list[1]";
//        String paths = "forkTasks[1]";
        MapUtils.setValue(this.currentMap, paths, 459);
        log.info("value: {}", MapUtils.getValueByPath(this.currentMap, Double.class, paths));
    }
}
