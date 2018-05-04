-- 20170822 修改用户表字段名
ALTER TABLE public.na_account_user
    RENAME phone_number TO phone;

ALTER TABLE public.na_account_user
    RENAME extension_number TO extension;

-- 20170828
ALTER TABLE "public"."na_account_resource"
    ADD COLUMN "code" varchar(25),
    ADD COLUMN "function" varchar(255);

-- 20170904
ALTER TABLE "public"."na_account_user"
    ADD COLUMN "lang" varchar(5),
    ADD COLUMN "timezone" varchar(20);

-- 20170907
ALTER TABLE "public"."na_dictionary"
    ADD COLUMN "type" int2;

-- 20170911
ALTER TABLE "public"."na_account_user"
    DROP COLUMN "language";

-- 20170912
ALTER TABLE "public"."na_account_user"
    ADD COLUMN "date_format" varchar(19);

-- 20170913
ALTER TABLE "public"."na_dictionary"
ADD PRIMARY KEY ("id");


-- 20170915 供应商分类、服务商分类、产品分类

drop table IF EXISTS na_vendor_category;

/*==============================================================*/
/* Table: na_vendor_category                                    */
/*==============================================================*/
create table na_vendor_category (
    id                   VARCHAR(20)          not null,
    parent_id            VARCHAR(20)          null,
    cn_name              VARCHAR(100)         not null,
    en_name              VARCHAR(50)          null,
    leaf                 INT2                 null,
    level                INT2                 not null,
    path                 VARCHAR(50)          null,
    sort                 INT2                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_VENDOR_CATEGORY primary key (id)
);

comment on table na_vendor_category is
'供应商分类';

comment on column na_vendor_category.parent_id is
'父ID';

comment on column na_vendor_category.cn_name is
'中文名';

comment on column na_vendor_category.en_name is
'英文名';

comment on column na_vendor_category.leaf is
'叶子';

comment on column na_vendor_category.level is
'层级';

comment on column na_vendor_category.path is
'路径';

comment on column na_vendor_category.sort is
'排序';

comment on column na_vendor_category.status is
'状态';

comment on column na_vendor_category.creator_id is
'创建人ID';

comment on column na_vendor_category.created_at is
'创建时间';

comment on column na_vendor_category.operator_id is
'操作人Id';

comment on column na_vendor_category.operated_at is
'操作时间';

comment on column na_vendor_category.department_id is
'隶属部门';


drop table IF EXISTS na_shipping_agent_category;

/*==============================================================*/
/* Table: na_shipping_agent_category                            */
/*==============================================================*/
create table na_shipping_agent_category (
    id                   VARCHAR(20)          not null,
    parent_id            VARCHAR(20)          null,
    cn_name              VARCHAR(100)         not null,
    en_name              VARCHAR(50)          null,
    leaf                 INT2                 null,
    level                INT2                 not null,
    path                 VARCHAR(50)          null,
    sort                 INT2                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_SHIPPING_AGENT_CATEGORY primary key (id)
);

comment on table na_shipping_agent_category is
'服务商分类';

comment on column na_shipping_agent_category.parent_id is
'父ID';

comment on column na_shipping_agent_category.cn_name is
'中文名';

comment on column na_shipping_agent_category.en_name is
'英文名';

comment on column na_shipping_agent_category.leaf is
'叶子';

comment on column na_shipping_agent_category.level is
'层级';

comment on column na_shipping_agent_category.path is
'路径';

comment on column na_shipping_agent_category.sort is
'排序';

comment on column na_shipping_agent_category.status is
'状态';

comment on column na_shipping_agent_category.creator_id is
'创建人ID';

comment on column na_shipping_agent_category.created_at is
'创建时间';

comment on column na_shipping_agent_category.operator_id is
'操作人Id';

comment on column na_shipping_agent_category.operated_at is
'操作时间';

comment on column na_shipping_agent_category.department_id is
'隶属部门';


drop table IF EXISTS na_product_category;

/*==============================================================*/
/* Table: na_product_category                                   */
/*==============================================================*/
create table na_product_category (
    id                   VARCHAR(20)          not null,
    parent_id            VARCHAR(20)          null,
    cn_name              VARCHAR(100)         not null,
    en_name              VARCHAR(50)          null,
    leaf                 INT2                 null,
    level                INT2                 not null,
    path                 VARCHAR(50)          null,
    sort                 INT2                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_PRODUCT_CATEGORY primary key (id)
);

comment on table na_product_category is
'产品分类';

comment on column na_product_category.parent_id is
'父ID';

comment on column na_product_category.cn_name is
'中文名';

comment on column na_product_category.en_name is
'英文名';

comment on column na_product_category.leaf is
'叶子';

comment on column na_product_category.level is
'层级';

comment on column na_product_category.path is
'路径';

comment on column na_product_category.sort is
'排序';

comment on column na_product_category.status is
'状态';

comment on column na_product_category.creator_id is
'创建人ID';

comment on column na_product_category.created_at is
'创建时间';

comment on column na_product_category.operator_id is
'操作人Id';

comment on column na_product_category.operated_at is
'操作时间';

comment on column na_product_category.department_id is
'隶属部门';


-- 20170915 供应商档案、供应商产品分类信息、供应商收款账号(保函)、供应商联系人

drop table IF EXISTS na_vendor;

/*==============================================================*/
/* Table: na_vendor                                             */
/*==============================================================*/
create table na_vendor (
    id                   VARCHAR(20)          not null,
    category_id          VARCHAR(20)          null,
    name                 VARCHAR(100)         not null,
    buyer_id             VARCHAR(20)          null,
    buyer_name           VARCHAR(50)          null,
    director             VARCHAR(50)          null,
    address              VARCHAR(200)         null,
    abn                  VARCHAR(100)         null,
    website              VARCHAR(100)         null,
    rating               VARCHAR(10)          null,
    photos               TEXT                 null,
    files                TEXT                 null,
    source               INT2                 null,
    currency             VARCHAR(10)          null,
    order_serial_number  INT4                 null,
    payment_provision    TEXT                 null,
    dyn_content           TEXT                 null,
    dyn_fields1          TEXT                 null,
    dyn_fields2          TEXT                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operated_at          TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_VENDOR primary key (id)
);

comment on table na_vendor is
'供应商';

comment on column na_vendor.category_id is
'产品类别';

comment on column na_vendor.name is
'公司名称';

comment on column na_vendor.buyer_id is
'采购员ID';

comment on column na_vendor.buyer_name is
'采购员名称';

comment on column na_vendor.director is
'法人';

comment on column na_vendor.address is
'工厂地址';

comment on column na_vendor.abn is
'工商营业执照注册号';

comment on column na_vendor.website is
'公司网址';

comment on column na_vendor.rating is
'信誉等级';

comment on column na_vendor.photos is
'供应商照片';

comment on column na_vendor.files is
'资质文件';

comment on column na_vendor.source is
'供应商来源';

comment on column na_vendor.currency is
'结算币种';

comment on column na_vendor.order_serial_number is
'订单累计号';

comment on column na_vendor.payment_provision is
'付款条款';

comment on column na_vendor.dyn_content is
'自定义内容';

comment on column na_vendor.dyn_fields1 is
'预备后期要增加字段
';

comment on column na_vendor.dyn_fields2 is
'预备后期要增加字段
';

comment on column na_vendor.status is
'状态';

comment on column na_vendor.creator_id is
'创建人ID';

comment on column na_vendor.created_at is
'创建时间';

comment on column na_vendor.operated_at is
'操作时间';

comment on column na_vendor.operator_id is
'操作人Id';

comment on column na_vendor.department_id is
'隶属部门';


drop table IF EXISTS na_vendor_product_category;

/*==============================================================*/
/* Table: na_vendor_product_category                            */
/*==============================================================*/
create table na_vendor_product_category (
    id                   VARCHAR(20)          not null,
    vendor_id            VARCHAR(20)          null,
    vendor_category_id   VARCHAR(100)         not null,
    alias                VARCHAR(100)         null,
    product_serial_number INT4                 null,
    constraint PK_NA_VENDOR_PRODUCT_CATEGORY primary key (id)
);

comment on table na_vendor_product_category is
'供应商分类信息';

comment on column na_vendor_product_category.vendor_id is
'供应商ID';

comment on column na_vendor_product_category.vendor_category_id is
'分类ID';

comment on column na_vendor_product_category.alias is
'分类别名';

comment on column na_vendor_product_category.product_serial_number is
'批次号';


drop table IF EXISTS na_vendor_bank_detail;

/*==============================================================*/
/* Table: na_vendor_bank_detail                                 */
/*==============================================================*/
create table na_vendor_bank_detail (
    id                   VARCHAR(20)          not null,
    vendor_id            VARCHAR(20)          not null,
    cn_company_name      VARCHAR(100)         null,
    en_company_name      VARCHAR(100)         null,
    cn_company_address   VARCHAR(150)         null,
    en_company_address   VARCHAR(150)         null,
    beneficiary_bank     VARCHAR(100)         not null,
    beneficiary_bank_address VARCHAR(150)         null,
    swift_code           VARCHAR(100)         null,
    cnaps                VARCHAR(100)         null,
    bank_account         VARCHAR(100)         null,
    currency             VARCHAR(10)          null,
    guarantee_letter     VARCHAR(100)         null,
    contract_guarantee_letter VARCHAR(100)         null,
    constraint PK_NA_VENDOR_BANK_DETAIL primary key (id)
);

comment on table na_vendor_bank_detail is
'供应商收款账号(保函)';

comment on column na_vendor_bank_detail.vendor_id is
'供应商id';

comment on column na_vendor_bank_detail.cn_company_name is
'公司中文名';

comment on column na_vendor_bank_detail.en_company_name is
'公司英文名';

comment on column na_vendor_bank_detail.cn_company_address is
'公司地址中文';

comment on column na_vendor_bank_detail.en_company_address is
'公司地址英文';

comment on column na_vendor_bank_detail.beneficiary_bank is
'开户银行';

comment on column na_vendor_bank_detail.beneficiary_bank_address is
'开户银行地址';

comment on column na_vendor_bank_detail.swift_code is
'银行代号(swift code)';

comment on column na_vendor_bank_detail.cnaps is
'银行代号(CNAPS)';

comment on column na_vendor_bank_detail.bank_account is
'银行账号';

comment on column na_vendor_bank_detail.currency is
'收款币种:
1) USD 2) AUD 3) RMB
';

comment on column na_vendor_bank_detail.guarantee_letter is
'供应商提供的保函文件
';

comment on column na_vendor_bank_detail.contract_guarantee_letter is
'合同履约担保函';



drop table IF EXISTS na_vendor_contacts;

/*==============================================================*/
/* Table: na_vendor_contacts                                   */
/*==============================================================*/
create table na_vendor_contacts (
    id                   VARCHAR(20)          not null,
    vendor_id            VARCHAR(20)          not null,
    name                 VARCHAR(50)          not null,
    company_position     VARCHAR(50)          null,
    phone                VARCHAR(20)          null,
    mobile               VARCHAR(20)          null,
    fax                  VARCHAR(20)          null,
    skype                VARCHAR(100)         null,
    qq                   VARCHAR(30)          null,
    email                VARCHAR(100)         null,
    type                 INT2                 null,
    constraint PK_NA_VENDOR_CONTACTOR primary key (id)
);

comment on table na_vendor_contacts is
'供应商联系人';

comment on column na_vendor_contacts.vendor_id is
'供应商id';

comment on column na_vendor_contacts.name is
'联系人名称';

comment on column na_vendor_contacts.company_position is
'职位';

comment on column na_vendor_contacts.phone is
'联系电话';

comment on column na_vendor_contacts.mobile is
'手机';

comment on column na_vendor_contacts.fax is
'传真';

comment on column na_vendor_contacts.skype is
'Skype';

comment on column na_vendor_contacts.qq is
'QQ';

comment on column na_vendor_contacts.email is
'邮箱';

comment on column na_vendor_contacts.type is
'联系人类型：1）销售 2）财务 3）负责人
';


-- 20170915 产品档案、供应商产品属性表、产品证书、组合产品
drop table IF EXISTS na_product;

/*==============================================================*/
/* Table: na_product                                            */
/*==============================================================*/
create table na_product (
    id                   VARCHAR(20)          not null,
    sku                  VARCHAR(100)         null,
    name                 VARCHAR(100)         null,
    package_name         VARCHAR(100)         null,
    barcode              VARCHAR(30)          null,
    combined             INT2                 null,
    category_id          VARCHAR(20)          null,
    sub_category_id      VARCHAR(20)          null,
    color                VARCHAR(10)          null,
    model                VARCHAR(50)          null,
    style                VARCHAR(200)         null,
    length               DECIMAL(20,2)        null,
    width                DECIMAL(20,2)        null,
    height               DECIMAL(20,2)        null,
    cbm                  DECIMAL(20,2)        null,
    cubic_weight         DECIMAL(20,2)        null,
    net_weight           DECIMAL(20,2)        null,
    seasonal             VARCHAR(50)          null,
    indoor_outdoor       VARCHAR(10)          null,
    electrical_product   INT2                 null,
    power_requirements   VARCHAR(255)         null,
    mandatory            INT2                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_PRODUCT primary key (id)
);

comment on table na_product is
'产品基础资料';

comment on column na_product.name is
'名称';

comment on column na_product.package_name is
'包装名';

comment on column na_product.barcode is
'条码';

comment on column na_product.combined is
'是否组合产品';

comment on column na_product.category_id is
'大分类';

comment on column na_product.sub_category_id is
'子分类';

comment on column na_product.color is
'颜色';

comment on column na_product.model is
'型号';

comment on column na_product.style is
'规格';

comment on column na_product.length is
'长 (cm)';

comment on column na_product.width is
'宽 (cm)';

comment on column na_product.height is
'高 (cm)';

comment on column na_product.cbm is
'体积';

comment on column na_product.cubic_weight is
'体积重量';

comment on column na_product.net_weight is
'净重';

comment on column na_product.seasonal is
'是否季节性';

comment on column na_product.indoor_outdoor is
'室内/户外';

comment on column na_product.electrical_product is
'是否电器产品';

comment on column na_product.power_requirements is
'电源要求';

comment on column na_product.mandatory is
'强制认证';

comment on column na_product.status is
'状态';

comment on column na_product.creator_id is
'创建人ID';

comment on column na_product.created_at is
'创建时间';

comment on column na_product.operator_id is
'操作人Id';

comment on column na_product.operated_at is
'操作时间';

comment on column na_product.department_id is
'隶属部门';

drop table IF EXISTS na_vendor_product_prop;

/*==============================================================*/
/* Table: na_vendor_product_prop                                */
/*==============================================================*/
create table na_vendor_product_prop (
    id                   VARCHAR(20)          not null,
    product_id           VARCHAR(20)          null,
    sku                  VARCHAR(100)         null,
    vendor_id            VARCHAR(20)          null,
    risk_rating          INT2                 null,
    images               TEXT                 null,
    product_parameter    VARCHAR(255)         null,
    product_detail       VARCHAR(255)         null,
    product_link         VARCHAR(255)         null,
    keywords             VARCHAR(100)         null,
    master_carton_l      DECIMAL(20,2)        null,
    master_carton_w      DECIMAL(20,2)        null,
    master_carton_h      DECIMAL(20,2)        null,
    master_carton_cbm    DECIMAL(20,2)        null,
    master_carton_gross_weight DECIMAL(20,2)        null,
    master_carton_cubic_weight DECIMAL(20,2)        null,
    inner_carton_l       DECIMAL(20,2)        null,
    inner_carton_w       DECIMAL(20,2)        null,
    inner_carton_h       DECIMAL(20,2)        null,
    inner_carton_cbm     DECIMAL(20,2)        null,
    inner_carton_gross_weight DECIMAL(20,2)        null,
    inner_carton_cubic_weight DECIMAL(20,2)        null,
    inner_carton_net_weight DECIMAL(20,2)        null,
    master_carton_structure VARCHAR(10)          null,
    master_carton_weight VARCHAR(10)          null,
    pcs_per_carton       INT2                 null,
    pcs_per_pallets      INT2                 null,
    shipping_mark       VARCHAR(255)         null,
    user_manual          VARCHAR(255)         null,
    product_cert         TEXT                 null,
    recommended_carrier  VARCHAR(100)         null,
    moq                  INT4                 null,
    estimated_avg_postage DECIMAL(20,4)        null,
    loading_port         VARCHAR(20)          null,
    lead_time            INT2                 null,
    target_bin           DECIMAL(20,4)        null,
    constraint PK_NA_VENDOR_PRODUCT_PROP primary key (id)
);

comment on table na_vendor_product_prop is
'供应商产品属性表';

comment on column na_vendor_product_prop.product_id is
'产品id';

comment on column na_vendor_product_prop.sku is
'产品编码';

comment on column na_vendor_product_prop.vendor_id is
'供应商id';

comment on column na_vendor_product_prop.risk_rating is
'风险评级';

comment on column na_vendor_product_prop.images is
'图片';

comment on column na_vendor_product_prop.product_parameter is
'产品参数';

comment on column na_vendor_product_prop.product_detail is
'产品细节';

comment on column na_vendor_product_prop.product_link is
'产品链接';

comment on column na_vendor_product_prop.keywords is
'产品关键词';

comment on column na_vendor_product_prop.master_carton_l is
'外箱长 (cm)';

comment on column na_vendor_product_prop.master_carton_w is
'外箱宽 (cm)';

comment on column na_vendor_product_prop.master_carton_h is
'外箱高 (cm)';

comment on column na_vendor_product_prop.master_carton_cbm is
'外箱体积';

comment on column na_vendor_product_prop.master_carton_gross_weight is
'外箱毛重';

comment on column na_vendor_product_prop.master_carton_cubic_weight is
'外箱体积重量';

comment on column na_vendor_product_prop.inner_carton_l is
'内箱长 (cm)';

comment on column na_vendor_product_prop.inner_carton_w is
'内箱宽 (cm)';

comment on column na_vendor_product_prop.inner_carton_h is
'内箱高 (cm)';

comment on column na_vendor_product_prop.inner_carton_cbm is
'内箱体积';

comment on column na_vendor_product_prop.inner_carton_gross_weight is
'内箱毛重';

comment on column na_vendor_product_prop.inner_carton_cubic_weight is
'内箱体积重量';

comment on column na_vendor_product_prop.inner_carton_net_weight is
'内箱净重';

comment on column na_vendor_product_prop.master_carton_structure is
'外箱纸箱规格:
A=A是指五层瓦楞纸板
K=A意思是双瓦楞纸板
K=K是进口纸,五层黄色纸';

comment on column na_vendor_product_prop.master_carton_weight is
'外箱纸箱克重:
1)150g
2)180g';

comment on column na_vendor_product_prop.pcs_per_carton is
'件/箱';

comment on column na_vendor_product_prop.pcs_per_pallets is
'件/托';

comment on column na_vendor_product_prop.shipping_mark is
'唛头格式:
需要在订单合同时做PDF附件，并且加上 Job No.';

comment on column na_vendor_product_prop.user_manual is
'说明书';

comment on column na_vendor_product_prop.product_cert is
'产品证书';

comment on column na_vendor_product_prop.recommended_carrier is
'推荐物流公司';

comment on column na_vendor_product_prop.moq is
'起订量';

comment on column na_vendor_product_prop.estimated_avg_postage is
'预估平均邮费';

comment on column na_vendor_product_prop.loading_port is
'出货港口';

comment on column na_vendor_product_prop.lead_time is
'生产周期(days)';

comment on column na_vendor_product_prop.target_bin is
'目标售价';

drop table IF EXISTS na_product_certificate;

/*==============================================================*/
/* Table: na_product_certificate                                */
/*==============================================================*/
create table na_product_certificate (
    id                   VARCHAR(20)          not null,
    product_id           VARCHAR(20)          null,
    sku                  VARCHAR(100)         null,
    vendor_id            VARCHAR(20)          null,
    vendor_name          VARCHAR(100)         null,
    relevant_standard    VARCHAR(255)         null,
    "desc"               TEXT                 null,
    certificate_number   VARCHAR(100)         null,
    effective_date       DATE                 null,
    valid_until          DATE                 null,
    certificate_file     TEXT                 null
);

comment on table na_product_certificate is
'产品证书';

comment on column na_product_certificate.product_id is
'产品id';

comment on column na_product_certificate.sku is
'产品编码';

comment on column na_product_certificate.vendor_id is
'供应商id';

comment on column na_product_certificate.vendor_name is
'供应商名称';

comment on column na_product_certificate.relevant_standard is
'相关标准';

comment on column na_product_certificate."desc" is
'描述';

comment on column na_product_certificate.certificate_number is
'认证编号';

comment on column na_product_certificate.effective_date is
'生效日期';

comment on column na_product_certificate.valid_until is
'失效日期';

comment on column na_product_certificate.certificate_file is
'证书文件';

drop table IF EXISTS na_combined_product;

/*==============================================================*/
/* Table: na_combined_product                                   */
/*==============================================================*/
create table na_combined_product (
    id                   VARCHAR(20)          not null,
    combined_sku         VARCHAR(100)         null,
    combined_name        VARCHAR(100)         null,
    product_id           VARCHAR(20)          null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_COMBINED_PRODUCT primary key (id)
);

comment on table na_combined_product is
'组合产品';

comment on column na_combined_product.combined_sku is
'编码';

comment on column na_combined_product.combined_name is
'名称';

comment on column na_combined_product.product_id is
'子产品id';

comment on column na_combined_product.status is
'状态';

comment on column na_combined_product.creator_id is
'创建人ID';

comment on column na_combined_product.created_at is
'创建时间';

comment on column na_combined_product.operator_id is
'操作人Id';

comment on column na_combined_product.operated_at is
'操作时间';

comment on column na_combined_product.department_id is
'隶属部门';


-- 20170918
CREATE TABLE "public"."na_account_role_resource" (
"id" varchar(20) NOT NULL,
"role_id" varchar(20),
"resource_id" varchar(20),
PRIMARY KEY ("id"),
FOREIGN KEY ("role_id") REFERENCES "public"."na_account_role" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY ("resource_id") REFERENCES "public"."na_account_resource" ("id") ON DELETE CASCADE ON UPDATE CASCADE
)
WITH (OIDS=FALSE)
;

