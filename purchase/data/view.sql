drop VIEW na_view_sta_order;
--订单统计表
create view na_view_sta_order as
WITH order_cost as (
    SELECT order_id,sailing_days,currency as cost_currency,detail.rate_aud_to_usd as cost_rate_aud_to_usd,rate_aud_to_rmb as cost_rate_aud_to_rmb,
      sum(COALESCE(price_cost_aud,0)  *COALESCE(order_qty,0) ) price_cost_aud,sum(COALESCE(price_cost_rmb,0)  * COALESCE(order_qty,0)) price_cost_rmd,sum(COALESCE(price_cost_usd,0)  * COALESCE(order_qty,0)) price_cost_usd,
      sum(COALESCE(port_fee_aud,0) * COALESCE(order_qty,0)) port_fee_aud,sum(COALESCE(port_fee_rmb,0) * COALESCE(order_qty,0)) port_fee_rmb,sum(COALESCE(port_fee_usd,0) * COALESCE(order_qty,0)) port_fee_usd,
      sum(COALESCE(charge_item_fee_aud,0) * COALESCE(order_qty,0)) charge_item_fee_aud,sum(COALESCE(charge_item_fee_rmb,0) * COALESCE(order_qty,0)) charge_item_fee_rmb,sum(COALESCE(charge_item_fee_usd,0) * COALESCE(order_qty,0)) charge_item_fee_usd,
      sum(COALESCE(tariff_aud,0) * COALESCE(order_qty,0)) tariff_aud,sum(COALESCE(tariff_rmb,0) * COALESCE(order_qty,0)) tariff_rmb,sum(COALESCE(tariff_usd,0) * COALESCE(order_qty,0)) tariff_usd,
      sum(COALESCE(custom_processing_fee_aud,0) * COALESCE(order_qty,0)) custom_processing_fee_aud,sum(COALESCE(custom_processing_fee_rmb,0) * COALESCE(order_qty,0)) custom_processing_fee_rmb,
      sum(COALESCE(custom_processing_fee_usd,0) * COALESCE(order_qty,0)) custom_processing_fee_usd,
      sum(COALESCE(other_fee_aud,0) * COALESCE(order_qty,0)) other_fee_aud,sum(COALESCE(other_fee_rmb,0) * COALESCE(order_qty,0)) other_fee_rmb,sum(COALESCE(other_fee_usd,0) * COALESCE(order_qty,0)) other_fee_usd,
      SUM (COALESCE(target_bin_aud,0) * COALESCE(order_qty,0)) total_sales_price_aud, SUM (target_bin_rmb * COALESCE(order_qty,0)) total_sales_price_rmb,SUM (target_bin_usd * COALESCE(order_qty,0)) total_sales_price_usd,
      sum(COALESCE(gst_aud,0) * COALESCE(order_qty,0)) gst_aud,sum(COALESCE(gst_rmb,0) * COALESCE(order_qty,0)) gst_rmb,sum(COALESCE(gst_usd,0) * COALESCE(order_qty,0)) gst_usd,
      sum(COALESCE(master_carton_cbm,0) * COALESCE(cartons,0)) total_packing_cbm,--装箱总体积
      sum(COALESCE(total_cost_aud,0) * COALESCE(order_qty,0)) total_cost_aud,sum(COALESCE(total_cost_rmb,0) * COALESCE(order_qty,0)) total_cost_rmb,sum(COALESCE(total_cost_usd,0) * COALESCE(order_qty,0)) total_cost_usd,
       sum(COALESCE (order_value_aud,0))  order_value_aud , sum(COALESCE (order_value_rmb,0))  order_value_rmb,sum(COALESCE (order_value_usd,0))  order_value_usd,--工厂交货价
      sum (COALESCE(port_fee_aud,0) * COALESCE(order_qty,0)*0.1) as freight_gst_aud,--运费税金（海运费+海运保险费）*税率
      sum (COALESCE(port_fee_rmb,0) *COALESCE(order_qty,0)*0.1 ) as freight_gst_usd,--运费税金（海运费+海运保险费）*税率
      sum (COALESCE(port_fee_usd,0) * COALESCE(order_qty,0)*0.1 ) as freight_gst_rmb,--运费税金（海运费+海运保险费）*税率
            sum (CASE WHEN c_type='1' THEN price_gp20_insurance_aud*num
          WHEN c_type='2' THEN price_gp40_insurance_aud*num
          WHEN c_type='3' THEN price_hq40_insurance_aud*num
          WHEN c_type='4' THEN price_lcl_insurance_aud*num
          ELSE
          0
      END
      ) as freight_insurance_aud, --海运保险费 海运保险*货柜数量
      sum (CASE WHEN c_type='1' THEN price_gp20_insurance_usd*num
          WHEN c_type='2' THEN price_gp40_insurance_usd*num
          WHEN c_type='3' THEN price_hq40_insurance_usd*num
          WHEN c_type='4' THEN price_lcl_insurance_usd*num
          ELSE
          0
      END
      ) as freight_insurance_usd, --海运保险费 海运保险*货柜数量
      sum (CASE WHEN c_type='1' THEN price_gp20_insurance_rmb*num
          WHEN c_type='2' THEN price_gp40_insurance_rmb*num
          WHEN c_type='3' THEN price_hq40_insurance_rmb*num
          WHEN c_type='4' THEN price_lcl_insurance_rmb*num
          ELSE
          0
      END
      ) as freight_insurance_rmb --海运保险费 海运保险*货柜数量
    from(select DISTINCT o.id order_id, od.product_id,od.order_qty,od.cartons,
          pvp.target_bin_aud,pvp.target_bin_usd,pvp.target_bin_rmb,--产品档案表内目标售价
           date_part('day', oa.eta - oa.etd) as sailing_days,
           pc.currency,pc.rate_aud_to_rmb,pc.rate_aud_to_usd,
           pc.price_cost_aud,pc.price_cost_rmb,pc.price_cost_usd,
           od.order_value_aud,od.order_value_usd,od.order_value_rmb,
           pc.port_fee_aud , pc.port_fee_rmb ,pc.port_fee_usd,
           pc.charge_item_fee_aud,pc.charge_item_fee_rmb,pc.charge_item_fee_usd,
           pc.tariff_aud,pc.tariff_rmb,pc.tariff_usd,
           pc.custom_processing_fee_aud,pc.custom_processing_fee_rmb,pc.custom_processing_fee_usd,
           pc.other_fee_aud,pc.other_fee_rmb,pc.other_fee_usd,
           pc.gst_aud,pc.gst_rmb,pc.gst_usd,
           pvp.master_carton_cbm,ncp.pcs_per_carton,--sku体积和数量
           pc.total_cost_aud,pc.total_cost_rmb,pc.total_cost_usd,ccm.c_type,ccm.num,sp.price_gp20_insurance_aud,
          sp.price_gp20_aud,sp.price_gp40_insurance_aud,sp.price_gp40_aud,sp.price_hq40_insurance_aud,
          sp.price_gp20_insurance_usd,sp.price_gp20_usd,sp.price_gp40_insurance_usd,sp.price_gp40_usd,
          sp.price_hq40_insurance_usd,sp.price_hq40_usd,sp.price_lcl_insurance_usd,sp.price_lcl_usd,
          sp.price_gp20_insurance_rmb,sp.price_gp20_rmb,sp.price_gp40_insurance_rmb,sp.price_gp40_rmb,
          sp.price_hq40_insurance_rmb,sp.price_hq40_rmb,sp.price_lcl_insurance_rmb,sp.price_lcl_rmb,
          sp.price_hq40_aud,sp.price_lcl_insurance_aud,sp.price_lcl_aud
         from na_purchase_contract_detail od
           left join na_purchase_contract o on od.order_id = o.id
           LEFT JOIN na_order_shipping_apply oa on o.flag_order_shipping_apply_id = oa.id
           LEFT JOIN na_product_vendor_prop pvp on od.sku=pvp.sku
           LEFT JOIN na_cost c on o.flag_order_shipping_plan_id = c.order_shipping_plan_id
           LEFT JOIN na_cost_product_cost pc on c.id = pc.cost_id and od.product_id = pc.product_id
           LEFT join na_cost_product ncp on c.id= ncp.cost_id
           LEFT JOIN   (select ccp.container_type as c_type,count(ccp.container_type) as num,ccp.order_id  from na_custom_clearance_packing ccp group by ccp.container_type,ccp .order_id) ccm on o.id = ccm.order_id
           LEFT JOIN na_order_shipping_apply_detail so on o.id = so.order_id
           LEFT JOIN na_service_provider_quotation_port sp on so.service_provider_quotation_id=sp.id) as detail
    group by order_id,sailing_days,currency,rate_aud_to_usd,rate_aud_to_rmb
)
  SELECT row_number() over() as id, oc.*, date_part('month',oc.Estimated_ETA) as month_eta, date_part('year',oc.Estimated_ETA) as year_eta
   from(
   SELECT DISTINCT t.id as order_id, t.order_number,t.order_title,--订单id/号/标题
  t.vendor_product_category_id,vpc.product_category_id, pc.cn_name as product_category_cn_name, pc.en_name as product_category_en_name,t.vendor_product_category_alias,t.order_index,--
  t.vendor_id,t.vendor_cn_name,t.vendor_en_name,--供应商
  t.origin_port_id,t.origin_port_cn_name  ,t.origin_port_en_name,--起始港
  COALESCE (cp.container_type,t.container_type :: VARCHAR ) as container_type  ,COALESCE (cc.container_qty,t.container_qty) as container_qty,--货柜类型、货柜数量
  t.status,t.created_at,t.end_time,t.start_time,--订单状态，订单创建时间，流程完成时间，流程申请时间
  t.currency,t.rate_aud_to_usd as contract_rate_aud_to_usd,t.rate_aud_to_rmb as contract_rate_aud_to_rmb,t.total_cbm,COALESCE (t.total_cbm,oc.total_packing_cbm) as total_packing_cbm,--币种汇率、总体积,装箱总体积
  t.total_price_aud,t.total_price_rmb,t.total_price_usd,--合同金额
  CASE WHEN t.eta IS NOT NULL THEN t.eta
       WHEN osp.eta IS NOT NULL THEN osp.eta
       WHEN ad.eta IS NOT NULL THEN ad.eta
       WHEN cc.eta IS  NOT NULL THEN cc.eta
       ELSE cc.end_time END AS Estimated_ETA,
  t.write_off_aud, t.write_off_rmb, t.write_off_usd,--冲销金额
  t.total_price_aud - COALESCE(t.write_off_aud,0) as final_total_price_aud,oc.order_value_aud as ex_work_aud,oc.order_value_rmb as ex_work_rmb,
  oc.order_value_usd as ex_work_usd,--最终发票金额,总货值
  t.total_price_rmb - COALESCE(t.write_off_rmb,0) as final_total_price_rmb,
  t.total_price_usd - COALESCE(t.write_off_usd,0) as final_total_price_usd,
  t.deposit_rate,t.payment_deposit_aud as deposit_aud,t.payment_deposit_rmb as deposit_rmb,t.payment_deposit_usd as deposit_usd ,t.deposit_type,--定金,定金类型
  t.rate_aud_to_rmb as deposit_rate_aud_to_rmb,t.rate_aud_to_usd as deposit_rate_aud_to_usd,--定金汇率
  br.rate_aud_to_rmb as balance_rate_aud_to_rmb,br.rate_aud_to_usd as balance_rate_aud_to_usd,--尾款汇率
  sp.rate_aud_to_rmb as service_rate_aud_to_rmb,sp.rate_aud_to_usd as service_rate_aud_to_usd,--服务商汇率
  nc.rate_aud_to_rmb as cost_rate_aud_to_rmb,nc.rate_aud_to_usd as cost_rate_aud_to_usd,--成本汇率

  CASE WHEN t.deposit_rate >0 THEN COALESCE(t.total_price_aud,0)  - COALESCE(t.deposit_aud,0) - COALESCE(t.write_off_aud,0)                    +COALESCE(t.total_other_aud,0)
       WHEN t.deposit_type=1 THEN COALESCE(t.total_price_aud,0) -  COALESCE(t.write_off_aud,0) +COALESCE(t.total_other_aud,0)
       ELSE 0 END AS balance_aud,
  CASE WHEN t.deposit_rate >0 THEN COALESCE(t.total_price_rmb,0) - COALESCE(t.deposit_rmb,0) - COALESCE(t.write_off_rmb,0)                    +COALESCE(t.total_other_rmb,0)
       WHEN t.deposit_type=1 THEN COALESCE(t.total_price_rmb,0) -  COALESCE(t.write_off_rmb,0) +COALESCE(t.total_other_rmb,0)
       ELSE 0 END AS balance_rmb,
  CASE WHEN t.deposit_rate >0 THEN COALESCE(t.total_price_usd,0) - COALESCE(t.deposit_usd,0) - COALESCE(t.write_off_usd,0)                    +COALESCE(t.total_other_usd,0)
       WHEN t.deposit_type=1 THEN COALESCE(t.total_price_usd,0) -  COALESCE(t.write_off_usd,0) +COALESCE(t.total_other_usd,0)
       ELSE 0 END AS balance_usd,
  pcd.end_time AS deposit_date,--定金时间
  t.flag_fee_payment_time AS balance_date,--费用分支付时间
  t.flag_fee_payment_time as estimated_balance,--（暂定）预计尾款
  COALESCE(ad.service_provider_id,osp.service_provider_id)service_provider_id,
  COALESCE (ad.service_provider_cn_name,osp.service_provider_cn_name)service_provider_cn_name,
  COALESCE (ad.service_provider_en_name,osp.service_provider_en_name)service_provider_en_name,--货代
  COALESCE (t.flag_order_shipping_apply_time,t.flag_order_shipping_plan_time)as agent_notification_date,--'货代通知日期（发货确认日期）
  t.ready_date,t.etd,--完货时间、发货时间
   CASE WHEN cc.end_time IS NOT NULL THEN cc.end_time
       WHEN cc.eta IS  NOT NULL THEN cc.eta
        WHEN ad.eta IS NOT NULL THEN ad.eta
       WHEN osp.eta IS NOT NULL THEN osp.eta
       ELSE t.eta END AS eta,--到岸时间
  t.total_order_qty,--总数量
  t.currency  as PICurrency,t.balance_payment_term as credit_terms,--采购合同币种
  (select min(wd.receive_start_date) from na_warehouse_plan_detail wd where wd.order_id = t.id) as delivery_time,--'到仓时间(送仓计划接收开始时间)
  date_part('day', t.eta - t.etd) arrival_days, -- '抵达天数（到仓时间-预计发货时间）'
  cc.start_time as shipping_doc_received_date, --清关文件接收日期
  cc.end_time as shipping_doc_forwarded_date,--清关文件转发日期
  oc.charge_item_fee_aud,oc.charge_item_fee_rmb, oc.charge_item_fee_usd, --'本地费用
  oc.total_sales_price_aud,total_sales_price_rmb,total_sales_price_usd,--总售价
  COALESCE(oc.port_fee_aud,0) port_fee_aud ,COALESCE( oc.port_fee_rmb,0) port_fee_rmb ,COALESCE( oc.port_fee_usd,0) port_fee_usd, --'海运费
  COALESCE(oc.port_fee_aud,0) + COALESCE(oc.charge_item_fee_aud,0) as total_freight_aud,--'总运费(海运费+本地费用)
  COALESCE(oc.port_fee_rmb,0) + COALESCE(oc.charge_item_fee_rmb,0) as total_freight_rmb,
  COALESCE(oc.port_fee_usd,0) + COALESCE(oc.charge_item_fee_usd,0) as total_freight_usd,
  coalesce(ppd.epfa,0)+ coalesce(nfrdd.epfa,0) as electronic_processing_fee_aud,--电放费
  coalesce(ppd.epfr,0)+ coalesce(nfrdd.epfr,0) as electronic_processing_fee_rmb,
  coalesce(ppd.epfu,0)+ coalesce(nfrdd.epfu,0) as electronic_processing_fee_usd,
  CASE WHEN ppd.epfa is not null THEN 1 ELSE  0 END AS telex_released ,--是否电放
   COALESCE(oc.tariff_aud,0) tariff_aud, COALESCE(oc.tariff_rmb,0) tariff_rmb, COALESCE(oc.tariff_usd,0) tariff_usd,--'关税
   COALESCE(oc.custom_processing_fee_aud,0) custom_processing_fee_aud,COALESCE(oc.custom_processing_fee_rmb,0) custom_processing_fee_rmb,COALESCE(oc.custom_processing_fee_usd,0) custom_processing_fee_usd,--'行政税
  oc.cost_currency,date_part('day',ad.etd - case when t.deposit_aud = 0 THEN nosa.end_time else t.flag_contract_deposit_time end) as lead_time, --'生产周期(etd-订单生效时间)
  date_part('day', t.eta - t.etd) as sailing_days, --'海运时长
 COALESCE (nad.cn_name,t.department_cn_name) as department_cn_name,COALESCE (nad.en_name,t.department_en_name)as department_en_name,COALESCE (au.department_id,t.department_id) as department_id, --采购员,采购不能
	t.creator_id as creator_id, t.creator_cn_name as creator_cn_name,t.creator_en_name as creator_en_name,
  COALESCE(oc.gst_aud,0) gst_aud, COALESCE(oc.gst_rmb,0) gst_rmb, COALESCE(oc.gst_usd,0) gst_usd,--'商品服务税
  oc.total_cost_aud,t.flag_asn_time, --总费用,收货通知
  oc.freight_gst_aud,oc.freight_gst_usd,oc.freight_gst_rmb,--运费税金
  oc.freight_insurance_aud,oc.freight_insurance_usd,oc.freight_insurance_rmb,nc.total_shipping_cbm-- 体积重量
from na_purchase_contract t
LEFT JOIN na_purchase_contract_deposit pcd on t.id=pcd.order_id
LEFT JOIN na_balance_refund br on t.id=br.order_id
LEFT JOIN na_vendor_product_category vpc on t.vendor_product_category_id = vpc.id
LEFT JOIN na_product_category pc on vpc.product_category_id = pc.id
LEFT JOIN na_custom_clearance cc on t.id = cc.order_id
LEFT JOIN na_account_user au on t.creator_id=au.id
LEFT JOIN na_account_department nad on nad.id = au.department_id
LEFT JOIN na_custom_clearance_packing cp on cc.id = cp.custom_clearance_id
LEFT JOIN na_order_shipping_apply_detail ad on t.flag_order_shipping_apply_id = ad.order_shipping_apply_id
LEFT JOIN na_order_shipping_apply nosa on t.flag_order_shipping_apply_id=nosa.id
LEFT JOIN na_service_provider_quotation_port sp on ad.service_provider_quotation_id=sp.service_provider_quotation_id
left join order_cost oc on t.id = oc.order_id
LEFT JOIN na_fee_register nfr on nfr.order_id = t.id
LEFT JOIN (SELECT nfrd.price_aud as epfa,nfrd.price_rmb as epfr,nfrd.price_usd as epfu, nfrd.fee_register_id from na_fee_register_detail nfrd where nfrd.item_cn_name='电放费')nfrdd on nfrdd.fee_register_id=nfr.id
LEFT JOIN (SELECT pcod.price_aud as epfa,pcod.price_rmb as epfr,pcod.price_usd as epfu, pcod.order_id FROM na_purchase_contract_other_detail pcod WHERE pcod.item_cn_name='电放费') ppd on t.id = ppd.order_id
LEFT JOIN na_cost nc on t.flag_order_shipping_plan_id=nc.id
LEFT JOIN na_order_shipping_plan osp on t.order_number=osp.order_numbers) oc





