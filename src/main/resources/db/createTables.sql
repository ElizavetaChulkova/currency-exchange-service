CREATE TABLE currency (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          code VARCHAR NOT NULL ,
                          full_name VARCHAR NOT NULL ,
                          sign VARCHAR NOT NULL );

CREATE UNIQUE INDEX idx_code ON currency (code);

CREATE TABLE exchangeRate (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              base_currency_id INTEGER NOT NULL ,
                              target_currency_id INTEGER NOT NULL ,
                              rate DECIMAL(6) NOT NULL ,
                              FOREIGN KEY (base_currency_id) REFERENCES currency (id) ON DELETE CASCADE,
                              FOREIGN KEY (target_currency_id) REFERENCES currency (id) ON DELETE CASCADE );

CREATE UNIQUE INDEX idx_base_target ON ExchangeRate (base_currency_id, target_currency_id);
