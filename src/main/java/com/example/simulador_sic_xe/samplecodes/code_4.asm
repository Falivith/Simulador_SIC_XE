HELP__  START   0
FIRST   LDA     #10
        DIV     BUFFER
        J       FINISH
BUFFER  WORD    10
FINISH  END     FIRST