--CREATE TABLE na_sta_order as SELECT row_number() over() as id,t.*, current_date as sta_date from na_view_sta_order t;

drop VIEW na_view_sta_cost;
--采购商品成本统计表
create view na_view_sta_cost as
  WITH order_cost as (
      SELECT SUM (COALESCE (master_carton_cbm,0)*COALESCE (order_qty,0)) total_cbm,order_id
      FROM ( SELECT DISTINCT o.id order_id,od.sku,od.order_qty,pvp.master_carton_cbm
        FROM na_purchase_contract_detail od
          left join na_purchase_contract o on od.order_id = o.id
          LEFT JOIN na_product_vendor_prop pvp on od.sku=pvp.sku
      ) as detail
        GROUP BY order_id
  )

  SELECT row_number() over() as id, t.* from (
select DISTINCT od.order_id, od.product_id,p.sku,od.order_qty,--采购数量
       pc.currency,pc.rate_aud_to_rmb,pc.rate_aud_to_usd,
       pc.price_cost_aud,pc.price_cost_rmb,pc.price_cost_usd,--采购成本(报价)
       pc.port_fee_aud,pc.port_fee_rmb,pc.port_fee_usd,--单件海运费
       pc.charge_item_fee_aud,pc.charge_item_fee_rmb,pc.charge_item_fee_usd,--本地费
       od.price_aud,od.price_usd,od.price_rmb,--单价
       pc.total_cost_aud,pc.total_cost_rmb,pc.total_cost_usd,--总成本
       pc.tariff_aud,pc.tariff_rmb,pc.tariff_usd,-- 关税
       pc.custom_processing_fee_aud,pc.custom_processing_fee_rmb,pc.custom_processing_fee_usd, --行政税(运费税金)
       pc.other_fee_aud,pc.other_fee_rmb,pc.other_fee_usd,--其它费用
       pc.gst_aud,pc.gst_rmb,pc.gst_usd,--单件服务税
       o.flag_asn_time,--收货确认时间ASN
       CASE  WHEN o.currency = 1 THEN pc.gst_aud
             WHEN o.currency = 2 THEN pc.gst_rmb
             WHEN o.currency = 3 THEN pc.gst_usd
             ELSE 0 END gst,--根据结算货币输出单件服务税
       pc.price_cost_aud - pc.port_fee_aud as final_cost_aud,pc.price_cost_rmb - pc.port_fee_rmb as final_cost_rmb,pc.price_cost_usd - pc.port_fee_usd as  final_cost_usd,----(到岸价)单件总费用
       ( pc.price_cost_aud - pc.port_fee_aud)*0.9 as total_cost_tax_aud,
       ( pc.price_cost_rmb - pc.port_fee_rmb)*0.9 as total_cost_tax_rmb,
       ( pc.price_cost_usd - pc.port_fee_usd)*0.9  as total_cost_tax_usd,--到岸不含税
       pc.port_fee_aud * 1.1 as freight_gst_aud, pc.port_fee_rmb * 1.1 as freight_gst_rmb, pc.port_fee_usd * 1.1 as freight_gst_usd,--单件含税运费
       COALESCE(pc.total_cost_aud,0)  * COALESCE(od.order_qty,0) as total_order_cost_aud, --订单中sku总成本
       COALESCE(pc.total_cost_rmb,0)  * COALESCE(od.order_qty,0)as total_order_cost_rmb,
       COALESCE(pc.total_cost_usd,0)  * COALESCE(od.order_qty,0)as total_order_cost_usd,
       COALESCE(pc.port_fee_aud,0)+COALESCE(pc.charge_item_fee_aud,0)+COALESCE(pc.tariff_aud,0)+COALESCE(pc.gst_aud,0)+coalesce(ppd.epfa,0)+ coalesce(nfrdd.epfa,0) as added_cost_aud, --费用增加/件
       pc.port_fee_rmb + pc.charge_item_fee_rmb + pc.tariff_rmb + pc.gst_rmb + coalesce(ppd.epfr,0)+ coalesce(nfrdd.epfr,0) as added_cost_rmb,
       pc.port_fee_usd + pc.charge_item_fee_usd + pc.tariff_usd + pc.gst_usd +coalesce(ppd.epfu,0)+ coalesce(nfrdd.epfu,0) as added_cost_usd,
       ( COALESCE(pc.port_fee_aud,0)+COALESCE(pc.charge_item_fee_aud,0)+COALESCE(pc.tariff_aud,0)+COALESCE(pc.gst_aud,0)+coalesce(ppd.epfa,0)+ coalesce(nfrdd.epfa,0)) * COALESCE(od.order_qty,0) as total_added_cost_aud, --总费用费用增
       ( COALESCE(pc.port_fee_rmb,0)+COALESCE(pc.charge_item_fee_rmb,0)+COALESCE(pc.tariff_rmb,0)+COALESCE(pc.gst_rmb,0)+coalesce(ppd.epfr,0)+ coalesce(nfrdd.epfr,0)) * od.order_qty as total_added_cost_rmb,
      ( COALESCE(pc.port_fee_usd,0)+COALESCE(pc.charge_item_fee_usd,0)+COALESCE(pc.tariff_usd,0)+COALESCE(pc.gst_usd,0)+coalesce(ppd.epfu,0)+ coalesce(nfrdd.epfu,0)) * od.order_qty as total_added_cost_usd,
       coalesce(ppd.epfa,0)+ coalesce(nfrdd.epfa,0) as electronic_processing_fee_aud,--电放费
      coalesce(ppd.epfr,0)+ coalesce(nfrdd.epfr,0) as electronic_processing_fee_rmb,
      coalesce(ppd.epfu,0)+ coalesce(nfrdd.epfu,0) as electronic_processing_fee_usd,
       p.cbm, p.cbm * od.order_qty as total_item_cbm,o.end_time,oc.total_cbm, -- 品类总体积,单件体积,总体积
        o.order_number,o.order_title

     from na_purchase_contract_detail od
       LEFT JOIN na_product p on od.product_id = p.id
       LEFT JOIN order_cost oc on oc.order_id = od.order_id
       left join na_purchase_contract o on od.order_id = o.id
       LEFT JOIN na_fee_register nfr on nfr.order_id = o.id
      LEFT JOIN (SELECT nfrd.price_aud as epfa,nfrd.price_rmb as epfr,nfrd.price_usd as epfu, nfrd.fee_register_id from na_fee_register_detail nfrd where nfrd.item_cn_name='电放费')nfrdd on nfrdd.fee_register_id=nfr.id
      LEFT JOIN (SELECT pcod.price_aud as epfa,pcod.price_rmb as epfr,pcod.price_usd as epfu, pcod.order_id FROM na_purchase_contract_other_detail pcod WHERE pcod.item_cn_name='电放费') ppd on o.id = ppd.order_id
       inner JOIN na_cost c on o.flag_order_shipping_plan_id = c.order_shipping_plan_id
       LEFT JOIN na_order_shipping_apply_detail osad on od.order_id=osad.order_id
       LEFT JOIN na_service_provider_quotation_port spqp on osad.service_provider_quotation_id=spqp.service_provider_quotation_id
       INNER JOIN na_cost_product_cost pc on c.id = pc.cost_id and od.product_id = pc.product_id) t;

