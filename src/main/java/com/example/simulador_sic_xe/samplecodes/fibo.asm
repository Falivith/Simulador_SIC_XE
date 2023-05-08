_FIBO_  START   0
FIRST   CLEAR   X
        LDS     #1
        LDT     ZERO
LOOP    STS     CURR
        ADDR    T,S
        LDT     CURR
        TIX     TEST
        JLT     LOOP
        J       FINISH
ZERO    WORD    0
CURR    WORD    0
TEST    WORD    9
FINISH  END     FIRST