_MAC2_  START   0
FIRST   STL     TEST
		STS		TEST1
		STT		TEST2
		STA		TEST3
CLEAR   X
LDS     #1
LDT     $ALO
J       FINISH
TEST    WORD    10
TEST1   WORD    1
TEST2   WORD    2
TEST3   WORD    3
FINISH  END     FIRST