drop VIEW na_view_sta_orders_cycle;
--生产周期统计表
CREATE VIEW na_view_sta_orders_cycle AS
  SELECT o.id, o.order_number,o.order_title,o.order_index,o.flag_asn_time,--收货确认时间ASN
  o.created_time as created_time, --创建时间
  o.vendor_id,o.vendor_cn_name, o.vendor_en_name, --供应商
  case when o.deposit_aud = 0 THEN o.end_time else o.flag_contract_deposit_time end as order_confirmed_date,--订单确认日期
  o.flag_contract_deposit_time as deposit_date, --付定金日
  o.ready_date as original_ready_date, --计划完货日
  ad.ready_date as adjust_ready_date, --订单实货日
  o.etd as original_etd, --计划etd
  o.retd as adjust_etd, --实际etd
  o.flag_order_shipping_apply_time as agent_notification_date,--'货代通知日期（发货确认日期）
  o.flag_fee_payment_time as balance_date, --尾款付款日
  CASE WHEN o.is_needed_qc = 3 THEN cc.created_at ELSE o.flag_order_qc_time END as qc_time,--QC日期
  cc.start_time as shipping_doc_received_date, --清关文件接收日期
  cc.end_time as shipping_doc_forwarded_date,--清关文件转发日期
  CASE  WHEN cc.end_time IS NOT NULL THEN cc.end_time
        WHEN cc.eta IS  NOT NULL THEN cc.eta
        WHEN ad.eta IS NOT NULL THEN ad.eta
        WHEN osp.eta IS NOT NULL THEN osp.eta
        ELSE o.eta  END AS eta,
  (select min(wd.receive_start_date) from na_warehouse_plan_detail wd where wd.order_id = o.id) as container_arriving_time,--'到仓时间(送仓计划接收开始时间)
  (select max(wd.receive_end_date) from na_warehouse_plan_detail wd where wd.order_id = o.id) as put_away_time, --货柜归还日
  case when o.deposit_aud > 0 then date_part('day', o.flag_contract_deposit_time - o.end_time) else 1 end as order_confirmed_cycle, --订单确认周期
  date_part('day',ad.etd - case when o.deposit_aud = 0 THEN o.end_time else o.flag_contract_deposit_time end) as lead_time, --工厂生产周期
  date_part('day', ad.etd - CASE WHEN o.is_needed_qc = 3 THEN cc.created_at ELSE o.flag_order_qc_time END) as container_port_cycle, --装柜送港周期
  date_part('day', ad.eta - cc.end_time) as custom_clear_cycle, --清关周期
  date_part('day', (select min(wd.receive_start_date) from na_warehouse_plan_detail wd where wd.order_id = o.id) - ad.eta) as container_arriving_cycle , --货柜到仓周期
  date_part('day', (select max(wd.receive_end_date) from na_warehouse_plan_detail wd where wd.order_id = o.id) - (select min(wd.receive_start_date) from na_warehouse_plan_detail wd where wd.order_id = o.id)) as unloading_cycle, --拆柜周期
  date_part('day', (select max(wd.receive_end_date) from na_warehouse_plan_detail wd where wd.order_id = o.id) - o.end_time) as order_cycle,o.status--订单总周期
  from na_purchase_contract o
  left join na_purchase_contract_deposit d on o.id = d.order_id
  LEFT JOIN na_flow_order_shipping_apply_detail ad on o.id = ad.order_id
  LEFT JOIN  na_order_shipping_plan osp on osp.order_numbers= o.order_number
  LEFT JOIN na_custom_clearance cc on o.id = cc.order_id;



