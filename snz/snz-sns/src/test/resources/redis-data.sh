hmset category:1 id 1 parentId 0 name '纺织' rank 1 type 2
set category:'纺织' 1
zadd category:0:children 1 1

hmset category:2 id 2 parentId 0 name '武器' rank 2 type 2
set category:'武器' 2
zadd category:0:children 2 2


hmset category:3 id 3 parentId 0 name '金属' rank 3 type 2
set category:'金属' 3
zadd category:0:children 3 3

hmset category:9 id 9 parentId 0 name '汽车' rank 4 type 2
set category:'汽车' 9
zadd category:0:children 3 9

hmset category:4 id 4 parentId 1 name '衣料' rank 2 type 2
set category:'衣料' 4
zadd category:1:children 2 4

hmset category:5 id 5 parentId 1 name '毛线' rank 1 type 2
set category:'毛线' 5
zadd category:1:children 1 5

hmset category:6 id 6 parentId 1 name '蚕丝' rank 0 type 2
set category:'蚕丝' 5
zadd category:1:children 0 5

hmset category:7 id 7 parentId 3 name '黄金' rank 0 type 2
set category:'黄金' 7
zadd category:3:children 0 7

hmset category:8 id 8 parentId 3 name '白银' rank 1 type 2
set category:'白银' 8
zadd category:3:children 1 8

hmset category:10 id 10 parentId 9 name '小型车' rank 0 type 2
set category:'小型车' 10
zadd category:9:children 0 10

hmset category:11 id 11 parentId 9 name '中型车' rank 1 type 2
set category:'中型车' 10
zadd category:9:children 1 11

hmset category:12 id 12 parentId 9 name '大型车' rank 2 type 2
set category:'大型车' 12
zadd category:9:children 2 12

set category:count 12



hmset attribute-key:1 id 1 name '布料用途' type 2 valueType 1
set attribute:key:'布料用途' 1

hmset attribute-key:2 id 2 name '布料材质' type 2 valueType 1
set attribute:key:'布料材质' 2

hmset attribute-key:3 id 3 name '布料风格' type 2 valueType 1
set attribute:key:'布料风格' 3

hmset attribute-key:4 id 4 name '纯度' type 2 valueType 1
set attribute:key:'纯度' 4

hmset attribute-key:5 id 5 name '光泽度' type 2 valueType 1
set attribute:key:'光泽度' 5

hmset attribute-key:6 id 6 name '品牌' type 2 valueType 1
set attribute:key:'品牌' 6

hmset attribute-key:7 id 7 name '车系' type 2 valueType 1
set attribute:key:'车系' 7

hmset attribute-key:8 id 8 name '车型' type 2 valueType 1
set attribute:key:'车型' 8

set attribute-key:count 8

hmset attribute-value:1 id 1 data '服装布料'
set attribute:value:'服装布料' 1

hmset attribute-value:2 id 2 data '沙发布料'
set attribute:value:'沙发布料' 2

hmset attribute-value:3 id 3 data '床品布料'
set attribute:value:'床品布料' 3

hmset attribute-value:4 id 4 data '棉布'
set attribute:value:'棉布' 4

hmset attribute-value:5 id 5 data '绸布'
set attribute:value:'绸布' 5

hmset attribute-value:6 id 6 data '麻布'
set attribute:value:'麻布' 6

hmset attribute-value:7 id 7 data '皮革'
set attribute:value:'皮革' 7

hmset attribute-value:8 id 8 data '日韩风格'
set attribute:value:'日韩风格' 8

hmset attribute-value:9 id 9 data '欧美风格'
set attribute:value:'欧美风格' 9

hmset attribute-value:10 id 10 data '田园风格'
set attribute:value:'田园风格' 10

hmset attribute-value:11 id 11 data '千足金'
set attribute:value:'千足金' 11

hmset attribute-value:12 id 12 data '铂金'
set attribute:value:'铂金' 12

