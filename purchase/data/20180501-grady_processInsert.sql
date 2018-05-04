INSERT INTO "public"."na_flow_processes"("id", "category_id", "code", "ver", "image", "content", "context", "is_publish", "status", "created_at", "updated_at", "publish_at", "deployment_id", "name", "dpmn", "process_definition_id", "model") VALUES ('ACFP20180401072300874309', NULL, 'FlowCustomClearance', NULL, NULL, '<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/test">
  <process id="FlowCustomClearance" name="Flow Custom Clearance" isExecutable="true">
    <startEvent id="startevent1" name="Start" activiti:initiator="applyAccount"></startEvent>
    <userTask id="usertask1" name="Buyer Assistant" activiti:assignee="${applyAccount}" activiti:candidateGroups="role:buyerAssistant" activiti:formKey="FlowCustomClearanceForm"></userTask>
    <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="usertask1"></sequenceFlow>
    <userTask id="usertask2" name="Buyer Approve" activiti:assignee="${buyerAccount}" activiti:candidateGroups="role:buyer|purchaseLeader" activiti:formKey="FlowCustomClearanceForm"></userTask>
    <sequenceFlow id="flow2" sourceRef="usertask1" targetRef="usertask2"></sequenceFlow>
    <exclusiveGateway id="exclusivegateway1" name="Exclusive Gateway"></exclusiveGateway>
    <sequenceFlow id="flow3" sourceRef="usertask2" targetRef="exclusivegateway1"></sequenceFlow>
    <userTask id="usertask3" name="Shipping Approve" activiti:assignee="${shippingAccount}" activiti:candidateGroups="role:shipping,findType:role" activiti:formKey="FlowCustomClearanceForm">
      <multiInstanceLoopCharacteristics isSequential="false" activiti:collection="${mulitiInstanceCompleteTask.findUsers(''shipping'',''role'')}" activiti:elementVariable="shippingAccount">
        <completionCondition>${mulitiInstanceCompleteTask.completeTask(execution)}</completionCondition>
      </multiInstanceLoopCharacteristics>
    </userTask>
    <sequenceFlow id="flow4" name="allow" sourceRef="exclusivegateway1" targetRef="usertask3">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${buyerApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <exclusiveGateway id="exclusivegateway3" name="Exclusive Gateway"></exclusiveGateway>
    <endEvent id="endevent1" name="End"></endEvent>
    <sequenceFlow id="flow8" name="allow" sourceRef="exclusivegateway3" targetRef="endevent1">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowCustomClearanceEndListener}"></activiti:executionListener>
      </extensionElements>
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${shippingApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow9" name="back" sourceRef="exclusivegateway1" targetRef="usertask1">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${buyerApproved==1 || buyerApproved==2}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow12" name="back" sourceRef="exclusivegateway3" targetRef="usertask2">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${shippingApproved==1}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow13" name="redo" sourceRef="exclusivegateway3" targetRef="usertask1">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${shippingApproved==2}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow14" sourceRef="usertask3" targetRef="exclusivegateway3"></sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_FlowCustomClearance">
    <bpmndi:BPMNPlane bpmnElement="FlowCustomClearance" id="BPMNPlane_FlowCustomClearance">
      <bpmndi:BPMNShape bpmnElement="startevent1" id="BPMNShape_startevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="40.0" y="101.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask1" id="BPMNShape_usertask1">
        <omgdc:Bounds height="71.0" width="121.0" x="120.0" y="83.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask2" id="BPMNShape_usertask2">
        <omgdc:Bounds height="71.0" width="121.0" x="330.0" y="83.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway1" id="BPMNShape_exclusivegateway1">
        <omgdc:Bounds height="40.0" width="40.0" x="524.0" y="98.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask3" id="BPMNShape_usertask3">
        <omgdc:Bounds height="70.0" width="116.0" x="640.0" y="84.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway3" id="BPMNShape_exclusivegateway3">
        <omgdc:Bounds height="40.0" width="40.0" x="890.0" y="98.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endevent1" id="BPMNShape_endevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="975.0" y="101.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
        <omgdi:waypoint x="75.0" y="118.0"></omgdi:waypoint>
        <omgdi:waypoint x="120.0" y="118.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow2" id="BPMNEdge_flow2">
        <omgdi:waypoint x="241.0" y="118.0"></omgdi:waypoint>
        <omgdi:waypoint x="330.0" y="118.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow3" id="BPMNEdge_flow3">
        <omgdi:waypoint x="451.0" y="118.0"></omgdi:waypoint>
        <omgdi:waypoint x="524.0" y="118.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow4" id="BPMNEdge_flow4">
        <omgdi:waypoint x="564.0" y="118.0"></omgdi:waypoint>
        <omgdi:waypoint x="640.0" y="119.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="26.0" x="564.0" y="118.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow8" id="BPMNEdge_flow8">
        <omgdi:waypoint x="930.0" y="118.0"></omgdi:waypoint>
        <omgdi:waypoint x="975.0" y="118.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="26.0" x="930.0" y="118.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow9" id="BPMNEdge_flow9">
        <omgdi:waypoint x="544.0" y="138.0"></omgdi:waypoint>
        <omgdi:waypoint x="543.0" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="439.0" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="332.0" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="210.0" y="175.0"></omgdi:waypoint>
        <omgdi:waypoint x="180.0" y="154.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="23.0" x="544.0" y="138.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow12" id="BPMNEdge_flow12">
        <omgdi:waypoint x="910.0" y="98.0"></omgdi:waypoint>
        <omgdi:waypoint x="910.0" y="32.0"></omgdi:waypoint>
        <omgdi:waypoint x="754.0" y="32.0"></omgdi:waypoint>
        <omgdi:waypoint x="390.0" y="32.0"></omgdi:waypoint>
        <omgdi:waypoint x="390.0" y="83.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="23.0" x="909.0" y="71.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow13" id="BPMNEdge_flow13">
        <omgdi:waypoint x="910.0" y="138.0"></omgdi:waypoint>
        <omgdi:waypoint x="909.0" y="218.0"></omgdi:waypoint>
        <omgdi:waypoint x="580.0" y="218.0"></omgdi:waypoint>
        <omgdi:waypoint x="180.0" y="218.0"></omgdi:waypoint>
        <omgdi:waypoint x="180.0" y="154.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="22.0" x="910.0" y="151.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow14" id="BPMNEdge_flow14">
        <omgdi:waypoint x="756.0" y="119.0"></omgdi:waypoint>
        <omgdi:waypoint x="890.0" y="118.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>', '清关申请', 2, 1, '2018-05-01 07:23:00.539', NULL, NULL, NULL, 'FlowCustomClearance', NULL, NULL, 'FlowCustomClearance');
INSERT INTO "public"."na_flow_processes"("id", "category_id", "code", "ver", "image", "content", "context", "is_publish", "status", "created_at", "updated_at", "publish_at", "deployment_id", "name", "dpmn", "process_definition_id", "model") VALUES ('ACFP20180401072320852705', NULL, 'FlowSamplePayment', NULL, NULL, '<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/test">
  <process id="FlowSamplePayment" name="Flow Sample Payment" isExecutable="true">
    <startEvent id="startevent2" name="Start" activiti:initiator="applyAccount"></startEvent>
    <userTask id="usertask5" name="Buyer" activiti:assignee="${applyAccount}" activiti:formKey="FlowSamplePaymentForm"></userTask>
    <sequenceFlow id="flow10" sourceRef="startevent2" targetRef="usertask5"></sequenceFlow>
    <userTask id="usertask6" name="Purchase Leader Approve" activiti:assignee="${purchaseLeaderAccount}" activiti:candidateGroups="role:purchaseLeader" activiti:formKey="FlowSamplePaymentForm"></userTask>
    <sequenceFlow id="flow11" sourceRef="usertask5" targetRef="usertask6"></sequenceFlow>
    <exclusiveGateway id="exclusivegateway4" name="Exclusive Gateway"></exclusiveGateway>
    <sequenceFlow id="flow12" sourceRef="usertask6" targetRef="exclusivegateway4"></sequenceFlow>
    <userTask id="usertask8" name="CN Accountant Approved" activiti:assignee="${cnAccountantAccount}" activiti:candidateGroups="role:cnAccountant,findType:role" activiti:formKey="FlowSamplePaymentForm"></userTask>
    <exclusiveGateway id="exclusivegateway6" name="Exclusive Gateway"></exclusiveGateway>
    <sequenceFlow id="flow16" sourceRef="usertask8" targetRef="exclusivegateway6"></sequenceFlow>
    <endEvent id="endevent2" name="End"></endEvent>
    <sequenceFlow id="flow17" name="allow" sourceRef="exclusivegateway6" targetRef="endevent2">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowSamplePaymentEndListener}"></activiti:executionListener>
      </extensionElements>
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${cnAccountantApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow18" name="back" sourceRef="exclusivegateway4" targetRef="usertask5">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==1 || purchaseLeaderApproved==2}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow21" name="redo" sourceRef="exclusivegateway6" targetRef="usertask5">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${cnAccountantApproved==2}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow22" sourceRef="exclusivegateway4" targetRef="usertask8">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow23" name="back" sourceRef="exclusivegateway6" targetRef="usertask6">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${cnAccountantApproved==1}]]></conditionExpression>
    </sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_FlowSamplePayment">
    <bpmndi:BPMNPlane bpmnElement="FlowSamplePayment" id="BPMNPlane_FlowSamplePayment">
      <bpmndi:BPMNShape bpmnElement="startevent2" id="BPMNShape_startevent2">
        <omgdc:Bounds height="35.0" width="35.0" x="52.0" y="105.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask5" id="BPMNShape_usertask5">
        <omgdc:Bounds height="55.0" width="105.0" x="140.0" y="95.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask6" id="BPMNShape_usertask6">
        <omgdc:Bounds height="72.0" width="120.0" x="340.0" y="87.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway4" id="BPMNShape_exclusivegateway4">
        <omgdc:Bounds height="40.0" width="40.0" x="543.0" y="102.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask8" id="BPMNShape_usertask8">
        <omgdc:Bounds height="76.0" width="120.0" x="662.0" y="85.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway6" id="BPMNShape_exclusivegateway6">
        <omgdc:Bounds height="40.0" width="40.0" x="861.0" y="102.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endevent2" id="BPMNShape_endevent2">
        <omgdc:Bounds height="35.0" width="35.0" x="990.0" y="105.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow10" id="BPMNEdge_flow10">
        <omgdi:waypoint x="87.0" y="122.0"></omgdi:waypoint>
        <omgdi:waypoint x="140.0" y="122.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow11" id="BPMNEdge_flow11">
        <omgdi:waypoint x="245.0" y="122.0"></omgdi:waypoint>
        <omgdi:waypoint x="340.0" y="123.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow12" id="BPMNEdge_flow12">
        <omgdi:waypoint x="460.0" y="123.0"></omgdi:waypoint>
        <omgdi:waypoint x="543.0" y="122.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow16" id="BPMNEdge_flow16">
        <omgdi:waypoint x="782.0" y="123.0"></omgdi:waypoint>
        <omgdi:waypoint x="861.0" y="122.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow17" id="BPMNEdge_flow17">
        <omgdi:waypoint x="901.0" y="122.0"></omgdi:waypoint>
        <omgdi:waypoint x="990.0" y="122.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="26.0" x="901.0" y="122.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow18" id="BPMNEdge_flow18">
        <omgdi:waypoint x="563.0" y="142.0"></omgdi:waypoint>
        <omgdi:waypoint x="563.0" y="197.0"></omgdi:waypoint>
        <omgdi:waypoint x="192.0" y="197.0"></omgdi:waypoint>
        <omgdi:waypoint x="192.0" y="150.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="100.0" x="563.0" y="142.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow21" id="BPMNEdge_flow21">
        <omgdi:waypoint x="881.0" y="102.0"></omgdi:waypoint>
        <omgdi:waypoint x="880.0" y="48.0"></omgdi:waypoint>
        <omgdi:waypoint x="564.0" y="48.0"></omgdi:waypoint>
        <omgdi:waypoint x="192.0" y="48.0"></omgdi:waypoint>
        <omgdi:waypoint x="192.0" y="95.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="100.0" x="881.0" y="61.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow22" id="BPMNEdge_flow22">
        <omgdi:waypoint x="583.0" y="122.0"></omgdi:waypoint>
        <omgdi:waypoint x="662.0" y="123.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow23" id="BPMNEdge_flow23">
        <omgdi:waypoint x="881.0" y="142.0"></omgdi:waypoint>
        <omgdi:waypoint x="880.0" y="224.0"></omgdi:waypoint>
        <omgdi:waypoint x="400.0" y="224.0"></omgdi:waypoint>
        <omgdi:waypoint x="400.0" y="159.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="100.0" x="881.0" y="142.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>', '样品付款', 2, 1, '2018-05-01 07:23:20.883', NULL, NULL, NULL, 'FlowSamplePayment', NULL, NULL, 'FlowSamplePayment');
INSERT INTO "public"."na_flow_processes"("id", "category_id", "code", "ver", "image", "content", "context", "is_publish", "status", "created_at", "updated_at", "publish_at", "deployment_id", "name", "dpmn", "process_definition_id", "model") VALUES ('ACFP20180401072354642904', NULL, 'FlowSample', NULL, NULL, '<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/test">
  <process id="FlowSample" name="Flow Sample" isExecutable="true">
    <startEvent id="startevent1" name="Start" activiti:initiator="applyAccount"></startEvent>
    <userTask id="usertask1" name="Buyer" activiti:assignee="${applyAccount}" activiti:formKey="FlowSampleForm"></userTask>
    <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="usertask1"></sequenceFlow>
    <userTask id="usertask2" name="Purchase Leader Approve" activiti:async="true" activiti:assignee="${purchaseLeaderAccount}" activiti:candidateGroups="role:purchaseLeader" activiti:formKey="FlowSampleForm"></userTask>
    <sequenceFlow id="flow3" sourceRef="usertask1" targetRef="usertask2"></sequenceFlow>
    <exclusiveGateway id="exclusivegateway2" name="Exclusive Gateway"></exclusiveGateway>
    <sequenceFlow id="flow4" sourceRef="usertask2" targetRef="exclusivegateway2"></sequenceFlow>
    <endEvent id="endevent1" name="End"></endEvent>
    <sequenceFlow id="flow7" name="allow" sourceRef="exclusivegateway2" targetRef="endevent1">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowSampleEndListener}"></activiti:executionListener>
      </extensionElements>
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow8" name="back" sourceRef="exclusivegateway2" targetRef="usertask1">
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==1 || purchaseLeaderApproved==2}]]></conditionExpression>
    </sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_FlowSample">
    <bpmndi:BPMNPlane bpmnElement="FlowSample" id="BPMNPlane_FlowSample">
      <bpmndi:BPMNShape bpmnElement="startevent1" id="BPMNShape_startevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="58.0" y="74.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask1" id="BPMNShape_usertask1">
        <omgdc:Bounds height="55.0" width="105.0" x="160.0" y="64.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask2" id="BPMNShape_usertask2">
        <omgdc:Bounds height="77.0" width="121.0" x="350.0" y="50.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway2" id="BPMNShape_exclusivegateway2">
        <omgdc:Bounds height="40.0" width="40.0" x="545.0" y="71.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endevent1" id="BPMNShape_endevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="690.0" y="74.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
        <omgdi:waypoint x="93.0" y="91.0"></omgdi:waypoint>
        <omgdi:waypoint x="160.0" y="91.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow3" id="BPMNEdge_flow3">
        <omgdi:waypoint x="265.0" y="91.0"></omgdi:waypoint>
        <omgdi:waypoint x="350.0" y="88.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow4" id="BPMNEdge_flow4">
        <omgdi:waypoint x="471.0" y="88.0"></omgdi:waypoint>
        <omgdi:waypoint x="545.0" y="91.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow7" id="BPMNEdge_flow7">
        <omgdi:waypoint x="585.0" y="91.0"></omgdi:waypoint>
        <omgdi:waypoint x="690.0" y="91.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="26.0" x="585.0" y="91.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow8" id="BPMNEdge_flow8">
        <omgdi:waypoint x="565.0" y="111.0"></omgdi:waypoint>
        <omgdi:waypoint x="564.0" y="164.0"></omgdi:waypoint>
        <omgdi:waypoint x="439.0" y="164.0"></omgdi:waypoint>
        <omgdi:waypoint x="317.0" y="164.0"></omgdi:waypoint>
        <omgdi:waypoint x="212.0" y="164.0"></omgdi:waypoint>
        <omgdi:waypoint x="212.0" y="119.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="23.0" x="566.0" y="138.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>', '样品申请', 2, 1, '2018-05-01 07:23:54.439', NULL, NULL, NULL, 'FlowSample', NULL, NULL, 'FlowSample');
