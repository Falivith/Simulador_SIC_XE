HELP__  START   16
FIRST   LDA     #10
        ADD     #10
        LDS     #10
        ADDR    A,S
        CLEAR   A
        CLEAR   S
        COMPR   A,S
        LDA     #10
        LDS     #10
        MULR    S,A
        SUBR    S,A
        RMO     A,S
        SHIFTL  A
        SHIFTR  A
        J       FINISH
ONE     WORD    1
TWO     BYTE    16
FINISH  END     FIRST