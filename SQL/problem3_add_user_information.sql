CREATE OR REPLACE PROCEDURE add_user_information
(pid IN NUMBER, pname IN VARCHAR2, ptel IN VARCHAR2)
IS
BEGIN
	INSERT INTO Members VALUES(pid,pname,ptel,0);
END;
/