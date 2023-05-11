_MACR_  START   0

EXMAC   MACRO   &VAL1
        STS     #10
        STT     #10
        STA     &VAL1
        MEND

FIRST   STL     TEST
        EXMAC   TEST1
        J       FINISH

TEST1   WORD    1
TEST2   WORD    2
TEST3   WORD    3
FINISH  END     FIRST