UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0138'
)
),guarantee_letter_name = 'Declaration Of Authorization（浙江欧沃得）.pdf' WHERE order_number = 'J18-0138';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0143'
)
),guarantee_letter_name = '更改账户证明 - 德清利衡.pdf' WHERE order_number = 'J18-0143';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0172'
)
),guarantee_letter_name = 'Bank information - Aqua Marine - order 2.pdf' WHERE order_number = 'J18-0172';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0147'
)
),guarantee_letter_name = 'Declaration of Authorization-Folding arm awning - Order 5.pdf' WHERE order_number = 'J18-0147';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0112'
)
),guarantee_letter_name = 'Bank Account Guarantee Letter.jpg' WHERE order_number = 'J18-0112';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0182'
)
),guarantee_letter_name = 'Bank Account Guarantee Letter.jpg' WHERE order_number = 'J18-0182';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0266'
)
),guarantee_letter_name = 'MOB Australian Banking Details.png' WHERE order_number = 'J18-0266';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" = (
SELECT guarantee_letter FROM na_flow_fee_register WHERE "id" = 'FFR201803300631508628760'
)
),guarantee_letter_name = 'MOB Australian Banking Details.png' WHERE "id" = 'FFR201803300631508628760';


UPDATE na_flow_fee_register set guarantee_letter = (
SELECT document_id FROM na_attachment WHERE "id" in (
SELECT guarantee_letter FROM na_flow_fee_register WHERE order_number = 'J18-0091-1'
)
),guarantee_letter_name = 'Bank_Letter-Zodiac-Order 1.PDF' WHERE order_number = 'J18-0091-1';


--更新业务表
UPDATE na_fee_register b set guarantee_letter = (
SELECT a.guarantee_letter FROM na_flow_fee_register a WHERE a."id" = b.business_id
);

UPDATE na_fee_register b set guarantee_letter_name = (
SELECT a.guarantee_letter_name FROM na_flow_fee_register a WHERE a."id" = b.business_id
);




