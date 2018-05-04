ALTER TABLE "public"."na_cost_product"
  ADD COLUMN "unit_cbm" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product"."unit_cbm" IS '单位体积';

ALTER TABLE "public"."na_cost_product_cost"
  ADD COLUMN "sub_total_cost_aud" numeric(20,4),
  ADD COLUMN "sub_total_cost_rmb" numeric(20,4),
  ADD COLUMN "sub_total_cost_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_cost_product_cost"."sub_total_cost_aud" IS '行成本总计，不含GST（AUD）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."sub_total_cost_rmb" IS '行成本总计，不含GST（RMB）';
COMMENT ON COLUMN "public"."na_cost_product_cost"."sub_total_cost_usd" IS '行成本总计，不含GST（USD）';


ALTER TABLE "public"."na_cost"
  ADD COLUMN "order_numbers" varchar(255);
COMMENT ON COLUMN "public"."na_cost"."order_numbers" IS '相关订单号';