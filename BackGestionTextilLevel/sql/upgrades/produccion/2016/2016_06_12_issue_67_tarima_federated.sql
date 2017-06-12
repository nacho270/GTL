-- EN LA A
grant all on gtl.t_tarima to 'root'@'192.168.1.13' identified by 's4l3m';

-- EN LA B
ALTER TABLE T_REMITO DROP FOREIGN KEY `FK47308C155BE90680`;

DROP TABLE T_TARIMA;

CREATE TABLE `t_tarima` (
  `P_ID` int(11) NOT NULL,
  `A_NUMERO` int(11) NOT NULL,
  PRIMARY KEY (`P_ID`)
) ENGINE=FEDERATED CONNECTION='mysql://root:s4l3m@192.168.1.250:3306/gtl/t_tarima';
