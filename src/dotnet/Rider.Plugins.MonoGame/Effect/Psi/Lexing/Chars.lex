%include Unicode.lex

NULL_CHAR=\u0000

TAB=\u0009
LF=\u000A
VERTICAL_TAB=\u000B
FF=\u000C
CR=\u000D
NEL=\u0085
UNICODE_CC_WHITESPACE_CHARS={TAB}{VERTICAL_TAB}
UNICODE_CC_SEPARATOR_CHARS={LF}{FF}{CR}{NEXT_LINE}


ZERO_WIDTH_SP=\u200B
ZERO_WIDTH_NBSP=\uFEFF
OTHER_CF_WHITESPACE=\u180E
UNICODE_CF_WHITESPACE_CHARS={ZERO_WIDTH_SP}{ZERO_WIDTH_NBSP}{OTHER_CF_WHITESPACE}


LINE_SEPARATOR=\u2028
UNICODE_ZL_CHARS={LINE_SEPARATOR}

PARAGRAPH_SEPARATOR=\u2029
UNICODE_ZP_CHARS={PARAGRAPH_SEPARATOR}

SP=\u0020
NBSP=\u00A0
OTHER_SP=\u1680\u2000-\u200A\u202F\u205F\u3000
UNICODE_ZS_CHARS={SP}{NBSP}{OTHER_SP}
