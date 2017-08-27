-- delete from role where role.role = 'ADMIN';
-- select * from role;
-- select * from authorization;
select u.login, a.role from user u join authorization a on u.login=a.login join role r on a.role=r.role where login = 'a';