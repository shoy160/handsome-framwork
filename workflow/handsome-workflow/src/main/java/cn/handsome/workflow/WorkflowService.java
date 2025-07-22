package cn.handsome.workflow;

import cn.handsome.workflow.domain.ExternalStorageLocation;
import cn.handsome.workflow.domain.SearchResult;
import cn.handsome.workflow.domain.Workflow;
import cn.handsome.workflow.domain.WorkflowDefine;
import cn.handsome.workflow.domain.WorkflowSimple;
import cn.handsome.workflow.domain.WorkflowSummary;
import cn.handsome.workflow.domain.params.RerunWorkflowParams;
import cn.handsome.workflow.domain.params.SkipTaskParams;
import cn.handsome.workflow.domain.params.StartWorkflowParams;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
public interface WorkflowService {

    String startWorkflow(StartWorkflowParams startWorkflowParams);

    default String startWorkflow(
            String name, Integer version, String correlationId, Integer priority, Map<String, Object> input
    ) {
        StartWorkflowParams params = new StartWorkflowParams(name, version);
        params.setCorrelationId(correlationId);
        params.setPriority(priority);
        params.setInput(input);
        return startWorkflow(params);
    }

    default String startWorkflow(
            String name, Integer version, String correlationId, Integer priority,
            Map<String, Object> input, String externalInputPayloadStoragePath, Map<String, String> taskToDomain,
            WorkflowDefine workflowDef
    ) {
        StartWorkflowParams params = new StartWorkflowParams(name, version);
        params.setCorrelationId(correlationId);
        params.setPriority(priority);
        params.setInput(input);
        params.setExternalInputPayloadStoragePath(externalInputPayloadStoragePath);
        params.setTaskToDomain(taskToDomain);
        params.setWorkflowDef(workflowDef);
        return startWorkflow(params);
    }

    List<Workflow> getWorkflows(
            String name, String correlationId, boolean includeClosed, boolean includeTasks
    );

    Map<String, List<Workflow>> getWorkflows(
            String name, boolean includeClosed, boolean includeTasks,
            List<String> correlationIds
    );

    Workflow getExecutionStatus(String workflowId, boolean includeTasks);

    List<WorkflowSimple> getExecutionStatusBatch(List<String> workflowIds);

    void deleteWorkflow(String workflowId, boolean archiveWorkflow);

    List<String> getRunningWorkflows(String workflowName, Integer version, Long startTime, Long endTime);

    void decideWorkflow(String workflowId);

    void pauseWorkflow(String workflowId);

    void resumeWorkflow(String workflowId);

    void skipTaskFromWorkflow(String workflowId, String taskReferenceName, SkipTaskParams skipTaskParams);

    String rerunWorkflow(String workflowId, RerunWorkflowParams params);

    void restartWorkflow(String workflowId, boolean useLatestDefinitions);

    void retryWorkflow(String workflowId, boolean resumeSubWorkflowTasks);

    void resetWorkflow(String workflowId);

    void terminateWorkflow(String workflowId, String reason);

    SearchResult<WorkflowSummary> searchWorkflows(
            int start, int size, String sort, String freeText, String query
    );

    SearchResult<WorkflowSummary> searchWorkflows(
            int start, int size, String sort, String freeText,
            String query, boolean withOutInputOutput
    );

    SearchResult<Workflow> searchWorkflowsV2(
            int start, int size, String sort, String freeText, String query
    );

    SearchResult<WorkflowSummary> searchWorkflows(
            int start, int size, List<String> sort, String freeText, String query
    );

    SearchResult<Workflow> searchWorkflowsV2(
            int start, int size, List<String> sort, String freeText, String query
    );

    SearchResult<WorkflowSummary> searchWorkflowsByTasks(
            int start, int size, String sort, String freeText, String query
    );

    SearchResult<Workflow> searchWorkflowsByTasksV2(
            int start, int size, String sort, String freeText, String query
    );

    SearchResult<WorkflowSummary> searchWorkflowsByTasks(
            int start, int size, List<String> sort, String freeText, String query
    );

    SearchResult<Workflow> searchWorkflowsByTasksV2(
            int start, int size, List<String> sort, String freeText, String query
    );

    ExternalStorageLocation getExternalStorageLocation(
            String path, String operation, String payloadType
    );
}