CREATE INDEX  ON "public"."na_account_role_resource" ("role_id");

CREATE INDEX  ON "public"."na_account_role_resource" ("resource_id");


-- 20170919
ALTER TABLE "public"."na_account_role_resource"
ADD COLUMN "model" varchar(60),
ADD COLUMN "action" varchar(100),
ADD COLUMN "data" varchar(20);


ALTER TABLE public.na_shipping_agent_category RENAME TO na_service_provider_category;
ALTER TABLE public.na_service_provider_category DROP CONSTRAINT pk_na_shipping_agent_category;
ALTER TABLE public.na_service_provider_category ADD CONSTRAINT pk_na_service_provider_category PRIMARY KEY (id);

ALTER TABLE public.na_product_category ALTER COLUMN path TYPE VARCHAR(255) USING path::VARCHAR(255);
ALTER TABLE public.na_service_provider_category ALTER COLUMN path TYPE VARCHAR(255) USING path::VARCHAR(255);
ALTER TABLE public.na_vendor_category ALTER COLUMN path TYPE VARCHAR(255) USING path::VARCHAR(255);


--20170921
ALTER TABLE "public"."na_account_department" DROP COLUMN "path";


ALTER TABLE public.na_vendor_product_prop RENAME TO na_product_vendor_prop;
ALTER TABLE public.na_product_vendor_prop DROP CONSTRAINT pk_na_vendor_product_prop;
ALTER TABLE public.na_product_vendor_prop ADD CONSTRAINT pk_na_product_vendor_prop PRIMARY KEY (id);


ALTER TABLE public.na_combined_product RENAME TO na_product_combined;
ALTER TABLE public.na_product_combined DROP CONSTRAINT pk_na_combined_product;
ALTER TABLE public.na_product_combined ADD CONSTRAINT pk_na_product_combined PRIMARY KEY (id);

ALTER TABLE "public"."na_account_resource" DROP COLUMN "path";

--20170925
ALTER TABLE "public"."na_product_category" DROP COLUMN "path";
ALTER TABLE "public"."na_service_provider_category" DROP COLUMN "path";
ALTER TABLE "public"."na_vendor_category" DROP COLUMN "path";

--20170926
ALTER TABLE public.na_vendor_bank_detail ALTER COLUMN beneficiary_bank DROP NOT NULL;

drop table IF EXISTS na_service_provider;

/*==============================================================*/
/* Table: na_service_provider                                   */
/*==============================================================*/
create table na_service_provider (
    id                   VARCHAR(20)          not null,
    category_id          VARCHAR(20)          null,
    name                 VARCHAR(100)         not null,
    buyer_id             VARCHAR(20)          null,
    buyer_name           VARCHAR(50)          null,
    director             VARCHAR(50)          null,
    address              VARCHAR(200)         null,
    abn                  VARCHAR(100)         null,
    website              VARCHAR(100)         null,
    rating               VARCHAR(10)          null,
    photos               TEXT                 null,
    files                TEXT                 null,
    source               INT2                 null,
    currency             VARCHAR(10)          null,
    payment_provision    TEXT                 null,
    dyn_content           TEXT                 null,
    dyn_fields1          TEXT                 null,
    dyn_fields2          TEXT                 null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_SERVICE_PROVIDER primary key (id)
);

comment on table na_service_provider is
'服务商档案';

comment on column na_service_provider.id is
'供应商编号';

comment on column na_service_provider.category_id is
'服务商类别ID';

comment on column na_service_provider.name is
'公司名称';

comment on column na_service_provider.buyer_id is
'采购员ID';

comment on column na_service_provider.buyer_name is
'采购员名称';

comment on column na_service_provider.director is
'法人';

comment on column na_service_provider.address is
'工厂地址';

comment on column na_service_provider.abn is
'工商营业执照注册号';

comment on column na_service_provider.website is
'公司网址';

comment on column na_service_provider.rating is
'信誉等级';

comment on column na_service_provider.photos is
'供应商照片';

comment on column na_service_provider.files is
'资质文件';

comment on column na_service_provider.source is
'供应商来源';

comment on column na_service_provider.currency is
'结算币种';

comment on column na_service_provider.payment_provision is
'付款条款';

comment on column na_service_provider.dyn_content is
'自定义内容';

comment on column na_service_provider.dyn_fields1 is
'预备后期要增加字段
';

comment on column na_service_provider.dyn_fields2 is
'预备后期要增加字段
';

comment on column na_service_provider.status is
'状态';

comment on column na_service_provider.creator_id is
'创建人ID';

comment on column na_service_provider.created_at is
'创建时间';

comment on column na_service_provider.operator_id is
'操作人Id';

comment on column na_service_provider.operated_at is
'操作时间';

comment on column na_service_provider.department_id is
'隶属部门';


drop table IF EXISTS na_service_provider_bank_detail;

/*==============================================================*/
/* Table: na_service_provider_bank_detail                       */
/*==============================================================*/
create table na_service_provider_bank_detail (
    id                   VARCHAR(20)          not null,
    service_provider_id  VARCHAR(20)          null,
    cn_company_name      VARCHAR(100)         null,
    en_company_name      VARCHAR(100)         null,
    cn_company_address   VARCHAR(150)         null,
    en_company_address   VARCHAR(150)         null,
    beneficiary_bank     VARCHAR(100)         null,
    beneficiary_bank_address VARCHAR(150)         null,
    swift_code           VARCHAR(100)         null,
    cnaps                VARCHAR(100)         null,
    bank_account         VARCHAR(100)         null,
    currency             VARCHAR(10)          null,
    guarantee_letter     VARCHAR(100)         null,
    contract_guarantee_letter VARCHAR(100)         null
);

comment on table na_service_provider_bank_detail is
'服务商收款信息';

comment on column na_service_provider_bank_detail.service_provider_id is
'服务商id';

comment on column na_service_provider_bank_detail.cn_company_name is
'公司中文名';

comment on column na_service_provider_bank_detail.en_company_name is
'公司英文名';

comment on column na_service_provider_bank_detail.cn_company_address is
'公司地址中文';

comment on column na_service_provider_bank_detail.en_company_address is
'公司地址英文';

comment on column na_service_provider_bank_detail.beneficiary_bank is
'开户银行';

comment on column na_service_provider_bank_detail.beneficiary_bank_address is
'开户银行地址';

comment on column na_service_provider_bank_detail.swift_code is
'银行代号(swift code)';

comment on column na_service_provider_bank_detail.cnaps is
'银行代号(CNAPS)';

comment on column na_service_provider_bank_detail.bank_account is
'银行账号';

comment on column na_service_provider_bank_detail.currency is
'收款币种:
1) USD 2) AUD 3) RMB
';

comment on column na_service_provider_bank_detail.guarantee_letter is
'供应商提供的保函文件
';

comment on column na_service_provider_bank_detail.contract_guarantee_letter is
'合同履约担保函';


drop table IF EXISTS na_service_provider_contacts;

/*==============================================================*/
/* Table: na_service_provider_contacts                          */
/*==============================================================*/
create table na_service_provider_contacts (
    id                   VARCHAR(20)          not null,
    service_provider_id  VARCHAR(20)          null,
    name                 VARCHAR(50)          not null,
    company_position     VARCHAR(50)          null,
    agent_company        VARCHAR(100)         null,
    port                 VARCHAR(50)          null,
    phone                VARCHAR(20)          null,
    mobile               VARCHAR(20)          null,
    fax                  VARCHAR(20)          null,
    skype                VARCHAR(100)         null,
    qq                   VARCHAR(30)          null,
    email                VARCHAR(100)         null,
    type                 INT2                 null,
    constraint PK_NA_SERVICE_PROVIDER_CONTACT primary key (id)
);

comment on table na_service_provider_contacts is
'服务商联系人';

comment on column na_service_provider_contacts.service_provider_id is
'服务商id';

comment on column na_service_provider_contacts.name is
'联系人';

comment on column na_service_provider_contacts.company_position is
'职位';

comment on column na_service_provider_contacts.agent_company is
'代理公司名';

comment on column na_service_provider_contacts.port is
'港口';

comment on column na_service_provider_contacts.phone is
'联系电话';

comment on column na_service_provider_contacts.mobile is
'手机';

comment on column na_service_provider_contacts.fax is
'传真';

comment on column na_service_provider_contacts.skype is
'Skype';

comment on column na_service_provider_contacts.qq is
'QQ';

comment on column na_service_provider_contacts.email is
'邮箱';

comment on column na_service_provider_contacts.type is
'联系人类型：1）销售 2）财务 3）负责人
';


drop table IF EXISTS na_new_product;

/*==============================================================*/
/* Table: na_new_product                                        */
/*==============================================================*/
create table na_new_product (
    id                   VARCHAR(20)          not null,
    sku                  VARCHAR(100)         null,
    name                 VARCHAR(100)         null,
    package_name         VARCHAR(100)         null,
    barcode              VARCHAR(30)          null,
    combined             INT2                 null,
    category_id          VARCHAR(20)          null,
    color                VARCHAR(10)          null,
    model                VARCHAR(50)          null,
    style                VARCHAR(200)         null,
    length               DECIMAL(20,2)        null,
    width                DECIMAL(20,2)        null,
    height               DECIMAL(20,2)        null,
    cbm                  DECIMAL(20,2)        null,
    cubic_weight         DECIMAL(20,2)        null,
    net_weight           DECIMAL(20,2)        null,
    seasonal             VARCHAR(50)          null,
    indoor_outdoor       VARCHAR(10)          null,
    electrical_product   INT2                 null,
    power_requirements   VARCHAR(255)         null,
    mandatory            INT2                 null,
    purchase_quantity    INT2                 null,
    images               TEXT                 null,
    product_parameter    VARCHAR(255)         null,
    product_detail       VARCHAR(255)         null,
    product_link         VARCHAR(255)         null,
    keywords             VARCHAR(100)         null,
    currency             VARCHAR(10)          null,
    price_aud            DECIMAL(20,4)        null,
    price_rmb            DECIMAL(20,4)        null,
    price_usd            DECIMAL(20,4)        null,
    exchange_rate_aud_to_rmb DECIMAL(20,4)         null,
    exchange_rate_aud_to_usd DECIMAL(20,4)         null,
    competitor_price     DECIMAL(20,4)        null,
    competitor_sales_volume INT4                 null,
    ebay_monthly_sales   DECIMAL(20,4)        null,
    vendor_id            VARCHAR(20)          null,
    shippping_mark       VARCHAR(255)         null,
    user_manual          VARCHAR(255)         null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_NEW_PRODUCT primary key (id)
);

comment on table na_new_product is
'新品档案
';

comment on column na_new_product.name is
'名称';

comment on column na_new_product.package_name is
'包装名';

comment on column na_new_product.barcode is
'条码';

comment on column na_new_product.combined is
'是否组合产品';

comment on column na_new_product.category_id is
'分类';

comment on column na_new_product.color is
'颜色';

comment on column na_new_product.model is
'型号';

comment on column na_new_product.style is
'规格';

comment on column na_new_product.length is
'长 (cm)';

comment on column na_new_product.width is
'宽 (cm)';

comment on column na_new_product.height is
'高 (cm)';

comment on column na_new_product.cbm is
'体积';

comment on column na_new_product.cubic_weight is
'体积重量';

comment on column na_new_product.net_weight is
'净重';

comment on column na_new_product.seasonal is
'是否季节性';

comment on column na_new_product.indoor_outdoor is
'室内/户外';

comment on column na_new_product.electrical_product is
'是否电器产品';

comment on column na_new_product.power_requirements is
'电源要求';

comment on column na_new_product.mandatory is
'强制认证';

comment on column na_new_product.purchase_quantity is
'采购数量';

comment on column na_new_product.images is
'图片';

comment on column na_new_product.product_parameter is
'产品参数';

comment on column na_new_product.product_detail is
'产品细节';

comment on column na_new_product.product_link is
'产品链接';

comment on column na_new_product.keywords is
'产品关键词';

comment on column na_new_product.currency is
'结算币种';

comment on column na_new_product.price_aud is
'报价（AUD）';

comment on column na_new_product.price_rmb is
'报价（RMB）';

comment on column na_new_product.price_usd is
'报价（USD）';

comment on column na_new_product.exchange_rate_aud_to_rmb is
'汇率 (AUD / RMB)';

comment on column na_new_product.exchange_rate_aud_to_usd is
'汇率 (AUD / USD)';

comment on column na_new_product.competitor_price is
'竞争对手卖价';

comment on column na_new_product.competitor_sales_volume is
'竞争对手销量';

comment on column na_new_product.ebay_monthly_sales is
'ebay月销售额';

comment on column na_new_product.vendor_id is
'供应商id';

comment on column na_new_product.shippping_mark is
'唛头格式:
需要在订单合同时做PDF附件，并且加上 Job No.';

comment on column na_new_product.user_manual is
'说明书';

comment on column na_new_product.status is
'状态';

comment on column na_new_product.creator_id is
'创建人ID';

comment on column na_new_product.created_at is
'创建时间';

comment on column na_new_product.operator_id is
'操作人Id';

comment on column na_new_product.operated_at is
'操作时间';

comment on column na_new_product.department_id is
'隶属部门';


drop table IF EXISTS na_new_product_bill;

/*==============================================================*/
/* Table: na_new_product_bill                                   */
/*==============================================================*/
create table na_new_product_bill (
    id                   VARCHAR(20)          not null,
    vendor_id            VARCHAR(50)          null,
    vendor_name          VARCHAR(50)          null,
    deposit_rate         DECIMAL(20,2)        null,
    terapeak_research    VARCHAR(255)         null,
    product_report       VARCHAR(255)         null,
    applied_date         TIMESTAMP            null,
    end_date             TIMESTAMP            null,
    applicant_id         VARCHAR(20)          null,
    department_id        VARCHAR(20)          null,
    status               INT2                 null,
    flow_status          INT2                 null,
    constraint PK_NA_NEW_PRODUCT_BILL primary key (id)
);

comment on table na_new_product_bill is
'新品开发单';

comment on column na_new_product_bill.id is
'单据流水号';

comment on column na_new_product_bill.vendor_id is
'供应商id';

comment on column na_new_product_bill.vendor_name is
'供应商名称';

comment on column na_new_product_bill.deposit_rate is
'订金率';

comment on column na_new_product_bill.terapeak_research is
'TP分析单';

comment on column na_new_product_bill.product_report is
'分析报告';

comment on column na_new_product_bill.applied_date is
'申请时间';

comment on column na_new_product_bill.end_date is
'完成时间';

comment on column na_new_product_bill.applicant_id is
'申请人ID';

comment on column na_new_product_bill.department_id is
'部门Id';

comment on column na_new_product_bill.status is
'状态
1）启用 2）禁用 3）删除
';

comment on column na_new_product_bill.flow_status is
'流程状态
1）审批 2）拒绝 3）通过
';

drop table IF EXISTS na_flow_new_product_detail;

/*==============================================================*/
/* Table: na_new_product_bill_detail                            */
/*==============================================================*/
create table na_new_product_bill_detail (
    id                   VARCHAR(20)          not null,
    bill_id              VARCHAR(20)          not null,
    product_id           VARCHAR(50)          null,
    constraint PK_NA_NEW_PRODUCT_BILL_DETAIL primary key (id)
);

comment on table na_flow_new_product_detail is
'新品开发单明细表';

comment on column na_flow_new_product_detail.id is
'id';

comment on column na_flow_new_product_detail.bill_id is
'单据流水号';

comment on column na_flow_new_product_detail.product_id is
'产品id';


--20170926
ALTER TABLE public.na_new_product_bill RENAME TO na_flow_new_product;

ALTER TABLE public.na_new_product_bill_detail RENAME TO na_flow_new_product_detail;

ALTER TABLE public.na_flow_new_product_detail RENAME COLUMN bill_id TO business_id;

ALTER TABLE public.na_flow_new_product RENAME COLUMN applied_date TO apply_time;

ALTER TABLE public.na_flow_new_product RENAME COLUMN end_date TO end_time;


-- 20170929
ALTER TABLE "public"."na_account_role_resource" ADD COLUMN "path" varchar(255);

ALTER TABLE "public"."na_account_role" ADD COLUMN "code" varchar(50), ADD UNIQUE ("code");

-- 20171007
ALTER TABLE "public"."na_flow_new_product" ADD COLUMN "created_at" timestamp NULL;
ALTER TABLE "public"."na_flow_new_product" ADD COLUMN "applicant_name" varchar(50),	ADD COLUMN "department_name" varchar(50);

-- 20171009
DROP TABLE public.na_new_product;
DROP TABLE public.na_flow_new_product_detail;
DROP TABLE public.na_flow_new_product;

/*==============================================================*/
/* Table: na_new_product                                        */
/*==============================================================*/
create table na_new_product (
    id                   VARCHAR(20)          not null,
    sku                  VARCHAR(100)         null,
    name                 VARCHAR(100)         null,
    package_name         VARCHAR(100)         null,
    barcode              VARCHAR(30)          null,
    combined             INT2                 null,
    category_id          VARCHAR(20)          null,
    color                VARCHAR(10)          null,
    model                VARCHAR(50)          null,
    style                VARCHAR(200)         null,
    length               DECIMAL(20,2)        null,
    width                DECIMAL(20,2)        null,
    height               DECIMAL(20,2)        null,
    cbm                  DECIMAL(20,2)        null,
    cubic_weight         DECIMAL(20,2)        null,
    net_weight           DECIMAL(20,2)        null,
    seasonal             VARCHAR(50)          null,
    indoor_outdoor       VARCHAR(10)          null,
    electrical_product   INT2                 null,
    power_requirements   VARCHAR(255)         null,
    mandatory            INT2                 null,
    images               TEXT                 null,
    product_parameter    VARCHAR(255)         null,
    product_detail       VARCHAR(255)         null,
    product_link         VARCHAR(255)         null,
    keywords             VARCHAR(100)         null,
    competitor_price     DECIMAL(20,4)        null,
    competitor_sale_record INT4                 null,
    ebay_monthly_sales   DECIMAL(20,4)        null,
    product_predict_profit DECIMAL(20,4)        null,
    currency             VARCHAR(10)          null,
    price_aud            DECIMAL(20,4)        null,
    price_rmb            DECIMAL(20,4)        null,
    price_usd            DECIMAL(20,4)        null,
    exchange_rate_aud_rmb DECIMAL(20,4)        null,
    exchange_rate_aud_usd DECIMAL(20,4)        null,
    order_qty            INT4                 null,
    order_value          DECIMAL(20,4)        null,
    vendor_id            VARCHAR(20)          null,
    shippping_mark       VARCHAR(255)         null,
    user_manual          VARCHAR(255)         null,
    terapeak_research    VARCHAR(255)         null,
    product_report       VARCHAR(255)         null,
    status               INT2                 null,
    creator_id           VARCHAR(20)          null,
    created_at           TIMESTAMP            null,
    operator_id          VARCHAR(20)          null,
    operated_at          TIMESTAMP            null,
    department_id        VARCHAR(20)          null,
    constraint PK_NA_NEW_PRODUCT primary key (id)
);

comment on table na_new_product is
'新品档案
';

comment on column na_new_product.name is
'名称';

comment on column na_new_product.package_name is
'包装名';

comment on column na_new_product.barcode is
'条码';

comment on column na_new_product.combined is
'是否组合产品';

comment on column na_new_product.category_id is
'分类';

comment on column na_new_product.color is
'颜色';

comment on column na_new_product.model is
'型号';

comment on column na_new_product.style is
'规格';

comment on column na_new_product.length is
'长 (cm)';

comment on column na_new_product.width is
'宽 (cm)';

comment on column na_new_product.height is
'高 (cm)';

comment on column na_new_product.cbm is
'体积';

comment on column na_new_product.cubic_weight is
'体积重量';

comment on column na_new_product.net_weight is
'净重';

comment on column na_new_product.seasonal is
'是否季节性';

comment on column na_new_product.indoor_outdoor is
'室内/户外';

comment on column na_new_product.electrical_product is
'是否电器产品';

comment on column na_new_product.power_requirements is
'电源要求';

comment on column na_new_product.mandatory is
'强制认证';

comment on column na_new_product.images is
'图片';

comment on column na_new_product.product_parameter is
'产品参数';

comment on column na_new_product.product_detail is
'产品细节';

comment on column na_new_product.product_link is
'产品链接';

comment on column na_new_product.keywords is
'产品关键词';

comment on column na_new_product.competitor_price is
'竞争对手卖价';

comment on column na_new_product.competitor_sale_record is
'竞争对手销量';

comment on column na_new_product.ebay_monthly_sales is
'eBay月销售额';

comment on column na_new_product.product_predict_profit is
'产品预计利润';

comment on column na_new_product.currency is
'结算币种';

comment on column na_new_product.price_aud is
'报价（AUD）';

comment on column na_new_product.price_rmb is
'报价（RMB）';

comment on column na_new_product.price_usd is
'报价（USD）';

comment on column na_new_product.exchange_rate_aud_rmb is
'人民币汇率';

comment on column na_new_product.exchange_rate_aud_usd is
'美元汇率';

comment on column na_new_product.order_qty is
'采购数量';

comment on column na_new_product.order_value is
'采购货值';

comment on column na_new_product.vendor_id is
'供应商id';

comment on column na_new_product.shippping_mark is
'唛头格式:
需要在订单合同时做PDF附件，并且加上 Job No.';

comment on column na_new_product.user_manual is
'说明书';

comment on column na_new_product.terapeak_research is
'TP分析单';

comment on column na_new_product.product_report is
'分析报告';

comment on column na_new_product.status is
'状态';

comment on column na_new_product.creator_id is
'创建人ID';

comment on column na_new_product.created_at is
'创建时间';

comment on column na_new_product.operator_id is
'操作人Id';

comment on column na_new_product.operated_at is
'操作时间';

comment on column na_new_product.department_id is
'隶属部门';

drop table na_flow_new_product;

