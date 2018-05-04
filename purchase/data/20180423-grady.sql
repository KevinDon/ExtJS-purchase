--送仓计划增加实货柜信息
ALTER TABLE "public"."na_asn" 
  ADD COLUMN "container_number" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "seals_number" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "container_type" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_asn"."container_number" IS '集装箱编号';

COMMENT ON COLUMN "public"."na_asn"."seals_number" IS '封印编号';

COMMENT ON COLUMN "public"."na_asn"."container_type" IS '柜型';


ALTER TABLE "public"."na_flow_asn" 
  ADD COLUMN "container_number" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "seals_number" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "container_type" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_flow_asn"."container_number" IS '集装箱编号';

COMMENT ON COLUMN "public"."na_flow_asn"."seals_number" IS '封印编号';

COMMENT ON COLUMN "public"."na_flow_asn"."container_type" IS '柜型';

ALTER TABLE "public"."na_flow_warehouse_plan" 
  ADD COLUMN "order_number" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "order_title" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_flow_warehouse_plan"."order_number" IS '订单编码';

COMMENT ON COLUMN "public"."na_flow_warehouse_plan"."order_title" IS '订单标题';

ALTER TABLE "public"."na_warehouse_plan" 
  ADD COLUMN "order_number" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "order_title" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_warehouse_plan"."order_number" IS '订单编码';

COMMENT ON COLUMN "public"."na_warehouse_plan"."order_title" IS '订单标题';



--在ASN业务表内添加同步标记字段
ALTER TABLE "public"."na_asn"
  ADD COLUMN "flag_sync_status  " int2,
  ADD COLUMN "flag_sync_date  " timestamp(6),
  ADD COLUMN "flag_sync_id" varchar(24);
COMMENT ON COLUMN "public"."na_asn"."flag_sync_status  " IS '同步状态';
COMMENT ON COLUMN "public"."na_asn"."flag_sync_date  " IS '同步时间';
COMMENT ON COLUMN "public"."na_asn"."flag_sync_id" IS '同步id';

ALTER TABLE "public"."na_asn"
  ADD COLUMN "flag_complete_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_complete_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_complete_time" timestamp(6) DEFAULT NULL;
COMMENT ON COLUMN "public"."na_asn"."flag_complete_status" IS '实收同步状态';
COMMENT ON COLUMN "public"."na_asn"."flag_complete_id" IS '实收同步id';
COMMENT ON COLUMN "public"."na_asn"."flag_complete_time" IS '实收同步时间';


--在ASN流程表内添加同步标记字段
ALTER TABLE "public"."na_flow_asn"
  ADD COLUMN "flag_sync_status  " int2,
  ADD COLUMN "flag_sync_date  " timestamp(6),
  ADD COLUMN "flag_sync_id" varchar(24);
COMMENT ON COLUMN "public"."na_flow_asn"."flag_sync_status  " IS '同步状态';
COMMENT ON COLUMN "public"."na_flow_asn"."flag_sync_date  " IS '同步时间';
COMMENT ON COLUMN "public"."na_flow_asn"."flag_sync_id" IS '同步id';

ALTER TABLE "public"."na_flow_asn"
  ADD COLUMN "flag_complete_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_complete_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "flag_complete_time" timestamp(6) DEFAULT NULL;
COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_status" IS '实收同步状态';
COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_id" IS '实收同步id';
COMMENT ON COLUMN "public"."na_flow_asn"."flag_complete_time" IS '实收同步时间';




