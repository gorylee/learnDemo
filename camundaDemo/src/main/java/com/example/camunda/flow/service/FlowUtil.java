package com.example.camunda.flow.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import example.common.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricIdentityLinkLog;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * camunda 流程工具类
 */
@Component
@Slf4j
public class FlowUtil {
    private static final FlowUtil util = new FlowUtil();

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @PostConstruct
    public void initialize() {
        util.repositoryService = repositoryService;
        util.runtimeService = runtimeService;
        util.taskService = taskService;
        util.historyService = historyService;
    }
    /**
     * 部署流程，参数本地bpmn的路径
     * @param filePath  bpmn路径
     * @param name  流程名称
     */
    public static void deployment(String filePath,String name){
        log.info("开始部署本地审批流程:{},文件地址:{}",name,filePath);
        util.repositoryService.createDeployment()
                .name(name)
                .addClasspathResource(filePath)
                .deploy();

        log.info("完成部署本地审批流程:{},文件地址:{}",name,filePath);
    }

    /**
     * 启动流程
     * @param key 流程标识,需唯一,如果存在相同key则会启动版本号最新的流程
     * @param businessKey  业务id
     * @param map 流程变量（包含节点审批人及业务判断变量等）
     */
    public static void startProcess(String key, Object businessKey, Map<String,Object> map){
        log.info("开始启动审批流程:{},业务源:{},流程变量: {}",key,businessKey.toString(), JSONUtil.toJsonStr(JSONUtil.parse(map)));
        ProcessInstance processInstance = util.runtimeService.startProcessInstanceByKey(key, businessKey.toString(), map);
        log.info("完成启动审批流程:{},业务源:{},流程实例ID:{}",key,businessKey.toString(), processInstance.getId());
    }

    /**
     * 销毁流程
     * @param key 流程标识,需唯一,如果存在相同key则会启动版本号最新的流程
     * @param businessKey  业务id
     * @param reason 销毁原因
     * @param reason 审批人
     */
    public static void destroyProcess(String key, String businessKey, String reason,String assignee){
        //查询待办任务
        List<Task> tasks = FlowUtil.getTaskByCandidateUserAndBusinessKey(key, businessKey, assignee);
        if(CollUtil.isEmpty(tasks)){
            log.info("{}完成审批任务失败:{}--{}",assignee,key,businessKey);
            throw new ResultException("暂未到您审批");
        }
        FlowUtil.claimTask(tasks.get(0).getId(),assignee);
        List<Task> taskList = FlowUtil.getTasksByBusinessKey(key, businessKey);
        if(CollUtil.isEmpty(taskList)) return;
        log.info("开始销毁审批流程:{},业务源:{},流程实例ID:{}:",key,businessKey, taskList.get(0).getProcessInstanceId());
        util.runtimeService.deleteProcessInstance(taskList.get(0).getProcessInstanceId(), reason);
        log.info("完成销毁审批流程:{},业务源:{},流程实例ID:{}",key,businessKey, taskList.get(0).getProcessInstanceId());
    }

    /**
     * 领取任务
     * @param taskId  任务id
     * @param candidateUser  候选用户名
     */
    public static void claimTask(String taskId,String candidateUser){
        Task task = util.taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        if(Objects.isNull(task)){
            log.info("{}领取审批任务失败:{}",candidateUser,taskId);
            throw new ResultException("任务领取失败");
        }
        util.taskService.claim(taskId,candidateUser);
    }

    /**
     * 完成任务
     * @param taskId  任务id
     * @param assignee  用户名
     */
    public static void completeTask(String taskId,String assignee){
        Task task = util.taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        if(Objects.isNull(task)){
            log.info("{}完成审批任务失败:{}",assignee,taskId);
            throw new ResultException("任务完成失败");
        }
        util.taskService.complete(taskId);
    }

    /**
     * 领取并完成任务
     * @param taskId  任务id
     * @param assignee  用户名
     */
    public static void claimAndCompleteTask(String taskId,String assignee){
        Task task = util.taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        if(Objects.isNull(task)){
            log.info("{}审批任务不存在:{}",assignee,taskId);
            throw new ResultException("审批任务不存在");
        }
        if(StrUtil.isNotBlank(task.getAssignee())){
            if(!task.getAssignee().equalsIgnoreCase(assignee)){
                log.info("{}完成审批任务失败:{}",assignee,taskId);
                throw new ResultException("暂未到您审批");
            }
            util.taskService.complete(taskId);
            return;
        }
        Task unClaimTask = util.taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(assignee)
                .singleResult();
        if(Objects.isNull(unClaimTask)){
            log.info("{}完成审批任务失败:{}",assignee,taskId);
            throw new ResultException("暂未到您审批");
        }
        util.taskService.claim(taskId,assignee);
        util.taskService.complete(taskId);
    }

    /**
     * 查询待办任务，参数：候选用户、业务id
     * @param candidateUser 候选用户
     * @param key 流程标识,需唯一,如果存在相同key则会启动版本号最新的流程
     * @param businessKey  业务id
     * @return
     */
    public static List<Task> getTaskByCandidateUserAndBusinessKey(String key,String businessKey,String candidateUser){
        return util.taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)
                .list();

    }

    /**
     * 查询待办任务，参数： 业务id
     * @param key 流程标识,需唯一,如果存在相同key则会启动版本号最新的流程
     * @param businessKey  业务id
     * @return
     */
    public static List<Task> getTasksByBusinessKey(String key, String businessKey){
        return util.taskService.createTaskQuery()
                .processDefinitionKey(key)
                .processInstanceBusinessKey(businessKey)
                .list();
    }

    /**
     * 查询待办任务，参数： 业务id
     * @param key 流程标识,需唯一,如果存在相同key则会启动版本号最新的流程
     * @param businessKey  业务id
     * @param assignee  用户名
     * @return
     */
    public static boolean claimAndCompleteTask(String key, String businessKey,String assignee){
        //查询待办任务
        List<Task> tasks = FlowUtil.getTaskByCandidateUserAndBusinessKey(key, businessKey, assignee);
        if(CollUtil.isEmpty(tasks)){
            log.info("{}完成审批任务失败:{}--{}",assignee,key,businessKey);
            throw new ResultException("暂未到您审批");
        }
        for (Task task : tasks) {
            FlowUtil.claimAndCompleteTask(task.getId(),assignee);
        }
        if(CollUtil.isEmpty(FlowUtil.getTasksByBusinessKey(key,businessKey))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询历史任务
     * @param key
     * @param businessKey
     * @return
     */
    public static List<HistoricTaskInstance> getHistoricTask(String processInstanceId,String key, String businessKey){
        return util.historyService.createHistoricTaskInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .processInstanceId(processInstanceId)
                .processDefinitionKey(key)
                .list();
    }

    /**
     * 查询历史实例
     * @param key
     * @param businessKey
     * @return
     */
    public static HistoricProcessInstance getLastProcessInstance(String key, String businessKey){
        return util.historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(key)
                .processInstanceBusinessKey(businessKey)
                .orderByProcessInstanceStartTime()
                .desc().list().get(0);
    }

    /**
     * 查询任务
     * @param key
     * @param taskId
     * @return
     */
    public static List<HistoricIdentityLinkLog> getHistoricIdentityLinkLog(String key, String taskId){
        return util.historyService.createHistoricIdentityLinkLogQuery()
                .processDefinitionKey(key)
                .taskId(taskId)
                .list();
    }


    /**
     * 查询任务
     * @param taskId
     * @return
     */
    public static List<IdentityLink> getTaskIdentityLink(String taskId){
        return util.taskService.getIdentityLinksForTask(taskId);
    }



}
