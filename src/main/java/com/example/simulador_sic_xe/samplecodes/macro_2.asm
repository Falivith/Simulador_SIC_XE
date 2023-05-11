_MAC2_  START   0

SIGMA   MACRO   &ALO
        CLEAR   X
        LDS     #1
        LDT     $ALO
        MEND

EXMAC   MACRO   &VAL1,&VAL2,&VAL3
        STS     &VAL1
        STT     &VAL2
        STA     &VAL3
        SIGMA   $VAL1
        MEND

FIRST   STL     TEST
        EXMAC   TEST1,TEST2,TEST3
        J       FINISH

TEST    WORD    10
TEST1   WORD    1
TEST2   WORD    2
TEST3   WORD    3
FINISH  END     FIRST