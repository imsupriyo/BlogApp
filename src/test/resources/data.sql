insert into role(id, name) values(1, 'ROLE_USER');
insert into role(id, name) values(2, 'ROLE_ADMIN');

insert into users(id, email, password, username) values(1, 'admin@gmail.com','$2a$10$01QK5.ZGfPP6PWlGTfL4keJT.tI6bTRXzoe/LMnya9Ec9WqMRAKzS','admin');
insert into users(id, email, password, username) values(2, 'test@gmail.com','$2a$10$.kURKRLJiETlIoNKolSwOu.mDdjnLUZ4Urn8fcbN0syzad.EGaYwC','test');

insert into user_role(role_id, user_id) values(1,2);
insert into user_role(role_id, user_id) values(2,1);