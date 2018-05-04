ALTER TABLE "public"."na_fee_register"
  ADD COLUMN "guarantee_letter_name" varchar(100),
  ADD COLUMN "contract_guarantee_letter_name" varchar(100);

COMMENT ON COLUMN "public"."na_fee_register"."guarantee_letter_name" IS '保函附件文件名';
COMMENT ON COLUMN "public"."na_fee_register"."contract_guarantee_letter_name" IS '合同履约担保函附件文件名';

ALTER TABLE "public"."na_flow_fee_register"
  ADD COLUMN "guarantee_letter_name" varchar(100),
  ADD COLUMN "contract_guarantee_letter_name" varchar(100);

COMMENT ON COLUMN "public"."na_flow_fee_register"."guarantee_letter_name" IS '保函附件文件名';
COMMENT ON COLUMN "public"."na_flow_fee_register"."contract_guarantee_letter_name" IS '合同履约担保函附件文件名';