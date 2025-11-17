CREATE TABLE rm_character (
                              id               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                              external_id      BIGINT UNSIGNED    NOT NULL,
                              name             VARCHAR(100)    NOT NULL,
                              status           VARCHAR(20)     NULL,
                              species          VARCHAR(50)     NULL,
                              type             VARCHAR(100)    NULL,
                              gender           VARCHAR(20)     NULL,
                              origin_name      VARCHAR(100)    NULL,
                              origin_url       VARCHAR(255)    NULL,
                              location_name    VARCHAR(100)    NULL,
                              location_url     VARCHAR(255)    NULL,
                              image_url        VARCHAR(255)    NULL,
                              episodes_json    TEXT            NULL,
                              api_url          VARCHAR(255)    NULL,
                              created_in_api   DATETIME        NULL,
                              PRIMARY KEY (id),
                              CONSTRAINT uk_rm_character_external_id UNIQUE (external_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

ALTER TABLE rm_character ADD FULLTEXT INDEX ft_rm_character_name(name);