hmset attribute-value:13 id 13 data '24K金'
set attribute:value:'24K金' 13


hmset attribute-value:14 id 14 data '无杂质'
set attribute:value:'无杂质' 14

hmset attribute-value:15 id 15 data '有杂质'
set attribute:value:'有杂质' 15

hmset attribute-value:16 id 16 data '完美品'
set attribute:value:'完美品' 16

set attribute-value:count 16

sadd category:4:keys 1 2 3
sadd category:7:keys 4 5

sadd category:4:key:1:values 1 2 3
sadd category:4:key:2:values 4 5 6
sadd category:4:key:3:values 8 9 10

sadd category:7:key:4:values 11 12 13
sadd category:7:key:5:values 14 15 16

hmset spu:1 id 1 categoryId 4 name '屌丝衣料' status 1
hmset spu:2 id 2 categoryId 4 name '高富帅衣料' status 1
hmset spu:3 id 3 categoryId 7 name '俄罗斯黄金' status 1
hmset spu:4 id 4 categoryId 7 name '中国黄金' status 1

set spu:count 4

hmset spu-attribute:1 id 1 spuId 1 attributeKeyId 1 attributeValueId 1
hmset spu-attribute:2 id 2 spuId 1 attributeKeyId 2 attributeValueId 6
hmset spu-attribute:3 id 3 spuId 2 attributeKeyId 1 attributeValueId 1
hmset spu-attribute:4 id 4 spuId 2 attributeKeyId 2 attributeValueId 7
hmset spu-attribute:5 id 5 spuId 3 attributeKeyId 4 attributeValueId 13
hmset spu-attribute:6 id 6 spuId 3 attributeKeyId 5 attributeValueId 16
hmset spu-attribute:7 id 7 spuId 4 attributeKeyId 4 attributeValueId 12
hmset spu-attribute:8 id 8 spuId 4 attributeKeyId 5 attributeValueId 15

set spu-attribute:count 8

sadd spu:1:attributes 1 2
sadd spu:2:attributes 3 4
sadd spu:3:attributes 5 6
sadd spu:4:attributes 7 8


