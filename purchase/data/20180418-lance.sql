--给Combo数据表增加类型, 1 COMBO及VAR SKU，2、VAR PARENT

ALTER TABLE "public"."na_product_combined"  ADD COLUMN "combo_type" int2;
COMMENT ON COLUMN "public"."na_product_combined"."combo_type" IS '组合产品类型：1 COMBO SKU，2、VAR SKU，3、VAR PARENT';
--初始化数据
UPDATE na_product_combined SET combo_type =1 WHERE combined_sku not like '%_PARENT' AND combo_type is null;
UPDATE na_product_combined SET combo_type =2 WHERE combined_sku like '%_VAR_' AND combo_type=1;
UPDATE na_product_combined SET combo_type =2 WHERE combined_sku like '%_VAR__' AND combo_type=1;
UPDATE na_product_combined SET combo_type =2 WHERE combined_sku like '%_VAR' AND combo_type=1;
