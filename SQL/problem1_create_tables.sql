CREATE TABLE Members(
	mid INT,
	mname VARCHAR2(20) NOT NULL,
	mtel VARCHAR2(20) NOT NULL,
	mrentcount INT CHECK (mrentcount >= 0),
	PRIMARY KEY(mid)
	);
CREATE TABLE Dvds(
	did INT,
	dtitle VARCHAR2(20) NOT NULL,
	PRIMARY KEY(did)
	);

CREATE TABLE Rentlist(
	mid INT REFERENCES Members(mid),
	did INT REFERENCES Dvds(did),
	rdate DATE,
	isreturned CHAR(1) DEFAULT 'N' CHECK (isreturned IN ('Y','N')),
	PRIMARY KEY(mid,did,rdate)
	);