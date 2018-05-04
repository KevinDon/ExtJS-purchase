-- ----------------------------
-- Table structure for na_sta_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_sta_order";
CREATE TABLE "public"."na_sta_order" (
  "id" varchar(64) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_title" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "vendor_product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "product_category_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "product_category_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "vendor_product_category_alias" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_index" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "vendor_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "vendor_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "vendor_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "origin_port_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "origin_port_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "origin_port_en_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "container_type" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "container_qty" numeric(20,4) DEFAULT NULL::numeric,
  "status" int2 DEFAULT NULL,
  "created_at" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "end_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "start_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "currency" int2 DEFAULT NULL,
  "contract_rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "contract_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "total_cbm" numeric(20,4) DEFAULT NULL::numeric,
  "total_packing_cbm" numeric(20,4) DEFAULT NULL::numeric,
  "total_price_aud" numeric(20,4) DEFAULT NULL::numeric,
  "total_price_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "total_price_usd" numeric(20,4) DEFAULT NULL::numeric,
  "estimated_eta" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "write_off_aud" numeric(20,4) DEFAULT NULL::numeric,
  "write_off_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "write_off_usd" numeric(20,4) DEFAULT NULL::numeric,
  "final_total_price_aud" numeric(20,4) DEFAULT NULL::numeric,
  "ex_work_aud" numeric(20,4) DEFAULT NULL::numeric,
  "ex_work_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "ex_work_usd" numeric(20,4) DEFAULT NULL::numeric,
  "final_total_price_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "final_total_price_usd" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_rate" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_aud" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_usd" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_type" int2 DEFAULT NULL,
  "deposit_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "balance_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "balance_rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "service_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "service_rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "cost_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "cost_rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "balance_aud" numeric(20,4) DEFAULT NULL::numeric,
  "balance_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "balance_usd" numeric(20,4) DEFAULT NULL::numeric,
  "deposit_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "balance_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "estimated_balance" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "service_provider_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "service_provider_cn_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "service_provider_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "agent_notification_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "ready_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "etd" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "eta" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "total_order_qty" int2 DEFAULT NULL,
  "picurrency" int2 DEFAULT NULL,
  "credit_terms" int2 DEFAULT NULL,
  "delivery_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "arrival_days" int2 DEFAULT NULL,
  "shipping_doc_received_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "shipping_doc_forwarded_date" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "charge_item_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "charge_item_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "charge_item_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "total_sales_price_aud" numeric(20,4) DEFAULT NULL::numeric,
  "total_sales_price_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "total_sales_price_usd" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "total_freight_aud" numeric(20,4) DEFAULT NULL::numeric,
  "total_freight_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "total_freight_usd" numeric(20,4) DEFAULT NULL::numeric,
  "electronic_processing_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "electronic_processing_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "electronic_processing_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "telex_released" int4 DEFAULT NULL,
  "tariff_aud" numeric(20,4) DEFAULT NULL::numeric,
  "tariff_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "tariff_usd" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "cost_currency" int2 DEFAULT NULL,
  "lead_time" int2 DEFAULT NULL,
  "sailing_days" int2 DEFAULT NULL,
  "department_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "department_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "department_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "creator_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "creator_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "creator_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "gst_aud" numeric(20,4) DEFAULT NULL::numeric,
  "gst_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "gst_usd" numeric(20,4) DEFAULT NULL::numeric,
  "total_cost_aud" numeric(20,4) DEFAULT NULL::numeric,
  "flag_asn_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "freight_gst_aud" numeric(20,4) DEFAULT NULL::numeric,
  "freight_gst_usd" numeric(20,4) DEFAULT NULL::numeric,
  "freight_gst_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "freight_insurance_aud" numeric(20,4) DEFAULT NULL::numeric,
  "freight_insurance_usd" numeric(20,4) DEFAULT NULL::numeric,
  "freight_insurance_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "month_eta" int2 DEFAULT NULL,
  "year_eta" int2 DEFAULT NULL,
  "cubic_weight" numeric(20,4) DEFAULT NULL,
  "total_shipping_cbm" numeric(20,4) DEFAULT NULL
)
;

--订单成本表增加三个体积字段
ALTER TABLE "public"."na_cost_order"
  ADD COLUMN "total_cbm" numeric(20,4),
  ADD COLUMN "total_packing_cbm" numeric(20,4),
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

COMMENT ON COLUMN "public"."na_cost_order"."total_cbm" IS '订单体积';

COMMENT ON COLUMN "public"."na_cost_order"."total_packing_cbm" IS '装柜体积';

COMMENT ON COLUMN "public"."na_cost_order"."total_shipping_cbm" IS '船运体积';

--在清关单增加成本计算标记位
ALTER TABLE "public"."na_custom_clearance_packing"
  ADD COLUMN "flag_cost_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_cost_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_cost_time" timestamp(6) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_custom_clearance_packing"."flag_cost_status" IS '成本计算完成标记';

COMMENT ON COLUMN "public"."na_custom_clearance_packing"."flag_cost_id" IS '成本id';

