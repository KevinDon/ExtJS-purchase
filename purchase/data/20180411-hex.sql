--清关单增加实收体积字段
ALTER TABLE "public"."na_custom_clearance"
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

COMMENT ON COLUMN "public"."na_custom_clearance"."total_shipping_cbm" IS '实装体积';

ALTER TABLE "public"."na_flow_custom_clearance"
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

COMMENT ON COLUMN "public"."na_flow_custom_clearance"."total_shipping_cbm" IS '实装体积';



--在正式合同表创建成本计算标记和asn创建标记
ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "flag_cost_status" int2,
  ADD COLUMN "flag_cost_id" varchar(24),
  ADD COLUMN "flag_cost_time" timestamp(6),
  ADD COLUMN "flag_asn_create_status" int2,
  ADD COLUMN "flag_asn_create_id" varchar(26),
  ADD COLUMN "flag_asn_create_time" timestamp(16);

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_cost_status" IS '成本计算完成标记';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_cost_id" IS '成本id';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_cost_time" IS '成本计算完成时间';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_asn_create_status" IS 'ASN创建标记';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_asn_create_id" IS 'ASN 的id';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_asn_create_time" IS 'ASN创建时间';