drop VIEW na_view_sta_quality;
--质检统计表
CREATE VIEW na_view_sta_quality AS
select row_number() over() as id,o.department_id, o.department_cn_name,o.department_en_name,--部门
o.creator_id as buyer_id,o.creator_cn_name as buyer_cn_name,o.creator_en_name as buyer_en_name, --采购员
r.order_id,o.order_number,o.order_title,--订单号
o.vendor_id,o.vendor_cn_name,o.vendor_en_name, --供应商
t.product_id, p.sku, p.name as product_name,--产品
r.report_time, --报告日期
null as notify_qc_agent_date,  --质检通知日期(暂定)
r.result, --报告结果,
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '1') as issue_a,--问题A
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '2') as issue_b,--问题B
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '3') as issue_c,--问题C
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '4') as issue_d,--问题D
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '5') as issue_r,--问题E
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '6') as issue_f,--问题F
  (select count(1) from na_reports_product_trouble_detail d where d.reports_product_id = r.id and d.trouble_detail_id = '7') as issue_g,--问题G
r.file--报告原件
from na_reports_product t
LEFT JOIN na_product p on t.product_id = p.id
LEFT JOIN na_reports r on t.reports_id = r.id
left join na_purchase_contract o on r.order_id = o.id
where r.module_name ='ReportOrderInspection';

