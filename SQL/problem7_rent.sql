CREATE OR REPLACE PROCEDURE renting
(pmid IN NUMBER, pdid IN NUMBER)
IS
	user_valid NUMBER;
	user_defined_error1 EXCEPTION;
	user_defined_error2 EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Rentlist WHERE did = pdid AND isreturned = 'N';
	IF counter > 0
	THEN 
		RAISE user_defined_error1;
	END IF;
	
	user_valid := is_user_delinquent(pmid);
	
	IF user_valid = 1
	THEN
		RAISE user_defined_error2;
	END IF;
	
	INSERT INTO Rentlist VALUES(pmid, pdid, sysdate, 'N');
	
EXCEPTION
	WHEN user_defined_error1 THEN
		RAISE_APPLICATION_ERROR(-20003,'This DVD has been rented and not returned yet.');
	WHEN user_defined_error2 THEN
		RAISE_APPLICATION_ERROR(-20004,'This user is a delinquent.');
END;
/