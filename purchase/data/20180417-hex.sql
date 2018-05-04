--ASN表新增同步标记
ALTER TABLE "public"."na_asn"
  ADD COLUMN "flag_sync_status  " int2,
  ADD COLUMN "flag_sync_date  " timestamp(6);

COMMENT ON COLUMN "public"."na_asn"."flag_sync_status  " IS '同步状态';

COMMENT ON COLUMN "public"."na_asn"."flag_sync_date  " IS '同步时间';
ALTER TABLE "public"."na_asn"
  ADD COLUMN "flag_sync_id" varchar(24);

COMMENT ON COLUMN "public"."na_asn"."flag_sync_id" IS '同步id';


--合同正式表添加订单完成状态标记位
ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "flag_complete_status" int2,
  ADD COLUMN "flag_complete_id" varchar(24),
  ADD COLUMN "flag_complete_time" timestamp(6);

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_complete_status" IS '订单完成状态';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_complete_id" IS '订单完成id';

COMMENT ON COLUMN "public"."na_purchase_contract"."flag_complete_time" IS '订单完成时间';

--在合同流程表添加订单完成标记，成本计算标记，ASN创建标记
ALTER TABLE "public"."na_flow_purchase_contract"
  ADD COLUMN "flag_cost_status" int2,
  ADD COLUMN "flag_cost_id" varchar(24),
  ADD COLUMN "flag_cost_time" timestamp(6),
  ADD COLUMN "flag_asn_create_status" int2,
  ADD COLUMN "flag_asn_create_id" varchar(26),
  ADD COLUMN "flag_asn_create_time" timestamp(16);

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_cost_status" IS '成本计算完成标记';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_cost_id" IS '成本id';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_cost_time" IS '成本计算完成时间';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_asn_create_status" IS 'ASN创建标记';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_asn_create_id" IS 'ASN 的id';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_asn_create_time" IS 'ASN创建时间';

ALTER TABLE "public"."na_flow_purchase_contract"
  ADD COLUMN "flag_complete_status" int2,
  ADD COLUMN "flag_complete_id" varchar(24),
  ADD COLUMN "flag_complete_time" timestamp(6);

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_complete_status" IS '订单完成状态';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_complete_id" IS '订单完成id';

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."flag_complete_time" IS '订单完成时间';


--在ASN流程表内添加同步标记字段
ALTER TABLE "public"."na_flow_asn"
  ADD COLUMN "flag_sync_id" varchar(24);

COMMENT ON COLUMN "public"."na_flow_asn"."flag_sync_id" IS '同步id';

--修改正式表/流程表合同表字段名
ALTER TABLE "public"."na_purchase_contract" RENAME COLUMN "total_shipping_cbm" TO "total_packing_cbm";

COMMENT ON COLUMN "public"."na_purchase_contract"."total_packing_cbm" IS '装箱体积';


ALTER TABLE "public"."na_flow_purchase_contract" RENAME COLUMN "total_shipping_cbm" TO "total_packing_cbm";

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."total_packing_cbm" IS '装箱体积';

--修改正式表，流程表的清关单字段
ALTER TABLE "public"."na_custom_clearance" RENAME COLUMN "total_shipping_cbm" TO "total_packing_cbm";

COMMENT ON COLUMN "public"."na_custom_clearance"."total_packing_cbm" IS '装箱体积';

ALTER TABLE "public"."na_flow_custom_clearance" RENAME COLUMN "total_shipping_cbm" TO "total_packing_cbm";

COMMENT ON COLUMN "public"."na_flow_custom_clearance"."total_packing_cbm" IS '装箱体积';

--增加正式表，流程表的费用等级 字段
ALTER TABLE "public"."na_fee_payment"
  ADD COLUMN "orderNumber" varchar(50),
  ADD COLUMN "order_title" varchar(200);

COMMENT ON COLUMN "public"."na_fee_payment"."orderNumber" IS '订单编号';

COMMENT ON COLUMN "public"."na_fee_payment"."order_title" IS '订单标题';

ALTER TABLE "public"."na_flow_fee_payment"
  ADD COLUMN "orderNumber" varchar(50),
  ADD COLUMN "order_title" varchar(200);

COMMENT ON COLUMN "public"."na_flow_fee_payment"."orderNumber" IS '订单编号';

COMMENT ON COLUMN "public"."na_flow_fee_payment"."order_title" IS '订单标题';


--修改字段备注
COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_status" IS '同步实收状态';

COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_id" IS '同步实收id';

COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_time" IS '同步实收时间';

COMMENT ON COLUMN "public"."na_asn"."flag_complete_status" IS '同步实收状态';

COMMENT ON COLUMN "public"."na_asn"."flag_complete_id" IS '同步实收id';

COMMENT ON COLUMN "public"."na_asn"."flag_complete_time" IS '同步实收时间';
