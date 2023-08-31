lexer grammar DiscountsLexer;

COMMA: ',';

AND: A N D;
OR: O R;
FOR: F O R;
BASKET: B A S K E T;
WITH: W I T H;
ALL: A L L;
APPLY: A P P L Y;
DISCOUNT: D I S C O U N T;
TO: T O;
MORE_Q: M O R E;

PERCENT: '%';

INT: [0-9]+;
ID: [A-Za-z_#]([A-Za-z#_0-9.])*;
SPACE: [ \r\t\u000C\n]+ -> channel(HIDDEN);

fragment A: [Aa];
fragment B: [Bb];
fragment C: [Cc];
fragment D: [Dd];
fragment E: [Ee];
fragment F: [Ff];
fragment G: [Gg];
fragment H: [Hh];
fragment I: [Ii];
fragment J: [Jj];
fragment K: [Kk];
fragment L: [Ll];
fragment M: [Mm];
fragment N: [Nn];
fragment O: [Oo];
fragment P: [Pp];
fragment Q: [Qq];
fragment R: [Rr];
fragment S: [Ss];
fragment T: [Tt];
fragment U: [Uu];
fragment V: [Vv];
fragment W: [Ww];
fragment X: [Xx];
fragment Y: [Yy];
fragment Z: [Zz];