hmset item:1 id 1 spuId 1 userId 11 siteId 11 name '屌丝必备衣料1亚麻棉毛绒' mainImage 'http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 10 soldQuantity 6 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:2 id 2 spuId 1 userId 11 siteId 11 name '屌丝必备衣料2' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 14 soldQuantity 8 price 15000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:3 id 3 spuId 1 userId 11 siteId 11 name '屌丝必备衣料3' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg' tradeType 1 status -1 onShelfAt 1355129365295 quantity 110 soldQuantity 22 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:4 id 4 spuId 2 userId 11 siteId 11 name '高富帅必备衣料1亚麻' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 103 soldQuantity 12 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:5 id 5 spuId 1 userId 11 siteId 11 name '低端屌丝-高仿阿玛尼绒布' mainImage 'http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 12 soldQuantity 12 price 40300 createdAt 1355129365295 updatedAt 1355129365295
hmset item:6 id 6 spuId 2 userId 11 siteId 11 name '屌丝中的战斗机' mainImage 'http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 105 soldQuantity 60 price 58000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:7 id 7 spuId 2 userId 11 siteId 11 name '耐克跑步鞋' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 18 soldQuantity 6 price 35000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:8 id 8 spuId 2 userId 11 siteId 11 name '端点科技fighting' mainImage 'http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 20 soldQuantity 2 price 55000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:9 id 9 spuId 1 userId 11 siteId 11 name '最牛逼的建站系统' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 10 soldQuantity 3 price 12300 createdAt 1355129365295 updatedAt 1355129365295
hmset item:10 id 10 spuId 1 userId 11 siteId 11 name '我的世界' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg' tradeType 1 status -1 onShelfAt 1355129365295 quantity 14 soldQuantity 0 price 78900 createdAt 1355129365295 updatedAt 1355129365295
hmset item:11 id 11 spuId 2 userId 11 siteId 11 name '青蓝计划' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 22 soldQuantity 13 price 100000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:12 id 12 spuId 2 userId 11 siteId 11 name '2012世界末日' mainImage 'http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 222 soldQuantity 103 price 990000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:13 id 13 spuId 2 userId 11 siteId 11 name '少年pi的奇幻漂流' mainImage 'http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 33 soldQuantity 3 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:14 id 14 spuId 2 userId 11 siteId 11 name '女王范儿' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 40 soldQuantity 4 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:15 id 15 spuId 2 userId 11 siteId 11 name '乔丹运动鞋' mainImage 'http://img08.taobaocdn.com/bao/uploaded/i8/T1P8VAXjFjXXbaB17._112142.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 40 soldQuantity 4 price 1000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:16 id 16 spuId 1 userId 11 siteId 11 name '科比81分' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1vIDsXjNiXXblxmQ__110505.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 33 soldQuantity 12 price 13300 createdAt 1355129365295 updatedAt 1355129365295
hmset item:17 id 17 spuId 1 userId 11 siteId 11 name '马布里政委' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T16Y6FXlRoXXagRlA9_074408.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 55 soldQuantity 54 price 12300 createdAt 1355129365295 updatedAt 1355129365295
hmset item:18 id 18 spuId 1 userId 11 siteId 11 name '姚明退役了' mainImage 'http://img01.taobaocdn.com/bao/uploaded/i1/T1TpTkXeNiXXbKJYw__105450.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 11 soldQuantity 11 price 29900 createdAt 1355129365295 updatedAt 1355129365295
hmset item:19 id 19 spuId 1 userId 11 siteId 11 name '马拉多纳最牛' mainImage 'http://img07.taobaocdn.com/bao/uploaded/i7/T1zbJDXhtxXXc198g2_044809.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 1000 soldQuantity 500 price 1000000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:20 id 20 spuId 2 userId 11 siteId 11 name '梅西的新进球记录' mainImage 'http://img03.taobaocdn.com/imgextra/i3/174251014/T2QOehXftbXXXXXXXX_!!174251014.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 100 soldQuantity 90 price 23000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:21 id 21 spuId 2 userId 11 siteId 11 name '永远的罗纳尔多' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T16_PIXgRpXXcXJyA3_050405.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 2014 soldQuantity 2012 price 91200 createdAt 1355129365295 updatedAt 1355129365295

hmset item:22 id 22 spuId 3 userId 11 siteId 11 name '俄罗斯天然黄金高富帅' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1R9niXotkXXXGglZ4_054051.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 100 soldQuantity 57 price 9120000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:23 id 23 spuId 3 userId 11 siteId 11 name '买黄金送钻石衣料' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 43 soldQuantity 41 price 9120000 createdAt 1355129365295 updatedAt 1355129365295
hmset item:24 id 24 spuId 3 userId 11 siteId 11 name '屌丝黄金' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 49 soldQuantity 12 price 2300 createdAt 1355129365295 updatedAt 1355129365295
hmset item:25 id 25 spuId 4 userId 11 siteId 11 name '中国劣质屌丝黄金' mainImage 'http://img03.taobaocdn.com/bao/uploaded/i3/T1bKjzXaFnXXbaqNZ8_100707.jpg_220x220.jpg' tradeType 1 status 1 onShelfAt 1355129365295 quantity 1000 soldQuantity 120 price 2300 createdAt 1355129365295 updatedAt 1355129365295

zadd item:all -1 1 -2 2 -3 3 -4 4 -5 5 -6 6 -7 7 -8 8 -9 9 -10 10 -11 11 -12 12 -13 13 -14 14 -15 15 -16 16 -17 17 -18 18 -19 19 -20 20 -21 21 -22 22 -23 23 -24 24 -25 25
set item:count 25

