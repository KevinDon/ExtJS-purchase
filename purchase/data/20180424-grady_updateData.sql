
-- 更新na_product_vendor_prop 的内箱体积、内箱体积重量、外箱体积、外箱体积重量
update na_product_vendor_prop set master_carton_cbm =master_carton_w*master_carton_h*master_carton_l/100/100/100 where master_carton_cbm = 0 or master_carton_cbm is null;
update na_product_vendor_prop set master_carton_cubic_weight =master_carton_cbm*250 where master_carton_cubic_weight = 0 or master_carton_cubic_weight is null;

update na_product_vendor_prop set inner_carton_cbm =inner_carton_w*inner_carton_h*inner_carton_l/100/100/100 where inner_carton_cbm = 0 or inner_carton_cbm is null;
update na_product_vendor_prop set inner_carton_cubic_weight =inner_carton_cbm*250 where inner_carton_cubic_weight = 0 or inner_carton_cubic_weight is null;

--采购合同流程表
-- 更新创建时间在J18-0081订单创建时间之后包含J18-0081的订单 的美元总货值
WITH tempDetail1 as (
select d1.business_id as business_id,sum(order_value_usd) as total_order_value_usd from  na_flow_purchase_contract_detail d1 where business_id in (select t2.id from na_flow_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where order_number='J18-0081') and t2.total_value_usd is null ) GROUP BY d1.business_id)
update na_flow_purchase_contract c1 set total_value_usd = (select total_order_value_usd from tempDetail1 d1 where d1.business_id=c1.id )
where  c1.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where order_number='J18-0081') and c1.total_value_usd is null ;
commit;
-- 更新创建时间在J18-0081订单创建时间之后包含J18-0081的订单 的人民币总货值
WITH tempDetail2 as (
select d1.business_id as business_id,sum(order_value_rmb) as total_order_value_rmb from  na_flow_purchase_contract_detail d1 where d1.business_id in (select t2.id from na_flow_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where t1.order_number='J18-0081') and t2.total_value_rmb is null) GROUP BY d1.business_id)
update na_flow_purchase_contract c2 set total_value_rmb = (select total_order_value_rmb from tempDetail2 d2 where d2.business_id=c2.id )
where  c2.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where t1.order_number='J18-0081') and c2.total_value_rmb is null;
commit;
-- 更新创建时间在J18-0081订单创建时间之后包含J18-0081的订单 的澳元总货值
WITH tempDetail3 as (
select d1.business_id as business_id,sum(order_value_aud) as total_order_value_aud from  na_flow_purchase_contract_detail d1 where d1.business_id in (select t2.id from na_flow_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where t1.order_number='J18-0081') and t2.total_value_aud is null) GROUP BY d1.business_id)
update na_flow_purchase_contract c3 set total_value_aud = (select total_order_value_aud from tempDetail3 d3 where d3.business_id=c3.id )  
where  c3.created_at >= (SELECT t1.created_at FROM "na_flow_purchase_contract" as t1 where t1.order_number='J18-0081') and c3.total_value_aud is null;
commit;

--采购合同业务表
WITH tempDetail1 as (
select d1.order_id as business_id,sum(order_value_usd) as total_order_value_usd from  na_purchase_contract_detail d1 where d1.order_id in (select t2.id from na_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where order_number='J18-0081') and t2.total_value_usd is null ) GROUP BY d1.order_id)
update na_purchase_contract c1 set total_value_usd = (select total_order_value_usd from tempDetail1 d1 where d1.business_id=c1.id )
where  c1.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where order_number='J18-0081') and c1.total_value_usd is null ;
commit;

WITH tempDetail2 as (
select d1.order_id as business_id,sum(order_value_rmb) as total_order_value_rmb from  na_purchase_contract_detail d1 where d1.order_id in (select t2.id from na_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where t1.order_number='J18-0081') and t2.total_value_rmb is null) GROUP BY d1.order_id)
update na_purchase_contract c2 set total_value_rmb = (select total_order_value_rmb from tempDetail2 d2 where d2.business_id=c2.id )
where  c2.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where t1.order_number='J18-0081') and c2.total_value_rmb is null;
commit;

WITH tempDetail3 as (
select d1.order_id as business_id,sum(order_value_aud) as total_order_value_aud from  na_purchase_contract_detail d1 where d1.order_id in (select t2.id from na_purchase_contract t2 where  t2.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where t1.order_number='J18-0081') and t2.total_value_aud is null) GROUP BY d1.order_id)
update na_purchase_contract c3 set total_value_aud = (select total_order_value_aud from tempDetail3 d3 where d3.business_id=c3.id )  
where  c3.created_at >= (SELECT t1.created_at FROM "na_purchase_contract" as t1 where t1.order_number='J18-0081') and c3.total_value_aud is null;
commit;
