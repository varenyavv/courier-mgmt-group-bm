-- Procedure to insert or update branch
create
or replace
procedure upsert_branch (
  p_error inout VARCHAR,
  p_is_insert in BOOLEAN,
  p_branch_code inout branch.branch_code % type,
  p_branch_name in branch.branch_name % type,
  p_address_line in branch.address_line % type,
  p_pincode in branch.pincode % type
) language plpgsql as
$$
declare
 -- No variable declarations at this time.
begin

if p_is_insert then
    p_branch_code = 'B' || lpad(cast (nextval('seq_branch_code') as varchar),4,'0');
    insert into branch (branch_code,branch_name,address_line,pincode)
        values (p_branch_code,p_branch_name,p_address_line,p_pincode);
    insert into service_pincode (pincode,branch_code)
        values (p_pincode,p_branch_code);
else
    update branch
    set branch_name = p_branch_name, address_line = p_address_line, pincode = p_pincode
    where branch_code = p_branch_code;

    update service_pincode
    set pincode = p_pincode
    where branch_code = p_branch_code;
end if;

exception
when others then p_error := sqlerrm;
end $$;

-- Procedure to insert or update agent
create or replace
procedure upsert_agent
    (
    p_error inout VARCHAR,
    p_is_insert in BOOLEAN,
    p_contact_num in agent.contact_num%type,
    p_name in agent.name%type,
    p_branch_code in agent.branch_code%type,
    p_address_line in agent.address_line%type,
    p_pincode in agent.pincode%type
    )
language plpgsql
as
$$
declare
 v_count int := 0;
begin

select count(*) into v_count from service_pincode where pincode = p_pincode;

if v_count = 0 then
    insert into service_pincode (pincode, branch_code) values (p_pincode, p_branch_code);
end if;

if p_is_insert then
	insert into agent (contact_num,name,branch_code,address_line,pincode)
        values (p_contact_num,p_name,p_branch_code,p_address_line,p_pincode);
else
    update agent
    set branch_code = p_branch_code, name = p_name, address_line = p_address_line, pincode = p_pincode
    where contact_num = p_contact_num;
end if;

exception
when others then p_error := sqlerrm;
end
$$;

-- Procedure to insert or update employee
create or replace
procedure upsert_employee
    (
    p_error inout VARCHAR,
    p_is_insert in BOOLEAN,
    p_employee_id inout employee.employee_id%type,
    p_contact_num in employee.contact_num%type,
    p_name in employee.name%type,
    p_branch_code in employee.branch_code%type
    )
language plpgsql
as
$$
declare
-- No variable declarations at this time.

begin
if p_is_insert then
    p_employee_id = nextval('seq_employee_id');

    insert into employee (employee_id,contact_num,name,branch_code)
        values (p_employee_id,p_contact_num,p_name,p_branch_code);
else
    update employee
    set contact_num = p_contact_num, branch_code = p_branch_code, name = p_name
    where employee_id = p_employee_id;
end if;

exception
when others then p_error := sqlerrm;
end
$$;