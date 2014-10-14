delete from categories;
delete from property_keys;
delete from property_values;
delete from category_property_keys;
delete from category_property_values;
delete from spus;
delete from spu_properties;
delete from items;
-- delete from users;
delete from item_details;

insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(1,0,'纺织',1,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(2,0,'武器',0,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(3,0,'金属',2,2,now(),now());


insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(4,1,'衣料',2,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(5,1,'毛线',1,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(6,1,'蚕丝',0,2,now(),now());

insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(7,3,'黄金',0,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(8,3,'白银',1,2,now(),now());

insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(9,0,'汽车',3,2,now(),now());


insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(10,9,'小型车',2,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(11,9,'中型车',1,2,now(),now());
insert into categories (id,parent_id,name,rank,type,created_at,updated_at) values(12,9,'大型车',0,2,now(),now());


insert into property_keys(id,name,type,value_type) values(1,'布料用途',2,1);
insert into property_keys(id,name,type,value_type) values(2,'布料材质',2,1);
insert into property_keys(id,name,type,value_type) values(3,'布料风格',2,1);
insert into property_keys(id,name,type,value_type) values(4,'纯度',2,1);
insert into property_keys(id,name,type,value_type) values(5,'光泽度',2,1);
insert into property_keys(id,name,type,value_type) values(6,'品牌',2,1);
insert into property_keys(id,name,type,value_type) values(7,'车系',2,1);
insert into property_keys(id,name,type,value_type) values(8,'车型',2,1);

insert into property_values(id,data) values(1,'服装布料');
insert into property_values(id,data) values(2,'沙发布料');
insert into property_values(id,data) values(3,'床品布料');
insert into property_values(id,data) values(4,'棉布');
insert into property_values(id,data) values(5,'绸布');
insert into property_values(id,data) values(6,'麻布');
insert into property_values(id,data) values(7,'皮革');
insert into property_values(id,data) values(8,'日韩风格');
insert into property_values(id,data) values(9,'欧美风格');
insert into property_values(id,data) values(10,'田园风格');
insert into property_values(id,data) values(11,'千足金');
insert into property_values(id,data) values(12,'铂金');
insert into property_values(id,data) values(13,'24K金');
insert into property_values(id,data) values(14,'无杂质');
insert into property_values(id,data) values(15,'有杂质');
insert into property_values(id,data) values(16,'完美品');
insert into property_values(id,data) values(17,'奥迪');
insert into property_values(id,data) values(18,'宝马');
insert into property_values(id,data) values(19,'奔驰');
insert into property_values(id,data) values(20,'福特');
insert into property_values(id,data) values(21,'丰田');
insert into property_values(id,data) values(22,'吉利');
insert into property_values(id,data) values(23,'宝马3系');
insert into property_values(id,data) values(24,'宝马5系');
insert into property_values(id,data) values(25,'宝马7系');
insert into property_values(id,data) values(26,'宝马mini');
insert into property_values(id,data) values(27,'奥迪A3');
insert into property_values(id,data) values(28,'奥迪A1');
insert into property_values(id,data) values(29,'奥迪A4');
insert into property_values(id,data) values(30,'丰田皇冠');
insert into property_values(id,data) values(31,'丰田凯美瑞');
insert into property_values(id,data) values(32,'宝马2003款');
insert into property_values(id,data) values(33,'宝马2011款');
insert into property_values(id,data) values(34,'奥迪2010款');
insert into property_values(id,data) values(35,'奥迪2009款');
insert into property_values(id,data) values(36,'丰田2008款');
insert into property_values(id,data) values(37,'丰田2005款');


insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (1,1,1,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (2,1,2,null,1);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (3,1,3,null,2);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (4,4,1,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (5,4,2,null,2);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (6,4,3,null,1);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (7,3,4,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (8,3,5,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (9,7,4,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (10,7,5,null,0);

insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (14,9,6,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (15,10,6,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (16,11,6,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (17,12,6,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (18,10,7,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (19,11,7,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (20,12,7,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (21,10,8,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (22,11,8,null,0);
insert into category_property_keys(id,category_id,property_key_id,display_name,rank) values (23,12,8,null,0);


insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(1,1,1,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(2,1,2,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(3,1,3,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(4,2,4,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(5,2,5,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(6,2,6,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(7,3,8,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(8,3,9,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(9,3,10,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(10,4,1,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(11,4,2,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(12,4,3,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(13,5,4,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(14,5,5,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(15,5,6,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(16,6,8,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(17,6,9,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(18,6,10,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(19,9,11,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(20,9,12,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(21,9,13,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(22,10,14,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(23,10,15,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(24,10,16,2);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(25,15,17,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(26,15,18,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(27,15,19,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(28,15,20,3);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(29,16,17,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(30,16,18,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(31,16,19,2);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(32,17,17,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(33,17,18,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(34,17,19,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(35,17,20,3);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(36,18,23,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(37,18,27,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(38,18,30,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(39,19,24,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(40,19,28,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(41,19,31,2);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(42,21,32,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(43,21,33,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(44,21,34,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(45,21,35,3);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(46,21,36,4);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(47,21,37,5);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(48,22,32,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(49,22,33,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(50,22,34,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(51,22,35,3);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(52,22,36,4);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(53,22,37,5);

insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(54,23,32,0);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(55,23,33,1);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(56,23,34,2);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(57,23,35,3);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(58,23,36,4);
insert into category_property_values(id,category_property_key_id,property_value_id,rank) values(59,23,37,5);


insert into spus (id,category_id,name,status,created_at,updated_at) values (1,4,'屌丝衣料',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (2,4,'高富帅衣料',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (3,7,'俄罗斯黄金',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (4,7,'中国黄金',1,now(),now());

insert into spus (id,category_id,name,status,created_at,updated_at) values (5,10,'宝马mini',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (6,10,'奥迪A1',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (7,11,'奥迪A4',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (8,11,'丰田凯美瑞',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (9,12,'宝马5系',1,now(),now());
insert into spus (id,category_id,name,status,created_at,updated_at) values (10,12,'丰田皇冠',1,now(),now());

insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(1,1,1,1,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(2,1,2,6,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(3,2,1,1,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(4,2,2,7,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(5,3,4,13,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(6,3,5,16,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(7,4,4,12,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(8,4,5,15,1);

insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(9,5,6,18,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(10,5,7,24,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(11,5,8,26,2);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(12,6,6,17,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(13,6,7,21,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(14,6,8,34,2);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(15,7,6,17,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(16,7,7,29,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(17,7,8,35,2);

insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(18,8,6,21,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(19,8,7,31,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(20,8,8,36,2);

insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(21,9,6,18,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(22,9,7,24,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(23,9,8,33,2);

insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(24,10,6,21,0);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(25,10,7,30,1);
insert into spu_properties (id,spu_id,property_key_id,property_value_id,rank) values(26,10,8,37,2);


insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(1,1,11,11,'屌丝必备衣料1亚麻棉毛绒','http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(2,1,11,11,'屌丝必备衣料2','http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg',330000,330100,1,1,now(),null,12,3,1800,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(3,1,11,11,'屌丝必备衣料3','http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg',330000,330100,1,-1,now(),now(),110,2,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(4,2,11,11,'高富帅必备衣料1亚麻','http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg',330000,330100,1,1,now(),null,103,6,100000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(5,1,22,12,'低端屌丝-高仿阿玛尼绒布','http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,40300,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(6,2,22,12,'屌丝中的战斗机','http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg',330000,330100,1,1,now(),null,105,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(7,2,22,12,'耐克跑步鞋','http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());

insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(8,1,11,11,'端点科技fighting','http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(9,1,11,11,'端点科技fighting2','http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,3,1800,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(10,1,11,11,'我的世界','http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg',330000,330100,1,-1,now(),now(),12,2,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(11,2,11,11,'青蓝计划','http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg',330000,330100,1,1,now(),null,0,6,100000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(12,1,22,12,'国庆来了','http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,40300,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(13,2,22,12,'中秋过了','http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(14,2,22,12,'女王范儿','http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg',330000,330100,1,1,now(),null,20,6,58000,now(),now());

insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(15,1,11,11,'乔丹运动鞋','http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(16,1,11,11,'科比81分','http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,3,1800,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(17,1,11,11,'马布里政委','http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg',330000,330100,1,-1,now(),now(),10,2,1000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(18,2,11,11,'姚明退役了','http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,100000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(19,1,22,12,'马拉多纳最牛','http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg',330000,330100,1,1,now(),null,30,6,40300,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(20,2,22,12,'梅西还是弱了点','http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(21,2,22,12,'永远的罗纳尔多','http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());

insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(22,3,33,14,'俄罗斯天然黄金高富帅','http://img03.taobaocdn.com/bao/uploaded/i3/T1R9niXotkXXXGglZ4_054051.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(23,3,33,14,'买黄金送钻石衣料','http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(24,3,33,14,'屌丝黄金','http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(25,4,33,14,'中国劣质屌丝黄金','http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg',330000,330100,1,1,now(),null,120,6,58000,now(),now());


insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(26,6,33,14,'奥迪A1 A3 A4L A5 A6L A7 A8改装专用LED车门镭射灯 迎宾灯 汽车','http://img01.taobaocdn.com/bao/uploaded/i1/T1xJruXl4eXXcopscT_011142.jpg_220x220.jpg',330000,330100,1,1,now(),null,110,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(27,6,33,14,'奥迪A1A3A4LA5/A6LQ3/Q5/Q7/TT亚麻棉毛绒冬季专用四季汽车坐座垫','http://img01.taobaocdn.com/bao/uploaded/i1/T1xJruXl4eXXcopscT_011142.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(28,5,33,14,'宝马mini 力帆320 奔驰smart 甲壳虫 POLO 车贴 全整车汽车','http://img02.taobaocdn.com/imgextra/i2/587696415/T2lD1UXjJaXXXXXXXX_!!587696415.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(29,5,33,14,'宝马mini countryman方向盘贴钻迷你改装专用汽车贴钻施华洛黄金','http://img02.taobaocdn.com/bao/uploaded/i2/T1qGOUXcJtXXawq0Z6_061318.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(30,7,33,14,'汽车奥迪A4L专用中网亮条/镀铬亮条/A5中网装饰条Q5/A4L中网改装黄金战衣','http://img04.taobaocdn.com/bao/uploaded/i4/T1nqSyXehBXXc2O4zX_085409.jpg_220x220.jpg',330000,330100,1,1,now(),null,110,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(31,7,33,14,'2013款奥迪A4L汽车轿车','http://img03.taobaocdn.com/bao/uploaded/i3/T1sbDVXeXgXXb0sqsT_010625.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(32,8,33,14,'丰田逸致卡罗拉专用座套花冠座套凯美瑞雅力士锐志汽车','http://img04.taobaocdn.com/bao/uploaded/i4/T1r3vTXoNgXXXvPA.2_045100.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(33,8,33,14,'丰田 凯美瑞 商务车 汽车','http://img01.taobaocdn.com/bao/uploaded/i1/T1gpmaXjdwXXc6mfna_121051.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());


insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(34,9,33,14,'10-12款宝马5系 商务 汽车','http://img02.taobaocdn.com/bao/uploaded/i2/T1zqvIXgXgXXbyBHc__105418.jpg_220x220.jpg',330000,330100,1,1,now(),null,110,6,54000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(35,9,33,14,'跑酷 BMW新宝马5系车 高档绒布内里','http://img02.taobaocdn.com/bao/uploaded/i2/T1Nsq3XlhmXXcUKvPb_094046.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(36,9,33,14,'钻石品质 宝马5系520 523li 屌丝','http://img04.taobaocdn.com/bao/uploaded/i4/T1m2vzXhpaXXXMdPfX_115620.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(37,10,33,14,'正品 丰田皇冠','http://img02.taobaocdn.com/bao/uploaded/i2/T1U.O.Xm0rXXX63GAT_010641.jpg_220x220.jpg',330000,330100,1,1,now(),null,10,6,58000,now(),now());
insert into items (id,spu_id,user_id,site_id,name,main_image,province,city,trade_type,status,on_shelf_at,off_shelf_at,quantity,sold_quantity,price,created_at,updated_at)
values(38,10,33,14,'尊贵黄金丰田皇冠','http://img03.taobaocdn.com/bao/uploaded/i3/T1JLDOXahlXXX70FM._080858.jpg_220x220.jpg',330000,330100,1,1,now(),null,110,6,54000,now(),now());

insert into item_details (id,item_id,image1,image2,image3,image4,packing_list) values (2,1,'/taobao.png','/baidu.png','/qq.png',null,'这是最好的时代,也是最坏的时代');

-- INSERT INTO `users` (`id`, `email`, `phone`, `nick`, `encrypted_password`, `type`, `status`, `tags`, `last_login`, `failed_attempts`, `parent`, `extra`, `token`, `locked_at`, `remember_created_at`, `created_at`, `updated_at`)
-- VALUES
-- 	(1,'aixforcetest@163.com',NULL,'aixforcetest','$shiro1$SHA-256$500000$WXOjvdvfq43cPmxZL5fG+Q==$DfoNhGiIid4/8l2oL3mHIHeZ9W0QVwuISPqXPiohsXQ=',0,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2012-09-13 12:38:38','2012-09-13 12:38:38'),
-- 	(2,'jlchen.cn@gmail.com',NULL,'jlchen.cn','$shiro1$SHA-256$500000$kNgONQY7Jx6ndYkR+3NbLw==$5dWBfv1ADnqVOTeVgARXb2JLg/XGYBNZyZVDZ64B6GY=',0,1,NULL,'2012-09-13 14:22:38',0,NULL,NULL,NULL,'2012-09-13 14:12:49',NULL,'2012-09-13 14:04:33','2012-09-13 14:21:55');



