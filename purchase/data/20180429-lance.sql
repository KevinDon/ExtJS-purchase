-- ����na_product_vendor_prop ��������������������������������������������
update na_product_vendor_prop set master_carton_cbm =master_carton_w*master_carton_h*master_carton_l/1000000 where 1=1;
update na_product_vendor_prop set master_carton_cubic_weight =master_carton_cbm*250 where master_carton_cbm>0;

update na_product_vendor_prop set inner_carton_cbm =inner_carton_w*inner_carton_h*inner_carton_l/1000000 where 1=1;
update na_product_vendor_prop set inner_carton_cubic_weight =inner_carton_cbm*250 where inner_carton_cbm >0;

UPDATE na_product_vendor_prop set inner_carton_w=master_carton_w, inner_carton_l= master_carton_l, inner_carton_h= master_carton_h, inner_carton_cbm= master_carton_cbm, inner_carton_cubic_weight= master_carton_cubic_weight WHERE (inner_carton_cbm=0 OR inner_carton_cbm is null) And master_carton_cbm>0;

UPDATE na_product_vendor_prop set master_carton_w=inner_carton_w, master_carton_l=inner_carton_l, master_carton_h=inner_carton_h, master_carton_cbm=inner_carton_cbm, master_carton_cubic_weight=inner_carton_cubic_weight WHERE inner_carton_cbm>0 And (master_carton_cbm=0 or master_carton_cbm is null);

SELECT inner_carton_cbm,master_carton_cbm,master_carton_w,inner_carton_w, * FROM na_product_vendor_prop WHERE 1=1;