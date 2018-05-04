--业务表发货计划添加清关标记
ALTER TABLE "public"."na_order_shipping_plan"
  ADD COLUMN "flag_custom_clearance_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_custom_clearance_time" timestamp(6) DEFAULT NULL,
  ADD COLUMN "flag_custom_clearance_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_order_shipping_plan"."flag_custom_clearance_status" IS '清关状态';

COMMENT ON COLUMN "public"."na_order_shipping_plan"."flag_custom_clearance_time" IS '清关时间';

COMMENT ON COLUMN "public"."na_order_shipping_plan"."flag_custom_clearance_id" IS '清关单号';

-- na_asn /na_flow_asn
-- flag_complete_status  int2  16 asn获取实收数完成状态
-- flag_complete_id varchar 24   asn获取实收数完成id
-- flag_complete_date timestamp asn获取实收数完成时间
ALTER TABLE "public"."na_asn"
  ADD COLUMN "flag_complete_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_complete_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_complete_time" timestamp(6) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_asn"."flag_complete_status" IS '订单完成状态';

COMMENT ON COLUMN "public"."na_asn"."flag_complete_id" IS '订单完成id';

COMMENT ON COLUMN "public"."na_asn"."flag_complete_time" IS '订单完成时间';

ALTER TABLE "public"."na_flow_asn"
  ADD COLUMN "flag_complete_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_complete_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_complete_time" timestamp(6) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_status" IS '订单完成状态';

COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_id" IS '订单完成id';

COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_time" IS '订单完成时间';