grammar AgentGrammar;

//--- LEXER: ---
//Operators
PLUS: '+';
MIN: '-';
MUL: '*';
DIV: '/';
EXP: '^';
ROOT: '√';
LESS: '<';
GREATER: '>';

//Order formule
ORDER: 'NewOrder';
IF: 'if';
THEN: 'then';
EQUALS: '=';
SEMICOLON: ';';

//Variables
STANDARD_VAR: 'Stock'
            | 'Budget'
            | 'Backlog'
            | 'ProductionCost'
            | 'StockCost'
            | 'BacklogCost'
            | 'IncomingOrder'
            | 'IncomingDelivery';
ORDER_VAR: 'Order';
PERSONAL_VAR: '@'[a-z]+([A-Z]?[a-z])*;

//Scalar
NUMBER: [0] | [1-9][0-9]*;
DECIMAL: [0-9] '.' [0-9]* | [1-9][0-9]* '.' [0-9]*;

//Brackets
OPEN_ROUND_BRACKET: '(';
CLOSE_ROUND_BRACKET: ')';
OPEN_SQUARE_BRACKET: '[';
CLOSE_SQUARE_BRACKET: ']';

//Skip whitespace
WS: [ \t\r\n]+ -> skip;


//--- PARSER: ---
behaviourFormula: variableAssignment* defaultOrder alternativeOrder*;

//Order
defaultOrder: ORDER EQUALS expression+;
alternativeOrder: ifClause thenClause SEMICOLON;

//variable assignment
variableAssignment: PERSONAL_VAR EQUALS expression;

//if_then
ifClause: IF OPEN_SQUARE_BRACKET comparison CLOSE_SQUARE_BRACKET;
thenClause: THEN OPEN_SQUARE_BRACKET ORDER_VAR EQUALS expression CLOSE_SQUARE_BRACKET | THEN OPEN_SQUARE_BRACKET variableAssignment CLOSE_SQUARE_BRACKET;
comparison: (expression | ORDER_VAR) (GREATER | LESS | EQUALS) expression;

//expressions
expression: expressionPart #subExpression
          | expression MUL expression #multiplication
          | expression DIV expression #division
          | expression MIN expression #subtraction
          | expression PLUS expression #addition;
expressionPart: exponentExpression
              | rootExpression
              | value
              | priorityExpression;
priorityExpression: OPEN_ROUND_BRACKET expression CLOSE_ROUND_BRACKET;
exponentExpression: (value | priorityExpression) EXP (value | priorityExpression)
                  | (value | priorityExpression) EXP
                    OPEN_ROUND_BRACKET (variable | scalar | priorityExpression) DIV (value | priorityExpression) CLOSE_ROUND_BRACKET;
rootExpression: ROOT (value | priorityExpression);

//values
value: scalar | variable;
scalar: NUMBER #number | DECIMAL #decimal;
variable: STANDARD_VAR #defaultVariable
        | PERSONAL_VAR #variableReference
        | ORDER_VAR #orderVariable;