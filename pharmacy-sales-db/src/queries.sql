-- June 2024
SELECT
    p.id as product_id,
    p.name as product_name,
    SUM(di.quantity) AS total_sold
FROM product p
         JOIN document_item di ON p.id = di.product_id
         JOIN document d      ON di.document_id = d.id
WHERE d.issue_at >= '2024-06-01'
  AND d.issue_at <  '2024-07-01'
GROUP BY p.id, p.name
ORDER BY p.name;


-- faktura June 2024
SELECT
    a.id as agent_id,
    a.name as agent_name,
    p.id as product_id,
    p.name as product_name,
    SUM(di.quantity) AS total_sold
FROM product p
         JOIN document_item di ON p.id = di.product_id
         JOIN document d ON di.document_id = d.id
         JOIN agent a ON a.id = d.agent_id
WHERE d.issue_at >= '2024-06-01'
  AND d.issue_at <  '2024-07-01'
  AND d.document_type = 'FAKTURA'
GROUP BY p.id, p.name, a.id, a.name
ORDER BY a.name, p.name;
