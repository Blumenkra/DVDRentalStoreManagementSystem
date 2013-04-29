CREATE OR REPLACE FUNCTION is_user_delinquent
(pid IN NUMBER)
RETURN NUMBER
IS
	rentdate Rentlist.rdate%TYPE;
	user_defined_error EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Members WHERE mid = pid;
	IF counter = 0
	THEN 
		RAISE user_defined_error;
	END IF;
	
	rentdate := sysdate;
	
	SELECT MIN(rdate) INTO rentdate
		FROM Rentlist
		WHERE mid = pid AND isreturned = 'N';
		
	IF (trunc(sysdate) - trunc(rentdate)) > 2 
	THEN 
		RETURN 1; 
	ELSE 
		RETURN 0; 
	END IF;
EXCEPTION
	WHEN user_defined_error THEN
		RAISE_APPLICATION_ERROR(-20001,'This user is not in MEMBERS table.');
END;
/