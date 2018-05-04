--20180328 临时权限表 和 在流程授权表加 flow_code
ALTER TABLE "public"."na_flow_agent"
  ADD COLUMN "flow_code" varchar(100);

CREATE TABLE "public"."na_flow_agent_resource" (
  "id" varchar(24) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "user_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "resource_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "action" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "data" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "model" varchar(60) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "path" varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "business_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "from_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "to_time" timestamp(6) DEFAULT NULL::timestamp without time zone,
  "role_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  CONSTRAINT "na_flow_agent_resource_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "na_flow_agent_resource_business_id_key" FOREIGN KEY ("business_id") REFERENCES "public"."na_flow_agent" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "na_flow_agent_resource_resource_id_fkey" FOREIGN KEY ("resource_id") REFERENCES "public"."na_account_resource" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "na_flow_agent_resource_role_id_fkey" FOREIGN KEY ("role_id") REFERENCES "public"."na_account_role" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT "na_flow_agent_resource_user_id_fkey" FOREIGN KEY ("user_id") REFERENCES "public"."na_account_user" ("id") ON DELETE CASCADE ON UPDATE CASCADE
)
;

ALTER TABLE "public"."na_flow_agent_resource"
  OWNER TO "purchase";

CREATE INDEX "na_flow_agent_resource_id_idx" ON "public"."na_flow_agent_resource" USING btree (
  "resource_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE INDEX "na_flow_agent_resource_role_id_idx" ON "public"."na_flow_agent_resource" USING btree (
  "user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON COLUMN "public"."na_flow_agent_resource"."business_id" IS '流程代理表id';


ALTER TABLE "public"."na_flow_processes"ADD COLUMN "model" varchar(100);
COMMENT ON COLUMN "public"."na_flow_processes"."model" IS '角色权限model';
ALTER TABLE "public"."na_flow_agent" ADD COLUMN "cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL;
ALTER TABLE "public"."na_flow_agent" ADD COLUMN "en_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL;