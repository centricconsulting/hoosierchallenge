drop table if exists transactions;
create table transactions (
  id serial primary key,
  created_at timestamp with time zone not null default now()
);

drop table if exists transaction_documents;
create table transaction_documents (
  id serial primary key,
  transaction_id int not null references transactions(id),
  created_at timestamp with time zone not null default now(),
  document bytea not null
);

drop table if exists transaction_rules;
create table transaction_rules (
  id serial primary key,
  transaction_id int not null references transactions(id),
  created_at timestamp with time zone not null default now(),
  rule_contents varchar(10000) not null
);