drop VIEW na_view_sta_problem;
--商品质量问题统计表
CREATE VIEW na_view_sta_problem AS

  select row_number() over() as id,d.trouble_ticket_id as ticket_no, --票号(待定)
    p.category_id, p.category_cn_name,p.category_en_name, --产品类目
    p.department_id,p.department_cn_name,p.department_en_name, --部门
    p.sku,tt.order_id, tt.order_number,
    tt.order_at, --下单时间
    tt.handle_status as order_sent,--订单发送状态（待确认）
    tt.sell_channel, --销售渠道（订单平台）
    tt.currency,tt.rate_aud_to_rmb,tt.rate_aud_to_usd, --币种，汇率
    tt.refund_amount_aud,tt.refund_amount_rmb,tt.refund_amount_usd, --退款金额
    tt.refund_at, --退款时间
    tt.approver_id,tt.approver_cn_name,tt.approver_en_name, --审核人
    d.code_main, --一级分类
    d.code_sub, --二级分类
    null as title, --问题标题（暂缺）
    null as description,--问题描述(暂缺)
    tt.created_at, --创建日期
    tt.vendor_id,tt.vendor_cn_name,tt.vendor_en_name --供应商
  from na_trouble_ticket_product_detail d
  LEFT JOIN na_product p on d.product_id = p.id
  LEFT JOIN na_trouble_ticket_product tp on d.trouble_ticket_product_id = tp.id
  LEFT JOIN na_trouble_ticket tt on d.trouble_ticket_id = tt.id
