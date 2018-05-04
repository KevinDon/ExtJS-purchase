--创建成本订单表
-- ----------------------------
-- Table structure for na_cost_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_cost_order";
CREATE TABLE "public"."na_cost_order" (
  "id" varchar(24) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "cost_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "order_number" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "currency" int2 DEFAULT NULL,
  "rate_aud_to_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "rate_aud_to_usd" numeric(20,4) DEFAULT NULL::numeric,
  "price_cost_aud" numeric(20,4) DEFAULT NULL::numeric,
  "price_cost_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "price_cost_usd" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "port_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "charge_item_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "charge_item_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "charge_item_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "tariff_aud" numeric(20,4) DEFAULT NULL::numeric,
  "tariff_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "tariff_usd" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "custom_processing_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "other_fee_aud" numeric(20,4) DEFAULT NULL::numeric,
  "other_fee_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "other_fee_usd" numeric(20,4) DEFAULT NULL::numeric,
  "total_cost_aud" numeric(20,4) DEFAULT NULL::numeric,
  "total_cost_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "total_cost_usd" numeric(20,4) DEFAULT NULL::numeric,
  "gst_aud" numeric(20,4) DEFAULT NULL::numeric,
  "gst_rmb" numeric(20,4) DEFAULT NULL::numeric,
  "gst_usd" numeric(20,4) DEFAULT NULL::numeric,
  "order_title" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."na_cost_order"."cost_id" IS '成本ID';
COMMENT ON COLUMN "public"."na_cost_order"."order_id" IS '订单id';
COMMENT ON COLUMN "public"."na_cost_order"."order_number" IS '订单编码';
COMMENT ON COLUMN "public"."na_cost_order"."currency" IS '结算币种';
COMMENT ON COLUMN "public"."na_cost_order"."rate_aud_to_rmb" IS '人民币汇率';
COMMENT ON COLUMN "public"."na_cost_order"."rate_aud_to_usd" IS '美元汇率';
COMMENT ON COLUMN "public"."na_cost_order"."price_cost_aud" IS '采购成本（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."price_cost_rmb" IS '采购成本（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."price_cost_usd" IS '采购成本（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."port_fee_aud" IS '海运费用（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."port_fee_rmb" IS '海运费用（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."port_fee_usd" IS '海运费用（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."charge_item_fee_aud" IS '本地费用（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."charge_item_fee_rmb" IS '本地费用（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."charge_item_fee_usd" IS '本地费用（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."tariff_aud" IS '关税（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."tariff_rmb" IS '关税（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."tariff_usd" IS '关税（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."custom_processing_fee_aud" IS '电子行政费用（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."custom_processing_fee_rmb" IS '电子行政费用（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."custom_processing_fee_usd" IS '电子行政费用（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."other_fee_aud" IS '其它费用（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."other_fee_rmb" IS '其它费用（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."other_fee_usd" IS '其它费用（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."total_cost_aud" IS '总成本（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."total_cost_rmb" IS '总成本（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."total_cost_usd" IS '总成本（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."gst_aud" IS 'GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."gst_rmb" IS 'GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."gst_usd" IS 'GST（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."order_title" IS '订单标题';
COMMENT ON TABLE "public"."na_cost_order" IS '成本计算_产品成本';

-- ----------------------------
-- Primary Key structure for table na_cost_order
-- ----------------------------
ALTER TABLE "public"."na_cost_order" ADD CONSTRAINT "na_cost_product_cost_copy1_pkey" PRIMARY KEY ("id");
