package code;

import code.Expr.Binary;
import code.Expr.Grouping;
import code.Expr.Literal;
import code.Expr.Unary;
import code.Expr.Visitor;

public class Interpreter implements Visitor<Object> {

    @Override
    public Object visitBinaryExpr(Binary expr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitBinaryExpr'");
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitGroupingExpr'");
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLiteralExpr'");
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitUnaryExpr'");
    }
    
}
