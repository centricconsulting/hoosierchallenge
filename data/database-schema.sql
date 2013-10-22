drop table if exists transactions cascade;
create table transactions (
  id serial primary key,
  created_at timestamp with time zone not null default now()
);

drop table if exists transaction_documents;
create table transaction_documents (
  id serial primary key,
  transaction_id int not null references transactions(id),
  created_at timestamp with time zone not null default now(),
  document varchar(10000000) not null,
  number_sections int not null default 0,
  title varchar(500),
  is_output boolean default false
);

drop table if exists transaction_rules;
create table transaction_rules (
  id serial primary key,
  transaction_id int not null references transactions(id),
  rule_version varchar(100) not null,
  document_title varchar(500) not null,
  created_at timestamp with time zone not null default now()
);

GRANT ALL PRIVILEGES ON TABLE transactions TO public;
GRANT ALL PRIVILEGES ON TABLE transaction_rules TO public;
GRANT ALL PRIVILEGES ON TABLE transaction_documents TO public;
GRANT ALL PRIVILEGES ON SEQUENCE transactions_id_seq TO public;
GRANT ALL PRIVILEGES ON SEQUENCE transaction_rules_id_seq TO public;
GRANT ALL PRIVILEGES ON SEQUENCE transaction_documents_id_seq TO public;