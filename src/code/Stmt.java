package code;

import java.util.List;

abstract class Stmt {
   interface Visitor<R> {
      R visitBlockStmt(Block stmt);

      R visitExpressionStmt(Expression stmt);

      R visitFunctionStmt(Function stmt);

      R visitIfStmt(If stmt);

      R visitPrintStmt(Print stmt);

      R visitScanStmt(Scan stmt);

      R visitWhileStmt(While stmt);

      R visitStringStmt(String stmt);

      R visitIntStmt(Int stmt);

      R visitFloatStmt(Float stmt);

      R visitCharStmt(Char stmt);

      R visitBoolStmt(Bool stmt);
   }

   static class Block extends Stmt {
      Block(List<Stmt> statements) {
         this.statements = statements;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitBlockStmt(this);
      }

      final List<Stmt> statements;
   }

   static class Expression extends Stmt {
      Expression(Expr expression) {
         this.expression = expression;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitExpressionStmt(this);
      }

      final Expr expression;
   }

   static class Function extends Stmt {
      Function(Token name, List<Parameter> params, List<Stmt> body) {
         this.name = name;
         this.params = params;
         this.body = body;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitFunctionStmt(this);
      }

      final Token name;
      final List<Parameter> params;
      final List<Stmt> body;
   }

   static class If extends Stmt {
      If(Expr condition, List<Stmt> thenBranch, List<Stmt> elseBranch) {
         this.condition = condition;
         this.thenBranch = thenBranch;
         this.elseBranch = elseBranch;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitIfStmt(this);
      }

      final Expr condition;
      final List<Stmt> thenBranch;
      final List<Stmt> elseBranch;
   }

   static class Print extends Stmt {
      Print(Expr expression) {
         this.expression = expression;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitPrintStmt(this);
      }

      final Expr expression;
   }

   static class Scan extends Stmt {

      Scan(List<Token> identifiers) {
         this.identifiers = identifiers;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitScanStmt(this);
      }

      List<Token> identifiers;

   }

   static class While extends Stmt {
      While(Expr condition, List<Stmt> body) {
         this.condition = condition;
         this.body = body;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitWhileStmt(this);
      }

      final Expr condition;
      final List<Stmt> body;
   }

   static class String extends Stmt {
      String(Token name, Expr initializer) {
         this.name = name;
         this.initializer = initializer;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitStringStmt(this);
      }

      final Token name;
      final Expr initializer;
   }

   static class Int extends Stmt {
      Int(Token name, Expr initializer) {
         this.name = name;
         this.initializer = initializer;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitIntStmt(this);
      }

      final Token name;
      final Expr initializer;
   }

   static class Float extends Stmt {
      Float(Token name, Expr initializer) {
         this.name = name;
         this.initializer = initializer;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitFloatStmt(this);
      }

      final Token name;
      final Expr initializer;
   }

   static class Char extends Stmt {
      Char(Token name, Expr initializer) {
         this.name = name;
         this.initializer = initializer;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitCharStmt(this);
      }

      final Token name;
      final Expr initializer;
   }

   static class Bool extends Stmt {
      Bool(Token name, Expr initializer) {
         this.name = name;
         this.initializer = initializer;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
         return visitor.visitBoolStmt(this);
      }

      final Token name;
      final Expr initializer;
   }

   abstract <R> R accept(Visitor<R> visitor);
}
