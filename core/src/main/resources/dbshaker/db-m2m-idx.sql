ALTER TABLE spare_to_model ADD FOREIGN KEY (spare_id) REFERENCES spares(id);
ALTER TABLE spare_to_model ADD FOREIGN KEY (model_id) REFERENCES models(id);

CREATE INDEX spare_idx ON spare_to_model(spare_id);
CREATE INDEX model_idx ON spare_to_model(model_id);