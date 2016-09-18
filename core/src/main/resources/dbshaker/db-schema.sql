CREATE MEMORY TABLE brands (
    id BIGINT NOT NULL,
    name VARCHAR(32)
);

CREATE MEMORY TABLE series (
    id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    name VARCHAR(32)
);

CREATE MEMORY TABLE models (
    id BIGINT NOT NULL,
    series_id BIGINT NOT NULL,
    name VARCHAR(32)
);

CREATE MEMORY TABLE spares (
    id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    name VARCHAR(32)
);

CREATE MEMORY TABLE spare_to_model (
    spare_id BIGINT NOT NULL,
    model_id BIGINT NOT NULL
);




