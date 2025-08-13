-- ----------------------------
-- Insert Sample Data
-- ----------------------------

-- LV1_SYMB (Level 1: A)
INSERT INTO LV1_SYMB (name, level, field1) VALUES
('A1', 'LV1', 'field1_A1'),
('A2', 'LV1', 'field1_A2');

-- LV2_SYMB (Level 2: B)
INSERT INTO LV2_SYMB (lv1_id, name, level, field1, field2) VALUES
(1, 'B1', 'LV2', 'field1_B1', 'field2_B1'),
(1, 'B2', 'LV2', 'field1_B2', 'field2_B2'),
(2, 'B3', 'LV2', 'field1_B3', 'field2_B3');

-- LV3_SYMB (Level 3: C)
INSERT INTO LV3_SYMB (lv2_id, name, level, field1, field2, field3) VALUES
(1, 'C1', 'LV3', 'field1_C1', 'field2_C1', 'field3_C1'),
(2, 'C2', 'LV3', 'field1_C2', 'field2_C2', 'field3_C2'),
(3, 'C3', 'LV3', 'field1_C3', 'field2_C3', 'field3_C3');

-- LV4_SYMB (Level 4: D)
INSERT INTO LV4_SYMB (lv3_id, name, level, field1, field2, field3, field4) VALUES
(1, 'D1', 'LV4', 'field1_D1', 'field2_D1', 'field3_D1', 'field4_D1'),
(2, 'D2', 'LV4', 'field1_D2', 'field2_D2', 'field3_D2', 'field4_D2'),
(3, 'D3', 'LV4', 'field1_D3', 'field2_D3', 'field3_D3', 'field4_D3');