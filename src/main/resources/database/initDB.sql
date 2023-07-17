CREATE TABLE IF NOT EXISTS clients
(
    id    BIGSERIAL PRIMARY KEY ,
    name  VARCHAR(200) NOT NULL ,
    email VARCHAR(254) NOT NULL ,
    phone VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS orderss
(
    id    BIGSERIAL PRIMARY KEY ,
    id_cl BIGSERIAL             ,
    name  VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS iml_param
(
    id         BIGSERIAL PRIMARY KEY ,
    prog_name  VARCHAR(16) NOT NULL  ,
    param_name VARCHAR(8)  NOT NULL  ,
    param_val  integer
);