;


drop VIEW na_view_sta_problem_team;
--商品质量问题统计表(小组总结)
CREATE VIEW na_view_sta_problem_team AS
  select row_number() over() as id, t.* from(
    select p.department_id,p.department_cn_name,p.department_en_name, --部门
      p.sku,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) courier_issue_unknown_reason_amount_aud,--产品退回退款金额
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) courier_issue_unknown_reason_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) courier_issue_unknown_reason_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '1' then 1 else 0 end) courier_issue_unknown_reason_qty, --产品退回退款数量
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) courier_issue_lost_allied_amount_aud,--产品丢失（Allied）
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) courier_issue_lost_allied_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) courier_issue_lost_allied_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '2' then 1 else 0 end) courier_issue_lost_allied_qty,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '3' then tt.refund_amount_aud else 0 end) courier_issue_lost_aupost_amount_aud,--产品丢失(Australia Post)
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '3' then tt.refund_amount_rmb else 0 end) courier_issue_lost_aupost_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '3' then tt.refund_amount_usd else 0 end) courier_issue_lost_aupost_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '3' then 1 else 0 end) courier_issue_lost_aupost_qty,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '4' then tt.refund_amount_aud else 0 end) courier_issue_lost_fastway_amount_aud,--产品丢失(Fastway)
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '4' then tt.refund_amount_rmb else 0 end) courier_issue_lost_fastway_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '4' then tt.refund_amount_usd else 0 end) courier_issue_lost_fastway_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '4' then 1 else 0 end) courier_issue_lost_fastway_qty,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '5' then tt.refund_amount_aud else 0 end) courier_issue_damaged_amount_aud,--产品损坏
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '5' then tt.refund_amount_rmb else 0 end) courier_issue_damaged_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '5' then tt.refund_amount_usd else 0 end) courier_issue_damaged_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '5' then 1 else 0 end) courier_issue_damaged_qty,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '6' then tt.refund_amount_aud else 0 end) courier_issue_delayed_amount_aud,--产品派送延迟
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '6' then tt.refund_amount_rmb else 0 end) courier_issue_delayed_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '6' then tt.refund_amount_usd else 0 end) courier_issue_delayed_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '6' then 1 else 0 end) courier_issue_delayed_qty,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '7' then tt.refund_amount_aud else 0 end) courier_issue_lost_direct_amount_aud,--产品丢失(Direct)
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '7' then tt.refund_amount_rmb else 0 end) courier_issue_lost_direct_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '7' then tt.refund_amount_usd else 0 end) courier_issue_lost_direct_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='courier_issue' and d.trouble_detail_id = '7' then 1 else 0 end) courier_issue_lost_direct_qty,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) customer_order_cancelled_amount_aud,--客人原因(取消订单)
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) customer_order_cancelled_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) customer_order_cancelled_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '1' then 1 else 0 end) customer_order_cancelled_qty,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) customer_incorrect_address_amount_aud,--客人原因(地址有误)
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) customer_incorrect_address_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) customer_incorrect_address_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '2' then 1 else 0 end) customer_incorrect_address_qty,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '3' then tt.refund_amount_aud else 0 end) customer_ordered_incorrect_amount_aud,--客人原因(客人买错）
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '3' then tt.refund_amount_rmb else 0 end) customer_ordered_incorrect_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '3' then tt.refund_amount_usd else 0 end) customer_ordered_incorrect_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '3' then 1 else 0 end) customer_ordered_incorrect_qty,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '4' then tt.refund_amount_aud else 0 end) customer_payment_amount_aud,--客人原因(支付问题)
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '4' then tt.refund_amount_rmb else 0 end) customer_payment_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '4' then tt.refund_amount_usd else 0 end) customer_payment_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '4' then 1 else 0 end) customer_payment_qty,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '5' then tt.refund_amount_aud else 0 end) customer_price_diff_amount_aud,--客人原因(价差)
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '5' then tt.refund_amount_rmb else 0 end) customer_price_diff_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '5' then tt.refund_amount_usd else 0 end) customer_price_diff_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='customer_at_fault' and d.trouble_detail_id = '5' then 1 else 0 end) customer_price_diff_qty,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) dispatch_issue_wrong_item_amount_aud,--发货操作（产品发错）
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) dispatch_issue_wrong_item_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) dispatch_issue_wrong_item_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '1' then 1 else 0 end) dispatch_issue_wrong_item_qty,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) dispatch_issue_wrong_spare_part_amount_aud,--发货操作（配件发错）
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) dispatch_issue_wrong_spare_part_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) dispatch_issue_wrong_spare_part_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '2' then 1 else 0 end) dispatch_issue_wrong_spare_part_qty,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '3' then tt.refund_amount_aud else 0 end) dispatch_issue_not_dispatched_amount_aud,--发货操作（产品未发）
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '3' then tt.refund_amount_rmb else 0 end) dispatch_issue_not_dispatched_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '3' then tt.refund_amount_usd else 0 end) dispatch_issue_not_dispatched_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='dispatch_issue' and d.trouble_detail_id = '3' then 1 else 0 end) dispatch_issue_not_dispatched_qty,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) listing_issue_stock_out_amount_aud,--广告原因（缺货）
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) listing_issue_stock_out_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) listing_issue_stock_out_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '1' then 1 else 0 end) listing_issue_stock_out_qty,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) listing_issue_described_amount_aud,--广告原因（描述不符）
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) listing_issue_described_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) listing_issue_described_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '2' then 1 else 0 end) listing_issue_described_qty,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '3' then tt.refund_amount_aud else 0 end) listing_issue_no_delivery_area_amount_aud,--广告原因（非配送区域）
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '3' then tt.refund_amount_rmb else 0 end) listing_issue_no_delivery_area_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '3' then tt.refund_amount_usd else 0 end) listing_issue_no_delivery_area_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='listing_issue' and d.trouble_detail_id = '3' then 1 else 0 end) listing_issue_no_delivery_area_qty,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) others_system_error_amount_aud,--其它(系统错误）
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) others_system_error_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) others_system_error_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '1' then 1 else 0 end) others_system_error_qty,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) others_fraudulent_purchase_amount_aud,--其它（诈骗订单）
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) others_fraudulent_purchase_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) others_fraudulent_purchase_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '2' then 1 else 0 end) others_fraudulent_purchase_qty,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '3' then tt.refund_amount_aud else 0 end) others_poor_communication_amount_aud,--其它（沟通不良）
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '3' then tt.refund_amount_rmb else 0 end) others_poor_communication_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '3' then tt.refund_amount_usd else 0 end) others_poor_communication_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='others' and d.trouble_detail_id = '3' then 1 else 0 end) others_poor_communication_qty,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) product_quality_miss_damaged_parts_amount_aud,--产品质量（配件丢失或损坏）
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) product_quality_miss_damaged_parts_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) product_quality_miss_damaged_parts_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '1' then 1 else 0 end) product_quality_miss_damaged_parts_qty,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '2' then tt.refund_amount_aud else 0 end) product_quality_cannot_be_used_amount_aud,--产品质量（不能使用）
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '2' then tt.refund_amount_rmb else 0 end) product_quality_cannot_be_used_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '2' then tt.refund_amount_usd else 0 end) product_quality_cannot_be_used_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='product_quality' and d.trouble_detail_id = '2' then 1 else 0 end) product_quality_cannot_be_used_qty,
      sum(case when d.code_main='ticket' and d.code_sub='recall' and d.trouble_detail_id = '1' then tt.refund_amount_aud else 0 end) recall_amount_aud,--产品召回
      sum(case when d.code_main='ticket' and d.code_sub='recall' and d.trouble_detail_id = '1' then tt.refund_amount_rmb else 0 end) recall_amount_rmb,
      sum(case when d.code_main='ticket' and d.code_sub='recall' and d.trouble_detail_id = '1' then tt.refund_amount_usd else 0 end) recall_amount_usd,
      sum(case when d.code_main='ticket' and d.code_sub='recall' and d.trouble_detail_id = '1' then 1 else 0 end) recall_qty

    from na_trouble_ticket_product_detail d
      LEFT JOIN na_product p on d.product_id = p.id
      LEFT JOIN na_trouble_ticket tt on d.trouble_ticket_id = tt.id
    group by p.department_id,p.department_cn_name,p.department_en_name,p.sku
  ) t;

