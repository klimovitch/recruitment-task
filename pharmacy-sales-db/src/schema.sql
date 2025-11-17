CREATE TABLE address (
                         id          	BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         street    		VARCHAR(100) NOT NULL,
                         post_code    	VARCHAR(6) NOT NULL,
                         city		    VARCHAR(50) NOT NULL
);

CREATE TABLE agent (
                       id          		BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       name    			VARCHAR(100) UNIQUE NOT NULL,
                       nip    			VARCHAR(10) UNIQUE NOT NULL,
                       address_id		BIGINT UNSIGNED,
                       FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE product (
                         id          		BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         name    			VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE document (
                          id          		BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          document_type 	ENUM ('PARAGON', 'FAKTURA') NOT NULL,
                          issue_at  		DATE NOT NULL,
                          agent_id			BIGINT UNSIGNED,
                          FOREIGN KEY (agent_id) REFERENCES agent(id)
);

CREATE INDEX idx_document_issue_at ON document(issue_at);
CREATE INDEX idx_document_type ON document(document_type);
CREATE INDEX idx_document_agent_id ON document(agent_id);

CREATE TABLE document_item (
                               id          		BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               quantity 		integer NOT NULL,
                               document_id 		BIGINT UNSIGNED NOT NULL,
                               product_id  		BIGINT UNSIGNED NOT NULL,
                               FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE INDEX idx_document_item_doc_prod ON document_item(document_id, product_id);