INSERT INTO "public"."na_flow_processes"("id", "category_id", "code", "ver", "image", "content", "context", "is_publish", "status", "created_at", "updated_at", "publish_at", "deployment_id", "name", "dpmn", "process_definition_id", "model") VALUES ('ACFP20180401072440167750', NULL, 'FlowPurchasePlan', NULL, NULL, '<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/test">
  <process id="FlowPurchasePlan" name="Flow Purchase Plan" isExecutable="true">
    <startEvent id="startevent1" name="Start" activiti:initiator="applyAccount"></startEvent>
    <userTask id="usertask1" name="Buyer" activiti:assignee="${applyAccount}" activiti:formKey="FlowPurchasePlanForm"></userTask>
    <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="usertask1"></sequenceFlow>
    <userTask id="usertask2" name="Purchase Leader Approve" activiti:assignee="${purchaseLeaderAccount}" activiti:candidateGroups="role:purchaseLeader" activiti:formKey="FlowPurchasePlanForm"></userTask>
    <sequenceFlow id="flow2" sourceRef="usertask1" targetRef="usertask2">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowReportsAllowListener}"></activiti:executionListener>
        <activiti:executionListener event="take" delegateExpression="${flowPurchasePlanStartListener}"></activiti:executionListener>
      </extensionElements>
    </sequenceFlow>
    <sequenceFlow id="flow3" sourceRef="usertask2" targetRef="exclusivegateway3"></sequenceFlow>
    <exclusiveGateway id="exclusivegateway3" name="Exclusive Gateway"></exclusiveGateway>
    <endEvent id="endevent1" name="End"></endEvent>
    <sequenceFlow id="flow8" name="allow" sourceRef="exclusivegateway3" targetRef="endevent1">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowPurchasePlanEndListener}"></activiti:executionListener>
      </extensionElements>
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==0}]]></conditionExpression>
    </sequenceFlow>
    <sequenceFlow id="flow13" name="back" sourceRef="exclusivegateway3" targetRef="usertask1">
      <extensionElements>
        <activiti:executionListener event="take" delegateExpression="${flowReportsBackListener}"></activiti:executionListener>
      </extensionElements>
      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${purchaseLeaderApproved==1 || purchaseLeaderApproved==2}]]></conditionExpression>
    </sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_FlowPurchasePlan">
    <bpmndi:BPMNPlane bpmnElement="FlowPurchasePlan" id="BPMNPlane_FlowPurchasePlan">
      <bpmndi:BPMNShape bpmnElement="startevent1" id="BPMNShape_startevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="130.0" y="109.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask1" id="BPMNShape_usertask1">
        <omgdc:Bounds height="55.0" width="105.0" x="230.0" y="99.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="usertask2" id="BPMNShape_usertask2">
        <omgdc:Bounds height="71.0" width="121.0" x="420.0" y="89.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="exclusivegateway3" id="BPMNShape_exclusivegateway3">
        <omgdc:Bounds height="40.0" width="40.0" x="640.0" y="106.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endevent1" id="BPMNShape_endevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="780.0" y="109.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
        <omgdi:waypoint x="165.0" y="126.0"></omgdi:waypoint>
        <omgdi:waypoint x="230.0" y="126.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow2" id="BPMNEdge_flow2">
        <omgdi:waypoint x="335.0" y="126.0"></omgdi:waypoint>
        <omgdi:waypoint x="420.0" y="124.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow3" id="BPMNEdge_flow3">
        <omgdi:waypoint x="541.0" y="124.0"></omgdi:waypoint>
        <omgdi:waypoint x="640.0" y="126.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow8" id="BPMNEdge_flow8">
        <omgdi:waypoint x="680.0" y="126.0"></omgdi:waypoint>
        <omgdi:waypoint x="780.0" y="126.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="100.0" x="680.0" y="126.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow13" id="BPMNEdge_flow13">
        <omgdi:waypoint x="660.0" y="106.0"></omgdi:waypoint>
        <omgdi:waypoint x="660.0" y="57.0"></omgdi:waypoint>
        <omgdi:waypoint x="544.0" y="57.0"></omgdi:waypoint>
        <omgdi:waypoint x="282.0" y="57.0"></omgdi:waypoint>
        <omgdi:waypoint x="282.0" y="99.0"></omgdi:waypoint>
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="14.0" width="100.0" x="660.0" y="87.0"></omgdc:Bounds>
        </bpmndi:BPMNLabel>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>', '采购计划', 2, 1, '2018-05-01 07:24:40.212', NULL, NULL, NULL, 'FlowPurchasePlan', NULL, NULL, 'FlowPurchasePlan');
