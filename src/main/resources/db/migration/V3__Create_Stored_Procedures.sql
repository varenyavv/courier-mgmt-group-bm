create or replace
procedure upsert_branch
    (
    p_is_insert in BOOLEAN,
    p_branch_code in branch.branch_code%type,
    p_branch_name in branch.branch_name%type,
    p_add_line in branch.add_line%type,
    p_pincode in branch.pincode%type,
    p_city in branch.city%type,
    p_state in branch.state%type,
    p_error inout VARCHAR
    )
language plpgsql
as
$$
declare
-- No variable declarations at this time.

begin
   if p_is_insert
   then
	insert
	into
	branch
    (
    branch_code,
	branch_name,
	add_line,
	pincode,
	city,
	state
    )
values (
     'B' || lpad(cast (nextval('seq_branch_code') as varchar), 4, '0'),
     p_branch_name,
     p_add_line,
     p_pincode,
     p_city,
     upper(p_state));
else
    update
	branch
set
	branch_name = p_branch_name,
	add_line = p_add_line,
	pincode = p_pincode,
	city = p_city,
	state = upper(p_state)
where
	branch_code = p_branch_code;
end if;

exception
when others then
        p_error := sqlerrm;
end
$$;

-- Agent_upsert Procedure

create or replace
procedure upsert_agent
    (
    p_is_insert in BOOLEAN,
    p_contact_num in agent.contact_num%type,
    p_name in agent.name%type,
    p_branch_code in agent.branch_code%type,    
    p_add_line in agent.add_line%type,
    p_pincode in agent.pincode%type,
    p_city in agent.city%type,
    p_state in agent.state%type,
    p_error inout VARCHAR
    )
language plpgsql
as
$$
declare
-- No variable declarations at this time.

begin
   if p_is_insert
   then
	insert
	into
	agent
    (
	contact_num,
	name,
      branch_code,	
	add_line,
	pincode,
	city,
	state
    )
values (    
	 p_contact_num,
     p_name,	 
     p_branch_code,
     p_add_line,
     p_pincode,
     p_city,
     upper(p_state));
else
    update
	agent
set
      branch_code = p_branch_code,
	name = p_name,
	add_line = p_add_line,
	pincode = p_pincode,
	city = p_city,
	state = upper(p_state)
where
	contact_num = p_contact_num;
end if;

exception
when others then
        p_error := sqlerrm;
end
$$