/*==============================================================*/
/* Table: na_flow_new_product                                   */
/*==============================================================*/
create table na_flow_new_product (
    id                   VARCHAR(20)          not null,
    vendor_id            VARCHAR(50)          null,
    vendor_name          VARCHAR(50)          null,
    deposit_rate         DECIMAL(20,2)        null,
    created_at           TIMESTAMP            null,
    apply_time           TIMESTAMP            null,
    end_time             TIMESTAMP            null,
    applicant_id         VARCHAR(20)          null,
    applicant_name       VARCHAR(20)          null,
    department_id        VARCHAR(20)          null,
    department_name      VARCHAR(20)          null,
    status               INT2                 null,
    flow_status          INT2                 null,
    constraint PK_NA_FLOW_NEW_PRODUCT primary key (id)
);

comment on table na_flow_new_product is
'新品开发单';

comment on column na_flow_new_product.id is
'单据流水号';

comment on column na_flow_new_product.vendor_id is
'供应商id';

comment on column na_flow_new_product.vendor_name is
'供应商名称';

comment on column na_flow_new_product.deposit_rate is
'订金率';

comment on column na_flow_new_product.created_at is
'创建时间';

comment on column na_flow_new_product.apply_time is
'申请时间';

comment on column na_flow_new_product.end_time is
'完成时间';

comment on column na_flow_new_product.applicant_id is
'申请人ID';

comment on column na_flow_new_product.applicant_name is
'申请人名称';

comment on column na_flow_new_product.department_id is
'部门Id';

comment on column na_flow_new_product.department_name is
'部门名称';

comment on column na_flow_new_product.status is
'状态
1）启用 2）禁用 3）删除
';

comment on column na_flow_new_product.flow_status is
'流程状态
1）审批 2）拒绝 3）通过
';

/*==============================================================*/
/* Table: na_flow_new_product_detail                            */
/*==============================================================*/
create table na_flow_new_product_detail (
    id                   VARCHAR(20)          not null,
    business_id          VARCHAR(20)          not null,
    product_id           VARCHAR(50)          null,
    constraint PK_NA_FLOW_NEW_PRODUCT_DETAIL primary key (id)
);

comment on table na_flow_new_product_detail is
'新品开发单明细表';

comment on column na_flow_new_product_detail.id is
'id';

comment on column na_flow_new_product_detail.business_id is
'单据流水号';

comment on column na_flow_new_product_detail.product_id is
'产品id';


ALTER TABLE public.na_flow_new_product ADD applicant_en_name VARCHAR(50) NULL;
ALTER TABLE public.na_flow_new_product ADD department_en_name VARCHAR(50) NULL;
ALTER TABLE public.na_flow_new_product RENAME COLUMN applicant_name TO applicant_cn_name;
ALTER TABLE public.na_flow_new_product ALTER COLUMN applicant_cn_name TYPE VARCHAR(50) USING applicant_cn_name::VARCHAR(50);
ALTER TABLE public.na_flow_new_product RENAME COLUMN department_name TO department_cn_name;
ALTER TABLE public.na_flow_new_product ALTER COLUMN department_cn_name TYPE VARCHAR(50) USING department_cn_name::VARCHAR(50);
ALTER TABLE public.na_flow_new_product ADD process_instance_id VARCHAR(64) NULL;

-- 20171010
drop table if EXISTS na_flow_operator_history;

/*==============================================================*/
/* Table: na_flow_operator_history                              */
/*==============================================================*/
create table na_flow_operator_history (
    id                   VARCHAR(20)          not null,
    business_id  VARCHAR(20)          not null,
    operator_id          VARCHAR(20)          null,
    operator_cn_name     VARCHAR(20)          null,
    operator_en_name     VARCHAR(20)          null,
    type                 INT2                 null,
    created_at          TIMESTAMP            null,
    remark               TEXT                 null,
    data                 TEXT                 null,
    constraint PK_NA_FLOW_OPERATOR_HISTORY primary key (id)
);

comment on table na_flow_operator_history is
'流程处理历史表';

comment on column na_flow_operator_history.business_id is
'业务id';

comment on column na_flow_operator_history.operator_id is
'操作人id';

comment on column na_flow_operator_history.operator_cn_name is
'操作人中文名';

comment on column na_flow_operator_history.operator_en_name is
'操作人英文名';

comment on column na_flow_operator_history.type is
'操作类型';

comment on column na_flow_operator_history.created_at is
'操作时间';

comment on column na_flow_operator_history.remark is
'审批备注';

comment on column na_flow_operator_history.data is
'数据';


-- 20171011
--供应商分类
ALTER TABLE public.na_vendor_category ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_vendor_category ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_vendor_category ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_vendor_category ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_vendor_category ADD department_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_vendor_category ADD department_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor_category.department_en_name IS '隶属部门英文名';

--供应商
ALTER TABLE public.na_vendor DROP name;
ALTER TABLE public.na_vendor DROP buyer_name;
ALTER TABLE public.na_vendor ADD cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.cn_name IS '供应商中文名';
ALTER TABLE public.na_vendor ADD en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.en_name IS '供应商英文名';
ALTER TABLE public.na_vendor ADD category_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.category_cn_name IS '供应商类别中文名';
ALTER TABLE public.na_vendor ADD category_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.category_en_name IS '供应商类别英文名';
ALTER TABLE public.na_vendor ADD buyer_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.buyer_cn_name IS '采购员中文名';
ALTER TABLE public.na_vendor ADD buyer_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.buyer_en_name IS '采购员英文名';
ALTER TABLE public.na_vendor ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_vendor ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_vendor ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_vendor ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_vendor ADD department_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_vendor ADD department_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_vendor.department_en_name IS '隶属部门英文名';
COMMENT ON COLUMN public.na_vendor.department_id IS '隶属部门ID';


--服务商分类
ALTER TABLE public.na_service_provider_category ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_service_provider_category ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_service_provider_category ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_service_provider_category ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_service_provider_category ADD department_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_service_provider_category ADD department_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider_category.department_en_name IS '隶属部门英文名';

--服务商
ALTER TABLE public.na_service_provider DROP name;
ALTER TABLE public.na_service_provider DROP buyer_name;
ALTER TABLE public.na_service_provider ADD cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.cn_name IS '服务商中文名';
ALTER TABLE public.na_service_provider ADD en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.en_name IS '服务商英文名';
ALTER TABLE public.na_service_provider ADD category_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.category_cn_name IS '服务商类别中文名';
ALTER TABLE public.na_service_provider ADD category_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.category_en_name IS '服务商类别英文名';
ALTER TABLE public.na_service_provider ADD buyer_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.buyer_cn_name IS '采购员中文名';
ALTER TABLE public.na_service_provider ADD buyer_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.buyer_en_name IS '采购员英文名';
ALTER TABLE public.na_service_provider ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_service_provider ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_service_provider ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_service_provider ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_service_provider.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_service_provider ADD department_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_service_provider ADD department_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_service_provider.department_en_name IS '隶属部门英文名';
COMMENT ON COLUMN public.na_service_provider.department_id IS '隶属部门ID';


--产品分类
ALTER TABLE public.na_product_category ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_product_category ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_product_category ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_product_category ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_product_category ADD department_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_product_category ADD department_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_category.department_en_name IS '隶属部门英文名';

--产品
ALTER TABLE public.na_product DROP sub_category_id;
ALTER TABLE public.na_product ADD category_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_product.category_cn_name IS '产品分类中文名';
ALTER TABLE public.na_product ADD category_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_product.category_en_name IS '产品分类别英文名';
ALTER TABLE public.na_product ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_product ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_product ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_product ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_product ADD department_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_product.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_product ADD department_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_product.department_en_name IS '隶属部门英文名';
COMMENT ON COLUMN public.na_product.department_id IS '隶属部门ID';


--新品档案
ALTER TABLE public.na_new_product DROP order_qty;
ALTER TABLE public.na_new_product DROP order_value;
ALTER TABLE public.na_new_product ADD category_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_new_product.category_cn_name IS '产品分类中文名';
ALTER TABLE public.na_new_product ADD category_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_new_product.category_en_name IS '产品分类别英文名';
ALTER TABLE public.na_new_product ADD vendor_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.vendor_cn_name IS '供应商中文名';
ALTER TABLE public.na_new_product ADD vendor_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.vendor_en_name IS '供应商英文名';
ALTER TABLE public.na_new_product ADD creator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_new_product ADD creator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_new_product ADD operator_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.operator_cn_name IS '操作人中文名';
ALTER TABLE public.na_new_product ADD operator_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_new_product.operator_en_name IS '操作人英文名';
ALTER TABLE public.na_new_product ADD department_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_new_product.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_new_product ADD department_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_new_product.department_en_name IS '隶属部门英文名';
COMMENT ON COLUMN public.na_new_product.department_id IS '隶属部门ID';



--20171012
--新品
ALTER TABLE public.na_new_product DROP competitor_price;
ALTER TABLE public.na_new_product DROP competitor_sale_record;
ALTER TABLE public.na_new_product DROP ebay_monthly_sales;
ALTER TABLE public.na_new_product DROP product_predict_profit;
ALTER TABLE public.na_new_product DROP price_aud;
ALTER TABLE public.na_new_product DROP price_rmb;
ALTER TABLE public.na_new_product DROP price_usd;
ALTER TABLE public.na_new_product DROP exchange_rate_aud_rmb;
ALTER TABLE public.na_new_product DROP exchange_rate_aud_usd;
ALTER TABLE public.na_new_product DROP order_qty;
ALTER TABLE public.na_new_product DROP order_value;

--新品开发流明细表
ALTER TABLE public.na_flow_new_product_detail ADD currency VARCHAR(10) NULL;
ALTER TABLE public.na_flow_new_product_detail ADD price_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD price_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD price_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD exchange_rate_aud_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD exchange_rate_aud_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD order_qty INT4 null;
ALTER TABLE public.na_flow_new_product_detail ADD order_value_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD order_value_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD order_value_usd  DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD product_predict_profit_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD product_predict_profit_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD product_predict_profit_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD competitor_price_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD competitor_price_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD competitor_price_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD competitor_sale_record INT4 null;
ALTER TABLE public.na_flow_new_product_detail ADD ebay_monthly_sales_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD ebay_monthly_sales_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product_detail ADD ebay_monthly_sales_usd DECIMAL(20,4) null;
COMMENT ON COLUMN public.na_flow_new_product_detail.currency IS '结算币种';
comment on column public.na_flow_new_product_detail.price_aud is '报价（AUD）';
comment on column public.na_flow_new_product_detail.price_rmb is '报价（RMB）';
comment on column public.na_flow_new_product_detail.price_usd is '报价（USD）';
comment on column public.na_flow_new_product_detail.exchange_rate_aud_rmb is '人民币汇率';
comment on column public.na_flow_new_product_detail.exchange_rate_aud_usd is '美元汇率';
comment on column public.na_flow_new_product_detail.order_qty is '采购数量';
comment on column public.na_flow_new_product_detail.order_value_aud is '采购货值（AUD）';
comment on column public.na_flow_new_product_detail.order_value_rmb is '采购货值（RMB）';
comment on column public.na_flow_new_product_detail.order_value_usd is '采购货值（USD）';
comment on column public.na_flow_new_product_detail.product_predict_profit_aud is '产品预计利润（AUD）';
comment on column public.na_flow_new_product_detail.product_predict_profit_rmb is '产品预计利润（RMB）';
comment on column public.na_flow_new_product_detail.product_predict_profit_usd is '产品预计利润（USD）';
comment on column public.na_flow_new_product_detail.competitor_price_aud is '竞争对手卖价（AUD）';
comment on column public.na_flow_new_product_detail.competitor_price_rmb is '竞争对手卖价（RMB）';
comment on column public.na_flow_new_product_detail.competitor_price_usd is '竞争对手卖价（USD）';
comment on column public.na_flow_new_product_detail.competitor_sale_record is '竞争对手销量';
comment on column public.na_flow_new_product_detail.ebay_monthly_sales_aud is 'eBay月销售额（AUD）';
comment on column public.na_flow_new_product_detail.ebay_monthly_sales_rmb is 'eBay月销售额（RMB）';
comment on column public.na_flow_new_product_detail.ebay_monthly_sales_usd is 'eBay月销售额（USD）';

--新品开发流表
ALTER TABLE public.na_flow_new_product DROP vendor_name;
ALTER TABLE public.na_flow_new_product ADD vendor_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_new_product.vendor_cn_name IS '供应商中文名';
ALTER TABLE public.na_flow_new_product ADD vendor_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_new_product.vendor_en_name IS '供应商中文名';
ALTER TABLE public.na_flow_new_product ADD currency VARCHAR(10) null;
ALTER TABLE public.na_flow_new_product ADD total_price_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD total_price_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD total_price_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD exchange_rate_aud_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD exchange_rate_aud_usd DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD total_order_qty INT4 null;
ALTER TABLE public.na_flow_new_product ADD total_order_value_aud DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD total_order_value_rmb DECIMAL(20,4) null;
ALTER TABLE public.na_flow_new_product ADD total_order_value_usd DECIMAL(20,4) null;
comment on column public.na_flow_new_product.currency is '结算币种';
comment on column public.na_flow_new_product.total_price_aud is '总报价（AUD）';
comment on column public.na_flow_new_product.total_price_rmb is '总报价（RMB）';
comment on column public.na_flow_new_product.total_price_usd is '总报价（USD）';
comment on column public.na_flow_new_product.exchange_rate_aud_rmb is '人民币汇率';
comment on column public.na_flow_new_product.exchange_rate_aud_usd is '美元汇率';
comment on column public.na_flow_new_product.total_order_qty is '总采购数量';
comment on column public.na_flow_new_product.total_order_value_aud is '总采购货值（AUD）';
comment on column public.na_flow_new_product.total_order_value_rmb is '总采购货值（RMB）';
comment on column public.na_flow_new_product.total_order_value_usd is '总采购货值（USD）';

--产品供应商属性
ALTER TABLE public.na_product_vendor_prop ADD vendor_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.vendor_cn_name IS '供应商中文名';
ALTER TABLE public.na_product_vendor_prop ADD vendor_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.vendor_en_name IS '供应商英文名';


-- 20171012 文件管理系统
CREATE TABLE "public"."na_files" (
  "id" varchar(22) NOT NULL,
  "name" varchar(255),
  "path" varchar(255),
  "extension" varchar(10),
  "byte" int4,
  "note" varchar(255),
  "category_id" varchar(22),
  "status" int2,
  "shared" int2,
  "created_at" timestamp(255),
  "updated_at" timestamp,
  "creator_id" varchar(22),
  "creator_cn_name" varchar(50),
  "creator_en_name" varchar(50),
  "department_id" varchar(255),
  "department_cn_name" varchar(50),
  "department_en_name" varchar(50),
  PRIMARY KEY ("id")
);
CREATE INDEX ON "public"."na_files" ("status");
CREATE INDEX ON "public"."na_files" ("shared");
CREATE INDEX ON "public"."na_files" ("created_at");
CREATE INDEX ON "public"."na_files" ("category_id");
CREATE INDEX ON "public"."na_files" ("creator_id");
CREATE INDEX ON "public"."na_files" ("department_id");

CREATE TABLE "public"."na_files_category" (
  "id" varchar(22) NOT NULL,
  "status" int2,
  "sort" int2,
  PRIMARY KEY ("id")
);
CREATE INDEX ON "public"."na_files_category" ("status");
CREATE INDEX ON "public"."na_files_category" ("sort");
ALTER TABLE "public"."na_files_category" ADD COLUMN "path" varchar(100);


CREATE TABLE "public"."na_files_category_desc" (
  "id" varchar(22) NOT NULL,
  "category_id" varchar(22),
  "title" varchar(50),
  "context" varchar(255),
  "lang" varchar(5),
  PRIMARY KEY ("id")
);
CREATE INDEX ON "public"."na_files_category_desc" ("category_id");


--20171016
ALTER TABLE public.na_product_vendor_prop DROP recommended_carrier;
ALTER TABLE public.na_product_vendor_prop ADD service_provider_id VARCHAR(20) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.service_provider_id IS '推荐物流公司ID';
ALTER TABLE public.na_product_vendor_prop ADD service_provider_cn_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.service_provider_cn_name IS '推荐物流公司中文名';
ALTER TABLE public.na_product_vendor_prop ADD service_provider_en_name VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.service_provider_en_name IS '推荐物流公司英文名';


ALTER TABLE public.na_vendor_category DROP operated_at;
ALTER TABLE public.na_vendor_category DROP operator_id;
ALTER TABLE public.na_vendor_category DROP operator_cn_name;
ALTER TABLE public.na_vendor_category DROP operator_en_name;
ALTER TABLE public.na_vendor_category ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_vendor_category.updated_at IS '更新时间';

ALTER TABLE public.na_vendor DROP operated_at;
ALTER TABLE public.na_vendor DROP operator_id;
ALTER TABLE public.na_vendor DROP operator_cn_name;
ALTER TABLE public.na_vendor DROP operator_en_name;
ALTER TABLE public.na_vendor ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_vendor.updated_at IS '更新时间';

ALTER TABLE public.na_service_provider_category DROP operated_at;
ALTER TABLE public.na_service_provider_category DROP operator_id;
ALTER TABLE public.na_service_provider_category DROP operator_cn_name;
ALTER TABLE public.na_service_provider_category DROP operator_en_name;
ALTER TABLE public.na_service_provider_category ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_service_provider_category.updated_at IS '更新时间';

ALTER TABLE public.na_product_category DROP operated_at;
ALTER TABLE public.na_product_category DROP operator_id;
ALTER TABLE public.na_product_category DROP operator_cn_name;
ALTER TABLE public.na_product_category DROP operator_en_name;
ALTER TABLE public.na_product_category ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product_category.updated_at IS '更新时间';

ALTER TABLE public.na_product DROP operated_at;
ALTER TABLE public.na_product DROP operator_id;
ALTER TABLE public.na_product DROP operator_cn_name;
ALTER TABLE public.na_product DROP operator_en_name;
ALTER TABLE public.na_product ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product.updated_at IS '更新时间';

ALTER TABLE public.na_product_combined DROP operated_at;
ALTER TABLE public.na_product_combined DROP operator_id;
ALTER TABLE public.na_product_combined DROP operator_cn_name;
ALTER TABLE public.na_product_combined DROP operator_en_name;
ALTER TABLE public.na_product_combined ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product_combined.updated_at IS '更新时间';

ALTER TABLE public.na_new_product DROP operated_at;
ALTER TABLE public.na_new_product DROP operator_id;
ALTER TABLE public.na_new_product DROP operator_cn_name;
ALTER TABLE public.na_new_product DROP operator_en_name;
ALTER TABLE public.na_new_product ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_new_product.updated_at IS '更新时间';

ALTER TABLE public.na_product_vendor_prop DROP target_bin;
ALTER TABLE public.na_product_vendor_prop ADD target_bin_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.target_bin_aud IS '目标售价(AUD)';
ALTER TABLE public.na_product_vendor_prop ADD target_bin_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.target_bin_rmb IS '目标售价(RMB)';
ALTER TABLE public.na_product_vendor_prop ADD target_bin_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.target_bin_usd IS '目标售价(USD)';
ALTER TABLE public.na_product_vendor_prop ADD rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.rate_aud_to_rmb IS '汇率(AUD/RMB)';
ALTER TABLE public.na_product_vendor_prop ADD rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.rate_aud_to_usd IS '汇率(AUD/USD)';


ALTER TABLE public.na_service_provider DROP operated_at;
ALTER TABLE public.na_service_provider DROP operator_id;
ALTER TABLE public.na_service_provider DROP operator_cn_name;
ALTER TABLE public.na_service_provider DROP operator_en_name;
ALTER TABLE public.na_service_provider ADD updated_at TIMESTAMP NULL;
COMMENT ON COLUMN public.na_service_provider.updated_at IS '更新时间';



--20171017
ALTER TABLE public.na_product_vendor_prop DROP estimated_avg_postage;
ALTER TABLE public.na_product_vendor_prop ADD estimated_avg_postage_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.estimated_avg_postage_aud IS '预估平均邮费(AUD)';
ALTER TABLE public.na_product_vendor_prop ADD estimated_avg_postage_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.estimated_avg_postage_rmb IS '预估平均邮费(RMB)';
ALTER TABLE public.na_product_vendor_prop ADD estimated_avg_postage_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.estimated_avg_postage_usd IS '预估平均邮费(USD)';
ALTER TABLE public.na_product_vendor_prop ADD currency INT2 NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.currency IS '币种';

-- 20171018 附增加件表
CREATE TABLE "public"."na_attachment" (
  "id" varchar(22) NOT NULL,
  "business_id" varchar(22),
  "document_id" varchar(22),
  "module_name" varchar(50),
  "category_id" varchar(22),
  PRIMARY KEY ("id")
);
COMMENT ON TABLE "public"."na_attachment" IS '附件表';

-- 20171019 站内信息表
CREATE TABLE "public"."na_message" (
  "id" varchar(22) NOT NULL,
  "to_user_id" varchar(22),
  "to_user_cn_name" varchar(12),
  "to_user_en_name" varchar(50),
  "from_user_id" varchar(22),
  "from_user_cn_name" varchar(12),
  "from_user_en_name" varchar(50),
  "title" varchar(100),
  "content" text,
  "status" int2,
  "read" int2,
  "type" int2,
  "target" int2,
  "created_at" timestamp(255),
  "updated_at" timestamp
);
COMMENT ON COLUMN "public"."na_message"."read" IS '状态：1未读, 2已读';
COMMENT ON COLUMN "public"."na_message"."type" IS '信息类型： 1系统信息；2用户信息';
COMMENT ON COLUMN "public"."na_message"."target" IS '路径类型：1POPWIN；2Tab；3URL';
COMMENT ON COLUMN "public"."na_message"."updated_at" IS '阅读时间';
ALTER TABLE "public"."na_message" ADD COLUMN "module_name" varchar(100), ADD COLUMN "business_id" varchar(22);

ALTER TABLE "public"."na_message" 
	ADD COLUMN "to_department_id" varchar(22),
	ADD COLUMN "to_department_cn_name" varchar(50),
	ADD COLUMN "to_department_en_name" varchar(50),
	ADD COLUMN "from_department_id" varchar(22),
	ADD COLUMN "from_department_cn_name" varchar(50),
	ADD COLUMN "from_department_en_name" varchar(50);
	
