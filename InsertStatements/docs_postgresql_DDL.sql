CREATE TABLE fe_user.doc (
    doc_id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    data BYTEA,
    PRIMARY KEY (doc_id)
);