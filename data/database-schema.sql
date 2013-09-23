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
  title varchar(500)
);

drop table if exists transaction_rules;
create table transaction_rules (
  id serial primary key,
  transaction_id int not null references transactions(id),
  created_at timestamp with time zone not null default now(),
  rule_contents varchar(10000) not null
);