-- 20171024
DROP TABLE IF EXISTS "public"."na_flow_agent";
CREATE TABLE "public"."na_flow_agent" (
  "id" varchar(22) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
  "to_user_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "to_user_cn_name" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "to_user_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "to_department_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "to_department_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "to_department_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_user_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_user_cn_name" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_user_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_department_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_department_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_department_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "from_time" timestamp(6) DEFAULT NULL,
  "to_time" timestamp(6) DEFAULT NULL,
  "status" int2 DEFAULT NULL,
  "created_at" timestamp(6) DEFAULT NULL,
  "updated_at" timestamp(6) DEFAULT NULL,
  "creator_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creator_cn_name" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creator_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "department_id" varchar(22) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "department_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "department_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL
);
-- ----------------------------
-- Primary Key structure for table na_flow_agent
-- ----------------------------
ALTER TABLE "public"."na_flow_agent" ADD CONSTRAINT "na_flow_agent_pkey" PRIMARY KEY ("id");




---20180103
ALTER TABLE public.na_product_vendor_prop ADD flag_dev_status INT2 NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_dev_status IS '新品开发状态';
ALTER TABLE public.na_product_vendor_prop ADD flag_dev_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_dev_time IS '新品开发时间';
ALTER TABLE public.na_product_vendor_prop ADD flag_dev_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_dev_id IS '新品开发id';
ALTER TABLE public.na_product_vendor_prop ADD flag_compliance_status INT2 NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_compliance_status IS '安检状态';
ALTER TABLE public.na_product_vendor_prop ADD flag_compliance_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_compliance_time IS '安检时间';
ALTER TABLE public.na_product_vendor_prop ADD flag_compliance_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_compliance_id IS '安检单号id';
ALTER TABLE public.na_product_vendor_prop ADD flag_qc_status INT2 NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_qc_status IS '质检状态';
ALTER TABLE public.na_product_vendor_prop ADD flag_qc_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_qc_time IS '质检时间';
ALTER TABLE public.na_product_vendor_prop ADD flag_qc_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.flag_qc_id IS '质检单id';
ALTER TABLE public.na_product_vendor_prop DROP dev_status;
ALTER TABLE public.na_product_vendor_prop DROP compliance_status;
ALTER TABLE public.na_product_vendor_prop DROP qc_status;


--20180104
ALTER TABLE public.na_flow_new_product_detail ADD new_product INT2 NULL;
COMMENT ON COLUMN public.na_flow_new_product_detail.new_product IS '是否新品';
ALTER TABLE public.na_new_product ADD new_product INT2 NULL;
COMMENT ON COLUMN public.na_new_product.new_product IS '是否新品';

ALTER TABLE public.na_flow_sample ADD buyer_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_sample.buyer_id IS '采购员ID';
ALTER TABLE public.na_flow_sample ADD buyer_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_sample.buyer_cn_name IS '采购员中文名';
ALTER TABLE public.na_flow_sample ADD buyer_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_sample.buyer_en_name IS '采购员英文名';

ALTER TABLE public.na_flow_sample_detail ADD sku VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.sku IS '产品编码';
ALTER TABLE public.na_flow_sample_detail ADD sample_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.sample_name IS '样品名';
ALTER TABLE public.na_flow_sample_detail ADD qty INT4 NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.qty IS '数量';
ALTER TABLE public.na_flow_sample_detail ADD sample_warehouse_date TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.sample_warehouse_date IS '样品入仓时间';
ALTER TABLE public.na_flow_sample_detail ADD sample_outbound_date TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.sample_outbound_date IS '样品出仓时间';


ALTER TABLE public.na_sample ADD skus TEXT NULL;
COMMENT ON COLUMN public.na_sample.skus IS '样品名，多个用英文逗号隔开';
ALTER TABLE public.na_sample ADD total_sample_fee_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_sample_fee_aud IS '样品总金额(AUD)';
ALTER TABLE public.na_sample ADD total_sample_fee_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_sample_fee_rmb IS '样品总金额(RMB)';
ALTER TABLE public.na_sample ADD total_sample_fee_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_sample_fee_usd IS '样品总金额(USD)';
ALTER TABLE public.na_sample ADD terapeak_research VARCHAR(255) NULL;
COMMENT ON COLUMN public.na_sample.terapeak_research IS 'TP分析单';
ALTER TABLE public.na_sample ADD product_report VARCHAR(255) NULL;
COMMENT ON COLUMN public.na_sample.product_report IS '分析报告';
ALTER TABLE public.na_sample ADD column_44 INT NULL;
ALTER TABLE public.na_sample ADD buyer_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_sample.buyer_id IS '采购员ID';
ALTER TABLE public.na_sample ADD buyer_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_sample.buyer_cn_name IS '采购员中文名';
ALTER TABLE public.na_sample ADD buyer_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_sample.buyer_en_name IS '采购员英文名';


/*==============================================================*/
/* Table: na_sample_detail                                      */
/*==============================================================*/
create table na_sample_detail (
    id                   VARCHAR(24)          not null,
    sample_id            VARCHAR(24)          null,
    product_id           VARCHAR(50)          null,
    sku                  VARCHAR(100)         null,
    sample_name          VARCHAR(100)         null,
    qty                  INT4                 null,
    currency             VARCHAR(10)          null,
    sample_fee_aud       DECIMAL(20,4)        null,
    sample_fee_rmb       DECIMAL(20,4)        null,
    sample_fee_usd       DECIMAL(20,4)        null,
    rate_aud_to_rmb      DECIMAL(20,4)        null,
    rate_aud_to_usd      DECIMAL(20,4)        null,
    sample_fee_refund    INT2                 null,
    sample_receiver      VARCHAR(100)         null,
    sample_warehouse_date TIMESTAMP            null,
    sample_outbound_date TIMESTAMP            null,
    constraint PK_NA_SAMPLE_DETAIL primary key (id)
);

comment on table na_sample_detail is
'样品申请单明细表(正式)';

comment on column na_sample_detail.id is
'id';

comment on column na_sample_detail.sample_id is
'样品单id';

comment on column na_sample_detail.product_id is
'产品id';

comment on column na_sample_detail.sku is
'产品编码';

comment on column na_sample_detail.sample_name is
'样品名';

comment on column na_sample_detail.qty is
'数量';

comment on column na_sample_detail.currency is
'结算币种';

comment on column na_sample_detail.sample_fee_aud is
'样品金额(AUD)';

comment on column na_sample_detail.sample_fee_rmb is
'样品金额(RMB)';

comment on column na_sample_detail.sample_fee_usd is
'样品金额(USD)';

comment on column na_sample_detail.rate_aud_to_rmb is
'人民币汇率';

comment on column na_sample_detail.rate_aud_to_usd is
'美元汇率';

comment on column na_sample_detail.sample_fee_refund is
'样品费是否可退
';

comment on column na_sample_detail.sample_receiver is
'样品去向';

comment on column na_sample_detail.sample_warehouse_date is
'样品入仓时间';

comment on column na_sample_detail.sample_outbound_date is
'样品出仓时间';



--20180105
ALTER TABLE public.na_flow_sample_payment ADD sample_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.sample_id IS '样品申请单id';
ALTER TABLE public.na_sample_payment ADD sample_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_sample_payment.sample_id IS '样品申请单id';

-- 报告基础表增加 报告应用的分类字段，目前仅产品分析、产品汇总、产品趋势分析报告使用。
  ALTER TABLE "public"."na_reports"
  ADD COLUMN "business_type" int2;
COMMENT ON COLUMN "public"."na_reports"."confirmed_id" IS '确认人ID';
COMMENT ON COLUMN "public"."na_reports"."module_name" IS '报告类型名';
COMMENT ON COLUMN "public"."na_reports"."business_type" IS '申请单位置：null 仅一种申请单，1第一种申请单，2第二种申请单';

--产品证书添加业务id字段
ALTER TABLE public.na_product_certificate ADD business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_product_certificate.business_id IS '单据流水号';

--20180108

ALTER TABLE public.na_flow_purchase_contract_detail ADD purchase_plan_detail_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_detail.purchase_plan_detail_id IS '采购计划明细ID';
ALTER TABLE public.na_purchase_contract_detail ADD purchase_plan_detail_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_contract_detail.purchase_plan_detail_id IS '采购计划明细ID';

ALTER TABLE public.na_flow_purchase_contract ADD is_needed_qc INT2 NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.is_needed_qc IS '是否需要质检';

--送仓计划增加采购计划号Id
ALTER TABLE "public"."na_flow_warehouse_plan_detail" ADD COLUMN "flow_order_shipping_plan_id" varchar(24);
COMMENT ON COLUMN "public"."na_flow_warehouse_plan_detail"."flow_order_shipping_plan_id" IS '发货计划号id';


--20180109

ALTER TABLE public.na_flow_sample_payment ADD sample_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.sample_business_id IS '样品申请单ID';
COMMENT ON COLUMN public.na_flow_sample_payment.sample_id IS '样品申请单id(正式)';

ALTER TABLE public.na_sample_payment ADD sample_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_sample_payment.sample_business_id IS '样品申请单ID';
COMMENT ON COLUMN public.na_sample_payment.sample_id IS '样品申请单id(正式)';


ALTER TABLE public.na_flow_sample_payment_detail ADD sku VARCHAR(100) NULL;
ALTER TABLE public.na_sample_payment_detail ADD sku VARCHAR(100) NULL;


--20180110
ALTER TABLE public.na_flow_purchase_contract ADD retd TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.retd IS '实际发货时间';
ALTER TABLE public.na_flow_purchase_contract ADD reta TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.reta IS '实际到岸时间';

ALTER TABLE public.na_purchase_contract ADD retd TIMESTAMP NULL;
COMMENT ON COLUMN public.na_purchase_contract.retd IS '实际发货时间';
ALTER TABLE public.na_purchase_contract ADD reta TIMESTAMP NULL;
COMMENT ON COLUMN public.na_purchase_contract.reta IS '实际到岸时间';

ALTER TABLE public.na_cost ADD retd TIMESTAMP NULL;
COMMENT ON COLUMN public.na_cost.retd IS '实际发货时间';
ALTER TABLE public.na_cost ADD reta TIMESTAMP NULL;
COMMENT ON COLUMN public.na_cost.reta IS '实际到岸时间';

ALTER TABLE public.na_flow_purchase_contract ADD container_type INT2 NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.container_type IS '货柜类型';
ALTER TABLE public.na_flow_purchase_contract ADD container_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.container_qty IS '货柜数量';

ALTER TABLE public.na_purchase_contract ADD container_type INT2 NULL;
COMMENT ON COLUMN public.na_purchase_contract.container_type IS '货柜类型';
ALTER TABLE public.na_purchase_contract ADD container_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.container_qty IS '货柜数量';

ALTER TABLE public.na_cost_product ALTER COLUMN vendor_id TYPE VARCHAR(24) USING vendor_id::VARCHAR(24);

ALTER TABLE public.na_flow_order_shipping_plan ADD flag_order_shipping_apply_statu INT2 NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_order_shipping_apply_statu IS '发货确认状态';
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_order_shipping_apply_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_order_shipping_apply_time IS '发货确认时间';
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_order_shipping_apply_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_order_shipping_apply_id IS '发货确认ID';
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_flow_order_shipping_apply_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_flow_order_shipping_apply_id IS '发货确认流程ID';

ALTER TABLE public.na_order_shipping_plan ADD flag_order_shipping_apply_statu INT2 NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_order_shipping_apply_statu IS '发货确认状态';
ALTER TABLE public.na_order_shipping_plan ADD flag_order_shipping_apply_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_order_shipping_apply_time IS '发货确认时间';
ALTER TABLE public.na_order_shipping_plan ADD flag_order_shipping_apply_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_order_shipping_apply_id IS '发货确认ID';
ALTER TABLE public.na_order_shipping_plan ADD flag_flow_order_shipping_apply_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_flow_order_shipping_apply_id IS '发货确认流程ID';

ALTER TABLE public.na_flow_balance_refund ADD order_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_balance_refund.order_business_id IS '订单流程id';
ALTER TABLE public.na_flow_balance_refund ADD sample_payment_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_balance_refund.sample_payment_id IS '样品付款ID(正式)';
ALTER TABLE public.na_flow_balance_refund ADD sample_payment_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_balance_refund.sample_payment_business_id IS '样品付款申请单ID';

ALTER TABLE public.na_balance_refund ADD order_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_balance_refund.order_business_id IS '订单流程id';
ALTER TABLE public.na_balance_refund ADD sample_payment_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_balance_refund.sample_payment_id IS '样品付款ID(正式)';
ALTER TABLE public.na_balance_refund ADD sample_payment_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_balance_refund.sample_payment_business_id IS '样品付款申请单ID';

ALTER TABLE public.na_flow_balance_refund_detail ADD product_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_balance_refund_detail.product_id IS '产品id';
ALTER TABLE public.na_flow_balance_refund_detail ADD sku VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_flow_balance_refund_detail.sku IS '产品编码';

ALTER TABLE public.na_balance_refund_detail ADD product_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_balance_refund_detail.product_id IS '产品id';
ALTER TABLE public.na_balance_refund_detail ADD sku VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_balance_refund_detail.sku IS '产品编码';

ALTER TABLE public.na_flow_order_shipping_apply_detail ADD order_shipping_plan_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_apply_detail.order_shipping_plan_id IS '发货计划号';
ALTER TABLE public.na_flow_order_shipping_apply_detail ADD order_shipping_plan_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_apply_detail.order_shipping_plan_business_id IS '发货计划流程ID';

ALTER TABLE public.na_order_shipping_apply_detail ADD order_shipping_plan_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_apply_detail.order_shipping_plan_id IS '发货计划号';
ALTER TABLE public.na_order_shipping_apply_detail ADD order_shipping_plan_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_apply_detail.order_shipping_plan_business_id IS '发货计划流程ID';


--20180111
ALTER TABLE public.na_flow_purchase_contract ADD electronic_processing_fee_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.electronic_processing_fee_aud IS '电放费（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD electronic_processing_fee_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.electronic_processing_fee_rmb IS '电放费（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD electronic_processing_fee_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.electronic_processing_fee_usd IS '电放费（USD）';
ALTER TABLE public.na_flow_purchase_contract ADD balance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.balance_aud IS '尾款（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD balance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.balance_rmb IS '尾款（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD balance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.balance_usd IS '尾款（USD）';
ALTER TABLE public.na_flow_purchase_contract ADD adjust_balance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.adjust_balance_aud IS '调整后尾款（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD adjust_balance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.adjust_balance_rmb IS '调整后尾款（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD adjust_balance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.adjust_balance_usd IS '调整后尾款（USD）';

ALTER TABLE public.na_purchase_contract ADD electronic_processing_fee_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.electronic_processing_fee_aud IS '电放费（AUD）';
ALTER TABLE public.na_purchase_contract ADD electronic_processing_fee_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.electronic_processing_fee_rmb IS '电放费（RMB）';
ALTER TABLE public.na_purchase_contract ADD electronic_processing_fee_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.electronic_processing_fee_usd IS '电放费（USD）';
ALTER TABLE public.na_purchase_contract ADD balance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.balance_aud IS '尾款（AUD）';
ALTER TABLE public.na_purchase_contract ADD balance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.balance_rmb IS '尾款（RMB）';
ALTER TABLE public.na_purchase_contract ADD balance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.balance_usd IS '尾款（USD）';
ALTER TABLE public.na_purchase_contract ADD adjust_balance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.adjust_balance_aud IS '调整后尾款（AUD）';
ALTER TABLE public.na_purchase_contract ADD adjust_balance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.adjust_balance_rmb IS '调整后尾款（RMB）';
ALTER TABLE public.na_purchase_contract ADD adjust_balance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.adjust_balance_usd IS '调整后尾款（USD）';


ALTER TABLE public.na_flow_asn_packing_detail ADD price_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_asn_packing_detail.price_aud IS '报价（AUD）';
ALTER TABLE public.na_flow_asn_packing_detail ADD price_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_asn_packing_detail.price_rmb IS '报价（RMB）';
ALTER TABLE public.na_flow_asn_packing_detail ADD price_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_asn_packing_detail.price_usd IS '报价（USD）';
ALTER TABLE public.na_flow_asn_packing_detail ADD chargeback_status INT2 NULL;
COMMENT ON COLUMN public.na_flow_asn_packing_detail.chargeback_status IS '扣款状态';

ALTER TABLE public.na_asn_packing_detail ADD price_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_asn_packing_detail.price_aud IS '报价（AUD）';
ALTER TABLE public.na_asn_packing_detail ADD price_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_asn_packing_detail.price_rmb IS '报价（RMB）';
ALTER TABLE public.na_asn_packing_detail ADD price_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_asn_packing_detail.price_usd IS '报价（USD）';
ALTER TABLE public.na_asn_packing_detail ADD chargeback_status INT2 NULL;
COMMENT ON COLUMN public.na_asn_packing_detail.chargeback_status IS '扣款状态';


ALTER TABLE public.na_flow_fee_register ADD beneficiary_bank_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_flow_fee_register.beneficiary_bank_address IS '开户银行地址';
ALTER TABLE public.na_flow_fee_register ADD company_cn_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_flow_fee_register.company_cn_address IS '公司地址中文';
ALTER TABLE public.na_flow_fee_register ADD company_en_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_flow_fee_register.company_en_address IS '公司地址英文';
ALTER TABLE public.na_flow_fee_register ADD guarantee_letter VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_fee_register.guarantee_letter IS '保函附件';
ALTER TABLE public.na_flow_fee_register ADD contract_guarantee_letter VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_fee_register.contract_guarantee_letter IS '合同履约担保函附件ID';


ALTER TABLE public.na_fee_register ADD beneficiary_bank_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_fee_register.beneficiary_bank_address IS '开户银行地址';
ALTER TABLE public.na_fee_register ADD company_cn_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_fee_register.company_cn_address IS '公司地址中文';
ALTER TABLE public.na_fee_register ADD company_en_address VARCHAR(150) NULL;
COMMENT ON COLUMN public.na_fee_register.company_en_address IS '公司地址英文';
ALTER TABLE public.na_fee_register ADD guarantee_letter VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_fee_register.guarantee_letter IS '保函附件';
ALTER TABLE public.na_fee_register ADD contract_guarantee_letter VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_fee_register.contract_guarantee_letter IS '合同履约担保函附件ID';



ALTER TABLE public.na_flow_custom_clearance_packing_detail ADD packing_qty INT4 NULL;
COMMENT ON COLUMN public.na_flow_custom_clearance_packing_detail.packing_qty IS '装箱件数';
COMMENT ON COLUMN public.na_flow_custom_clearance_packing_detail.order_qty IS '采购数量';
COMMENT ON COLUMN public.na_flow_custom_clearance_packing_detail.cartons IS '采购箱数';
ALTER TABLE public.na_flow_custom_clearance_packing_detail RENAME COLUMN received_cartons TO packing_cartons;
COMMENT ON COLUMN public.na_flow_custom_clearance_packing_detail.packing_cartons IS '装箱箱数';

ALTER TABLE public.na_custom_clearance_packing_detail ADD packing_qty INT4 NULL;
COMMENT ON COLUMN public.na_custom_clearance_packing_detail.packing_qty IS '装箱件数';
COMMENT ON COLUMN public.na_custom_clearance_packing_detail.order_qty IS '采购数量';
COMMENT ON COLUMN public.na_custom_clearance_packing_detail.cartons IS '采购箱数';
ALTER TABLE public.na_custom_clearance_packing_detail RENAME COLUMN received_cartons TO packing_cartons;
COMMENT ON COLUMN public.na_custom_clearance_packing_detail.packing_cartons IS '装箱箱数';


--20180113

/*==============================================================*/
/* Table: na_purchase_balance_refund_union                      */
/*==============================================================*/
create table na_purchase_balance_refund_union (
    id                   VARCHAR(24)          not null,
    balance_refund_business_id VARCHAR(24)          null,
    balance_refund_id    VARCHAR(24)          null,
    purchase_plan_business_id VARCHAR(24)          null,
    purchase_plan_id     VARCHAR(24)          null,
    purchase_contract_business_id VARCHAR(24)          null,
    purchase_contract_id VARCHAR(24)          null,
    constraint PK_na_purchase_balance_refund_union primary key (id)
);

comment on table na_purchase_balance_refund_union is
'采购计划-差额退款';

comment on column na_purchase_balance_refund_union.id is
'id';

comment on column na_purchase_balance_refund_union.balance_refund_business_id is
'差额退款单流程id';

comment on column na_purchase_balance_refund_union.balance_refund_id is
'差额退款单id';

comment on column na_purchase_balance_refund_union.purchase_plan_business_id is
'采购计划单流程id';

comment on column na_purchase_balance_refund_union.purchase_plan_id is
'采购计划单id';

comment on column na_purchase_balance_refund_union.purchase_contract_business_id is
'采购合同单流程id';

comment on column na_purchase_balance_refund_union.purchase_contract_id is
'采购合同单id';


--20180116
ALTER TABLE public.na_flow_purchase_contract ADD deposit_type INT2 NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.deposit_type IS '定金类型';
ALTER TABLE public.na_purchase_contract ADD deposit_type INT2 NULL;
COMMENT ON COLUMN public.na_purchase_contract.deposit_type IS '定金类型';

ALTER TABLE public.na_flow_bank_account ADD deposit_type INT2 NULL;
COMMENT ON COLUMN public.na_flow_bank_account.deposit_type IS '定金类型';
ALTER TABLE public.na_bank_account ADD deposit_type INT2 NULL;
COMMENT ON COLUMN public.na_bank_account.deposit_type IS '定金类型';

COMMENT ON COLUMN public.na_purchase_contract.balance_payment_term IS '尾款条款';
ALTER TABLE public.na_purchase_contract RENAME COLUMN after_closed TO balance_credit_term;
COMMENT ON COLUMN public.na_purchase_contract.balance_credit_term IS '尾款账期';

COMMENT ON COLUMN public.na_flow_purchase_contract.balance_payment_term IS '尾款条款';
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN after_closed TO balance_credit_term;
COMMENT ON COLUMN public.na_flow_purchase_contract.balance_credit_term IS '尾款账期';

