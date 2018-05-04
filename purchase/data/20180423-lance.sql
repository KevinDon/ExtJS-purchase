--采购合同申请表中增加清关结束后的货值尾款
ALTER TABLE "public"."na_flow_purchase_contract"
  ADD COLUMN "adjust_value_balance_aud" numeric(20,4),
  ADD COLUMN "adjust_value_balance_rmb" numeric(20,4),
  ADD COLUMN "adjust_value_balance_usd" numeric(20,4);

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."total_other_aud" IS '其它费用（AUD）';
COMMENT ON COLUMN "public"."na_flow_purchase_contract"."total_other_deposit_aud" IS '其它费用定金（AUD）';
COMMENT ON COLUMN "public"."na_flow_purchase_contract"."adjust_value_balance_aud" IS '调整后货值尾款（AUD）';
COMMENT ON COLUMN "public"."na_flow_purchase_contract"."adjust_value_balance_rmb" IS '调整后货值尾款（RMB）';
COMMENT ON COLUMN "public"."na_flow_purchase_contract"."adjust_value_balance_usd" IS '调整后货值尾款（USD）';

--采购合同业务表中增加清关结束后的货值尾款
ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "adjust_value_balance_aud" numeric(20,4),
  ADD COLUMN "adjust_value_balance_rmb" numeric(20,4),
  ADD COLUMN "adjust_value_balance_usd" numeric(20,4);

COMMENT ON COLUMN "public"."na_purchase_contract"."total_other_aud" IS '其它费用（AUD）';
COMMENT ON COLUMN "public"."na_purchase_contract"."total_other_deposit_aud" IS '其它费用定金（AUD）';
COMMENT ON COLUMN "public"."na_purchase_contract"."adjust_value_balance_aud" IS '调整后货值尾款（AUD）';
COMMENT ON COLUMN "public"."na_purchase_contract"."adjust_value_balance_rmb" IS '调整后货值尾款（RMB）';
COMMENT ON COLUMN "public"."na_purchase_contract"."adjust_value_balance_usd" IS '调整后货值尾款（USD）';