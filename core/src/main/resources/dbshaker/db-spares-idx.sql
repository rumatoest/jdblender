ALTER TABLE spares ADD PRIMARY KEY (id);
ALTER TABLE spares ADD FOREIGN KEY (brand_id) REFERENCES brands(id);

CREATE HASH INDEX flag_idx ON spares(flag);
CREATE HASH INDEX label_idx ON spares(label);
CREATE INDEX num_idx ON spares(num);