ALTER TABLE public.na_vendor ADD column_45 INT NULL;
ALTER TABLE public.na_vendor ADD balance_payment_term INT2 NULL;
COMMENT ON COLUMN public.na_vendor.balance_payment_term IS '尾款条款';
ALTER TABLE public.na_vendor ADD balance_credit_term INT2 NULL;
COMMENT ON COLUMN public.na_vendor.balance_credit_term IS '尾款账期';

ALTER TABLE public.na_custom_clearance RENAME COLUMN delivery_terms TO trade_term;
COMMENT ON COLUMN public.na_custom_clearance.trade_term IS '贸易条款';
ALTER TABLE public.na_flow_custom_clearance RENAME COLUMN delivery_terms TO trade_term;
COMMENT ON COLUMN public.na_flow_custom_clearance.trade_term IS '贸易条款';
ALTER TABLE public.na_flow_purchase_contract ADD trade_term VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.trade_term IS '贸易条款';
ALTER TABLE public.na_purchase_contract ADD trade_term VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_purchase_contract.trade_term IS '贸易条款';
ALTER TABLE public.na_vendor ADD trade_term VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_vendor.trade_term IS '贸易条款';

ALTER TABLE public.na_flow_purchase_contract_detail ADD purchase_plan_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_detail.purchase_plan_id IS '采购计划ID';
ALTER TABLE public.na_flow_purchase_contract_detail ADD purchase_plan_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_detail.purchase_plan_business_id IS '采购计划流程ID';

ALTER TABLE public.na_purchase_contract_detail ADD purchase_plan_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_contract_detail.purchase_plan_id IS '采购计划ID';
ALTER TABLE public.na_purchase_contract_detail ADD purchase_plan_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_contract_detail.purchase_plan_business_id IS '采购计划流程ID';


ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_id TO buyer_info_id;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_cn_name TO buyer_Info_cn_name;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_en_name TO buyer_Info_en_name;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_cn_address TO buyer_Info_cn_address;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_en_address TO buyer_Info_en_address;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_phone TO buyer_Info_phone;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_fax TO buyer_Info_fax;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_contact_cn_name TO buyer_Info_contact_cn_name;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_contact_en_name TO buyer_Info_contact_en_name;
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN buyer_email TO buyer_Info_email;

--20180117
ALTER TABLE public.na_cost ADD order_shipping_plan_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_cost.order_shipping_plan_business_id IS '发货计划流程id';

ALTER TABLE public.na_product_vendor_prop ALTER COLUMN master_carton_cbm TYPE NUMERIC(20,4) USING master_carton_cbm::NUMERIC(20,4);
ALTER TABLE public.na_product_vendor_prop ALTER COLUMN inner_carton_cbm TYPE NUMERIC(20,4) USING inner_carton_cbm::NUMERIC(20,4);
ALTER TABLE public.na_product ALTER COLUMN cbm TYPE NUMERIC(20,4) USING cbm::NUMERIC(20,4);
ALTER TABLE public.na_purchase_plan ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);
ALTER TABLE public.na_flow_purchase_plan ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);
ALTER TABLE public.na_sta_cost ALTER COLUMN cbm TYPE NUMERIC(20,4) USING cbm::NUMERIC(20,4);
ALTER TABLE public.na_sta_cost ALTER COLUMN total_item_cbm TYPE NUMERIC(20,4) USING total_item_cbm::NUMERIC(20,4);
ALTER TABLE public.na_sta_cost ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);
ALTER TABLE public.na_cost_product ALTER COLUMN master_carton_cbm TYPE NUMERIC(20,4) USING master_carton_cbm::NUMERIC(20,4);
ALTER TABLE public.na_sta_order ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);
ALTER TABLE public.na_flow_purchase_contract ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);
ALTER TABLE public.na_purchase_contract ALTER COLUMN total_cbm TYPE NUMERIC(20,4) USING total_cbm::NUMERIC(20,4);

ALTER TABLE public.na_product ALTER COLUMN color TYPE VARCHAR(50) USING color::VARCHAR(50);

--20180118

ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_rate_aud_to_rmb IS '上次汇率(AUD/RMB)';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_rate_aud_to_usd IS '上次汇率(AUD/USD)';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_aud IS '上次20尺柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_rmb IS '上次20尺柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_usd IS '上次20尺柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_aud IS '上次40尺柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_rmb IS '上次40尺柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_usd IS '上次40尺柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_aud IS '上次40尺高柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_rmb IS '上次40尺高柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_usd IS '上次40尺高柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_aud IS '上次散货价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_rmb IS '上次散货价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_usd IS '上次散货价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_insurance_aud IS '上次20尺柜保险价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_insurance_rmb IS '上次20尺柜保险价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp20_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp20_insurance_usd IS '上次20尺柜保险价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_insurance_aud IS '上次40尺柜保险价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_insurance_rmb IS '上次40尺柜保险价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_gp40_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_gp40_insurance_usd IS '上次40尺柜保险价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_insurance_aud IS '上次40尺高柜保险价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_insurance_rmb IS '上次40尺高柜保险价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_hq40_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_hq40_insurance_usd IS '上次40尺高柜保险价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_insurance_aud IS '上次散货保险价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_insurance_rmb IS '上次散货保险价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_price_lcl_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_price_lcl_insurance_usd IS '上次散货保险价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_sailing_days INT2 NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_sailing_days IS '上次海运时长： 单位：天';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_frequency INT2 NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_frequency IS '上次发运频率，单位：周';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_gp20_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_gp20_qty IS '上次20尺柜量';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_gp40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_gp40_qty IS '上次40尺柜量';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_hq40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_hq40_qty IS '上次40尺高柜量';
ALTER TABLE public.na_flow_service_provider_quotation_port ADD prev_lcl_cbm DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_port.prev_lcl_cbm IS '上次散货立方数';


ALTER TABLE public.na_service_provider_quotation_port ADD prev_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_rate_aud_to_rmb IS '上次汇率(AUD/RMB)';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_rate_aud_to_usd IS '上次汇率(AUD/USD)';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_aud IS '上次20尺柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_rmb IS '上次20尺柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_usd IS '上次20尺柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_aud IS '上次40尺柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_rmb IS '上次40尺柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_usd IS '上次40尺柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_aud IS '上次40尺高柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_rmb IS '上次40尺高柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_usd IS '上次40尺高柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_aud IS '上次散货价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_rmb IS '上次散货价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_usd IS '上次散货价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_insurance_aud IS '上次20尺柜保险价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_insurance_rmb IS '上次20尺柜保险价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp20_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp20_insurance_usd IS '上次20尺柜保险价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_insurance_aud IS '上次40尺柜保险价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_insurance_rmb IS '上次40尺柜保险价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_gp40_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_gp40_insurance_usd IS '上次40尺柜保险价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_insurance_aud IS '上次40尺高柜保险价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_insurance_rmb IS '上次40尺高柜保险价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_hq40_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_hq40_insurance_usd IS '上次40尺高柜保险价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_insurance_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_insurance_aud IS '上次散货保险价（AUD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_insurance_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_insurance_rmb IS '上次散货保险价（RMB）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_price_lcl_insurance_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_price_lcl_insurance_usd IS '上次散货保险价（USD）';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_sailing_days INT2 NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_sailing_days IS '上次海运时长： 单位：天';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_frequency INT2 NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_frequency IS '上次发运频率，单位：周';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_gp20_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_gp20_qty IS '上次20尺柜量';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_gp40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_gp40_qty IS '上次40尺柜量';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_hq40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_hq40_qty IS '上次40尺高柜量';
ALTER TABLE public.na_service_provider_quotation_port ADD prev_lcl_cbm DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_port.prev_lcl_cbm IS '上次散货立方数';


ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_rate_aud_to_rmb IS '上次汇率(AUD/RMB)';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_rate_aud_to_usd IS '上次汇率(AUD/USD)';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp20_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp20_aud IS '上次20尺柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp20_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp20_rmb IS '上次20尺柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp20_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp20_usd IS '上次20尺柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp40_aud IS '上次40尺柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp40_rmb IS '上次40尺柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_gp40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_gp40_usd IS '上次40尺柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_hq40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_hq40_aud IS '上次40尺高柜价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_hq40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_hq40_rmb IS '上次40尺高柜价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_hq40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_hq40_usd IS '上次40尺高柜价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_lcl_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_lcl_aud IS '上次散货价（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_lcl_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_lcl_rmb IS '上次散货价（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_lcl_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_lcl_usd IS '上次散货价（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_other_aud IS '上次其它费用（AUD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_other_rmb IS '上次其它费用（RMB）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_price_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_price_other_usd IS '上次其它费用（USD）';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_gp20_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_gp20_qty IS '上次20尺柜量';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_gp40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_gp40_qty IS '上次40尺柜量';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_hq40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_hq40_qty IS '上次40尺高柜量';
ALTER TABLE public.na_flow_service_provider_quotation_charge_item ADD prev_lcl_cbm DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation_charge_item.prev_lcl_cbm IS '上次散货立方数';


ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_rate_aud_to_rmb IS '上次汇率(AUD/RMB)';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_rate_aud_to_usd IS '上次汇率(AUD/USD)';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp20_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp20_aud IS '上次20尺柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp20_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp20_rmb IS '上次20尺柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp20_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp20_usd IS '上次20尺柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp40_aud IS '上次40尺柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp40_rmb IS '上次40尺柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_gp40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_gp40_usd IS '上次40尺柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_hq40_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_hq40_aud IS '上次40尺高柜价（AUD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_hq40_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_hq40_rmb IS '上次40尺高柜价（RMB）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_hq40_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_hq40_usd IS '上次40尺高柜价（USD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_lcl_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_lcl_aud IS '上次散货价（AUD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_lcl_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_lcl_rmb IS '上次散货价（RMB）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_lcl_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_lcl_usd IS '上次散货价（USD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_other_aud IS '上次其它费用（AUD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_other_rmb IS '上次其它费用（RMB）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_price_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_price_other_usd IS '上次其它费用（USD）';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_gp20_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_gp20_qty IS '上次20尺柜量';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_gp40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_gp40_qty IS '上次40尺柜量';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_hq40_qty DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_hq40_qty IS '上次40尺高柜量';
ALTER TABLE public.na_service_provider_quotation_charge_item ADD prev_lcl_cbm DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation_charge_item.prev_lcl_cbm IS '上次散货立方数';



ALTER TABLE public.na_purchase_contract_detail ADD sku VARCHAR(100) NULL;
ALTER TABLE public.na_flow_purchase_contract_detail ADD sku VARCHAR(100) NULL;

ALTER TABLE public.na_flow_order_shipping_plan_detail ADD order_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan_detail.order_business_id IS '订单流程id';

ALTER TABLE public.na_order_shipping_plan_detail ADD order_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_plan_detail.order_business_id IS '订单流程id';

--20180119
ALTER TABLE public.na_flow_purchase_contract ALTER COLUMN order_date TYPE TIMESTAMP USING order_date::TIMESTAMP;
ALTER TABLE public.na_purchase_contract ALTER COLUMN order_date TYPE TIMESTAMP USING order_date::TIMESTAMP;


--20180122

ALTER TABLE public.na_product_quotation ADD detail_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_product_quotation.detail_business_id IS '订单流程id';

ALTER TABLE public.na_flow_new_product_detail ADD product_quotation_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_new_product_detail.product_quotation_id IS '采购询价ID';
ALTER TABLE public.na_flow_new_product_detail ADD product_quotation_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_new_product_detail.product_quotation_business_id IS '采购询价流程ID';
ALTER TABLE public.na_flow_new_product_detail ADD product_quotation_detail_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_new_product_detail.product_quotation_detail_business_id IS '采购询价流程明细ID';

ALTER TABLE public.na_new_product ADD product_quotation_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_new_product.product_quotation_id IS '采购询价ID';
ALTER TABLE public.na_new_product ADD product_quotation_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_new_product.product_quotation_business_id IS '采购询价流程ID';
ALTER TABLE public.na_new_product ADD product_quotation_detail_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_new_product.product_quotation_detail_business_id IS '采购询价流程明细ID';


--20180123
ALTER TABLE public.na_sta_problem ALTER COLUMN vendor_cn_name TYPE VARCHAR(100) USING vendor_cn_name::VARCHAR(100);
ALTER TABLE public.na_sta_problem ALTER COLUMN vendor_en_name TYPE VARCHAR(150) USING vendor_en_name::VARCHAR(150);

ALTER TABLE public.na_cost_product ALTER COLUMN vendor_cn_name TYPE VARCHAR(100) USING vendor_cn_name::VARCHAR(100);
ALTER TABLE public.na_cost_product ALTER COLUMN vendor_en_name TYPE VARCHAR(150) USING vendor_en_name::VARCHAR(150);

ALTER TABLE public.na_balance_refund ALTER COLUMN vendor_cn_name TYPE VARCHAR(100) USING vendor_cn_name::VARCHAR(100);
ALTER TABLE public.na_balance_refund ALTER COLUMN vendor_en_name TYPE VARCHAR(150) USING vendor_en_name::VARCHAR(150);

ALTER TABLE public.na_flow_balance_refund ALTER COLUMN vendor_cn_name TYPE VARCHAR(100) USING vendor_cn_name::VARCHAR(100);
ALTER TABLE public.na_flow_balance_refund ALTER COLUMN vendor_en_name TYPE VARCHAR(150) USING vendor_en_name::VARCHAR(150);

ALTER TABLE public.na_trouble_ticket ALTER COLUMN vendor_cn_name TYPE VARCHAR(100) USING vendor_cn_name::VARCHAR(100);
ALTER TABLE public.na_trouble_ticket ALTER COLUMN vendor_en_name TYPE VARCHAR(150) USING vendor_en_name::VARCHAR(150);

--20180123
ALTER TABLE "public"."na_trouble_ticket"
  ADD COLUMN "return_method" int2,
  ADD COLUMN "return_tracking_number" varchar(100),
  ADD COLUMN "return_cost_aud" numeric(20,4),
  ADD COLUMN "return_cost_rmb" numeric(20,4),
  ADD COLUMN "return_cost_usd" numeric(20,4),
  ADD COLUMN "return_initiated" timestamp(6),
  ADD COLUMN "return_received" timestamp(6),
  ADD COLUMN "inspection_outcome" int2,
  ADD COLUMN "is_shipped" int2;
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_method" IS '退货方式';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_tracking_number" IS '退货跟踪号';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_cost_aud" IS '退货成本AUD';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_cost_rmb" IS '退货成本RMB';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_cost_usd" IS '退货成本USD';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_initiated" IS '退货发起日期';
COMMENT ON COLUMN "public"."na_trouble_ticket"."return_received" IS '退货收到日期';
COMMENT ON COLUMN "public"."na_trouble_ticket"."inspection_outcome" IS '退货质检状态';
COMMENT ON COLUMN "public"."na_trouble_ticket"."is_shipped" IS '原订单是否发货';


--20180124
ALTER TABLE public.na_product_vendor_prop ADD vendor_code VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_product_vendor_prop.vendor_code IS '供应商编码';

ALTER TABLE public.na_flow_custom_clearance ADD photos TEXT NULL;
COMMENT ON COLUMN public.na_flow_custom_clearance.photos IS '图片';
ALTER TABLE public.na_custom_clearance ADD photos TEXT NULL;
COMMENT ON COLUMN public.na_custom_clearance.photos IS '图片';

drop VIEW na_view_purchase_plan_detail;
CREATE VIEW na_view_purchase_plan_detail AS
    SELECT t.id, t.purchase_plan_id, t.product_id, t.sku, t.currency, t.price_aud, t.price_rmb, t.price_usd, t.moq, t.rate_aud_to_rmb, t.rate_aud_to_usd, t.prev_price_aud, t.prev_price_rmb, t.prev_price_usd, t.prev_moq, t.prev_rate_aud_to_rmb, t.prev_rate_aud_to_usd, t.origin_port_id, t.origin_port_cn_name, t.origin_port_en_name, t.destination_port_id, t.destination_port_cn_name, t.destination_port_en_name, t.order_qty, t.order_qty_carton, t.already_order_qty,
        (SELECT t2.total_order_qty FROM (SELECT na_purchase_plan_detail.product_id, sum(na_purchase_plan_detail.order_qty) AS total_order_qty
                                         FROM na_purchase_plan_detail
                                         GROUP BY na_purchase_plan_detail.product_id) t2
        WHERE t2.product_id= t.product_id) AS total_order_qty,
        pp.pcs_per_carton,
        m.vendor_id, m.vendor_cn_name, m.vendor_en_name
    FROM (na_purchase_plan_detail t
        JOIN na_purchase_plan m ON t.purchase_plan_id=m.id
        JOIN na_product p on t.product_id = p.id
        Join na_product_vendor_prop pp on p.id = pp.product_id)
    where t.order_qty is not null  and (t.order_qty > t.already_order_qty or t.already_order_qty is null) and  p.status = 1 and p.new_product = 2 and pp.risk_rating < 5
;

--20180125
ALTER TABLE public.na_trouble_ticket_remark ADD creator_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.creator_id IS '创建人ID';
ALTER TABLE public.na_trouble_ticket_remark ADD creator_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.creator_cn_name IS '创建人中文名';
ALTER TABLE public.na_trouble_ticket_remark ADD creator_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.creator_en_name IS '创建人英文名';
ALTER TABLE public.na_trouble_ticket_remark ADD department_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.department_id IS '隶属部门';
ALTER TABLE public.na_trouble_ticket_remark ADD department_cn_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.department_cn_name IS '隶属部门中文名';
ALTER TABLE public.na_trouble_ticket_remark ADD department_en_name VARCHAR(100) NULL;
COMMENT ON COLUMN public.na_trouble_ticket_remark.department_en_name IS '隶属部门英文名';

ALTER TABLE public.na_flow_sample_detail ADD fee_refunded INT2 NULL;
COMMENT ON COLUMN public.na_flow_sample_detail.fee_refunded IS '费用已退';
ALTER TABLE public.na_sample_detail ADD fee_refunded INT2 NULL;
COMMENT ON COLUMN public.na_sample_detail.fee_refunded IS '费用已退';

ALTER TABLE public.na_trouble_ticket ALTER COLUMN sell_channel TYPE VARCHAR(50) USING sell_channel::VARCHAR(50);

ALTER TABLE "public"."na_flow_fee_payment" ADD COLUMN "fee_register_business_id" varchar(24);
COMMENT ON COLUMN "public"."na_flow_fee_payment"."fee_register_business_id" IS '费用登记流程Id';


--20180126
ALTER TABLE public.na_flow_compliance_apply_detail ADD remark TEXT NULL;
COMMENT ON COLUMN public.na_flow_compliance_apply_detail.remark IS '备注';
ALTER TABLE public.na_compliance_apply ADD remark TEXT NULL;
COMMENT ON COLUMN public.na_compliance_apply.remark IS '备注';

--采购合同选择采购计划明细
drop VIEW na_view_purchase_plan_detail;
CREATE VIEW na_view_purchase_plan_detail AS
    SELECT t.id, t.purchase_plan_id, t.product_id, t.sku, t.currency, t.price_aud, t.price_rmb, t.price_usd, t.moq, t.rate_aud_to_rmb, t.rate_aud_to_usd, t.prev_price_aud, t.prev_price_rmb, t.prev_price_usd, t.prev_moq, t.prev_rate_aud_to_rmb, t.prev_rate_aud_to_usd, t.origin_port_id, t.origin_port_cn_name, t.origin_port_en_name, t.destination_port_id, t.destination_port_cn_name, t.destination_port_en_name, t.order_qty, t.order_qty_carton, t.already_order_qty,
        (SELECT t2.total_order_qty FROM (SELECT na_purchase_plan_detail.product_id, sum(na_purchase_plan_detail.order_qty) AS total_order_qty
                                         FROM na_purchase_plan_detail
                                         GROUP BY na_purchase_plan_detail.product_id) t2
        WHERE t2.product_id= t.product_id) AS total_order_qty,
        pp.pcs_per_carton,
        m.vendor_id, m.vendor_cn_name, m.vendor_en_name
    FROM (na_purchase_plan_detail t
        JOIN na_purchase_plan m ON t.purchase_plan_id=m.id
        JOIN na_product p on t.product_id = p.id
        Join na_product_vendor_prop pp on p.id = pp.product_id)
    where t.order_qty is not null  and (t.order_qty > t.already_order_qty or t.already_order_qty is null) and  p.status = 1 and p.new_product = 2 and pp.risk_rating < 5 and m.hold = 2
;


ALTER TABLE public.na_flow_custom_clearance_packing ADD hold INT2 NULL;
COMMENT ON COLUMN public.na_flow_custom_clearance_packing.hold IS '冻结';

ALTER TABLE public.na_custom_clearance_packing ADD hold INT2 NULL;
COMMENT ON COLUMN public.na_custom_clearance_packing.hold IS '冻结';

ALTER TABLE public.na_flow_purchase_plan ADD trade_term VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_flow_purchase_plan.trade_term IS '交易条款';

ALTER TABLE public.na_purchase_plan ADD trade_term VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_purchase_plan.trade_term IS '交易条款';


--20180129
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_cost_status INT2 NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_cost_status IS '成本计算状态';
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_cost_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_cost_time IS '成本计算时间';
ALTER TABLE public.na_flow_order_shipping_plan ADD flag_cost_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.flag_cost_id IS '成本计算ID';

ALTER TABLE public.na_order_shipping_plan ADD flag_cost_status INT2 NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_cost_status IS '成本计算状态';
ALTER TABLE public.na_order_shipping_plan ADD flag_cost_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_cost_time IS '成本计算时间';
ALTER TABLE public.na_order_shipping_plan ADD flag_cost_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.flag_cost_id IS '成本计算ID';

ALTER TABLE public.na_product_quotation ADD product_status INT2 NULL;
COMMENT ON COLUMN public.na_product_quotation.product_status IS '产品状态';

-- 问题记录字段类型调整
ALTER TABLE "public"."na_trouble_ticket"
ALTER COLUMN "handle_mode" TYPE int2 USING "handle_mode"::int2,
ALTER COLUMN "priority" TYPE int2 USING "priority"::int2;

--20180131
ALTER TABLE public.na_flow_fee_register_detail ADD type INT2 NULL;
COMMENT ON COLUMN public.na_flow_fee_register_detail.type IS '类型';
ALTER TABLE public.na_fee_register_detail ADD type INT2 NULL;
COMMENT ON COLUMN public.na_fee_register_detail.type IS '类型';

