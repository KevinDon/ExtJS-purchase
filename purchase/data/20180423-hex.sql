--cost_product_cost增加字段
ALTER TABLE "public"."na_cost_product_cost"
  ADD COLUMN "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  ADD COLUMN "order_qty" int4 DEFAULT NULL;

COMMENT ON COLUMN "public"."na_cost_product_cost"."order_number" IS '订单编号';

COMMENT ON COLUMN "public"."na_cost_product_cost"."order_qty" IS '采购数量';