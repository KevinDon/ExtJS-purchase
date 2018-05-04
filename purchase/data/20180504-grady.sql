ALTER TABLE "public"."na_flow_balance_refund_detail" 
  ALTER COLUMN "asn_id" TYPE text COLLATE "pg_catalog"."default" USING "asn_id"::text,
  ALTER COLUMN "asn_number" TYPE text COLLATE "pg_catalog"."default" USING "asn_number"::text;