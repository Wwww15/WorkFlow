package com.example.comundademo1;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.*;
import org.camunda.bpm.model.xml.ModelInstance;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComundaParser {

    private final static String PREFIX = "SCNX";

    private final static String TABLE = PREFIX + "Table";

    private final static String INPUT = PREFIX + "Input";

    private final static String INPUT_Expression = PREFIX + "InputExpression";

    private final static String OUTPUT = PREFIX + "OutPut";

    private final static String RULE = PREFIX + "Rule";

    private final static String INPUT_ENTRY = PREFIX + "InputEntry";

    private final static String OUT_ENTRY = PREFIX + "OutputEntry";

    private final static String SEPARATOR = "_";

    private final Map<String,Class<? extends DmnElement>> paramTypeMap = new HashMap<>();

    private Set<Decision> needParseDecisions;

    private Set<Text> needParseTexts;

    /**
     * 解析decision
     */
    public void parseDecision() {
        if (CollectionUtils.isEmpty(needParseDecisions)) {
            return;
        }
        needParseDecisions.parallelStream().forEach(item -> {
            fillDecision(item);
        });
        needParseTexts.parallelStream().forEach(item -> {
            fillText(item);
        });
    }


    /**
     * 填充decision
     *
     * @param decision
     */
    private void fillDecision(Decision decision) {
        //获取id
        String decisionId = decision.getId();
        //根据id获取对应的远程decision
        String remoteDecisionJson = remoteDecisionJson(decisionId);
        if (StringUtils.isBlank(remoteDecisionJson)) {
            return;
        }
        //填充desigion
        doParseDecision(remoteDecisionJson, decision);
    }


    /**
     * 填充text $[PC0027-CC06｜分层金额下限]
     * @param text
     */
    private void fillText(Text text) {
        String textContent = text.getTextContent();
        //获取 $[ 后, | 前的字符串
        String captureText = captureText(textContent);
        String remotePerpertyJson = remotePerpertyJson(captureText);
        if(StringUtils.isBlank(remotePerpertyJson)){
            return;
        }
        doParseText(remotePerpertyJson,text);
    }


    /**
     * 截取字符串，获取id
     * @return
     */
    private String captureText(String textContent) {
        String regex = "(?<=\\$\\[).+(?=])";
        Pattern patter = Pattern.compile(regex);
        Matcher matcher = patter.matcher(textContent);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }

    /**
     * 获取远程的属性规则的json格式
     *
     * @param propertyId
     * @return
     */
    private String remotePerpertyJson(String propertyId) {
        //这里 | 分割
        if (propertyId.equals("PC0027-CC06")) {
            return "{code:\"PC0027-CC06\", name:\"分层金额下限\", type:\"range\", rule:[300,1000]}";
        }
        return "";
    }


    /**
     * 获取远程的decision的json格式
     *
     * @param decisionId
     * @return
     */
    private String remoteDecisionJson(String decisionId) {
        if (decisionId.equals("rule1")) {
            return "{inputs:[{code:\"PC0027-CC04\",name:\"分层标记\",type:\"1\"}],outputs:[{code:\"PC0027-CC05\",name:\"分层类型\",type:\"2\"}],rule:[{\"PC0027-CC04\":[\"1\"],\"PC0027-CC05\":[\"2\",\"3\"]},{\"PC0027-CC04\":[\"2\"],\"PC0027-CC05\":[\"1\"]}]}";
        }
        return "";
    }

    /**
     * 解析远程decision（json格式）
     *
     * @param remoteDecisionJson
     * @param decision
     */
    private void doParseDecision(String remoteDecisionJson, Decision decision) {
        JSONObject jsonObject = JSONObject.parseObject(remoteDecisionJson);
        //设置一个decisionTable
        DecisionTable table = generateTable(decision);
        doParseInput(jsonObject, table);
        doParseOutput(jsonObject, table);
        doParseRule(jsonObject, table);
    }

    /**
     * 解析input
     *
     * @param jsonObject
     * @param table
     */
    private void doParseInput(JSONObject jsonObject, DecisionTable table) {
        List<ComundaParam> inputNodes = jsonObject.getObject("inputs", new TypeReference<List<ComundaParam>>() {
        });
        if (CollectionUtils.isEmpty(inputNodes)) {
            return;
        }
        inputNodes.stream().forEach(item -> {
            generateInput(table,item);
            //添加input到paramTypeMap中
            paramTypeMap.put(item.getCode(),InputEntry.class);
        });
    }

    /**
     * 解析输出
     *
     * @param jsonObject
     * @param table
     */
    private void doParseOutput(JSONObject jsonObject, DecisionTable table) {
        List<ComundaParam> outputNodes = jsonObject.getObject("outputs", new TypeReference<List<ComundaParam>>() {
        });
        if (CollectionUtils.isEmpty(outputNodes)) {
            return;
        }
        outputNodes.stream().forEach(item -> {
            generateOutPut(table,item);
            //添加output到paramTypeMap中
            paramTypeMap.put(item.getCode(),OutputEntry.class);
        });
    }

    /**
     * 解析rule
     *
     * @param jsonObject
     * @param table
     */
    private void doParseRule(JSONObject jsonObject, DecisionTable table) {
        List<Map<String,List>> ruleNodes = jsonObject.getObject("rule", new TypeReference<List<Map<String,List>>>() {
        });
        if (CollectionUtils.isEmpty(ruleNodes)) {
            return;
        }
        ruleNodes.stream().forEach(item -> {
            generateRule(table,item);
        });
    }


    /**
     * 解析处理text
     * @param remotePerpertyJson
     * @param text
     */
    private void doParseText(String remotePerpertyJson, Text text) {
        JSONObject jsonObject = JSONObject.parseObject(remotePerpertyJson);
        Object rule = jsonObject.get("rule");
        if(Objects.isNull(rule)) {
            return;
        }
        text.setTextContent(rule.toString());
    }



    /**
     * 生成table
     *
     * @param
     */
    private DecisionTable generateTable(DmnModelElementInstance parentElement) {
        DecisionTable table = createElement(parentElement, generateId(TABLE), DecisionTable.class);
        //默认一个策略
        table.setHitPolicy(HitPolicy.UNIQUE);
        return table;
    }


    /**
     * 生成input
     * @param parentElement
     * @param comundaParam
     */
    private Input generateInput(DmnModelElementInstance parentElement,ComundaParam comundaParam) {
        Input input = createElement(parentElement, generateId(INPUT), Input.class);
        input.setLabel(comundaParam.getName());
        InputExpression inputExpression = createElement(input, generateId(INPUT_Expression), InputExpression.class);
        //转化参数类型
        String type = convertType(comundaParam.getType());
        inputExpression.setTypeRef(type);
        return input;
    }

    /**
     * 生成output
     * @param parentElement
     * @param comundaParam
     */
    private Output generateOutPut(DmnModelElementInstance parentElement,ComundaParam comundaParam) {
        Output output = createElement(parentElement, generateId(INPUT), Output.class);
        output.setLabel(comundaParam.getName());
        String type = convertType(comundaParam.getType());
        output.setTypeRef(type);
        return output;
    }

    /**
     * 生成rule
     * @param parentElement
     * @param ruleMap
     */
    private Rule generateRule(DmnModelElementInstance parentElement,Map<String, List> ruleMap) {
        Rule rule = createElement(parentElement, generateId(RULE), Rule.class);
        ruleMap.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            Class<? extends DmnElement> elementClass = paramTypeMap.get(key);
            boolean isInput = elementClass.equals(InputEntry.class);
            DmnElement entryElement = createElement(rule, generateId(isInput ? INPUT_ENTRY : OUT_ENTRY), elementClass);
            Text textElement = createElement(entryElement, "", Text.class);
            textElement.setTextContent(entry.getValue().toString());
        });
        return rule;
    }

    /**
     * 创建节点
     * @param parentElement
     * @param id
     * @param elementClass
     * @param <T>
     * @return
     */
    private  <T extends DmnModelElementInstance> T createElement(DmnModelElementInstance parentElement, String id, Class<T> elementClass) {
        ModelInstance modelInstance = parentElement.getModelInstance();
        T element = modelInstance.newInstance(elementClass);
        if(StringUtils.isNotBlank(id)) {
            element.setAttributeValue("id",id,true);
        }
        parentElement.addChildElement(element);
        return element;
    }

    /**
     * 生成id
     *
     * @param prefix
     * @return
     */
    private String generateId(String prefix) {
        return prefix + SEPARATOR + RandomStringUtils.randomAlphabetic(6);
    }


    /**
     * 类型转换，数字类型转字符串
     * @param type
     * @return
     */
    private String convertType(String type) {
        if(StringUtils.isBlank(type)) {
            return null;
        }
        ComundaParamType comundaParamType = Arrays.stream(ComundaParamType.values()).parallel().findFirst().filter(item -> type.equals(item.getValue())).get();
        //TODO 可能没找到
        return comundaParamType.getComundaType();
    }

}
