--扩充字段长度和去掉属性中的空格

ALTER TABLE "public"."na_flow_agent" 
  ALTER COLUMN "to_user_cn_name" TYPE varchar(50) COLLATE "pg_catalog"."default";

ALTER TABLE "public"."na_asn" RENAME COLUMN "flag_sync_status  " TO "flag_sync_status";

ALTER TABLE "public"."na_asn" RENAME COLUMN "flag_sync_date  " TO "flag_sync_date";


ALTER TABLE "public"."na_flow_custom_clearance" 
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;


--给收货通知柜详情添加订单相关信息字段

ALTER TABLE "public"."na_flow_asn_packing_detail" 
  ADD COLUMN "order_id" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_flow_asn_packing_detail"."order_id" IS '订单id';

COMMENT ON COLUMN "public"."na_flow_asn_packing_detail"."order_number" IS '订单号';


ALTER TABLE "public"."na_asn_packing_detail" 
  ADD COLUMN "order_id" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_asn_packing_detail"."order_id" IS '订单id';

COMMENT ON COLUMN "public"."na_asn_packing_detail"."order_number" IS '订单号';
