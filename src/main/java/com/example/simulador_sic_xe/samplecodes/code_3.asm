TEST__  START   0
        LDA     #10
LOOP    ADD     ONE
        COMP    MAX
        JEQ     END
        +J      LOOP
ONE     WORD    1
MAX     WORD    100
ZERO    WORD    0
        END     TEST__