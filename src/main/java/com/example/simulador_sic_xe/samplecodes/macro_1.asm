_MACR_  START   0

EXMAC   MACRO   &VAL1,&VAL2,&VAL3
        STS     &VAL1
        STT     &VAL2
        STA     &VAL3
        MEND

FIRST   STL     TEST
        EXMAC   TEST1,TEST2,TEST3
        J       FINISH

TEST1   WORD    1
TEST2   WORD    2
TEST3   WORD    3
FINISH  END     FIRST