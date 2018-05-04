ALTER TABLE "public"."na_cost_charge_item"
  ADD COLUMN "container_type" int2,
  ADD COLUMN "container_qty" numeric(20,4) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_cost_charge_item"."container_type" IS '柜型(1; Gp20, 2,Gp40, 3,Hq40 4,Lc)';

COMMENT ON COLUMN "public"."na_cost_charge_item"."container_qty" IS '货柜数量';

ALTER TABLE "public"."na_cost_charge_item"
  ADD COLUMN "received_price_aud" numeric(20,4) DEFAULT NULL,
  ADD COLUMN "received_price_rmb" numeric(20,4) DEFAULT NULL,
  ADD COLUMN "received_price_usd" numeric(20,4) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_cost_charge_item"."received_price_aud" IS '实收金额（AUD）';

COMMENT ON COLUMN "public"."na_cost_charge_item"."received_price_rmb" IS '实收金额（RMB）';

COMMENT ON COLUMN "public"."na_cost_charge_item"."received_price_usd" IS '实收金额（USD）';