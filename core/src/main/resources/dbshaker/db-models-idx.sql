ALTER TABLE models ADD PRIMARY KEY HASH (id);
ALTER TABLE models ADD FOREIGN KEY (series_id) REFERENCES series(id);

