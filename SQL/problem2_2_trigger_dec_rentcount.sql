CREATE OR REPLACE TRIGGER dec_rentcount_trigger
AFTER UPDATE OF isreturned ON Rentlist
FOR EACH ROW
WHEN (old.isreturned = 'N' AND new.isreturned = 'Y')
BEGIN
	UPDATE Members
	SET mrentcount = mrentcount - 1
	WHERE mid = :old.mid;
END;
/