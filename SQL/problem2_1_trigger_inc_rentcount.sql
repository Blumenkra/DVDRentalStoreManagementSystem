CREATE OR REPLACE TRIGGER inc_rentcount_trigger
AFTER INSERT ON Rentlist
FOR EACH ROW
BEGIN
	UPDATE Members
	SET mrentcount = mrentcount + 1
	WHERE mid = :new.mid;
END;
/
