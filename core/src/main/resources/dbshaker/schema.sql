CREATE MEMORY TABLE brands (
    id BIGINT,
    name VARCHAR(32)
);

CREATE MEMORY TABLE series (
    id BIGINT,
    brand_id BIGINT,
    name VARCHAR(32)
);

CREATE MEMORY TABLE models (
    id BIGINT,
    series_id BIGINT,
    name VARCHAR(32)
);

CREATE MEMORY TABLE spares (
    id BIGINT,
    brand_id BIGINT,
    name VARCHAR(32)
);

CREATE MEMORY TABLE spare_to_model (
    spare_id BIGINT,
    model_id BIGINT
);




