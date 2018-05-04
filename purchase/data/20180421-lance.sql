-- Combo 的Parent SKU增加产品分类
ALTER TABLE "public"."na_product_combined"
  ADD COLUMN "category_id" varchar(24),
  ADD COLUMN "category_cn_name" varchar(100),
  ADD COLUMN "category_en_name" varchar(100);

COMMENT ON COLUMN "public"."na_product_combined"."category_id" IS '产品分类';
COMMENT ON COLUMN "public"."na_product_combined"."category_cn_name" IS '产品分类中文名';
COMMENT ON COLUMN "public"."na_product_combined"."category_en_name" IS '产品分类英文名';

ALTER TABLE na_cronjob ALTER COLUMN "id" TYPE varchar(50);

INSERT INTO "na_cronjob"("id", "code", "name", "type", "year", "day_of_week", "month", "day_of_month", "hour", "minute", "second", "day_of_week_unit", "month_unit", "day_of_month_unit", "hour_unit", "minute_unit", "remind_type", "role_codes", "is_system", "status", "updated_at")
VALUES ('coj_product_combin_parent_sync', 'coj_product_combined_parent_sync', '同步产品组合PARENT', '2', '*', '*', '*', '*', '*', '30', '0', '1', '1', '1', '1', '2', '0', 'admin', '1', 1, '2018-04-21 00:00:00.000');
