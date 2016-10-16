ALTER TABLE models ADD PRIMARY KEY (id);
ALTER TABLE models ADD FOREIGN KEY (series_id) REFERENCES series(id);