--20180201
ALTER TABLE public.na_flow_fee_register_detail ADD item_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_fee_register_detail.item_id IS '项目id';
ALTER TABLE public.na_flow_fee_register_detail ADD item_cn_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_fee_register_detail.item_cn_name IS '项目中文名';
ALTER TABLE public.na_flow_fee_register_detail ADD item_en_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_fee_register_detail.item_en_name IS '项目英文名';

ALTER TABLE public.na_fee_register_detail ADD item_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_fee_register_detail.item_id IS '项目id';
ALTER TABLE public.na_fee_register_detail ADD item_cn_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_fee_register_detail.item_cn_name IS '项目中文名';
ALTER TABLE public.na_fee_register_detail ADD item_en_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_fee_register_detail.item_en_name IS '项目英文名';

ALTER TABLE public.na_flow_purchase_plan_detail ADD hold INT2 NULL;
COMMENT ON COLUMN public.na_flow_purchase_plan_detail.hold IS '是否冻结';

ALTER TABLE public.na_purchase_plan_detail ADD hold INT2 NULL;
COMMENT ON COLUMN public.na_purchase_plan_detail.hold IS '是否冻结';

ALTER TABLE public.na_asn ADD CONSTRAINT pk_asn PRIMARY KEY (id);
ALTER TABLE public.na_asn_packing ADD CONSTRAINT pk_asn_packing PRIMARY KEY (id);
ALTER TABLE public.na_asn_packing_detail ADD CONSTRAINT pk_asn_packing_detail PRIMARY KEY (id);
ALTER TABLE public.na_flow_asn ADD CONSTRAINT pk_flow_asn PRIMARY KEY (id);
ALTER TABLE public.na_flow_asn_packing ADD CONSTRAINT pk_flow_asn_packing PRIMARY KEY (id);
ALTER TABLE public.na_flow_asn_packing_detail ADD CONSTRAINT pk_flow_asn_packing_detail PRIMARY KEY (id);

--20180202
ALTER TABLE public.na_flow_product_quotation_detail ADD hold INT2 NULL;
COMMENT ON COLUMN public.na_flow_product_quotation_detail.hold IS '是否挂起';

--加宽问题产品SKU字段长度
ALTER TABLE "public"."na_trouble_ticket_product"
  ALTER COLUMN "sku" TYPE varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE public.na_flow_order_shipping_plan ADD eta TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.eta IS '预计到岸时间';

ALTER TABLE public.na_order_shipping_plan ADD eta TIMESTAMP NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.eta IS '预计到岸时间';

--2018-02-05
ALTER TABLE "public"."na_purchase_plan"
  ALTER COLUMN "creator_cn_name" TYPE varchar(50) COLLATE "pg_catalog"."default",
  ALTER COLUMN "creator_en_name" TYPE varchar(50) COLLATE "pg_catalog"."default",
  ALTER COLUMN "department_cn_name" TYPE varchar(50) COLLATE "pg_catalog"."default",
  ALTER COLUMN "department_en_name" TYPE varchar(50) COLLATE "pg_catalog"."default";
  
  ALTER TABLE "public"."na_flow_purchase_plan"
  ALTER COLUMN "department_cn_name" TYPE varchar(40) COLLATE "pg_catalog"."default",
  ALTER COLUMN "department_en_name" TYPE varchar(40) COLLATE "pg_catalog"."default";
  
  ALTER TABLE "public"."na_trouble_ticket_product" 
  ALTER COLUMN "order_number" TYPE varchar(255) COLLATE "pg_catalog"."default";

ALTER TABLE public.na_trouble_ticket_product ADD job_no VARCHAR(100) NULL;

--2018-02-07
ALTER TABLE public.na_product ADD sync_time TIMESTAMP NULL;
COMMENT ON COLUMN public.na_product.sync_time IS '同步时间';

--2018-02-12
--采购合同选择采购计划明细
drop VIEW na_view_purchase_plan_detail;
CREATE VIEW na_view_purchase_plan_detail AS
    SELECT t.id, t.purchase_plan_id, t.product_id, t.sku, t.currency, t.price_aud, t.price_rmb, t.price_usd, t.moq, t.rate_aud_to_rmb, t.rate_aud_to_usd, t.prev_price_aud, t.prev_price_rmb, t.prev_price_usd, t.prev_moq, t.prev_rate_aud_to_rmb, t.prev_rate_aud_to_usd, t.origin_port_id, t.origin_port_cn_name, t.origin_port_en_name, t.destination_port_id, t.destination_port_cn_name, t.destination_port_en_name, t.order_qty, t.order_qty_carton, t.already_order_qty,
        (SELECT t2.total_order_qty FROM (SELECT na_purchase_plan_detail.product_id, sum(na_purchase_plan_detail.order_qty) AS total_order_qty
                                         FROM na_purchase_plan_detail
                                         GROUP BY na_purchase_plan_detail.product_id) t2
        WHERE t2.product_id= t.product_id) AS total_order_qty,
        pp.pcs_per_carton,
        m.vendor_id, m.vendor_cn_name, m.vendor_en_name,
        m.creator_id, m.creator_cn_name, m.creator_en_name
    FROM (na_purchase_plan_detail t
        JOIN na_purchase_plan m ON t.purchase_plan_id=m.id
        JOIN na_product p on t.product_id = p.id
        Join na_product_vendor_prop pp on p.id = pp.product_id)
    where t.order_qty is not null  and (t.order_qty > t.already_order_qty or t.already_order_qty is null) and  p.status = 1 and p.new_product = 2 and pp.risk_rating < 5 and t.hold = 2
;

--2018-02-13
ALTER TABLE na_product_vendor_prop ADD COLUMN hs_code_id VARCHAR(24);

--2018-02-26
update na_flow_bank_account set deposit_rate = deposit_rate / 100 where deposit_type = 2 and deposit_rate > 1;
update na_bank_account set deposit_rate = deposit_rate / 100 where deposit_type = 2 and deposit_rate > 1;