COMMENT ON COLUMN "public"."na_custom_clearance_packing"."flag_cost_time" IS '成本计算完成时间';

ALTER TABLE "public"."na_flow_custom_clearance_packing"
  ADD COLUMN "flag_cost_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_cost_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_cost_time" timestamp(6) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_flow_custom_clearance_packing"."flag_cost_status" IS '成本计算完成标记';

COMMENT ON COLUMN "public"."na_flow_custom_clearance_packing"."flag_cost_id" IS '成本id';

COMMENT ON COLUMN "public"."na_flow_custom_clearance_packing"."flag_cost_time" IS '成本计算完成时间';

--增加陈本表的船运体积字段
ALTER TABLE "public"."na_cost"
  ADD COLUMN "total_shipping_cbm" numeric(20,4) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_cost"."total_shipping_cbm" IS ' 实装体积';


--成本总表增加费用字段
ALTER TABLE "public"."na_cost"
  ADD COLUMN "payment_sum_aud" numeric(20,4),
  ADD COLUMN "payment_sum_usd" numeric(20,4),
  ADD COLUMN "payment_sum_rmb" numeric(20,4),
  ADD COLUMN "freight_sum_aud" numeric(20,4),
  ADD COLUMN "freight_sum_usd" numeric(20,4),
  ADD COLUMN "freight_sum_rmb" numeric(20,4),
  ADD COLUMN "freight_container_qty" int2,
  ADD COLUMN "charge_sum_aud" numeric(20,4),
  ADD COLUMN "charge_sum_usd" numeric(20,4),
  ADD COLUMN "charge_sum_rmb" numeric(20,4),
  ADD COLUMN "tariff_sum_rmb" numeric(20,4),
  ADD COLUMN "tariff_sum_usd" numeric(20,4),
  ADD COLUMN "tariff_sum_aud" numeric(20,4),
  ADD COLUMN "value_sum_aud" numeric(20,4) DEFAULT NULL,
  ADD COLUMN "value_sum_rmb" numeric(20,4) DEFAULT NULL,
  ADD COLUMN "value_sum_usd" numeric(20,4) DEFAULT NULL,
  ADD COLUMN "other_sum_aud" numeric(20,4),
  ADD COLUMN "other_sum_usd" numeric(20,4),
  ADD COLUMN "other_sum_rmb" numeric(20,4),
  ADD COLUMN "write_off_sum_aud" numeric(20,4),
  ADD COLUMN "write_off_sum_rmb" numeric(20,4),
  ADD COLUMN "write_off_sum_usd" numeric(20,4);

COMMENT ON COLUMN "public"."na_cost"."payment_sum_aud" IS '费用支付合计(AUD)';

COMMENT ON COLUMN "public"."na_cost"."payment_sum_usd" IS '费用支付合计(USD)';

COMMENT ON COLUMN "public"."na_cost"."payment_sum_rmb" IS '费用支付合计(RMB)';

COMMENT ON COLUMN "public"."na_cost"."freight_sum_aud" IS '海运费用合计(Aud)';

COMMENT ON COLUMN "public"."na_cost"."freight_sum_usd" IS '海运费用合计(USD)';

COMMENT ON COLUMN "public"."na_cost"."freight_sum_rmb" IS '海运费用合计(Rmb)';

COMMENT ON COLUMN "public"."na_cost"."freight_container_qty" IS '海运货柜数';

COMMENT ON COLUMN "public"."na_cost"."charge_sum_aud" IS '本地费用(AUD)';

COMMENT ON COLUMN "public"."na_cost"."charge_sum_usd" IS '本地费用(USD)';

COMMENT ON COLUMN "public"."na_cost"."charge_sum_rmb" IS '本地费用(RMB)';

COMMENT ON COLUMN "public"."na_cost"."tariff_sum_rmb" IS '关税(RMB)';

COMMENT ON COLUMN "public"."na_cost"."tariff_sum_usd" IS '关税(USD)';

COMMENT ON COLUMN "public"."na_cost"."tariff_sum_aud" IS '关税(AUD)';

COMMENT ON COLUMN "public"."na_cost"."value_sum_aud" IS '合同金额（AUD）';

COMMENT ON COLUMN "public"."na_cost"."value_sum_rmb" IS '合同金额（RMB）';

COMMENT ON COLUMN "public"."na_cost"."value_sum_usd" IS '合同金额（USD）';

COMMENT ON COLUMN "public"."na_cost"."other_sum_aud" IS '其他费用(AUD)';

COMMENT ON COLUMN "public"."na_cost"."other_sum_usd" IS '其他费用(USD)';

COMMENT ON COLUMN "public"."na_cost"."other_sum_rmb" IS '其他费用(RMB)';

COMMENT ON COLUMN "public"."na_cost"."write_off_sum_aud" IS '冲销费用(AUD)';

COMMENT ON COLUMN "public"."na_cost"."write_off_sum_rmb" IS '冲销费用(RMB)';

COMMENT ON COLUMN "public"."na_cost"."write_off_sum_usd" IS '冲销费用(USD)';