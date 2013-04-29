CREATE OR REPLACE FUNCTION estimate_late_fee
(pid IN NUMBER)
RETURN NUMBER
IS
	rentdate Rentlist.rdate%TYPE;
	latedate NUMBER;
	user_defined_error EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Dvds WHERE did = pid;
	IF counter = 0
	THEN 
		RAISE user_defined_error;
	END IF;
	
	SELECT rdate INTO rentdate
		FROM Rentlist
		WHERE did = pid AND isreturned = 'N';
	
	latedate := trunc(sysdate) - trunc(rentdate);	
	IF latedate < 3 
	THEN 
		RETURN 0; 
	ELSE 
		RETURN (latedate - 2)*500; 
	END IF;
	
EXCEPTION
	WHEN NO_DATA_FOUND THEN
		RETURN 0;
	WHEN user_defined_error THEN
		RAISE_APPLICATION_ERROR(-20002,'This DVD is not in DVDS table.');
END;
/