insert into ref_user_roles values (DEFAULT, 'USER') on conflict do nothing;
insert into ref_user_roles values (DEFAULT, 'TEACHER') on conflict do nothing;
insert into ref_user_roles values (DEFAULT, 'ADMIN') on conflict do nothing;
