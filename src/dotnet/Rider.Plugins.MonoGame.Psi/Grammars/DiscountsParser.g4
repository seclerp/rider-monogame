parser grammar DiscountsParser;

options { tokenVocab=DiscountsLexer; }

rules:	(FOR BASKET WITH basket)? (APPLY apply)+ EOF;

basket
	: products 			#basket_products
	| basket (AND | OR) basket      #basket_and_or
	;

apply
	: INT discount (TO products)?
	;

discount
	: INT DISCOUNT
	;

quantity
	: INT OR MORE_Q
	;

product: ID;
products: product (COMMA product)*;