--采购合同选择采购计划明细
drop VIEW na_view_purchase_plan_detail;
CREATE VIEW na_view_purchase_plan_detail AS
  SELECT t.id, t.purchase_plan_id, t.product_id, t.sku, t.currency, t.price_aud, t.price_rmb, t.price_usd, t.moq, t.rate_aud_to_rmb, t.rate_aud_to_usd, t.prev_price_aud, t.prev_price_rmb, t.prev_price_usd, t.prev_moq, t.prev_rate_aud_to_rmb, t.prev_rate_aud_to_usd, t.origin_port_id, t.origin_port_cn_name, t.origin_port_en_name, t.destination_port_id, t.destination_port_cn_name, t.destination_port_en_name, t.order_qty, t.order_qty_carton, t.already_order_qty,
    (SELECT t2.total_order_qty FROM (SELECT na_purchase_plan_detail.product_id, sum(na_purchase_plan_detail.order_qty) AS total_order_qty
                                     FROM na_purchase_plan_detail
                                     GROUP BY na_purchase_plan_detail.product_id) t2
    WHERE t2.product_id= t.product_id) AS total_order_qty,
    pp.pcs_per_carton,
    m.vendor_id, m.vendor_cn_name, m.vendor_en_name,
    m.creator_id, m.creator_cn_name, m.creator_en_name
  FROM (na_purchase_plan_detail t
    JOIN na_purchase_plan m ON t.purchase_plan_id=m.id
    JOIN na_product p on t.product_id = p.id
    Join na_product_vendor_prop pp on p.id = pp.product_id)
  where t.order_qty is not null  and (t.order_qty > t.already_order_qty or t.already_order_qty is null) and  p.status = 1 and p.new_product = 2 and pp.risk_rating < 5 and t.hold = 2 and m.status = 1
;
