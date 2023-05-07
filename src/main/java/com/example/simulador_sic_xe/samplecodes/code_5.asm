HELP__  START   16
FIRST   LDA     #10
        LDS     #10
        ADDR    A,S
        CLEAR   A
        CLEAR   S
        COMPR   A,S
        LDA     #10
        LDS     #10
        MULR    A,S
        END     FIRST