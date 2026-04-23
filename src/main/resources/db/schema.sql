-- EPIC 2 initial schema

CREATE TABLE IF NOT EXISTS menu_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price_cents INTEGER NOT NULL,
    active INTEGER NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT
);

CREATE TABLE IF NOT EXISTS customer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    document TEXT,
    phone TEXT,
    email TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_header (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER,
    status TEXT NOT NULL,
    total_cents INTEGER NOT NULL DEFAULT 0,
    issued_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS order_item (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    menu_item_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price_cents INTEGER NOT NULL,
    total_price_cents INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_header(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_item(id)
);

CREATE INDEX IF NOT EXISTS idx_order_header_customer_id ON order_header(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_item(order_id);
CREATE INDEX IF NOT EXISTS idx_order_item_menu_item_id ON order_item(menu_item_id);

-- EPIC 3 sample seed data (idempotent)
INSERT INTO menu_item (name, description, price_cents, active)
SELECT 'Classic Burger', 'Beef burger with fries', 2890, 1
WHERE NOT EXISTS (
    SELECT 1 FROM menu_item WHERE name = 'Classic Burger'
);

INSERT INTO menu_item (name, description, price_cents, active)
SELECT 'Lemon Soda', 'Refreshing sparkling drink', 790, 1
WHERE NOT EXISTS (
    SELECT 1 FROM menu_item WHERE name = 'Lemon Soda'
);

-- EPIC 4 sample seed data (idempotent)
INSERT INTO customer (name, document, phone, email)
SELECT 'Maria Silva', '12345678910', '+55 11 98888-1111', 'maria.silva@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM customer WHERE name = 'Maria Silva' AND document = '12345678910'
);

INSERT INTO customer (name, document, phone, email)
SELECT 'Joao Santos', '98765432100', '+55 11 97777-2222', 'joao.santos@example.com'
WHERE NOT EXISTS (
    SELECT 1 FROM customer WHERE name = 'Joao Santos' AND document = '98765432100'
);
