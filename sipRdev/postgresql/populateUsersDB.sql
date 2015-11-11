create or replace function populate_users() returns integer as $$
DECLARE
user_id INTEGER;
BEGIN
        FOR i IN 10000..20000 LOOP
		    SELECT nextval('hibernate_sequence') into user_id;
		    INSERT INTO users VALUES (user_id, '1234', i);
		END LOOP;

        return 1;
END;
$$ language plpgsql;