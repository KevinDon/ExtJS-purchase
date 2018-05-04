--费用支付增加费用类型字段，值为费用登记类型
ALTER TABLE "public"."na_fee_payment"
  ADD COLUMN "fee_type" int2;
COMMENT ON COLUMN "public"."na_fee_payment"."fee_type" IS '支付类型（复制登记类型）';

ALTER TABLE "public"."na_flow_fee_payment"
  ADD COLUMN "fee_type" int2;
COMMENT ON COLUMN "public"."na_flow_fee_payment"."fee_type" IS '支付类型（复制登记类型）';

--初始化原始数据
UPDATE na_flow_fee_payment t1 set fee_type = (SELECT fee_type from na_flow_fee_register WHERE t1.fee_register_business_id=id ) WHERE t1.fee_type is null;
UPDATE na_fee_payment t1 set fee_type = (SELECT fee_type from na_fee_register WHERE t1.fee_register_id=id ) WHERE t1.fee_type is null;



ALTER TABLE "public"."na_flow_fee_payment"
  ADD COLUMN "vendor_id" varchar(24),
  ADD COLUMN "vendor_cn_name" varchar(100),
  ADD COLUMN "vendor_en_name" varchar(100);
COMMENT ON COLUMN "public"."na_flow_fee_payment"."vendor_id" IS '供应商ID';
COMMENT ON COLUMN "public"."na_flow_fee_payment"."vendor_cn_name" IS '供应商中文名';
COMMENT ON COLUMN "public"."na_flow_fee_payment"."vendor_en_name" IS '供应商英文名';

ALTER TABLE "public"."na_fee_payment"
  ADD COLUMN "vendor_id" varchar(24),
  ADD COLUMN "vendor_cn_name" varchar(100),
  ADD COLUMN "vendor_en_name" varchar(100);
COMMENT ON COLUMN "public"."na_fee_payment"."vendor_id" IS '供应商ID';
COMMENT ON COLUMN "public"."na_fee_payment"."vendor_cn_name" IS '供应商中文名';
COMMENT ON COLUMN "public"."na_fee_payment"."vendor_en_name" IS '供应商英文名';

--初始化原始数据
UPDATE na_flow_fee_payment t1 set
  vendor_id = (SELECT vendor_id from na_flow_fee_register WHERE t1.fee_register_business_id=id ) ,
	vendor_cn_name = (SELECT vendor_cn_name from na_flow_fee_register WHERE t1.fee_register_business_id=id ),
	vendor_en_name = (SELECT vendor_en_name from na_flow_fee_register WHERE t1.fee_register_business_id=id )
WHERE t1.vendor_id is null;

UPDATE na_fee_payment t1 set
  vendor_id = (SELECT vendor_id from na_fee_register WHERE t1.fee_register_id=id ) ,
	vendor_cn_name = (SELECT vendor_cn_name from na_fee_register WHERE t1.fee_register_id=id ),
	vendor_en_name = (SELECT vendor_en_name from na_fee_register WHERE t1.fee_register_id=id )
WHERE t1.vendor_id is null;