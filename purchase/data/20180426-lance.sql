--费用登记明细表增加　是否用于成本计算的字段
ALTER TABLE "public"."na_fee_register_detail"
  ADD COLUMN "apply_cost" int2;
COMMENT ON COLUMN "public"."na_fee_register_detail"."apply_cost" IS '用于成本计算';

ALTER TABLE "public"."na_flow_fee_register_detail"
  ADD COLUMN "apply_cost" int2;
COMMENT ON COLUMN "public"."na_flow_fee_register_detail"."apply_cost" IS '用于成本计算';