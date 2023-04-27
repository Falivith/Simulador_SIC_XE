INCR    START   0
        LDA     ZERO
LOOP    ADD     ONE
        COMP    MAX
        JEQ     END
        J       LOOP
ONE     BYTE    1
MAX     BYTE    100
ZERO    WORD    0
        END     INCR