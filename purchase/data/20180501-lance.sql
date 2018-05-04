--成本计算中增加装运件数和装运箱数
ALTER TABLE "public"."na_cost_product_cost"
  ADD COLUMN "order_id" varchar(24),
  ADD COLUMN "packing_qty" numeric(20,4),
  ADD COLUMN "packing_cartons" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product_cost"."order_id" IS '订单ID';
COMMENT ON COLUMN "public"."na_cost_product_cost"."packing_qty" IS '装运件数';
COMMENT ON COLUMN "public"."na_cost_product_cost"."packing_cartons" IS '装运箱数';

--成本计算中区分产品GST和本地费GST
ALTER TABLE "public"."na_cost_product_cost"
  ADD COLUMN "local_gst_aud" numeric(20,4),
  ADD COLUMN "local_gst_rmb" numeric(20,4),
  ADD COLUMN "local_gst_usd" numeric(20,4),
  ADD COLUMN "value_gst_aud" numeric(20,4),
  ADD COLUMN "value_gst_rmb" numeric(20,4),
  ADD COLUMN "value_gst_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product_cost"."gst_aud" IS '总GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."gst_rmb" IS '总GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."gst_usd" IS '总GST（USD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."local_gst_aud" IS '本地费GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."local_gst_rmb" IS '本地费GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."local_gst_usd" IS '本地费GST（USD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."value_gst_aud" IS '货值GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."value_gst_rmb" IS '货值GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."value_gst_usd" IS '货值GST（USD）';

ALTER TABLE "public"."na_cost_product_cost"
  ADD COLUMN "price_aud" numeric(20,4),
  ADD COLUMN "price_rmb" numeric(20,4),
  ADD COLUMN "price_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product_cost"."price_aud" IS '采购单价（AUD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."price_rmb" IS '采购单价（RMB）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."price_usd" IS '采购单价（USD）';


ALTER TABLE "public"."na_cost_order"
  ADD COLUMN "local_gst_aud" numeric(20,4),
  ADD COLUMN "local_gst_rmb" numeric(20,4),
  ADD COLUMN "local_gst_usd" numeric(20,4),
  ADD COLUMN "value_gst_aud" numeric(20,4),
  ADD COLUMN "value_gst_rmb" numeric(20,4),
  ADD COLUMN "value_gst_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_order"."gst_aud" IS '总GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."gst_rmb" IS '总GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."gst_usd" IS '总GST（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."local_gst_aud" IS '总本地费GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."local_gst_rmb" IS '总本地费GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."local_gst_usd" IS '总本地费GST（USD）';
COMMENT ON COLUMN "public"."na_cost_order"."value_gst_aud" IS '总货值GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_order"."value_gst_rmb" IS '总货值GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_order"."value_gst_usd" IS '总货值GST（USD）';

ALTER TABLE "public"."na_cost"
  ADD COLUMN "value_gst_aud" numeric(20,4),
  ADD COLUMN "value_gst_rmb" numeric(20,4),
  ADD COLUMN "value_gst_usd" numeric(20,4),
  ADD COLUMN "local_gst_aud" numeric(20,4),
  ADD COLUMN "local_gst_rmb" numeric(20,4),
  ADD COLUMN "local_gst_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost"."value_gst_aud" IS '货值GST（AUD）';
COMMENT ON COLUMN "public"."na_cost"."value_gst_rmb" IS '货值GST（RMB）';
COMMENT ON COLUMN "public"."na_cost"."value_gst_usd" IS '货值GST（USD）';
COMMENT ON COLUMN "public"."na_cost"."local_gst_aud" IS '本地费GST（AUD）';
COMMENT ON COLUMN "public"."na_cost"."local_gst_rmb" IS '本地费GST（RMB）';
COMMENT ON COLUMN "public"."na_cost"."local_gst_usd" IS '本地费GST（USD）';

ALTER TABLE "public"."na_cost_product"
  ADD COLUMN "packing_qty" numeric(20,4),
  ADD COLUMN "packing_cartons" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product"."packing_qty" IS '装柜件数';
COMMENT ON COLUMN "public"."na_cost_product"."packing_cartons" IS '装柜箱数';


ALTER TABLE "public"."na_fee_payment"
  ADD COLUMN "order_id" varchar(24);
COMMENT ON COLUMN "public"."na_fee_payment"."order_id" IS '订单ID';

ALTER TABLE "public"."na_flow_fee_payment"
  ADD COLUMN "order_id" varchar(24);
COMMENT ON COLUMN "public"."na_flow_fee_payment"."order_id" IS '订单ID';

--初始化
update na_flow_fee_payment t1 set order_id = (select id from na_purchase_contract where t1.order_number = order_number) WHERE order_id is null;
update na_fee_payment t1 set order_id = (select id from na_purchase_contract where t1.order_number = order_number) WHERE order_id is null;