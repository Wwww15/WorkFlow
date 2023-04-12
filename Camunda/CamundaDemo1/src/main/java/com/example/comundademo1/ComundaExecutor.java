package com.example.comundademo1;

import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Text;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ComundaExecutor {


    public void execute() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"https://www.omg.org/spec/DMN/20191111/MODEL/\" xmlns:dmndi=\"https://www.omg.org/spec/DMN/20191111/DMNDI/\" xmlns:dc=\"http://www.omg.org/spec/DMN/20180521/DC/\" xmlns:di=\"http://www.omg.org/spec/DMN/20180521/DI/\" id=\"dish\" name=\"Dish\" namespace=\"http://camunda.org/schema/1.0/dmn\">\n" +
                "  <decision id=\"calc\" name=\"利率计算\">\n" +
                "    <informationRequirement id=\"InformationRequirement_1l7cl6c\">\n" +
                "      <requiredInput href=\"#InputData_0u4ec8c\" />\n" +
                "    </informationRequirement>\n" +
                "    <informationRequirement id=\"InformationRequirement_1r8o8ad\">\n" +
                "      <requiredDecision href=\"#rule1\" />\n" +
                "    </informationRequirement>\n" +
                "    <decisionTable id=\"DecisionTable_1fuya75\" hitPolicy=\"FIRST\">\n" +
                "      <input id=\"InputClause_16ih50s\" label=\"交易金额\">\n" +
                "        <inputExpression id=\"LiteralExpression_1sr2zuf\" typeRef=\"string\" />\n" +
                "      </input>\n" +
                "      <input id=\"InputClause_03xb1ac\" label=\"分层类型\">\n" +
                "        <inputExpression id=\"LiteralExpression_1ci0gps\" typeRef=\"string\">\n" +
                "          <text></text>\n" +
                "        </inputExpression>\n" +
                "        <inputValues id=\"UnaryTests_1ixll7a\">\n" +
                "          <text>\"1\",\"2\",\"3\"</text>\n" +
                "        </inputValues>\n" +
                "      </input>\n" +
                "      <output id=\"OutputClause_0q7oltb\" typeRef=\"string\" />\n" +
                "      <rule id=\"DecisionRule_0qxtibz\">\n" +
                "        <inputEntry id=\"UnaryTests_1l63bp8\">\n" +
                "          <text>\"300,1000\"</text>\n" +
                "        </inputEntry>\n" +
                "        <inputEntry id=\"UnaryTests_1p7w5b8\">\n" +
                "          <text>2</text>\n" +
                "        </inputEntry>\n" +
                "        <outputEntry id=\"LiteralExpression_1tfx23q\">\n" +
                "          <text>1%</text>\n" +
                "        </outputEntry>\n" +
                "      </rule>\n" +
                "      <rule id=\"DecisionRule_0q9vwel\">\n" +
                "        <inputEntry id=\"UnaryTests_110zl2c\">\n" +
                "          <text></text>\n" +
                "        </inputEntry>\n" +
                "        <inputEntry id=\"UnaryTests_0ggbzoq\">\n" +
                "          <text>3</text>\n" +
                "        </inputEntry>\n" +
                "        <outputEntry id=\"LiteralExpression_0roc3dk\">\n" +
                "          <text>2%</text>\n" +
                "        </outputEntry>\n" +
                "      </rule>\n" +
                "      <rule id=\"DecisionRule_0n5mpnr\">\n" +
                "        <inputEntry id=\"UnaryTests_0rkj1a9\">\n" +
                "          <text></text>\n" +
                "        </inputEntry>\n" +
                "        <inputEntry id=\"UnaryTests_1qq73ih\">\n" +
                "          <text></text>\n" +
                "        </inputEntry>\n" +
                "        <outputEntry id=\"LiteralExpression_03df5yc\">\n" +
                "          <text>3%</text>\n" +
                "        </outputEntry>\n" +
                "      </rule>\n" +
                "    </decisionTable>\n" +
                "  </decision>\n" +
                "  <decision id=\"rule1\" name=\"属性间关系1\">\n" +
                "    <informationRequirement id=\"InformationRequirement_03nfyza\">\n" +
                "      <requiredInput href=\"#InputData_1ufilbt\" />\n" +
                "    </informationRequirement>\n" +
                "  </decision>\n" +
                "  <inputData id=\"InputData_0u4ec8c\" name=\"交易金额\" />\n" +
                "  <inputData id=\"InputData_1ufilbt\" name=\"层级标记\" />\n" +
                "  <dmndi:DMNDI>\n" +
                "    <dmndi:DMNDiagram id=\"DMNDiagram_0egb666\">\n" +
                "      <dmndi:DMNShape id=\"DMNShape_0jm9co5\" dmnElementRef=\"calc\">\n" +
                "        <dc:Bounds height=\"80\" width=\"180\" x=\"490\" y=\"100\" />\n" +
                "      </dmndi:DMNShape>\n" +
                "      <dmndi:DMNShape id=\"DMNShape_0ww405o\" dmnElementRef=\"rule1\">\n" +
                "        <dc:Bounds height=\"80\" width=\"180\" x=\"350\" y=\"270\" />\n" +
                "      </dmndi:DMNShape>\n" +
                "      <dmndi:DMNShape id=\"DMNShape_14y897b\" dmnElementRef=\"InputData_0u4ec8c\">\n" +
                "        <dc:Bounds height=\"45\" width=\"125\" x=\"698\" y=\"288\" />\n" +
                "      </dmndi:DMNShape>\n" +
                "      <dmndi:DMNEdge id=\"DMNEdge_0pwrjzl\" dmnElementRef=\"InformationRequirement_1l7cl6c\">\n" +
                "        <di:waypoint x=\"761\" y=\"288\" />\n" +
                "        <di:waypoint x=\"610\" y=\"200\" />\n" +
                "        <di:waypoint x=\"610\" y=\"180\" />\n" +
                "      </dmndi:DMNEdge>\n" +
                "      <dmndi:DMNEdge id=\"DMNEdge_15gyjze\" dmnElementRef=\"InformationRequirement_1r8o8ad\">\n" +
                "        <di:waypoint x=\"440\" y=\"270\" />\n" +
                "        <di:waypoint x=\"550\" y=\"200\" />\n" +
                "        <di:waypoint x=\"550\" y=\"180\" />\n" +
                "      </dmndi:DMNEdge>\n" +
                "      <dmndi:DMNShape id=\"DMNShape_1sb6lm4\" dmnElementRef=\"InputData_1ufilbt\">\n" +
                "        <dc:Bounds height=\"45\" width=\"125\" x=\"377\" y=\"408\" />\n" +
                "      </dmndi:DMNShape>\n" +
                "      <dmndi:DMNEdge id=\"DMNEdge_0wk8o5s\" dmnElementRef=\"InformationRequirement_03nfyza\">\n" +
                "        <di:waypoint x=\"440\" y=\"408\" />\n" +
                "        <di:waypoint x=\"440\" y=\"370\" />\n" +
                "        <di:waypoint x=\"440\" y=\"350\" />\n" +
                "      </dmndi:DMNEdge>\n" +
                "    </dmndi:DMNDiagram>\n" +
                "  </dmndi:DMNDI>\n" +
                "</definitions>\n";
        //生成字节流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xml.getBytes());
        DmnModelInstance dmnModelInstance = Dmn.readModelFromStream(byteArrayInputStream);
        //获取需要解析的决策
        Collection<Decision> decisions = dmnModelInstance.getModelElementsByType(Decision.class);
        Set<Decision> needParseDecisions = decisions.parallelStream().filter(item -> needDcisionParse(item)).collect(Collectors.toSet());
        //获取需要解析的待填text
        Collection<Text> texts = dmnModelInstance.getModelElementsByType(Text.class);
        Set<Text> needParseTexts = texts.parallelStream().filter(item -> needTextnParse(item)).collect(Collectors.toSet());
        //设置解析规则
        ComundaParser comundaParser = new ComundaParser(needParseDecisions,needParseTexts);
        //调用解析
        comundaParser.parseDecision();
    }

    /**
     * 判断是否需要解析的决策节点
     * @return
     */
    public  boolean needDcisionParse(Decision decision) {
        Collection<DecisionTable> tableCollection = decision.getModelInstance().getModelElementsByType(DecisionTable.class);
        return null != tableCollection && !tableCollection.isEmpty();
    }

    /**
     * 判断是否需要解析的text节点
     * @return
     */
    public  boolean needTextnParse(Text text) {
        return text.getTextContent().contains("$");
    }
}
