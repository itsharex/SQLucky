package net.tenie.plugin.DB2Connector.impl;

import java.util.HashMap;
import java.util.Map;

public class Db2ErrorCode {
	
	static private Map<String, String> code = new HashMap<>();
	
	static public String translateErrMsg(String msg) {
		String str = "";
		String code = "";
		if( msg.contains( "SQLCODE")) {
		   code = msg.substring(msg.indexOf("SQLCODE=")+8, msg.indexOf(",")); 
		str = ErrorCodeMsg(code);
		}
		return code + " " + str;
	}
	static public String ErrorCodeMsg(String key) {
		return code.get(key);
	}
	
	static {
		code.put("+012",	 "01545 未限定的列名被解释为一个有相互关系的引用");
		code.put("+098",	 "01568 动态SQL语句用分号结束");
		
		code.put("+12",	 "01545 未限定的列名被解释为一个有相互关系的引用");
		code.put("+98",	 "01568 动态SQL语句用分号结束");
		
		code.put("+100",	 "02000 没有找到满足SQL语句的行");
		code.put("+110",	 "01561 用DATA CAPTURE定义的表的更新操作不能发送到原来的子系统");
		code.put("+111",	 "01590 为2型索引设置了SUBPAGES语句");
		code.put("+117",	 "01525 要插入的值的个数不等于被插入表的列数");
		code.put("+162",	 "01514 指定的表空间被置为检查挂起状态");
		code.put("+203",	 "01552 使用非唯一的名字来解决命名的限定列");
		code.put("+204",	 "01532 命名的对象未在DB2中定义");
		code.put("+206",	 "01533 命名的列不在SQL语句中指定的任何表中存在");
		code.put("+218",	 "01537 因为SQL语句引用一个远程对象，不能为该SQL语句执行EXPLAIN");
		code.put("+219",	 "01532 命名的PLAN TABLE不存在");
		code.put("+220",	 "01546 不正确定义PLAN TABLE，检查命名列的定义");
		code.put("+236",	 "01005 SQLDA中的SQLN的值至少应于所描述的列的个数一样大");
		code.put("+237",	 "01594 至少有一个被描述的列应该是单值类型，因此扩展的SQLVAR条目需要另外的空间");
		code.put("+238",	 "01005 至少应有一个被描述的列是一个LOB，因此扩展的SQLVAR条目需要另外的空间");
		code.put("+239",	 "01005 至少应有一个被描述的列应是单值类型，因此扩展的SQLVAR条目需要另外的空间");
		code.put("+304",	 "01515 该值不能被分配给宿主变量，因为该值不再数据类型的范围之内");
		code.put("+331",	 "01520 不能被翻译的字符串，因此被设置为NULL");
		code.put("+339",	 "01569 由于与DB2 2.2版本的子系统连接，所以可能存在字符转换问题");
		code.put("+394",	 "01629 使用优化提示来选择访问路径");
		code.put("+395",	 "01628 设置了无效的优化提示，原因代码指定了为什么，忽略优化提示");
		code.put("+402",	 "01521 未知的位置");
		code.put("+403",	 "01522 本地不存在CREAT ALIAS对象");
		code.put("+434",	 "01608 在DB2未来发布的版本中将不支持指定的特性，IBM建议你停止使用这些特性");
		code.put("+445",	 "01004 值被CAST函数截取");
		code.put("+462",	 "01Hxx 由用户定义的函数或存储过程发出的警告");
		code.put("+464",	 "01609 命名的存储过程超出了它可能返回的查询结果集的个数限制");
		code.put("+466",	 "01610 指定由命名的存储过程返回的查询结果集的个数。成功完成");
		code.put("+494",	 "01614 由存储过程返回的结果集的个数超过了由ASSOCIATE LOCATORS语句指定的结果集定位器的个数");
		code.put("+495",	 "01616 因为倒台SQL的成本估算超出了在ELST中指定的警告阀值，所以发出警告");
		code.put("+535",	 "01591 请求一个主健的定位更新，或请求一个使用自我引出 约束的表的删除操作");
		code.put("+541",	 "01543 命名外健是一个重复的引用约束");
		code.put("+551",	 "01548 命名的授权ID缺少在命名的DB2对象上执行命名操作的权限");
		code.put("+552",	 "01542 命名的授权ID缺少执行命名操作的权限");
		code.put("+558",	 "01516 已经被授权该PUBLIC，因此WITH GRANT OPTION不可用");
		code.put("+561",	 "01523 对ALTER REFERENCES INDEX 和TRIGGER特权，PUBLIC AT ALL LOCATION无效");
		code.put("+562",	 "01560 因为GRANTEE已经拥有这些特权，所以一个或更多的特权被忽略");
		code.put("+585",	 "01625 模式名指定了不止一次");
		code.put("+599",	 "01596 没有为长字符数据类型（BLOB，CLOB和DBCLOB）建立比较函数");
		code.put("+610",	 "01566 由于建立了一个指定为DEFER YES的索引，指定的对象处于PENDING状态，或者因为使用了ALTER INDEX改变关键值的范围，所以指定的对象处于PENDING状态");
		code.put("+625",	 "01518 因为删除了主健索引，所以表定义被标注为不完整");
		code.put("+626",	 "01529 删除了加强UNIQUE约束的索引，唯一性不在被加强");
		code.put("+645",	 "01528 因为建立的索引中没有包含NULL，所以WHERE NOT NULL被忽略");
		code.put("+650",	 "01538 不能更改或者建立已命名的表为从属表");
		code.put("+653",	 "01551 在已指定的分区表空间中尚没有建立指定的分区索引，所以分区索引不可得");
		code.put("+655",	 "01597 为CREATE或ALTER STOGROUP语句指定特定或者非特定的卷ID，在DB2较新发布的版本中（版本6以后）将不再支持他们");
		code.put("+658",	 "01600 当建立目录索引时，不能指定SUBPAGES语句，SUBPAGES将被忽略，并缺省为1");
		code.put("+664",	 "01540 分区索引的限制关键字超出了最大值");
		code.put("+738",	 "01530 已命名的对象的更改可能像只读系统中对象的改变要求一样");
		code.put("+799",	 "0157 SET语句中引用的特定寄存器不存在，将忽略 SET请求");
		code.put("+802",	 "01519 数据溢出或者因除法异常而引起的数据异常错误");
		code.put("+806",	 "01553 ISOLATION（RR）与LOCKSIZE PAGE 冲突");
		code.put("+807",	 "01554 由于十进制乘法导致溢出");
		code.put("+863",	 "01539 连接成功，但是只支持SBCS");
		code.put("+2000",	 "56094 SUBPAGES不等于1的1型索引不能成为数据共享环境中的缓冲池组依赖者");
		code.put("+2002",	 "01624 因为指定的缓冲池不允许超高速缓存，GNPCACHE指定被忽略");
		code.put("+2007",	 "01602 因为DB2子系统的参数禁用“提示(hiats）”所以不能指定优化提示");
		code.put("+30100",	 "01558 分布式协议错误被检测到，提供原来的SQLCODE和SQLSTATE");
		code.put("-007",	 "42601 SQL语句中由非法字符");
		code.put("-010",	 "42603 字符串常量非正常终止；检查到有遗漏的引号标志");
		code.put("-029",	 "42601 需要INTO语句");
		code.put("-060",	 "42815 某特定数据类型的长度或者标量规范无效");
		code.put("-084",	 "42612 不能执行SQL语句，因为该语句对动态SQL无效或者对OS/390的DB2无效");
		code.put("-097",	 "42601 在单位类型、用户自定义的函数以及过程中不能使用带有CAST的LONG VARCHAR或LONGVARGRAPHIC");
		
		code.put("-7",	 "42601 SQL语句中由非法字符");
		code.put("-10",	 "42603 字符串常量非正常终止；检查到有遗漏的引号标志");
		code.put("-29",	 "42601 需要INTO语句");
		code.put("-60",	 "42815 某特定数据类型的长度或者标量规范无效");
		code.put("-84",	 "42612 不能执行SQL语句，因为该语句对动态SQL无效或者对OS/390的DB2无效");
		code.put("-97",	 "42601 在单位类型、用户自定义的函数以及过程中不能使用带有CAST的LONG VARCHAR或LONGVARGRAPHIC");
		
		code.put("-101",	 "54001 SQL语句超出了已确定的DB2限制：例如，表的数目太多，语句中的字节太多");
		code.put("-102",	 "54002 字符串常量太长");
		code.put("-103",	 "42604 无效数学文字");
		code.put("-104",	 "42601 SQL语句中遇到非法符号");
		code.put("-105",	 "42604 无效的字符串格式；通常引用一个格式不正确的图形字符串");
		code.put("-107",	 "42622 对象名太长");
		code.put("-108",	 "42601 RENAME语句中指定的名字有错误，不能使用限定词");
		code.put("-109",	 "42601 指定了无效语句；例如CREATE VIEW不能包含ORDER BY 语句");
		code.put("-110",	 "42606 遇到了无效的十六进制的文字");
		code.put("-111",	 "42901 指定的列函数没有给出列名");
		code.put("-112",	 "42607 无效的列函数语法；列函数不能运行与其他的列函数之上");
		code.put("-113",	 "42602 遇到无效字符");
		code.put("-114",	 "42961 该语句的位置名称必须与当前服务器匹配，但是却没有匹配");
		code.put("-115",	 "42601 因为比较运算符没有伴着一个表达式或者列表，遇到了无效谓词");
		code.put("-117",	 "42802 待插入的数值的个数于被插入的行中的列数不相等");
		code.put("-118",	 "42902 数据修改语句(UPDATE或DELETE）和FROM语句中的表和视图命名不合法");
		code.put("-119",	 "42803 HAVING语句中的列的列表与GROUP BY语句中的列列表不匹配");
		code.put("-120",	 "42903 不允许WHERE语句、SET语句、VALUES语句或者SET ASSIGNMENT语句引用列函数");
		code.put("-121",	 "42701 在INSERT或UPDATE语句中，某一列被非法引用了两次");
		code.put("-122",	 "42803 非法使用了列函数。因为没有用于一个列函数的所有列不再GROUP BY语句中");
		code.put("-123",	 "42601 特定位置的参数必须是一个常数或者一个关键词");
		code.put("-125",	 "42805 ORDER BY语句中指定了无效数字，该数字要么小于1要么大于选定的列数");
		code.put("-126",	 "42829 不能为一个UPDATE语句指定ORDER BY语句");
		code.put("-127",	 "42905 在子选择中DISTINCT只能指定一次");
		code.put("-128",	 "42601 SQL谓词中NULL使用不当");
		code.put("-129",	 "54004 SQL语句中包含的表多于15个");
		code.put("-130",	 "22019 ESCAPE语句必须为一个字符");
		code.put("22025",	 "无效的ESCAPE模式");
		code.put("-131",	 "42818 LIKE谓词只能用于字符数据");
		code.put("-132",	 "42824 LIKE语句、ESCAPE语句、LOCATE函数或POSSTR函数中有无效运算对象");
		code.put("-133",	 "42906 无效相关子查询引用");
		code.put("-134",	 "42907 大于255字节的列被不正确使用");
		code.put("-136",	 "54005 排序关键字的长度大于4000字节");
		code.put("-137",	 "54006 被连接的字符串太大；字符的最大值为32767；图形的最大值为16382");
		code.put("-138",	 "22011 SUBSTR列函数的第二个或第三个操作符无效");
		code.put("-142",	 "42612 不支持的SQL语句。该语句可能在另外的RDBMS上有效，也有可能在其他的上下文中有效（例如，VALUES只能在触发器中出现）");
		code.put("-144",	 "58003 指定的段号无效");
		code.put("-147",	 "42809 某一源函数不能更改。要改变源函数，必须删除该源函数并重新建立他");
		code.put("-148",	 "42809 RENAME和ALTER无法执行。RENAME不能对视图或者活动RI.ST表重新命名。ALTER不能用于改变列的长度，因为该列参与了RI、一个用户退出程序、全局的临时表或打开DATACAPTURE CHANGES表的列");
		code.put("-150",	 "42807 触发活动的INSERT，UPDATE或DELETE语句中指定了无效的视图更新或一个无效的转换表");
		code.put("-151",	 "42808 试图更新一个不可更新的视图的列、一个DB2 CATALOG表的列或者一个ROWID列");
		code.put("-152",	 "42809 DROP CHECK试图删除一个参照约束，或者DROP FOREIGN试图删除一个检查约束");
		code.put("-153",	 "42908 无效的视图建立请求，必须为旋转列表中列出的列出的未命名的列或者重复的列提供一个名字");
		code.put("-154",	 "42909 不能用UNION、UNION ALL或者一个远程表建立视图");
		code.put("-156",	 "42809 在视图上建立索引是非法的，或者在ALTER TABLE，CREATE TRIGGER，DROP TABLE或LOCK TABLE语句上指定一个不是表的其他对象这是无效的");
		code.put("-157",	 "42810 必须在FOREIGN KEY语句中指定一个表名");
		code.put("-158",	 "42811 视图的列和选择列表中的列不相匹配");
		code.put("-159",	 "42089 无效DROP或COMMENT ON语句");
		code.put("-160",	 "42813 对该视图的WITH CHECK OPTION无效");
		code.put("-161",	 "44000 正被更新的视图WITH CHECK OPTION语句使得这行不能被插入或更新");
		code.put("-164",	 "42502 用户没有建立这个视图的权限");
		code.put("-170",	 "42605 标量函数指定了无效的参数个数");
		code.put("-171",	 "42815 标量函数指定了无效的数据类型长度或者无效数值");
		code.put("-173",	 "42801 在非只读型的游标上不能指定隔离级别UR");
		code.put("-180",	 "22007 DATE、TIME、TIMESTAMP值的字符串表示法的语法不对");
		code.put("-181",	 "22001 不是有效的DATE、TIME、TIMESTAMP值");
		code.put("-182",	 "42816 在算术表达式中的日期/时间值无效");
		code.put("-183",	 "22008 在算术表达式中返回的日期/时间值的结果不在有效值的范围内");
		code.put("-184",	 "42610 没有正确使用日期/时间值的参数标记");
		code.put("-185",	 "57008 没有定义本定的日期/时间出口");
		code.put("-186",	 "22505 改变本定的日期/时间出口引发这个程序的长度无效");
		code.put("-187",	 "22506 MVS返回无效的当前日期/时间");
		code.put("-188",	 "22503 字符串表示无效");
		code.put("-189",	 "22522 指定的编码字符集的ID无效或没有定义");
		code.put("-190",	 "42837 不能象所设定的那样改变（ALTER）列。只能改变（ALTER）VARCHAR列的长度");
		code.put("-191",	 "22504 字符串中包含了无效的混合数据");
		code.put("-197",	 "42877 当两个或多个表被联合在一起排序时，限定的列名不能在ORDER BY语句中使用");
		code.put("-198",	 "42617 试图对空的字符串发布一个PREPARE或EXECUTE IMMEDIATE语句");
		code.put("-199",	 "42601 SQL语句中使用了非法关键词");
		code.put("-203",	 "42702 模糊列引用");
		code.put("-204",	 "42704 没有定义的对象名");
		code.put("-205",	 "42703 指定的表的列名无效");
		code.put("-206",	 "42703 列名没有在FROM语句所引用的任何表中，或者没有在定义触发器所在的表中");
		code.put("-208",	 "42707 不能ORDER BY指定列，应为该列不在选择列表中");
		code.put("-212",	 "42712 指定的表名在触发器中不允许多次使用，只能使用一次");
		code.put("-214",	 "42822 DISTINCT、ORDER BY 引起的无效表达式");
		code.put("-219",	 "42704 因为PLAN_TABLE不存在，EXPLAIN无法执行");
		code.put("-220",	 "55002 遇到无效的PLAN_TABLE列");
		code.put("-221",	 "55002 如果为PLAN_TABLE定义了可供选择的列，那么，必须定义所有的列");
		code.put("-229",	 "42708 指定的现场找不到");
		code.put("-240",	 "428B4 LOCK TABLE语句的PART子句无效");
		code.put("-250",	 "42718 没有定义本地位置名");
		code.put("-251",	 "42602 记号无效");
		code.put("-300",	 "22024 宿主变量或参数中的字符串不是以NULL为终止");
		code.put("-301",	 "42895 无效的宿主变量数据类型");
		code.put("-302",	 "22001 输入的变量值对指定的列无效");
		code.put("22003",	 "输入的变量值对指定的列而言太大");
		code.put("-303",	 "42806 因为数据类型不兼容，不能分配数值");
		code.put("-304",	 "22003 因为数据超出了范围，不能分配数值");
		code.put("-305",	 "22002 没有NULL指示符变量");
		code.put("-309",	 "22512 因为引用的宿主变量被设置成NULL，所以谓词无效");
		code.put("-310",	 "22501 十进制的宿主变量或参数包含非十进制数据");
		code.put("-311",	 "22501 输入的宿主变量长度无效，或者时负值或者太大");
		code.put("-312",	 "42618 没有定义宿主变量或者宿主变量不可用");
		code.put("-313",	 "07001 宿主变量的个数不等于参数标识的个数");
		code.put("-314",	 "42714 模糊的宿主变量引用");
		code.put("-327",	 "22525 在最后分区的关键字范围内，不能插入行");
		code.put("-330",	 "22021 不能成功的翻译字符串");
		code.put("-331",	 "22021 字符串不能分配到宿主变量，因为其不能成功的被翻译");
		code.put("-332",	 "57017 不能为两个命名的编码字符集的ID定义翻译规则");
		code.put("-333",	 "56010 子类型无效导致翻译失败");
		code.put("-338",	 "42972 ON语句无效，必须引用连接的列");
		code.put("-339",	 "56082 访问DB2 2.2版本的子系统被拒绝，原因时ASCII到EBCDIC翻译不能进行");
		code.put("-350",	 "42962 无效的大对象规范");
		code.put("-351",	 "56084 SELECT列表中有不支持的数据类型");
		code.put("-352",	 "56084 输入列表中有不支持的数据类型");
		code.put("-355",	 "42993 LOB列太大，以至不能被记录在日志中");
		code.put("-372",	 "428C1 每个表只允许有一个ROWID列");
		code.put("-390",	 "42887 在上下文中指定的函数无效");
		code.put("-392",	 "42855 自从前一次FETCH以来，指定游标的SQLDA已被不恰当的改变");
		code.put("-396",	 "38505 在最后的访问过程中，视图执行SQL语句");
		code.put("-397",	 "428D3 在某一列上不恰当的指定了GENERATED因为该列不是ROWID数据类型");
		code.put("-398",	 "428D2 为某一个宿主变量请求LOCATOR，但是该宿主变量不是一个LOB");
		code.put("-399",	 "22511 在INSERT语句中为ROWID列指定的值无效");
		code.put("-400",	 "54027 在DB2编目中定义的用户自定义索引不能超过100个");
		code.put("-401",	 "42818 算术操作符或比较操作符的操作对象不是兼容的");
		code.put("-402",	 "42819 算术函数不能用于字符或日期时间数据");
		code.put("-404",	 "22001 SQL语句指定的字符串太长");
		code.put("-405",	 "42820 数值文字超出了范围");
		code.put("-406",	 "22003 计算出的或者倒出的数值超出了范围");
		code.put("-407",	 "23502 不能把NULL值插到定义为NOT NULL的列中");
		code.put("-408",	 "42821 数值不能被更新或插入，因为他与列的数据类型不兼容");
		code.put("-409",	 "42607 COUNT函数指定的运算对象无效");
		code.put("-410",	 "42820 浮点文字笔30个字符的最大允许长度长");
		code.put("-411",	 "56040 CURRENT SQLID使用无效");
		code.put("-412",	 "42823 在子查询的选择列表中遇到了多个列");
		code.put("-413",	 "22003 当转换为一个数字型数据类型时，数据溢出");
		code.put("-414",	 "42824 LIKE谓词不能运行于用数字或日期时间类型定义的列");
		code.put("-415",	 "42825 为UNION操作指定的选择列表不是联合兼容的");
		code.put("-416",	 "42907 包含UNION操作符的SQL语句不允许有长的字符串列");
		code.put("-417",	 "42609 两参数标识符作为运算对象被指定在同一谓词的两边");
		code.put("-418",	 "42610 参数标识符使用无效");
		code.put("-419",	 "42911 十进制除法无效");
		code.put("-420",	 "22018 字符串自变量值不符合函数的要求");
		code.put("-421",	 "42826 UNION操作的选择列表中没有提供相同数目的列");
		code.put("-423",	 "0F001 为LOB或结果集定位器指定的值无效");
		code.put("-426",	 "2D528 在不允许更新的应用服务器不允许执行COMMIT语句");
		code.put("-427",	 "2D529 在不允许更新的应用服务器不允许执行ROLLBACK语句");
		code.put("-430",	 "38503 在用户自定义的函数或存储过程中遇到了错误");
		code.put("-433",	 "22001 指定的值太长");
		code.put("-435",	 "428B3 无效的应用定义的SQLSTATE");
		code.put("-438",	 "xxxxx 使用了RAISE_ERROR函数的应用发出了一个错误");
		code.put("-440",	 "42884 存储过程或用户自定义函数的参数列表参数个数于预期的个数不匹配");
		code.put("-441",	 "42601 与标量函数一起使用DISTINCT或ALL是不正确的用法");
		code.put("-443",	 "42601 指定的外部函数返回错误的SQLSTATE");
		code.put("-444",	 "42724 与被称为存储过程或用户自定义函数有关的程序不能找到");
		code.put("-449",	 "42878 对存储过程或用户自定义的 函数，CREATE或ALTER语句不正确（缺失EXTERNAL NAME 子句)");
		code.put("-450",	 "39501 存储过程或用户自定义函数写入存储器的值超过了参数声明的长度");
		code.put("-451",	 "42815 CREATE FUNCTION中指定了不正确的数据类型");
		code.put("-453",	 "42880 用户自定义函数中的RETURNS语句无效");
		code.put("-454",	 "42723 指定的函数识别标记与已存在的另一函数的识别标记冲突");
		code.put("-455",	 "42882 模式名不比配");
		code.put("-456",	 "42710 为用户自定义函数指定的函数名已经存在");
		code.put("-457",	 "42939 用户自定义函数或用户自定义类型正试图使用系统中定义的函数或者类型所用的名称");
		code.put("-458",	 "42883 没有找到函数");
		code.put("-463",	 "39001 特定的外部例程返回无效的SQLSTATE");
		code.put("-469",	 "42886 参数定义为OUT或INOUT的CALL语句必须提供宿主变量");
		code.put("-470",	 "39002 指定了NULL参数，但是该例程却不支持NULL");
		code.put("-471",	 "55023 存储过程或用户自定义函数失败：提供原因代码");
		code.put("-472",	 "24517 外部的函数程序使游标处于打开状态");
		code.put("-473",	 "42918 用户自定义数据类型命名不能和系统定义的数据类型一样");
		code.put("-475",	 "42866 结果类型不能被转换成RETURNS类型");
		code.put("-476",	 "42725 在其模式中该函数不是独一无二的");
		code.put("-478",	 "42893 不能DROP或REVOKE特定的对象，因为其他对象依赖于该对象");
		code.put("-480",	 "51030 直到存储过程已经被CALL后，DESCRIBE PROCEDURE和ASSOCIATE LOCATORS才能被发布");
		code.put("-482",	 "51030 存储过程不返回到任何一个定位器");
		code.put("-483",	 "42885 CREATE FUNCTION语句中的参数个数与源函数中的参数个数不匹配");
		code.put("-487",	 "38001 选择了NO SQL选项建立指定的存储过程或用户自定义函数，但却视图发布SQL语句");
		code.put("-491",	 "42601 CREATE FUNCTION语句无效，因为该语句没有RETURNS语句或者因为该语句没有指定有效的SOURCE或者EXTERNAL语句");
		code.put("-492",	 "42879 指定函数的指定参数的个数有错误");
		code.put("-495",	 "57051 语句的估计处理器成本超出了资源限制");
		code.put("-496",	 "51033 语句无法执行，因为当前服务器与调用存储过程的服务器不同");
		code.put("-497",	 "54041 指定的数据库超过了32767 OBID的上限，或者CREATE DATABASE语句使之达到了32511DBID的上限");
		code.put("-499",	 "24516 指定的游标已被分配到结果集，该结果集来自已经指定的存储过程");
		code.put("-500",	 "24501 因为连接被破坏，WITH HOLD游标被关闭");
		code.put("-501",	 "24501 在试图获取数据或关闭一个游标前必须打开一个游标");
		code.put("-502",	 "24502 在没有关闭游标前不能再次打开游标");
		code.put("-503",	 "42912 因为列在游标的FOR UPDATE OF语句中没有被指定，该游标用于获取该列，所以不能更新该列");
		code.put("-504",	 "34000 不能引用一个游标，因为他不是定义到程序里的");
		code.put("-507",	 "24501 在试图更新或者删除WHERE CURRENT OF前，必须打开游标");
		code.put("-508",	 "24504 因为被引用的游标当前不是处于数据行上，所以不能被更新或删除");
		code.put("-509",	 "42827 除了在游标上指定的那个表（该表由WHERE CURRENT OF语句引用的）以外，再也不能从别的表上更新数据");
		code.put("-510",	 "42828 表或视图不能被修改");
		code.put("-511",	 "42829 对不可修改的表或视图，FOR UPDATE OF语句无效");
		code.put("-512",	 "56023 对远程对象的无效引用");
		code.put("-513",	 "42924 一个别名不能再被定义成另外的别名");
		code.put("-514",	 "26501 游标尚没有被准备");
		code.put("-516",	 "26501 试图描述未准备好的SQL语句");
		code.put("-517",	 "07005 因为SQL语句尚没有准备好，游标无效");
		code.put("-518",	 "07003 试图执行尚没有准备好的SQL语句");
		code.put("-519",	 "24506 当为游标的SQL语句发布一个准备语句是，游标不能是打开的");
		code.put("-525",	 "51015 不能在已指定的程序包中执行SQL语句，因为在绑定时间内该程序包无效");
		code.put("-526",	 "42995 在给定的上下文中，不能使用全局的临时表");
		code.put("-530",	 "23503 对特定的约束名指定了无效的外健值");
		code.put("-531",	 "23504 从版本5开始，父关键字的多行更新将试图删除一个外关键字依赖的父关键字值，在版本5以前，当引用主关键值外健值当前存在时，试图更新该主健值");
		code.put("-532",	 "23504 删除操作违反了已指定的参照约束");
		code.put("-533",	 "21501 多行插入无效，试图将多行插到自我引用的表中");
		code.put("-534",	 "21502 可改变主健列值的更新语句不能在同一时刻用于更新多行");
		code.put("-535",	 "21502 当从自我引用表中删除数据或者更新主健列时，不能指定WHERE CURRENT OF。不是版本5的子系统才调用该代码");
		code.put("-536",	 "42914 因为某一特定表的参照约束存在，所以删除语句无效");
		code.put("-537",	 "42709 在外健语句或主健语句的规范中，每个列的出现不能多于一次");
		code.put("-538",	 "42830 无效的外健；不符合引用的表没有主健");
		code.put("-539",	 "42888 不能定义外健，因为被引用的表没有主健");
		code.put("-540",	 "57001 表定义不完整，直到为主健建立了唯一索引或UNIQUE语句、或者包含GENERATED BYDEFAULT属性的ROWID列");
		code.put("-542",	 "42831 可以为空的列不允许作为主健的一部分包含在内");
		code.put("-543",	 "23511 因为该表是指定了SET NULL删除规则的参照约束的父表而且检查约束不允许NULL，所以DELETE不能发生");
		code.put("-544",	 "23512 不能用ALTER添加检查约束，因为已存在的某行与该检查约束冲突");
		code.put("-545",	 "23513 INSERT或者UPDATE导致检查约束冲突");
		code.put("-546",	 "42621 在CREATE或ALTER TABLE中指定的检查约束无效");
		code.put("-548",	 "42621 因为指定的列而引起的检查约束无效");
		code.put("-549",	 "42509 DYNAMICRULES（BIND）计划或程序包的无效SQL语句");
		code.put("-551",	 "42501 用户试图对不拥有权限的特定的对象进行操作，或者表不存在");
		code.put("-552",	 "42502 用户试图执行未被授权的操作");
		code.put("-553",	 "42503 不能指定CURRENT SQLID，因为用户尚没有被允许改变那个ID");
		code.put("-554",	 "42502 不能对你本身赋予一个权限");
		code.put("-555",	 "42502 不能对你本身撤销一个权限");
		code.put("-556",	 "42504 不能撤销用户没有拥有的权限");
		code.put("-557",	 "42852 指定了不一致的授予或撤销关键词");
		code.put("-558",	 "56025 为授予或撤销语句指定了无效的语句（一个或一组）");
		code.put("-559",	 "57002 DB2权限机制已经禁用，授予或者撤销不能被发布");
		code.put("-567",	 "42501 指定的权限ID缺少对指定的程序包的绑定权限");
		code.put("-571",	 "25000 不允许多点更新");
		code.put("-573",	 "42890 不能定义参照约束，因为已指定的父表中在指定的列上没有唯一健");
		code.put("-574",	 "42864 指定的缺省与列定义冲突");
		code.put("-577",	 "38002 试图修改用户自定义函数中的数据或者存储过程中的数据，但这些对象的建立没有选择MODIFIES SQL DATA选项");
		code.put("-579",	 "38004 试图修改用户自定义函数中的数据或者存储过程中的数据，但这些对象的建立没有选择READ SQL DATA选项，也没有选择MODIFIES SQL DATA选项");
		code.put("-580",	 "42625 CASE表达式中的结果表达式不能都是空的");
		code.put("-581",	 "42804 CASE表达式中的结果表达式为不兼容的数据类型");
		code.put("-582",	 "42625 SEARCHED－WHEN－CLAUSE中的查找条件指定了一个限定的、IN或EXISTS谓词");
		code.put("-583",	 "42845 指定的函数失败，因为他不是决定性的，或者可能有外部动作");
		code.put("-585",	 "42732 在当前路径中模式名不止一次出现");
		code.put("-586",	 "42907 CURRENT PATH专用寄存器在长度上不能超过254字符");
		code.put("-587",	 "428C6 项目引用的列表必须是同一个家族");
		code.put("-590",	 "42734 在命名的存储过程或用户自定义的函数中的参数必须是独一无二的");
		code.put("-592",	 "42510 没有授权权限，让你在WLM环境中建立的存储过程或者用户自定义函数");
		code.put("-601",	 "42710 试图创建（或重命名）已经存在的对象");
		code.put("-602",	 "54008 CREATE INDEX语句中指定的列太多");
		code.put("-603",	 "23515 因为发现有重复值，所以不能建立唯一的索引");
		code.put("-604",	 "42611 在CREATE或ALTER TABLE语句中的为数据类型指定的长度、精度以及标度无效");
		code.put("-607",	 "42832 指定的INSERT、UPDATE或DELETE语句不能被发布，应为这些语句对DB2 CATLOG表执行写操作");
		code.put("-611",	 "53088 当LOCKSIZE是TABLE或者TABLESPACE时，LOCKMAX必须为0");
		code.put("-612",	 "42711 在同一个表、索引或试图中不允许有重复列名");
		code.put("-613",	 "54008 主健或UNIQUE约束太长或者包含了太多的列");
		code.put("-614",	 "54008 已经超过了索引的内部健长度的最大长度（255）限制");
		code.put("-615",	 "55006 不能删除这个程序包，因为该程序包目前正在执行");
		code.put("-616",	 "42893 指定的对象不能被删除，因为其他对象依赖于该对象");
		code.put("-617",	 "56089 对于DB2版本6，1型索引无效。对于以前的版本，1型索引不能用LOCKSIZE ROW或LARGE表空间定义");
		code.put("-618",	 "42832 对DB2 CATALOG表的请求操作时不允许的");
		code.put("-619",	 "55011 DSNDB07不能修改，除非他先被停止了");
		code.put("-620",	 "53001 对在DSNDB07中的表空间不允许指定该关键词");
		code.put("-621",	 "58001 遇到了重复的DBID，遇到了系统问题");
		code.put("-622",	 "56031 不能指定FOR MIXED DATA因为没有安装混合数据选项");
		code.put("-623",	 "55012 不能为单一的表定义多个族索引");
		code.put("-624",	 "42889 不能为单一的表定义多个主健");
		code.put("-625",	 "55014 用主健定义的表要求唯一索引");
		code.put("-626",	 "55015 不能发布ALTER语句来改变PRIQTY SECQTY或ERASE，除非先停止了表空间");
		code.put("-627",	 "55016 不能发布ALTER语句来改变PRIQTY SECQTY或ERASE，除非先把表空间定义为使用存储器组的表空间");
		code.put("-628",	 "42613 指定语句时相互排斥的（例如，不能分区一个分段的表空间）");
		code.put("-629",	 "42834 因为该外健不能包含空值，所以SET NULL无效");
		code.put("-630",	 "56089 不能为1型索引指定WHERE NOT NULL");
		code.put("-631",	 "54008 无效的外健；要么是比254个字节长，要么包含的列数多于40");
		code.put("-632",	 "42915 指定的删除规则禁止把这个表定义为已制定表的从属表");
		code.put("-633",	 "42915 无效删除规则；必须使用特定的强制删除规则");
		code.put("-634",	 "42915 在这种情况下，DELETE CASCADE不允许");
		code.put("-635",	 "42915 删除规则不能有差异或者不能为SET NULL");
		code.put("-636",	 "56016 在分区索引健的升序或降序规范中，分区所以必须与该规范一致");
		code.put("-637",	 "42614 遇到重复的关键词");
		code.put("-638",	 "42601 在CREATE TABLE语句中缺少列定义");
		code.put("-639",	 "56027 带有SET NULL的删除规则的外健的可空列不能是分区索引的列");
		code.put("-640",	 "56089 不能为这个表空间指定LOCKSIZE ROW，因为在该表空间中的表上定义了1型索引");
		code.put("-642",	 "54021 唯一约束包含太多的列");
		code.put("-643",	 "54024 检查约束超出了3800个字符的最大长度");
		code.put("-644",	 "42615 在SQL语句中为关键词指定的值无效");
		code.put("-646",	 "55017 在指定的分区表空间或者缺省表空间中不能创建表，因为指定的表空间已经包含了一个表");
		code.put("-647",	 "57003 指定的缓冲池无效，因为他没有被激活");
		code.put("-650",	 "56090 ALTER INDEX不能被执行；提供了原因代码");
		code.put("-651",	 "54025 如果CREARE或ALTER TABLE被允许，表对象的描述词（object descriptor,OBD）将超过最大值（32KB）");
		code.put("-652",	 "23506 遇到了EDITRPROC或VALIDPROC冲突");
		code.put("-653",	 "57004 在分区表空间中的表不可用，因为分区索引尚未被创建");
		code.put("-655",	 "56036 在卷的列表中，STOGROUP不能指定为特定的或不特定（“*”）的卷");
		code.put("-658",	 "42917 当试图删除指定的对象时，无法删除该对象，该对象的删除必须通过删除与之相关联的对象完成");
		code.put("-660",	 "53035 不正确的分区索引规范，必须为族索引定义有限制的关键字");
		code.put("-661",	 "53036 分区索引没有指定恰当的分区数目");
		code.put("-662",	 "53037 试图在未分区的表空间（分段的或简单的）上建立分区索引");
		code.put("-663",	 "53038 为分区索引指定的关键字限制值是一个无效数字");
		code.put("-665",	 "53039 为ALTER TABLESOACE语句指定了无效的PART语句");
		code.put("-666",	 "57005 SQL语句不能被处理，因为指定的函数当前正处于进行过程中");
		code.put("-667",	 "42917 不能明确的删除分区表空间的族索引，必须除去分区表空间来去掉分区索引");
		code.put("-668",	 "56018 不能向用EDITPROC定义的表中添加列");
		code.put("-669",	 "42917 不能显式的删除分区表空间中的表，必须删除分区表空间来删除表");
		code.put("-670",	 "54010 表的记录长度超过了页面的大小");
		code.put("-671",	 "53040 不能更改指定的表空间的缓冲池，因为这将改变表空间的页面大小");
		code.put("-672",	 "55035 在命名的表上不允许DROP");
		code.put("-676",	 "53041 只有4KB的缓冲池可被用于一个索引");
		code.put("-677",	 "57011 缓冲池扩展失败，由于可用的虚拟内存的大小不足");
		code.put("-678",	 "53045 为才分区索引中指定的限制健提供的值与数据类型不符");
		code.put("-679",	 "57006 不能创建某一个特定对象，因为该对象的一个drop目前正在挂起");
		code.put("-680",	 "54011 对DB2表不能超过750列");
		code.put("-681",	 "23507 列违反了指定的FIELDPROC");
		code.put("-682",	 "57010 不能载入FIELDPROC");
		code.put("-683",	 "42842 列、单值类型、函数或者过程无效，因为不兼容语句。例如，指定的INTEGER具有FORBITDATA选项");
		code.put("-684",	 "54012 指定的文字列表不能超过254个字节");
		code.put("-685",	 "58002 FIELDPROC返回一个无效的域描述");
		code.put("-686",	 "53043 用FIELDPROC定义的一个列不能与一个使用不同的FIELDPROC定义的列作比较");
		code.put("-687",	 "53044 列不能与一个非兼容字段类型的列比较");
		code.put("-688",	 "58002 返回不正确的数据");
		code.put("-689",	 "54011 从属表定义了太多的列");
		code.put("-690",	 "23508 数据定义的控制支持拒绝这个语句");
		code.put("-691",	 "57018 命名的注册表不存在");
		code.put("-692",	 "57018 命名的索引不存在，但命名的注册表需要该索引");
		code.put("-693",	 "55003 命名的注册表/索引的命名列无效");
		code.put("-694",	 "57023 DROP正在命名的注册表上挂起");
		code.put("-696",	 "42898 由于相关的名字或者转换表的名字使用不正确，指定的触发器无效");
		code.put("-697",	 "42899 FOR EACH语句被指定，因此与OLD合NEW相关的名字是不允许的，或者不能为一个BEFORE触发器指定OLD_TABLE和NEW_TABLE");
		code.put("-713",	 "42815 指定的专用寄存器是无效的");
		code.put("-715",	 "56064 命名的程序不能被运行，因为他依赖与你所安装的DB2版本的部件，但是你的数据中心没有安装这个部件");
		code.put("-716",	 "56065 命名的程序使用这个版本的不正确的发行版本做了预编译");
		code.put("-717",	 "56066 BIND失败，因为他依赖与你所安装的DB2版本的部件，但是你的数据中心没有安装这个部件");
		code.put("-718",	 "56067 REBIND失败，因为IBMREQD列无效");
		code.put("-719",	 "42710 不能BIND ADD一个已经存在的程序包");
		code.put("-720",	 "42710 不能BIND REPLACE一个已经存在的程序包版本");
		code.put("-721",	 "42710 程序包的一致性记号必须是独一无二的");
		code.put("-722",	 "42704 绑定错误，因为指定的程序包不存在");
		code.put("-723",	 "09000 一个触发的SQL语句接受到一个错误");
		code.put("-724",	 "54038 达到了（16）级联间接的SQL语句的最大项目");
		code.put("-725",	 "42721 对专门指定的寄存器提供了一个无效值");
		code.put("-726",	 "55030 因为SYSPKSYSTEM条目，不能绑定这个程序包");
		code.put("-728",	 "56080 指定的数据类型不能与私有协议发布一起使用");
		code.put("-729",	 "429B1 用COMMIT ON RETURN定义的存储过程不能用作嵌套的CALL过程的目标");
		code.put("-730",	 "56053 在只读的共享数据库中为表定义的参照完整性无效");
		code.put("-731",	 "56054 VSAM数据集必须用SHAREOPTION（1.3）定义");
		code.put("-732",	 "56055 被定义为只读型数据库却拥有没有定义空间或者索引空间的DB2子系统");
		code.put("-733",	 "56056 只读共享数据库的定义不一致");
		code.put("-734",	 "56057 一旦一个数据库被定义为ROSHARE READ，他将不能被更改为其他不同的ROSHARE状态");
		code.put("-735",	 "55004 用DBID名称标识的数据库不再是一个只读共享数据库");
		code.put("-736",	 "53014 命名的DBID无效");
		code.put("-737",	 "53056 在这种状况下，不能建立一个隐含的表空间");
		code.put("-739",	 "56088 因为同时指定了ALLOW PARALLEL和MODIELES SQL DATA这两个语句，因此已设定的函数将不能再被更改");
		code.put("-740",	 "51034 在这种方式下不能用MODIELES SQL DATA定义指定的函数");
		code.put("-741",	 "55030 已经为命名的共享组成员的数据定义了工作文件数据库");
		code.put("-742",	 "53004 DSNDB07是隐含的工作文件数据库");
		code.put("-746",	 "57053 在特定的触发器、存储过程或函数中的SQL语句违反嵌套SQL限制");
		code.put("-747",	 "57054 指定的表是不可用的除非为LOB数据列建立起必须的辅助表");
		code.put("-748",	 "54042 在指定的辅助表上已经有一个索引");
		code.put("-750",	 "42986 不能对已指定的表重新命名,因为他至少在一个现存的视图或触发器中被引用");
		code.put("-751",	 "42987 存储过程或用户自定义的函数试图执行一个不允许执行的SQL语句。DB2的线程被置于MUST_ROLLBACK状态");
		code.put("-752",	 "0A001 无效CONNECT语句");
		code.put("-763",	 "560A1 无效的表空间名");
		code.put("-764",	 "560A2 LOB表空间必须与其相关的基表空间同在一个数据库中");
		code.put("-765",	 "560A3 表和数据库不兼容");
		code.put("-766",	 "560A4 不能对辅助表进行请求的操作");
		code.put("-767",	 "42626 CREATE INDEX失败，因为在辅助表中为索引指定了列，或者因为没有为非辅助表的索引指定列");
		code.put("-768",	 "560A50 不能为指定的列或者指定的分区建立辅助表，因为其辅助表已经存在");
		code.put("-769",	 "53096 CREATE AUX TABLE的规格与基表不匹配");
		code.put("-770",	 "530A6 指定的表必须有一个ROWID列，那么该表才可以包含一个LOB列");
		code.put("-771",	 "428C7 无效的ROWID列规范");
		code.put("-797",	 "42987 CREATE TRIGGER包含不被支持的语法");
		code.put("-798",	 "428C9 不能把一个值插入到用GENERATED ALWAYS定义的ROWID列");
		code.put("-802",	 "22012 某一特定操作发生了异常错误。被零除");
		code.put("22003",	 "某一特定操作发生了异常错误。但不是被零除");
		code.put("-803",	 "23505 不能插入行，因为这将违反唯一索引的约束");
		code.put("-804",	 "07002 SQLDA的调用参数列表有误");
		code.put("-805",	 "51002 在计划中没有发现DBRM或程序包名");
		code.put("-807",	 "23509 对已指定的环境和连接，该程序包不可用");
		code.put("-808",	 "08001 CONECT语句与程序中的第一个CONNECT语句不一致");
		code.put("-811",	 "21000 当多行作为一内嵌的选择语句的返回结果是，必须使用游标");
		code.put("-812",	 "22508 在CURRENT PACKAGESET中的ID集合是空白的，语句不能被执行");
		code.put("-815",	 "42920 在一个内置选择语句或者一个基本谓词的子查询中，显式的或隐含的指定了GROUP BY或HAVING语句");
		code.put("-817",	 "25000 执行SQL语句将可能导致禁止更新用户数据或DB2编目");
		code.put("-818",	 "5103 计划<－>载入组件的时间戳不匹配，在执行计划中没有从同一个预编译中建立DBRM，该预编译是作为组件载入的");
		code.put("-819",	 "58004 视图不能重建，因为在DB2编目中存储的分析树长度为0");
		code.put("-820",	 "58004 在这个DB2版本的DB2编目中遇到了无效值");
		code.put("-822",	 "51004 在SQLDA中遇到了无效地址");
		code.put("-840",	 "54004 在选择列表或插入列表中返回的项目太多");
		code.put("-842",	 "08002 到指定位置的连接已经存在");
		code.put("-843",	 "08003 SET CONNECTION或RELEASE语句无法执行，因为连接不存在");
		code.put("-870",	 "58026 宿主变量描述符的个数不等于语句中宿主变量的个数");
		code.put("-872",	 "51302 这个子系统已指定了有效的CCSID");
		code.put("-873",	 "53090 同一SQL语句中，不能同时引用EBCDIC表中的定义的列和ASCII表中定义的列");
		code.put("-874",	 "53901 指定对象的编码方案与其表空间的编码方案不匹配");
		code.put("-875",	 "42988 指定的操作符不能用于ASCII数据");
		code.put("-876",	 "53092 不能为指定的原因创建对象：提供了原因代码");
		code.put("-877",	 "53093 数据库或表空间不允许用ASCII，必须使用EBCDIC");
		code.put("-878",	 "53094 该PLAN——TABLE不能是ASCII，必须使用EBCDIC");
		code.put("-879",	 "53095 指定对象的CREATE或ALTER语句不能将列、单值类型，某个存储过程或用户自定义函数的参数定义为以下类型：MAXED DATA，GRAPHIC，VARGRAPHIC，LONGVARGRAPHIC，因为系统没有为指定的编码方案定义相应的CCSID");
		code.put("-900",	 "08003 应用处理没有连接到应用服务器，语句不能被执行");
		code.put("-901",	 "58004 遇到时断时续的系统错误，该错误不能抑制后继的SQL语句的执行");
		code.put("-902",	 "58005 内部控制块的指针错误，要求重新绑定");
		code.put("-904",	 "57011 指定的资源不可用");
		code.put("-905",	 "57014 超出了资源上限");
		code.put("-906",	 "51005 因为重大错误，SQL语句无法执行");
		code.put("-908",	 "23510 当前资源限制设施的规范或者自动重绑定的系统参数不允许BIND，REBIND，AUTOREBIND");
		code.put("-909",	 "57007 对象已被删除");
		code.put("-910",	 "57007 因为在该对象上挂起DROP，所以不能访问该对象");
		code.put("-911",	 "40001 当前工作单元已被回滚");
		code.put("-913",	 "57033 因为死锁或超时导致不成功执行");
		code.put("-917",	 "42969 绑定程序包已经失败");
		code.put("-918",	 "51021 SQL语句不能被执行，因为连接丢失");
		code.put("-919",	 "56045 需要一个ROLLBACK");
		code.put("-922",	 "42505 连接权限失败。试图从TSO、CICS或IMS访问DB2，同时相应的连接设施处于非活动的状态");
		code.put("-923",	 "57015 因为DB2不可用，所以不能建立连接");
		code.put("-924",	 "58006 遇到了DB2内部的连接错误：提供了原因代码");
		code.put("-925",	 "2D521 SQL的COMMIT语句不能从CICS或IMS/TM发布");
		code.put("-926",	 "2D521 SQL的ROLLBACK语句不能从CICS或IMS/TM发布");
		code.put("-927",	 "51006 当正在连接的环境没有建立时，语言接口被调用。利用DSN命令激发该程序");
		code.put("-929",	 "58002 数据获取退出已经失败（DPROP）");
		code.put("-939",	 "51021 由于远程服务器的未请求的回滚，要求一个回滚");
		code.put("-947",	 "56038 SQL语句失败，因为更新不能被传播（DPROP）");
		code.put("-948",	 "56062 DDF没有启动，分布式操作无效");
		code.put("-950",	 "42705 在SQL语句中指定的位置在SYSIBM.LOCATIONS中没有定义");
		code.put("-965",	 "51021 存储过程非正常终止（在DB2 6之前的版本）");
		code.put("-981",	 "57015 当前不是处于允许SQL的状态时，试图在RRSAF中执行SQL");
		code.put("-991",	 "57015 调用连接不能建立一个到DB2的隐含或开放连接");
		code.put("-2001",	 "53089 为储存过程指定的宿主变量参数的个数不等于预期的参数个数");
		code.put("-20003",	 "560A7 不能为GRECP中的表空间或索引指定GBPCACHE NONE");
		code.put("-20004",	 "560A8 对于WORKFILE对象。8KB或16Kb的缓冲池页面大小无效");
		code.put("-20005",	 "54035 指定的对象类型超出了内部的ID极限");
		code.put("-20006",	 "53097 当没有指定WLM环境时，LOB不能被指定为参数");
		code.put("-20070",	 "53098 不能非LOB列建立一个辅助表");
		code.put("-20071",	 "53099 必须指定WLM环境名");
		code.put("-20072",	 "56052 指定的权限ID不拥有在触发器程序包上执行BIND所需的权限");
		code.put("-20073",	 "42927 不能按照指定的要求更改命名的函数，因为在现存的视图定义中引用了该函数");
		code.put("-20074",	 "42939 不能建立指定的对象，因为“SYS”是一个保留的前缀");
		code.put("-20100",	 "56059 在被触发的SQL语句中有BIND错误，指定了错误的SQLCODE和SQLSTATE");
		code.put("-20101",	 "56059 由于指定的原因代码，该函数失败");
		code.put("-20102",	 "42849 在CREATE或ALTER FUNCTION语句中不能使用指定的选项");
		code.put("-20104",	 "42856 更改一个CCSID失败");
		code.put("-20106",	 "42945 不能改变表空间或数据库的CCSID，因为现存的试图引用");
		code.put("-30000",	 "58008 DRDA分布协议错误；处理可以继续");
		code.put("-30002",	 "57057 使用DRDA的分布式客户把OPEN语句连接到PREPARE，但PREPARE接受到一个SQLCODE为＋495的警告");
		code.put("-30020",	 "58009 DRDA分布协议错误；对话被解除");
		code.put("-30021",	 "58010 DRDA分布协议错误；处理不能继续");
		code.put("-30030",	 "58013 违反分布协议：COMMIT不成功，对话被解除（AS）");
		code.put("-30040",	 "57012 因为不能得到资源，执行失败，处理可以继续（AS）");
		code.put("-30041",	 "57013 因为不能得到资源，执行失败，处理不能成功的继续（AS）");
		code.put("-30050",	 "58011 执行不成功，在BIND过程中不能执行语句");
		code.put("-30051",	 "58012 特定的BIND过程不是处于活动状态（远程BIND），从而导致失败");
		code.put("-30052",	 "42932 程序准备的假设错误");
		code.put("-30053",	 "42506 程序包的拥有者遭遇授权失败");
		code.put("-30060",	 "08004 RBD遭遇授权失败");
		code.put("-30061",	 "08004 指定了无效或者没有存在的RDB");
		code.put("-30070",	 "58014 目标子系统不支持这个命令");
		code.put("-30071",	 "58015 目标子系统不支持这个对象");
		code.put("-30072",	 "58016 目标子系统不支持这个参数");
		code.put("-30073",	 "58017 目标子系统不支持这个参数值");
		code.put("-30074",	 "58018 应答信息不被支持");
		code.put("-30080",	 "08001 SNA通信错误");
		code.put("-30081",	 "58019 TCP/IP通信错误");
		code.put("-30082",	 "08001 由于安全冲突、通信失败：提供了原因代码");
		code.put("-30090",	 "25000 指定的操作对远程执行失败");
		code.put("-30104",	 "56095 在绑定选项与绑定值中有错误");
		code.put("-30105",	 "56096 指定的绑定选项不兼容");
		
		
	}
	
}