--2018-02-27 帮助表
DROP TABLE IF EXISTS "public"."na_helps";
CREATE TABLE  "public"."na_helps" (
  "id" varchar(24) NOT NULL DEFAULT NULL,
  "title" varchar(255) DEFAULT NULL,
  "content" text DEFAULT NULL,
  "sort" int4 DEFAULT NULL,
  "status" int2 DEFAULT NULL,
  "relations" varchar(255) DEFAULT NULL
);
COMMENT ON COLUMN "public"."na_helps"."title" IS '标题';
COMMENT ON COLUMN "public"."na_helps"."content" IS '正文';
COMMENT ON COLUMN "public"."na_helps"."sort" IS '显示排序';
COMMENT ON COLUMN "public"."na_helps"."status" IS '显示状态';
COMMENT ON COLUMN "public"."na_helps"."relations" IS '关联帮助[{id:'''', title:'''', sort:''''},{...}]';

CREATE INDEX "na_help_relation_idx" ON "na_helps" USING btree ("relations" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST);
CREATE INDEX "na_help_sort_idx" ON "na_helps" USING btree ("sort" "pg_catalog"."int4_ops" ASC NULLS LAST);
CREATE INDEX "na_help_title_idx" ON "na_helps" USING btree ("title" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST);
ALTER TABLE "public"."na_helps" ADD CONSTRAINT "na_help_id_key" UNIQUE ("id");
ALTER TABLE "public"."na_helps" ADD CONSTRAINT "na_help_pkey" PRIMARY KEY ("id");

INSERT INTO "na_account_resource"("id", "parent_id", "cn_name", "en_name", "leaf", "level", "type", "url", "status", "sort", "item_icon", "created_at", "code", "function")
VALUES ('MOD201801270551331501217', 'MOD20170728160348129', '系统帮助', 'System Help', 1, 1, 2, 'HelpView', 1, 5, NULL, '2018-02-27 05:51:33.547', 'Help', '[["normal|list","normal|add","normal|edit","normal|del"],[],["data|data","data|1","data|2","data|3","data|4"]]');


--2018-03-01
update na_product_quotation set product_status = 2 where exists (SELECT 1 from na_product p where p.status = 2 and p.id = product_id);

ALTER TABLE public.na_flow_order_shipping_plan ADD order_numbers VARCHAR(3000) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_plan.order_numbers IS '订单编号';
ALTER TABLE public.na_order_shipping_plan ADD order_numbers VARCHAR(3000) NULL;
COMMENT ON COLUMN public.na_order_shipping_plan.order_numbers IS '订单编号';
ALTER TABLE public.na_flow_order_shipping_apply ADD order_numbers VARCHAR(3000) NULL;
COMMENT ON COLUMN public.na_flow_order_shipping_apply.order_numbers IS '订单编号';
ALTER TABLE public.na_order_shipping_apply ADD order_numbers VARCHAR(3000) NULL;
COMMENT ON COLUMN public.na_order_shipping_apply.order_numbers IS '订单编号';


ALTER TABLE public.na_flow_service_provider_quotation ADD name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_service_provider_quotation.name IS '报价名称';
ALTER TABLE public.na_service_provider_quotation ADD name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_service_provider_quotation.name IS '报价名称';



drop table na_auto_code;

/*==============================================================*/
/* Table: na_auto_code                                            */
/*==============================================================*/
create table na_auto_code (
    id                   VARCHAR(24)          not null,
    title                VARCHAR(200)         null,
    code				         VARCHAR(100)		  null,
    format               TEXT                 null,
    main_value           INT4                 null,
    main_value_clean_rule VARCHAR(200)         null,
    sub_value            INT4                 null,
    sub_value_clean_rule  VARCHAR(200)         null,
    department_id        VARCHAR(24)          null,
    department_cn_name   VARCHAR(200)         null,
    department_en_name   VARCHAR(200)         null,
    description          TEXT                 null,
    updated_at           TIMESTAMP            null,
    group_no              INT2                 null,
    sort                 INT2                 null,
    last_value           VARCHAR(100)         null,
    status                 INT2         null,
    constraint PK_na_auto_code primary key (id)
);

comment on table na_auto_code is
'编号记录表';

comment on column na_auto_code.title is
'标题';

comment on column na_auto_code.code is
'规则类型';

comment on column na_auto_code.format is
'格式： 可变字串型，用于编码格式的模板内容（需要能解析：固定字符串、年、月、日、部门名（前几位）、用户名（前几位）、供应商Code、主累加值、从累加值；可用Freemarker方式解析）';

comment on column na_auto_code.main_value is
'主累加值： 整型，用于编码格式中累加值部分的记录；';

comment on column na_auto_code.main_value_clean_rule is
'主累加号清0条件：可变字串型，在生成规则中使主累加号从1开始的判断条件；';

comment on column na_auto_code.sub_value is
'从累加值： 整型，用于编码格式中累加值部分的记录（备用字段）';

comment on column na_auto_code.sub_value_clean_rule is
'从累加号清0条件：可变字串型，在生成规则中使从累加号从1开始的判断条件';

comment on column na_auto_code.department_id is
'隶属部门id';

comment on column na_auto_code.department_cn_name is
'隶属部门中文名';

comment on column na_auto_code.department_en_name is
'隶属部门英文名';

comment on column na_auto_code.description is
'描述';

comment on column na_auto_code.updated_at is
'更新时间';

comment on column na_auto_code.group_no is
'组编号';

comment on column na_auto_code.sort is
'排序';

--2018-03-05
--初始化发货计划、发货确认order_no字段值
update na_flow_order_shipping_plan t
set order_numbers =
(SELECT string_agg(d.order_number, ',') from na_flow_order_shipping_plan_detail d where d.business_id = t.id);

update na_order_shipping_plan t
set order_numbers =
(SELECT string_agg(d.order_number, ',') from na_order_shipping_plan_detail d where d.order_shipping_plan_id = t.id);

update na_flow_order_shipping_apply t
set order_numbers =
(SELECT string_agg(d.order_number, ',') from na_flow_order_shipping_apply_detail d where d.business_id = t.id);

update na_order_shipping_apply t
set order_numbers =
(SELECT string_agg(d.order_number, ',') from na_order_shipping_apply_detail d where d.order_shipping_apply_id = t.id);

--初始化旧产品的订单质检标记数值
UPDATE na_product set qc_index = qc_index + 2 WHERE id like 'PDT2017123123%' and new_product = 2;

ALTER TABLE public.na_bank_account ADD beneficiary_cn_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_bank_account.beneficiary_cn_name IS '收款人中文名';
ALTER TABLE public.na_bank_account ADD beneficiary_en_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_bank_account.beneficiary_en_name IS '收款人英文名';
ALTER TABLE public.na_flow_bank_account ADD beneficiary_cn_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_bank_account.beneficiary_cn_name IS '收款人中文名';
ALTER TABLE public.na_flow_bank_account ADD beneficiary_en_name VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_bank_account.beneficiary_en_name IS '收款人英文名';


--2018-03-07
ALTER TABLE public.na_flow_purchase_contract ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_aud IS ' 其它费用（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_rmb IS '其它费用（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_usd IS '其它费用（USD）';

ALTER TABLE public.na_purchase_contract ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_aud IS ' 其它费用（AUD）';
ALTER TABLE public.na_purchase_contract ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_rmb IS '其它费用（RMB）';
ALTER TABLE public.na_purchase_contract ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_usd IS '其它费用（USD）';


ALTER TABLE public.na_flow_purchase_contract ADD total_value_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_value_aud IS '总货值（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD total_value_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_value_rmb IS '总货值（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD total_value_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_value_usd IS '总货值（USD）';

ALTER TABLE public.na_purchase_contract ADD total_value_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_value_aud IS '总货值（AUD）';
ALTER TABLE public.na_purchase_contract ADD total_value_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_value_rmb IS '总货值（RMB）';
ALTER TABLE public.na_purchase_contract ADD total_value_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_value_usd IS '总货值（USD）';

--采购合同其它费用明细
CREATE TABLE na_flow_purchase_contract_other_detail
(
    id              VARCHAR(24) NOT NULL
        CONSTRAINT pk_na_flow_purchase_contract_other_detail
        PRIMARY KEY,
    business_id     VARCHAR(24),
    item_id         VARCHAR(24),
    item_cn_name    VARCHAR(200),
    item_en_name    VARCHAR(200),
    currency        SMALLINT,
    price_aud       NUMERIC(20, 4),
    price_rmb       NUMERIC(20, 4),
    price_usd       NUMERIC(20, 4),
    qty             INTEGER,
    rate_aud_to_rmb NUMERIC(20, 4),
    rate_aud_to_usd NUMERIC(20, 4),
    remark          TEXT,
    created_at      TIMESTAMP,
    settlement_type SMALLINT

);

COMMENT ON TABLE na_flow_purchase_contract_other_detail IS '采购合同其它费用明细单';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.id IS 'id';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.business_id IS '单据流水号';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.item_id IS '项目id';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.item_cn_name IS '项目中文名';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.item_en_name IS '项目英文名';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.currency IS '结算币种';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.price_aud IS '单价（AUD）';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.price_rmb IS '实收（RMB）';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.price_usd IS '实收（USD）';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.qty IS '数量';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.rate_aud_to_rmb IS '人民币汇率';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.rate_aud_to_usd IS '美元汇率';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.remark IS '备注';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.created_at IS '创建时间';

COMMENT ON COLUMN na_flow_purchase_contract_other_detail.settlement_type IS '结算类型';


--
CREATE TABLE na_purchase_contract_other_detail
(
    id              VARCHAR(24) NOT NULL
        CONSTRAINT pk_na_purchase_contract_other_detail
        PRIMARY KEY,
    order_id     VARCHAR(24),
    item_id         VARCHAR(24),
    item_cn_name    VARCHAR(200),
    item_en_name    VARCHAR(200),
    currency        SMALLINT,
    price_aud       NUMERIC(20, 4),
    price_rmb       NUMERIC(20, 4),
    price_usd       NUMERIC(20, 4),
    qty             INTEGER,
    rate_aud_to_rmb NUMERIC(20, 4),
    rate_aud_to_usd NUMERIC(20, 4),
    remark          TEXT,
    created_at      TIMESTAMP,
    settlement_type SMALLINT

);

COMMENT ON TABLE na_purchase_contract_other_detail IS '采购合同其它费用明细单';

COMMENT ON COLUMN na_purchase_contract_other_detail.id IS 'id';

COMMENT ON COLUMN na_purchase_contract_other_detail.order_id IS '订单id';

COMMENT ON COLUMN na_purchase_contract_other_detail.item_id IS '项目id';

COMMENT ON COLUMN na_purchase_contract_other_detail.item_cn_name IS '项目中文名';

COMMENT ON COLUMN na_purchase_contract_other_detail.item_en_name IS '项目英文名';

COMMENT ON COLUMN na_purchase_contract_other_detail.currency IS '结算币种';

COMMENT ON COLUMN na_purchase_contract_other_detail.price_aud IS '单价（AUD）';

COMMENT ON COLUMN na_purchase_contract_other_detail.price_rmb IS '实收（RMB）';

COMMENT ON COLUMN na_purchase_contract_other_detail.price_usd IS '实收（USD）';

COMMENT ON COLUMN na_purchase_contract_other_detail.qty IS '数量';

COMMENT ON COLUMN na_purchase_contract_other_detail.rate_aud_to_rmb IS '人民币汇率';

COMMENT ON COLUMN na_purchase_contract_other_detail.rate_aud_to_usd IS '美元汇率';

COMMENT ON COLUMN na_purchase_contract_other_detail.remark IS '备注';

COMMENT ON COLUMN na_purchase_contract_other_detail.created_at IS '创建时间';

COMMENT ON COLUMN na_purchase_contract_other_detail.settlement_type IS '结算类型';


--2018-03-08
ALTER TABLE public.na_flow_purchase_contract_deposit RENAME COLUMN total_deposit_aud TO total_value_deposit_aud;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_value_deposit_aud IS '总货值定金（AUD）';
ALTER TABLE public.na_flow_purchase_contract_deposit RENAME COLUMN total_deposit_rmb TO total_value_deposit_rmb;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_value_deposit_rmb IS '总货值定金（RMB）';
ALTER TABLE public.na_flow_purchase_contract_deposit RENAME COLUMN total_deposit_usd TO total_value_deposit_usd;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_value_deposit_usd IS '总货值定金（USD）';

ALTER TABLE public.na_flow_purchase_contract_deposit ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_other_aud IS '其它费用（AUD）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_other_rmb IS '其它费用（RMB）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.total_other_usd IS '其它费用（USD）';

ALTER TABLE public.na_flow_purchase_contract_deposit ADD payable_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payable_aud IS '应付款（AUD）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD payable_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payable_rmb IS '应付款（RMB）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD payable_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payable_usd IS '应付款（USD）';

ALTER TABLE public.na_flow_purchase_contract_deposit ADD payment_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payment_aud IS '实付款（AUD）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD payment_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payment_rmb IS '实付款（RMB）';
ALTER TABLE public.na_flow_purchase_contract_deposit ADD payment_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payment_usd IS '实付款（USD）';

ALTER TABLE public.na_purchase_contract_deposit RENAME COLUMN total_deposit_aud TO total_value_deposit_aud;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_value_deposit_aud IS '总货值定金（AUD）';
ALTER TABLE public.na_purchase_contract_deposit RENAME COLUMN total_deposit_rmb TO total_value_deposit_rmb;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_value_deposit_rmb IS '总货值定金（RMB）';
ALTER TABLE public.na_purchase_contract_deposit RENAME COLUMN total_deposit_usd TO total_value_deposit_usd;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_value_deposit_usd IS '总货值定金（USD）';

ALTER TABLE public.na_purchase_contract_deposit ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_other_aud IS '其它费用（AUD）';
ALTER TABLE public.na_purchase_contract_deposit ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_other_rmb IS '其它费用（RMB）';
ALTER TABLE public.na_purchase_contract_deposit ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.total_other_usd IS '其它费用（USD）';

ALTER TABLE public.na_purchase_contract_deposit ADD payable_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payable_aud IS '应付款（AUD）';
ALTER TABLE public.na_purchase_contract_deposit ADD payable_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payable_rmb IS '应付款（RMB）';
ALTER TABLE public.na_purchase_contract_deposit ADD payable_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payable_usd IS '应付款（USD）';

ALTER TABLE public.na_purchase_contract_deposit ADD payment_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payment_aud IS '实付款（AUD）';
ALTER TABLE public.na_purchase_contract_deposit ADD payment_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payment_rmb IS '实付款（RMB）';
ALTER TABLE public.na_purchase_contract_deposit ADD payment_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payment_usd IS '实付款（USD）';


ALTER TABLE public.na_flow_fee_register_detail ADD settlement_type INT2 NULL;
COMMENT ON COLUMN public.na_flow_fee_register_detail.settlement_type IS '结算类型';
ALTER TABLE public.na_fee_register_detail ADD settlement_type INT2 NULL;
COMMENT ON COLUMN public.na_fee_register_detail.settlement_type IS '结算类型';


--2018-03-09
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN received_rate_aud_to_rmb TO payment_rate_aud_to_rmb;
COMMENT ON COLUMN public.na_flow_purchase_contract.payment_rate_aud_to_rmb IS '实付定金汇率(AUD/RMB)';
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN received_rate_aud_to_usd TO payment_rate_aud_to_usd;
COMMENT ON COLUMN public.na_flow_purchase_contract.payment_rate_aud_to_usd IS '实付定金汇率(AUD/USD)';

ALTER TABLE public.na_purchase_contract RENAME COLUMN received_rate_aud_to_rmb TO payment_rate_aud_to_rmb;
COMMENT ON COLUMN public.na_purchase_contract.payment_rate_aud_to_rmb IS '实付定金汇率(AUD/RMB)';
ALTER TABLE public.na_purchase_contract RENAME COLUMN received_rate_aud_to_usd TO payment_rate_aud_to_usd;
COMMENT ON COLUMN public.na_purchase_contract.payment_rate_aud_to_usd IS '实付定金汇率(AUD/USD)';


ALTER TABLE public.na_flow_purchase_contract_deposit RENAME COLUMN received_rate_aud_to_rmb TO payment_rate_aud_to_rmb;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payment_rate_aud_to_rmb IS '实付定金汇率(AUD/RMB)';
ALTER TABLE public.na_flow_purchase_contract_deposit RENAME COLUMN received_rate_aud_to_usd TO payment_rate_aud_to_usd;
COMMENT ON COLUMN public.na_flow_purchase_contract_deposit.payment_rate_aud_to_usd IS '实付定金汇率(AUD/USD)';

ALTER TABLE public.na_purchase_contract_deposit RENAME COLUMN received_rate_aud_to_rmb TO payment_rate_aud_to_rmb;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payment_rate_aud_to_rmb IS '实付定金汇率(AUD/RMB)';
ALTER TABLE public.na_purchase_contract_deposit RENAME COLUMN received_rate_aud_to_usd TO payment_rate_aud_to_usd;
COMMENT ON COLUMN public.na_purchase_contract_deposit.payment_rate_aud_to_usd IS '实付定金汇率(AUD/USD)';


ALTER TABLE public.na_flow_fee_payment ADD payment_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_fee_payment.payment_rate_aud_to_rmb IS '实付汇率（AUD/RMB）';
ALTER TABLE public.na_flow_fee_payment ADD payment_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_fee_payment.payment_rate_aud_to_usd IS '实付汇率（AUD/USD）';

ALTER TABLE public.na_fee_payment ADD payment_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_fee_payment.payment_rate_aud_to_rmb IS '实付汇率（AUD/RMB）';
ALTER TABLE public.na_fee_payment ADD payment_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_fee_payment.payment_rate_aud_to_usd IS '实付汇率（AUD/USD）';

COMMENT ON COLUMN public.na_flow_fee_payment.total_price_aud IS '应付总金额（AUD）';
COMMENT ON COLUMN public.na_flow_fee_payment.total_price_rmb IS '应付总金额（RMB）';
COMMENT ON COLUMN public.na_flow_fee_payment.total_price_usd IS '应付总金额（USD）';
ALTER TABLE public.na_flow_fee_payment RENAME COLUMN received_total_price_aud TO payment_total_price_aud;
COMMENT ON COLUMN public.na_flow_fee_payment.payment_total_price_aud IS '实付总金额（AUD）';
ALTER TABLE public.na_flow_fee_payment RENAME COLUMN received_total_price_rmb TO payment_total_price_rmb;
COMMENT ON COLUMN public.na_flow_fee_payment.payment_total_price_rmb IS '实付总金额（RMB）';
ALTER TABLE public.na_flow_fee_payment RENAME COLUMN received_total_price_usd TO payment_total_price_usd;
COMMENT ON COLUMN public.na_flow_fee_payment.payment_total_price_usd IS '实付总金额（USD）';

COMMENT ON COLUMN public.na_fee_payment.total_price_aud IS '应付总金额（AUD）';
COMMENT ON COLUMN public.na_fee_payment.total_price_rmb IS '应付总金额（RMB）';
COMMENT ON COLUMN public.na_fee_payment.total_price_usd IS '应付总金额（USD）';
ALTER TABLE public.na_fee_payment RENAME COLUMN received_total_price_aud TO payment_total_price_aud;
COMMENT ON COLUMN public.na_fee_payment.payment_total_price_aud IS '实付总金额（AUD）';
ALTER TABLE public.na_fee_payment RENAME COLUMN received_total_price_rmb TO payment_total_price_rmb;
COMMENT ON COLUMN public.na_fee_payment.payment_total_price_rmb IS '实付总金额（RMB）';
ALTER TABLE public.na_fee_payment RENAME COLUMN received_total_price_usd TO payment_total_price_usd;
COMMENT ON COLUMN public.na_fee_payment.payment_total_price_usd IS '实付总金额（USD）';

ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN received_deposit_aud TO payment_deposit_aud;
COMMENT ON COLUMN public.na_flow_purchase_contract.payment_deposit_aud IS '实付定金（AUD）';
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN received_deposit_rmb TO payment_deposit_rmb;
COMMENT ON COLUMN public.na_flow_purchase_contract.payment_deposit_rmb IS '实付定金（RMB）';
ALTER TABLE public.na_flow_purchase_contract RENAME COLUMN received_deposit_usd TO payment_deposit_usd;
COMMENT ON COLUMN public.na_flow_purchase_contract.payment_deposit_usd IS '实付定金（USD）';


ALTER TABLE public.na_purchase_contract RENAME COLUMN received_deposit_aud TO payment_deposit_aud;
COMMENT ON COLUMN public.na_purchase_contract.payment_deposit_aud IS '实付定金（AUD）';
ALTER TABLE public.na_purchase_contract RENAME COLUMN received_deposit_rmb TO payment_deposit_rmb;
COMMENT ON COLUMN public.na_purchase_contract.payment_deposit_rmb IS '实付定金（RMB）';
ALTER TABLE public.na_purchase_contract RENAME COLUMN received_deposit_usd TO payment_deposit_usd;
COMMENT ON COLUMN public.na_purchase_contract.payment_deposit_usd IS '实付定金（USD）';

-- 增加AutoCode管理菜单项
INSERT INTO "na_account_resource"("id", "parent_id", "cn_name", "en_name", "leaf", "level", "type", "url", "status", "sort", "item_icon", "created_at", "code", "function")
VALUES ('MOD201802010337006944916', 'MOD20170728160348129', '自动生成编码', 'Auto Code', 1, 1, 2, 'AutoCodeView', 1, 6, NULL, '2018-03-01 03:37:00.049', 'AutoCode', '[["normal|list","normal|add","normal|edit","normal|del"],[],["data|data","data|1","data|2","data|3","data|4"]]');

--2018-03-12
ALTER TABLE public.na_flow_purchase_contract_detail ADD is_need_deposit INT2 NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract_detail.is_need_deposit IS '是否需要定金';
ALTER TABLE public.na_purchase_contract_detail ADD is_need_deposit INT2 NULL;
COMMENT ON COLUMN public.na_purchase_contract_detail.is_need_deposit IS '是否需要定金';

--2018-03-12
--增加AutoCode自动生成编码规则
INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031458512995', '订单质检报告[Richard Team]', 'order_inspection', '${departmentEnName?substring(0,2)?upper_case} IN${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201709261129178673511', 'Richard', 'Richard', '订单质检报告[Richard Team]', '2018-03-12 03:14:58.442', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031815725625', '样品质检报告标题[Ken Team]', 'sample_inspection', '${departmentEnName?substring(0,2)?upper_case} T${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201709261130013244134', 'Ken', 'Ken', '样品质检报告标题[Ken Team]', '2018-03-12 03:18:15.248', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212055115131718', '样品质检报告[Lestat Team]', 'sample_inspection', '${departmentEnName?substring(0,2)?upper_case} T${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP20170811135000104', 'Lestat', 'Lestat', '<font face="tahoma, arial, verdana, sans-serif">样品质检报告[Lestat Team]</font>', '2018-03-12 05:51:15.129', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031152525509', '订单质检报告标题[Bonnie Team]', 'order_inspection', '${departmentEnName?substring(0,2)?upper_case} IN${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201800030853145616084', 'Bonnie', 'Bonnie', '<font face="tahoma, arial, verdana, sans-serif">订单质检报告标题[Bonnie Team]</font>', '2018-03-12 06:18:22.661', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031338516234', '订单质检报告标题[Ken Team]', 'order_inspection', '${departmentEnName?substring(0,2)?upper_case} IN${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201709261130013244134', 'Ken', 'Ken', '订单质检报告标题[Ken Team]', '2018-03-12 06:18:28.651', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031555198006', '订单质检报告[Lestat Team]', 'order_inspection', '${departmentEnName?substring(0,2)?upper_case} IN${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP20170811135000104', 'Lestat', 'Lestat', '订单质检报告[Lestat Team]', '2018-03-12 06:18:38.471', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031718669257', '样品质检报告标题[Bonnie Team]', 'sample_inspection', '${departmentEnName?substring(0,2)?upper_case} T${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201800030853145616084', 'Bonnie', 'Bonnie', '<font face="tahoma, arial, verdana, sans-serif">样品质检报告标题[Bonnie Team]</font>', '2018-03-12 06:18:43.243', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212031957879223', '样品质检报告标题[Richard Team]', 'sample_inspection', '${departmentEnName?substring(0,2)?upper_case} T${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP201709261129178673511', 'Richard', 'Richard', '样品质检报告标题[Richard Team]', '2018-03-12 06:18:49.964', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212062122147250', '样品质检报告标题[Vic Team]', 'sample_inspection', '${departmentEnName?substring(0,2)?upper_case} T${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP20180822000000111', 'Vic', 'Vic', '<font face="tahoma, arial, verdana, sans-serif">样品质检报告标题[Vic Team]</font>', '2018-03-12 06:21:22.067', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status")
VALUES ('ATCD20180212062304348822', '订单质检报告标题[Vic Team]', 'order_inspection', '${departmentEnName?substring(0,2)?upper_case} IN${.now?string(''yyyy'')}${(mainValue + 1)?string(''0000'')}', 0, NULL, NULL, NULL, 'DEP20180822000000111', 'Vic', 'Vic', '订单质检报告标题[Vic Team]', '2018-03-12 06:23:04.871', 1, 1, NULL, 1);


--2018-03-15
ALTER TABLE public.na_flow_asn ADD flag_sync_status INT2 NULL;
COMMENT ON COLUMN public.na_flow_asn.flag_sync_status IS '同步状态';
ALTER TABLE public.na_flow_asn ADD flag_sync_date TIMESTAMP NULL;
COMMENT ON COLUMN public.na_flow_asn.flag_sync_date IS '同步时间';

--2018-03-16
ALTER TABLE public.na_product ADD purchase_type INT2 NULL;
COMMENT ON COLUMN public.na_product.purchase_type IS '采购类型：1、国际采购、2、本地采购';

--2018-03-16
--增加生成barcord和ean的规则
INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180216071807158043', 'ean编码', 'ean', '93700${(mainValue + 1)?string(''00000000'')}', 0, NULL, NULL, NULL, 'DEP20180822000000111', 'Vic', 'Vic', '<span style="font-size: 13.3333px;">ean编码</span>', '2018-03-16 09:48:41.763', 1, 1, '9350062192541', 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180216071941304912', 'barcode编码', 'barcode', 'NA${(mainValue + 1)?string(''000000'')}', 15755, NULL, NULL, NULL, 'DEP20180822000000111', 'Vic', 'Vic', '<font face="tahoma, arial, verdana, sans-serif">barcode编码</font>', '2018-03-16 09:48:41.766', 1, 1, 'NA015755', 1);

--2018-03-19
--增加生成安检报告标题和编码规则
INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180219065521670381', '安检报告标题[Lestat Team]', 'product_compliance', 'CC ${departmentEnName?substring(0,2)?upper_case} ${.now?string(''yyyy'')}${(mainValue + 1)?string(''000000'')}', 0, NULL, NULL, NULL, 'DEP20170811135000104', 'Lestat', 'Lestat', '安检报告标题[Lestat Team]', '2018-03-19 07:21:05.086', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180219065403906789', '安检报告标题[Richard Team]', 'product_compliance', 'CC ${departmentEnName?substring(0,2)?upper_case} ${.now?string(''yyyy'')}${(mainValue + 1)?string(''000000'')}', 0, NULL, NULL, NULL, 'DEP201709261129178673511', 'Richard', 'Richard', '安检报告标题[Richard Team]', '2018-03-19 07:21:10.189', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180219065309538036', '安检报告标题[Ken Team]', 'product_compliance', 'CC ${departmentEnName?substring(0,2)?upper_case} ${.now?string(''yyyy'')}${(mainValue + 1)?string(''000000'')}', 0, NULL, NULL, NULL, 'DEP201709261130013244134', 'Ken', 'Ken', '安检报告标题[Ken Team]', '2018-03-19 07:21:14.799', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180219060741890076', '安检报告标题[Vic Team]', 'product_compliance', 'CC ${departmentEnName?substring(0,2)?upper_case} ${.now?string(''yyyy'')}${(mainValue + 1)?string(''000000'')}', 0, NULL, NULL, NULL, 'DEP20180822000000111', 'Vic', 'Vic', '<font face="tahoma, arial, verdana, sans-serif">安检报告标题[Vic Team]</font>', '2018-03-19 07:21:19.529', 1, 1, NULL, 1);

INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180219065205601664', '安检报告标题[Bonnie Team]', 'product_compliance', 'CC ${departmentEnName?substring(0,2)?upper_case} ${.now?string(''yyyy'')}${(mainValue + 1)?string(''000000'')}', 0, NULL, NULL, NULL, 'DEP201800030853145616084', 'Bonnie', 'Bonnie', '<font face="tahoma, arial, verdana, sans-serif">安检报告标题[Bonnie Team]</font>', '2018-03-19 06:52:05.993', 1, 1, NULL, 1);

--2018-03-20
ALTER TABLE public.na_flow_purchase_contract ADD total_other_deposit_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_deposit_aud IS ' 其它费用定金（AUD）';
ALTER TABLE public.na_flow_purchase_contract ADD total_other_deposit_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_deposit_rmb IS '其它费用定金（RMB）';
ALTER TABLE public.na_flow_purchase_contract ADD total_other_deposit_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_purchase_contract.total_other_deposit_usd IS '其它费用定金（USD）';

ALTER TABLE public.na_purchase_contract ADD total_other_deposit_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_deposit_aud IS ' 其它费用定金（AUD）';
ALTER TABLE public.na_purchase_contract ADD total_other_deposit_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_deposit_rmb IS '其它费用定金（RMB）';
ALTER TABLE public.na_purchase_contract ADD total_other_deposit_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_purchase_contract.total_other_deposit_usd IS '其它费用定金（USD）';

--2018-03-20
--供应商编码自动生成规则
INSERT INTO "na_auto_code"("id", "title", "code", "format", "main_value", "main_value_clean_rule", "sub_value", "sub_value_clean_rule", "department_id", "department_cn_name", "department_en_name", "description", "updated_at", "group_no", "sort", "last_value", "status") VALUES
('ATCD20180220071745764963', '供应商编码', 'vendor_code', 'VEN${(mainValue + 1)?string(''0000'')}', 900, NULL, NULL, NULL, NULL, NULL, NULL, '<font face="tahoma, arial, verdana, sans-serif">供应商编码</font>', '2018-03-20 07:47:55.351', 1, 1, 'VEN0900', 1);


--2018-03-21
ALTER TABLE public.na_flow_sample ADD remark TEXT NULL;
COMMENT ON COLUMN public.na_flow_sample.remark IS '备注';
ALTER TABLE public.na_flow_sample ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample.total_other_aud IS '其它总费用（AUD）';
ALTER TABLE public.na_flow_sample ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample.total_other_rmb IS '其它总费用（RMB）';
ALTER TABLE public.na_flow_sample ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample.total_other_usd IS '其它总费用（USD）';


ALTER TABLE public.na_sample ADD remark TEXT NULL;
COMMENT ON COLUMN public.na_sample.remark IS '备注';
ALTER TABLE public.na_sample ADD total_other_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_other_aud IS '其它总费用（AUD）';
ALTER TABLE public.na_sample ADD total_other_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_other_rmb IS '其它总费用（RMB）';
ALTER TABLE public.na_sample ADD total_other_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample.total_other_usd IS '其它总费用（USD）';



--样品其它费用明细
CREATE TABLE na_flow_sample_other_detail
(
    id              VARCHAR(24) NOT NULL
        CONSTRAINT pk_na_flow_sample_other_detail
        PRIMARY KEY,
    business_id     VARCHAR(24),
    item_id         VARCHAR(24),
    item_cn_name    VARCHAR(200),
    item_en_name    VARCHAR(200),
    currency        SMALLINT,
    price_aud       NUMERIC(20, 4),
    price_rmb       NUMERIC(20, 4),
    price_usd       NUMERIC(20, 4),
    qty             INTEGER,
    rate_aud_to_rmb NUMERIC(20, 4),
    rate_aud_to_usd NUMERIC(20, 4),
    remark          TEXT,
    created_at      TIMESTAMP

);

COMMENT ON TABLE na_flow_sample_other_detail IS '采购合同其它费用明细单';

COMMENT ON COLUMN na_flow_sample_other_detail.id IS 'id';

COMMENT ON COLUMN na_flow_sample_other_detail.business_id IS '单据流水号';

COMMENT ON COLUMN na_flow_sample_other_detail.item_id IS '项目id';

COMMENT ON COLUMN na_flow_sample_other_detail.item_cn_name IS '项目中文名';

COMMENT ON COLUMN na_flow_sample_other_detail.item_en_name IS '项目英文名';

COMMENT ON COLUMN na_flow_sample_other_detail.currency IS '结算币种';

COMMENT ON COLUMN na_flow_sample_other_detail.price_aud IS '单价（AUD）';

COMMENT ON COLUMN na_flow_sample_other_detail.price_rmb IS '实收（RMB）';

COMMENT ON COLUMN na_flow_sample_other_detail.price_usd IS '实收（USD）';

COMMENT ON COLUMN na_flow_sample_other_detail.qty IS '数量';

COMMENT ON COLUMN na_flow_sample_other_detail.rate_aud_to_rmb IS '人民币汇率';

COMMENT ON COLUMN na_flow_sample_other_detail.rate_aud_to_usd IS '美元汇率';

COMMENT ON COLUMN na_flow_sample_other_detail.remark IS '备注';

COMMENT ON COLUMN na_flow_sample_other_detail.created_at IS '创建时间';

CREATE TABLE na_sample_other_detail
(
    id              VARCHAR(24) NOT NULL
        CONSTRAINT pk_na_sample_other_detail
        PRIMARY KEY,
    sample_id     VARCHAR(24),
    item_id         VARCHAR(24),
    item_cn_name    VARCHAR(200),
    item_en_name    VARCHAR(200),
    currency        SMALLINT,
    price_aud       NUMERIC(20, 4),
    price_rmb       NUMERIC(20, 4),
    price_usd       NUMERIC(20, 4),
    qty             INTEGER,
    rate_aud_to_rmb NUMERIC(20, 4),
    rate_aud_to_usd NUMERIC(20, 4),
    remark          TEXT,
    created_at      TIMESTAMP

);

COMMENT ON TABLE na_sample_other_detail IS '采购合同其它费用明细单';

COMMENT ON COLUMN na_sample_other_detail.id IS 'id';

COMMENT ON COLUMN na_sample_other_detail.sample_id IS '样品单id';

COMMENT ON COLUMN na_sample_other_detail.item_id IS '项目id';

COMMENT ON COLUMN na_sample_other_detail.item_cn_name IS '项目中文名';

COMMENT ON COLUMN na_sample_other_detail.item_en_name IS '项目英文名';

COMMENT ON COLUMN na_sample_other_detail.currency IS '结算币种';

COMMENT ON COLUMN na_sample_other_detail.price_aud IS '单价（AUD）';

COMMENT ON COLUMN na_sample_other_detail.price_rmb IS '实收（RMB）';

COMMENT ON COLUMN na_sample_other_detail.price_usd IS '实收（USD）';

COMMENT ON COLUMN na_sample_other_detail.qty IS '数量';

COMMENT ON COLUMN na_sample_other_detail.rate_aud_to_rmb IS '人民币汇率';

COMMENT ON COLUMN na_sample_other_detail.rate_aud_to_usd IS '美元汇率';

COMMENT ON COLUMN na_sample_other_detail.remark IS '备注';

COMMENT ON COLUMN na_sample_other_detail.created_at IS '创建时间';


ALTER TABLE public.na_flow_sample_payment ADD payment_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.payment_rate_aud_to_rmb IS '实付汇率（AUD/RMB）';
ALTER TABLE public.na_flow_sample_payment ADD payment_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.payment_rate_aud_to_usd IS '实付汇率（AUD/USD）';
ALTER TABLE public.na_flow_sample_payment ADD payment_total_sample_fee_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.payment_total_sample_fee_aud IS '实付总金额（AUD）';
ALTER TABLE public.na_flow_sample_payment ADD payment_total_sample_fee_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.payment_total_sample_fee_rmb IS '实付总金额（RMB）';
ALTER TABLE public.na_flow_sample_payment ADD payment_total_sample_fee_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_flow_sample_payment.payment_total_sample_fee_usd IS '实付总金额（USD）';


ALTER TABLE public.na_sample_payment ADD payment_rate_aud_to_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample_payment.payment_rate_aud_to_rmb IS '实付汇率（AUD/RMB）';
ALTER TABLE public.na_sample_payment ADD payment_rate_aud_to_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample_payment.payment_rate_aud_to_usd IS '实付汇率（AUD/USD）';
ALTER TABLE public.na_sample_payment ADD payment_total_sample_fee_aud DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample_payment.payment_total_sample_fee_aud IS '实付总金额（AUD）';
ALTER TABLE public.na_sample_payment ADD payment_total_sample_fee_rmb DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample_payment.payment_total_sample_fee_rmb IS '实付总金额（RMB）';
ALTER TABLE public.na_sample_payment ADD payment_total_sample_fee_usd DECIMAL(20,4) NULL;
COMMENT ON COLUMN public.na_sample_payment.payment_total_sample_fee_usd IS '实付总金额（USD）';


--2018-03-21
--会签流程当前处理人字段类型修改
ALTER TABLE "public"."na_flow_balance_refund"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_bank_account"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_compliance_apply"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_fee_payment"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_order_shipping_plan"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_purchase_contract"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

ALTER TABLE "public"."na_flow_purchase_contract_deposit"
  ALTER COLUMN "assignee_cn_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_cn_name"::text,
  ALTER COLUMN "assignee_en_name" TYPE text COLLATE "pg_catalog"."default" USING "assignee_en_name"::text;

--增加流程发布管理表
CREATE TABLE "public"."na_flow_processes" (
  "id" varchar(24) NOT NULL,
  "category_id" varchar(24),
  "code" varchar(60),
  "ver" int4,
  "image" varchar(255),
  "content" text,
  "context" text,
  "is_publish" int2,
  "status" int2,
  "created_at" timestamp(255),
  "updated_at" timestamp,
  "publish_at" timestamp(255),
  PRIMARY KEY ("id")
);
COMMENT ON COLUMN "public"."na_flow_processes"."category_id" IS '流程分类';
COMMENT ON COLUMN "public"."na_flow_processes"."code" IS '流程编码';
COMMENT ON COLUMN "public"."na_flow_processes"."ver" IS '发布版本';
COMMENT ON COLUMN "public"."na_flow_processes"."image" IS '流程图';
COMMENT ON COLUMN "public"."na_flow_processes"."content" IS '流程定义';
COMMENT ON COLUMN "public"."na_flow_processes"."context" IS '流程说明';
COMMENT ON COLUMN "public"."na_flow_processes"."is_publish" IS '是否发布';
COMMENT ON COLUMN "public"."na_flow_processes"."status" IS '记录状态';
COMMENT ON COLUMN "public"."na_flow_processes"."created_at" IS '创建时间';
COMMENT ON COLUMN "public"."na_flow_processes"."updated_at" IS '修改时间';
COMMENT ON COLUMN "public"."na_flow_processes"."publish_at" IS '发布时间';

--2018-03-22
ALTER TABLE "public"."na_flow_processes" ADD COLUMN "deployment_id" varchar(24),ADD COLUMN "name" varchar(60);
COMMENT ON COLUMN "public"."na_flow_processes"."deployment_id" IS '流程发布id';
COMMENT ON COLUMN "public"."na_flow_processes"."name" IS '流程名';


ALTER TABLE "public"."na_flow_processes" ADD COLUMN "dpmn" varchar(255),ADD COLUMN "process_definition_id" varchar(50);
COMMENT ON COLUMN "public"."na_flow_processes"."image" IS '流程图资源名';
COMMENT ON COLUMN "public"."na_flow_processes"."dpmn" IS 'dpmn资源名';
COMMENT ON COLUMN "public"."na_flow_processes"."process_definition_id" IS '流程定义id';

--20180323 插入工作流管理模块配置
INSERT INTO "na_account_resource"("id", "parent_id", "cn_name", "en_name", "leaf", "level", "type", "url", "status", "sort", "item_icon", "created_at", "code", "function") VALUES ('MOD201802210931137851739', 'MOD20170822101010115', '工作流管理', 'Flow Processes', 1, 1, 2, 'FlowProcessesView', 1, 2, 'fa fa-fw fa-cogs', '2018-03-21 09:31:13.659', 'FlowProcesses', '[["normal|list","normal|add","normal|edit","normal|del"],[],["data|data","data|1","data|2","data|3","data|4"]]');


ALTER TABLE public.na_flow_order_qc ADD order_number VARCHAR(50) NULL;
COMMENT ON COLUMN public.na_flow_order_qc.order_number IS '订单编号';
update na_flow_order_qc t set order_number = (SELECT d.order_number from na_flow_order_qc_detail d where d.business_id = t.id);


-- ----------------------------
-- Table structure for na_sta_order 合同统计表
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_sta_order";
CREATE TABLE "public"."na_sta_order" (
  "id" int8 DEFAULT NULL,
  "order_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_title" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_product_category_alias" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_index" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_en_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "container_type" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "container_qty" int8 DEFAULT NULL,
  "status" int2 DEFAULT NULL,
  "created_at" timestamp(6) DEFAULT NULL,
  "end_time" timestamp(6) DEFAULT NULL,
  "start_time" timestamp(6) DEFAULT NULL,
  "currency" int2 DEFAULT NULL,
  "rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "total_cbm" numeric(20,4) DEFAULT NULL,
  "total_price_aud" numeric(20,4) DEFAULT NULL,
  "total_price_rmb" numeric(20,4) DEFAULT NULL,
  "total_price_usd" numeric(20,4) DEFAULT NULL,
  "estimated_eta" timestamp(6) DEFAULT NULL,
  "write_off_aud" numeric(20,4) DEFAULT NULL,
  "write_off_rmb" numeric(20,4) DEFAULT NULL,
  "write_off_usd" numeric(20,4) DEFAULT NULL,
  "final_total_price_aud" numeric(20,4) DEFAULT NULL,
  "ex_work" numeric(20,4) DEFAULT NULL,
  "final_total_price_rmb" numeric(20,4) DEFAULT NULL,
  "final_total_price_usd" numeric(20,4) DEFAULT NULL,
  "deposit_rate" numeric(20,4) DEFAULT NULL,
  "deposit_aud" numeric(20,4) DEFAULT NULL,
  "deposit_rmb" numeric(20,4) DEFAULT NULL,
  "deposit_usd" numeric(20,4) DEFAULT NULL,
  "balance_aud" numeric(20,4) DEFAULT NULL,
  "balance_rmb" numeric(20,4) DEFAULT NULL,
  "balance_usd" numeric(20,4) DEFAULT NULL,
  "deposit_date" timestamp(6) DEFAULT NULL,
  "balance_date" timestamp(6) DEFAULT NULL,
  "service_provider_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "service_provider_cn_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "service_provider_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "agent_notification_date" timestamp(6) DEFAULT NULL,
  "ready_date" timestamp(6) DEFAULT NULL,
  "etd" timestamp(6) DEFAULT NULL,
  "eta" timestamp(6) DEFAULT NULL,
  "picurrency" int2 DEFAULT NULL,
  "credit_terms" int2 DEFAULT NULL,
  "delivery_time" timestamp(6) DEFAULT NULL,
  "arrival_days" int2 DEFAULT NULL,
  "shipping_doc_received_date" timestamp(6) DEFAULT NULL,
  "shipping_doc_forwarded_date" timestamp(6) DEFAULT NULL,
  "charge_item_fee_aud" numeric(20,4) DEFAULT NULL,
  "charge_item_fee_rmb" numeric(20,4) DEFAULT NULL,
  "charge_item_fee_usd" numeric(20,4) DEFAULT NULL,
  "port_fee_aud" numeric(20,4) DEFAULT NULL,
  "port_fee_rmb" numeric(20,4) DEFAULT NULL,
  "port_fee_usd" numeric(20,4) DEFAULT NULL,
  "total_freight_aud" numeric(20,4) DEFAULT NULL,
  "total_freight_rmb" numeric(20,4) DEFAULT NULL,
  "total_freight_usd" numeric(20,4) DEFAULT NULL,
  "electronic_processing_fee_aud" numeric(20,4) DEFAULT NULL,
  "telex_released" int4 DEFAULT NULL,
  "tariff_aud" numeric(20,4) DEFAULT NULL,
  "tariff_rmb" numeric(20,4) DEFAULT NULL,
  "tariff_usd" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_aud" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_rmb" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_usd" numeric(20,4) DEFAULT NULL,
  "cost_currency" int2 DEFAULT NULL,
  "cost_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "cost_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "gst_aud" numeric(20,4) DEFAULT NULL,
  "gst_rmb" numeric(20,4) DEFAULT NULL,
  "gst_usd" numeric(20,4) DEFAULT NULL,
  "total_cost_aud" numeric(20,4) DEFAULT NULL,
  "freigh_gst_aud" numeric(20,4) DEFAULT NULL,
  "lead_time" int2 DEFAULT NULL,
  "sailing_days" int2 DEFAULT NULL,
  "buyer_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "buyer_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "buyer_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "estimatedBalance" numeric(20,4) DEFAULT NULL,
  "month_eta" int2 DEFAULT NULL,
  "year_eta" int2 DEFAULT NULL
)
;


ALTER TABLE public.na_flow_order_qc ADD order_title VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_flow_order_qc.order_title IS '订单标题';
update na_flow_order_qc t set order_title = (SELECT d.order_title from na_flow_order_qc_detail d where d.business_id = t.id);
ALTER TABLE public.na_reports ADD order_title VARCHAR(200) NULL;
COMMENT ON COLUMN public.na_reports.order_title IS '订单标题';
update na_reports t set order_title = (SELECT o.order_title from na_purchase_contract o where o.id = t.order_id);

-- ----------------------------
-- Table structure for na_sta_order 29/03/2018 09:25:56
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_sta_order";
CREATE TABLE "public"."na_sta_order" (
  "id" varchar(64) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_title" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "product_category_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_product_category_alias" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_index" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "origin_port_en_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "container_type" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "container_qty" numeric(20,4) DEFAULT NULL,
  "status" int2 DEFAULT NULL,
  "created_at" timestamp(6) DEFAULT NULL,
  "end_time" timestamp(6) DEFAULT NULL,
  "start_time" timestamp(6) DEFAULT NULL,
  "currency" int2 DEFAULT NULL,
  "contract_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "contract_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "total_cbm" numeric(20,4) DEFAULT NULL,
  "total_packing_cbm" numeric(20,4) DEFAULT NULL,
  "total_price_aud" numeric(20,4) DEFAULT NULL,
  "total_price_rmb" numeric(20,4) DEFAULT NULL,
  "total_price_usd" numeric(20,4) DEFAULT NULL,
  "estimated_eta" timestamp(6) DEFAULT NULL,
  "write_off_aud" numeric(20,4) DEFAULT NULL,
  "write_off_rmb" numeric(20,4) DEFAULT NULL,
  "write_off_usd" numeric(20,4) DEFAULT NULL,
  "final_total_price_aud" numeric(20,4) DEFAULT NULL,
  "ex_work_aud" numeric(20,4) DEFAULT NULL,
  "ex_work_rmb" numeric(20,4) DEFAULT NULL,
  "ex_work_usd" numeric(20,4) DEFAULT NULL,
  "final_total_price_rmb" numeric(20,4) DEFAULT NULL,
  "final_total_price_usd" numeric(20,4) DEFAULT NULL,
  "deposit_rate" numeric(20,4) DEFAULT NULL,
  "deposit_aud" numeric(20,4) DEFAULT NULL,
  "deposit_rmb" numeric(20,4) DEFAULT NULL,
  "deposit_usd" numeric(20,4) DEFAULT NULL,
  "deposit_type" int2 DEFAULT NULL,
  "deposit_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "deposit_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "balance_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "balance_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "service_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "service_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "cost_rate_aud_to_rmb" numeric(20,4) DEFAULT NULL,
  "cost_rate_aud_to_usd" numeric(20,4) DEFAULT NULL,
  "balance_aud" numeric(20,4) DEFAULT NULL,
  "balance_rmb" numeric(20,4) DEFAULT NULL,
  "balance_usd" numeric(20,4) DEFAULT NULL,
  "deposit_date" timestamp(6) DEFAULT NULL,
  "balance_date" timestamp(6) DEFAULT NULL,
  "estimated_balance" timestamp(6) DEFAULT NULL,
  "service_provider_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "service_provider_cn_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "service_provider_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "agent_notification_date" timestamp(6) DEFAULT NULL,
  "ready_date" timestamp(6) DEFAULT NULL,
  "etd" timestamp(6) DEFAULT NULL,
  "eta" timestamp(6) DEFAULT NULL,
  "total_order_qty" int2 DEFAULT NULL,
  "picurrency" int2 DEFAULT NULL,
  "credit_terms" int2 DEFAULT NULL,
  "delivery_time" timestamp(6) DEFAULT NULL,
  "arrival_days" int2 DEFAULT NULL,
  "shipping_doc_received_date" timestamp(6) DEFAULT NULL,
  "shipping_doc_forwarded_date" timestamp(6) DEFAULT NULL,
  "charge_item_fee_aud" numeric(20,4) DEFAULT NULL,
  "charge_item_fee_rmb" numeric(20,4) DEFAULT NULL,
  "charge_item_fee_usd" numeric(20,4) DEFAULT NULL,
  "total_sales_price_aud" numeric(20,4) DEFAULT NULL,
  "total_sales_price_rmb" numeric(20,4) DEFAULT NULL,
  "total_sales_price_usd" numeric(20,4) DEFAULT NULL,
  "port_fee_aud" numeric(20,4) DEFAULT NULL,
  "port_fee_rmb" numeric(20,4) DEFAULT NULL,
  "port_fee_usd" numeric(20,4) DEFAULT NULL,
  "total_freight_aud" numeric(20,4) DEFAULT NULL,
  "total_freight_rmb" numeric(20,4) DEFAULT NULL,
  "total_freight_usd" numeric(20,4) DEFAULT NULL,
  "electronic_processing_fee_aud" numeric(20,4) DEFAULT NULL,
  "electronic_processing_fee_rmb" numeric(20,4) DEFAULT NULL,
  "electronic_processing_fee_usd" numeric(20,4) DEFAULT NULL,
  "telex_released" int4 DEFAULT NULL,
  "tariff_aud" numeric(20,4) DEFAULT NULL,
  "tariff_rmb" numeric(20,4) DEFAULT NULL,
  "tariff_usd" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_aud" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_rmb" numeric(20,4) DEFAULT NULL,
  "custom_processing_fee_usd" numeric(20,4) DEFAULT NULL,
  "cost_currency" int2 DEFAULT NULL,
  "lead_time" int2 DEFAULT NULL,
  "sailing_days" int2 DEFAULT NULL,
  "department_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "department_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "department_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creator_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creator_cn_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "creator_en_name" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "gst_aud" numeric(20,4) DEFAULT NULL,
  "gst_rmb" numeric(20,4) DEFAULT NULL,
  "gst_usd" numeric(20,4) DEFAULT NULL,
  "total_cost_aud" numeric(20,4) DEFAULT NULL,
  "flag_asn_time" timestamp(6) DEFAULT NULL,
  "freight_gst_aud" numeric(20,4) DEFAULT NULL,
  "freight_gst_usd" numeric(20,4) DEFAULT NULL,
  "freight_gst_rmb" numeric(20,4) DEFAULT NULL,
  "freight_insurance_aud" numeric(20,4) DEFAULT NULL,
  "freight_insurance_usd" numeric(20,4) DEFAULT NULL,
  "freight_insurance_rmb" numeric(20,4) DEFAULT NULL,
  "month_eta" int2 DEFAULT NULL,
  "year_eta" int2 DEFAULT NULL
)
;


--清除订单报表数据
DELETE FROM na_sta_order
--批量导入订单报表数据
INSERT INTO na_sta_order SELECT * FROM na_view_sta_order
--清除订单报表非法数据（一般要和上一个操作一起执行）
delete  from na_sta_order WHERE port_fee_usd = 'NaN' or total_freight_aud='NaN' or custom_processing_fee_aud = 'NaN'


ALTER TABLE "public"."na_purchase_contract_detail"
  ADD COLUMN "sales_price_aud" numeric(20,4),
  ADD COLUMN "sales_price_rmb" numeric(20,4),
  ADD COLUMN "sales_price_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_purchase_contract_detail"."sales_price_aud" IS '销售价';
COMMENT ON COLUMN "public"."na_purchase_contract_detail"."sales_price_rmb" IS '销售价';
COMMENT ON COLUMN "public"."na_purchase_contract_detail"."sales_price_usd" IS '销售价';


ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "total_gst_aud" numeric(20,4),
  ADD COLUMN "total_gst_rmb" numeric(20,4),
  ADD COLUMN "total_gst_usd" numeric(20,4);
COMMENT ON COLUMN "public"."na_purchase_contract"."total_gst_aud" IS '总GST（AUD）';
COMMENT ON COLUMN "public"."na_purchase_contract"."total_gst_rmb" IS '总GST（RMB）';
COMMENT ON COLUMN "public"."na_purchase_contract"."total_gst_usd" IS '总GST（USD）';

--修改订单报表字段名
ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_dept_cn_name" TO "department_cn_name";

ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_dept_en_name" TO "department_en_name";

ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_dept_id" TO "department_id";

ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_id" TO "creator_id";

ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_cn_name" TO "creator_cn_name";

ALTER TABLE "public"."na_sta_order" RENAME COLUMN "buyer_en_name" TO "creator_en_name";






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


ALTER TABLE "public"."na_flow_processes"
  ADD COLUMN "model" varchar(100);
COMMENT ON COLUMN "public"."na_flow_processes"."model" IS '角色权限model';
-- ----------------------------
-- Table structure for na_sta_orders_cycle 订单周期统计表
-- ----------------------------
DROP TABLE IF EXISTS "public"."na_sta_orders_cycle";
CREATE TABLE "public"."na_sta_orders_cycle" (
  "id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_number" varchar(50) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_title" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_index" varchar(30) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "flag_asn_time" timestamp(6) DEFAULT NULL,
  "created_time" timestamp(6) DEFAULT NULL,
  "vendor_id" varchar(24) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "vendor_en_name" varchar(150) COLLATE "pg_catalog"."default" DEFAULT NULL,
  "order_confirmed_date" timestamp(6) DEFAULT NULL,
  "deposit_date" timestamp(6) DEFAULT NULL,
  "original_ready_date" timestamp(6) DEFAULT NULL,
  "adjust_ready_date" timestamp(6) DEFAULT NULL,
  "original_etd" timestamp(6) DEFAULT NULL,
  "adjust_etd" timestamp(6) DEFAULT NULL,
  "agent_notification_date" timestamp(6) DEFAULT NULL,
  "balance_date" timestamp(6) DEFAULT NULL,
  "qc_time" timestamp(6) DEFAULT NULL,
  "shipping_doc_received_date" timestamp(6) DEFAULT NULL,
  "shipping_doc_forwarded_date" timestamp(6) DEFAULT NULL,
  "eta" timestamp(6) DEFAULT NULL,
  "container_arriving_time" timestamp(6) DEFAULT NULL,
  "put_away_time" timestamp(6) DEFAULT NULL,
  "order_confirmed_cycle" int2 DEFAULT NULL,
  "lead_time" int2 DEFAULT NULL,
  "container_port_cycle" int2 DEFAULT NULL,
  "custom_clear_cycle" int2 DEFAULT NULL,
  "container_arriving_cycle" int2 DEFAULT NULL,
  "unloading_cycle" int2 DEFAULT NULL,
  "order_cycle" int2 DEFAULT NULL,
  "status" int2 DEFAULT NULL
)
;
--20180328 流程授权设置表增加字段
ALTER TABLE "public"."na_flow_agent" ADD COLUMN "cn_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL;
ALTER TABLE "public"."na_flow_agent" ADD COLUMN "en_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL;

--订单周期表cronjob字段添加
INSERT INTO "public"."na_cronjob"("id", "code", "name", "type", "year", "day_of_week", "month", "day_of_month", "hour", "minute", "second", "day_of_week_unit", "month_unit", "day_of_month_unit", "hour_unit", "minute_unit", "remind_type", "role_codes", "is_system", "status", "updated_at") VALUES ('sta-orders-cycle-sync', 'sta_orders_cycle_sync', '同步订单周期报表', '2', '*', '*', '*', '*', '*', '30', '0', '1', '1', '1', '1', '2', '0', 'admin', '1', 1, '2018-03-29 08:03:58.015');

--2018-04-04
ALTER TABLE public.na_flow_purchase_plan_detail ADD product_quotation_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_plan_detail.product_quotation_id IS '采购询价id';
ALTER TABLE public.na_flow_purchase_plan_detail ADD product_quotation_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_plan_detail.product_quotation_business_id IS '采购询价单据id';
ALTER TABLE public.na_flow_purchase_plan_detail ADD product_quotation_detail_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_flow_purchase_plan_detail.product_quotation_detail_business_id IS '采购询价单据id';

ALTER TABLE public.na_purchase_plan_detail ADD product_quotation_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_plan_detail.product_quotation_id IS '采购询价id';
ALTER TABLE public.na_purchase_plan_detail ADD product_quotation_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_plan_detail.product_quotation_business_id IS '采购询价单据id';
ALTER TABLE public.na_purchase_plan_detail ADD product_quotation_detail_business_id VARCHAR(24) NULL;
COMMENT ON COLUMN public.na_purchase_plan_detail.product_quotation_detail_business_id IS '采购询价单据id';

--20180405 产品组合明细表里添加EAN字段
ALTER TABLE "public"."na_product_combined_detail"
  ADD COLUMN "ean" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL;

COMMENT ON COLUMN "public"."na_product_combined_detail"."ean" IS 'EAN标识';
--20180405 将产品EAN数据导入产品组合明细表
UPDATE  pcd set pcd.ean = o.ean
FROM na_product o left join na_product_combined_detail pcd
on o.id = pcd.product_id

--20180405 增加产品组合同步cronjob数据
INSERT INTO "public"."na_cronjob"("id", "code", "name", "type", "year", "day_of_week", "month", "day_of_month", "hour", "minute", "second", "day_of_week_unit", "month_unit", "day_of_month_unit", "hour_unit", "minute_unit", "remind_type", "role_codes", "is_system", "status", "updated_at") VALUES ('coj_product_combin_sync', 'coj_product_combined_sync', '同步产品组合', '2', '*', '*', '*', '*', '*', '30', '0', '1', '1', '1', '1', '2', '0', 'admin', '1', 1, '2018-03-29 08:03:58.015');

--20180405 产品组合表增加同步字段
ALTER TABLE "public"."na_product_combined"
  ADD COLUMN "flag_sync_status" int2 DEFAULT NULL,
  ADD COLUMN "flag_sync_date" timestamp(6) DEFAULT NULL;

COMMENT ON COLUMN "public"."na_product_combined"."flag_sync_status" IS '1，已更新未同步，2已添加未同步，3已同步';

COMMENT ON COLUMN "public"."na_product_combined"."flag_sync_date" IS '同步日期';

ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "cubic_weight" numeric(20,4);

COMMENT ON COLUMN "public"."na_purchase_contract"."cubic_weight" IS '体积重量';

--20180410 合同表添加实装体积和体积重量
ALTER TABLE "public"."na_purchase_contract"
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

COMMENT ON COLUMN "public"."na_purchase_contract"."total_shipping_cbm" IS '实装体积';
--20180410 订单报表添加实装体积和体积重量
ALTER TABLE "public"."na_sta_order"
  ADD COLUMN "cubic_weight" numeric(20,4);
ALTER TABLE "public"."na_sta_order"
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

ALTER TABLE "public"."na_flow_purchase_contract"
  ADD COLUMN "cubic_weight" numeric(20,4);

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."cubic_weight" IS '体积重量';

--20180410 合同表添加实装体积和体积重量
ALTER TABLE "public"."na_flow_purchase_contract"
  ADD COLUMN "total_shipping_cbm" numeric(20,4);

COMMENT ON COLUMN "public"."na_flow_purchase_contract"."total_shipping_cbm" IS '实装体积';

--20180411 合同报表取消 体积重量字段
ALTER TABLE "public"."na_sta_order"
  DROP COLUMN "cubic_weight";

