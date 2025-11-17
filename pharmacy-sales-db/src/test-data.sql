INSERT INTO product (name) VALUES
('Ibuprofen'),
('Paracetamol'),
('Syrop na kaszel'),
('Witamina D3'),
('Magnez B6'),
('Probiotyk');


INSERT INTO address (street, post_code, city) VALUES
('Ul. Kwiatowa 10',  '00-001', 'Warszawa'),
('Ul. Zielona 5',    '30-002', 'Krakow'),
('Ul. Ogrodowa 3',   '80-003', 'Gdansk');


INSERT INTO agent (name, nip, address_id) VALUES
('Przychodnia Alfa', '1234567890', 1),
('Dom Seniora Beta', '9876543210', 2),
('Klinika Medica',   '1112223334', 3);


-- PARAGONY
INSERT INTO document (document_type, issue_at, agent_id) VALUES
('PARAGON', '2024-06-03', NULL),
('PARAGON', '2024-06-10', NULL),
('PARAGON', '2024-06-21', NULL);

-- FAKTURY
INSERT INTO document (document_type, issue_at, agent_id) VALUES
('FAKTURA', '2024-06-05', 1),  -- Alfa
('FAKTURA', '2024-06-15', 2),  -- Beta
('FAKTURA', '2024-06-25', 3);  -- Medica


INSERT INTO document (document_type, issue_at, agent_id) VALUES
('FAKTURA', '2024-07-02', 1);


-- PARAGONY (document_id 1,2,3)
INSERT INTO document_item (quantity, document_id, product_id) VALUES
                                                                  -- paragon 1 (03.06)
                                                                  (1, 1, 1),   -- 1 x Ibuprofen
                                                                  (1, 1, 2),   -- 1 x Paracetamol
                                                                  -- paragon 2 (10.06)
                                                                  (2, 2, 3),   -- 2 x Syrop na kaszel
                                                                  (1, 2, 4),   -- 1 x Witamina D3
                                                                  -- paragon 3 (21.06)
                                                                  (1, 3, 5),   -- 1 x Magnez B6
                                                                  (2, 3, 4);   -- 2 x Witamina D3

-- FAKTURY (document_id 4,5,6)
INSERT INTO document_item (quantity, document_id, product_id) VALUES
                                                                  -- faktura 4 (Alfa)
                                                                  (20, 4, 1),   -- 20 x Ibuprofen
                                                                  (10, 4, 3),   -- 10 x Syrop na kaszel
                                                                  -- faktura 5 (Beta)
                                                                  (15, 5, 2),   -- 15 x Paracetamol
                                                                  (25, 5, 4),   -- 25 x Witamina D3
                                                                  (10, 5, 6),   -- 10 x Probiotyk
                                                                  -- faktura 6 (Medica)
                                                                  (12, 6, 5),   -- 12 x Magnez B6
                                                                  (18, 6, 6);   -- 18 x Probiotyk

-- document_id 7
INSERT INTO document_item (quantity, document_id, product_id) VALUES
(5, 7, 1),
(7, 7, 2);
