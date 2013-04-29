CREATE OR REPLACE FUNCTION returning
(pid IN NUMBER)
RETURN NUMBER
IS
	user_defined_error EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Rentlist WHERE did = pid AND isreturned = 'N';
	IF counter = 0
	THEN 
		RAISE user_defined_error;
	END IF;
	
	counter := estimate_late_fee(pid);
	
	UPDATE Rentlist
	SET isreturned = 'Y'
	WHERE did = pid AND isreturned = 'N';
	
	RETURN counter;
	
EXCEPTION
	WHEN user_defined_error THEN
		RAISE_APPLICATION_ERROR(-20005,'This DVD was not rented.');
END;
/